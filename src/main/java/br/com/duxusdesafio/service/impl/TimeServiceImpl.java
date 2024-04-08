package br.com.duxusdesafio.service.impl;

import br.com.duxusdesafio.dto.IntegranteDTO;
import br.com.duxusdesafio.dto.TimeDTO;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.ComposicaoTimeRepository;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TimeServiceImpl implements TimeService {

    private final TimeRepository timeRepository;
    private final IntegranteRepository integranteRepository;
    private final ComposicaoTimeRepository composicaoTimeRepository;

    @Autowired
    public TimeServiceImpl(TimeRepository timeRepository, IntegranteRepository integranteRepository, ComposicaoTimeRepository composicaoTimeRepository) {
        this.timeRepository = timeRepository;
        this.integranteRepository = integranteRepository;
        this.composicaoTimeRepository = composicaoTimeRepository;
    }

    @Override
    public TimeDTO criarTime(TimeDTO timeDTO) {
        // Verificar se já existe um time com o mesmo nome e data
        Optional<Time> existingTime = timeRepository.findByNomeAndData(timeDTO.getNome(), timeDTO.getData());
        if (existingTime.isPresent()) {
            throw new RuntimeException("Já existe um time cadastrado com o mesmo nome e data.");
        }

        // Se não existir, criar o novo time
        Time newTime = new Time();
        newTime.setNome(timeDTO.getNome());
        newTime.setData(timeDTO.getData());

        Time savedTime = timeRepository.save(newTime);
        return convertToDTO(savedTime);
    }

    @Override
    public TimeDTO criarTimeComIntegrantes(TimeDTO timeDTO) {
        // Cria um novo time
        Time time = new Time();
        time.setNome(timeDTO.getNome());
        time.setData(timeDTO.getData());

        // Salva o novo time no banco de dados
        Time savedTime = timeRepository.save(time);

        // Para cada integrante no DTO, cria um novo Integrante e associa ao time
        List<ComposicaoTime> composicoes = new ArrayList<>();
        for (IntegranteDTO integranteDTO : timeDTO.getIntegrantes()) {
            Integrante integrante = new Integrante();
            integrante.setNome(integranteDTO.getNome());
            integrante.setFuncao(integranteDTO.getFuncao());
            integrante.setFranquia(integranteDTO.getFranquia());
            integrante.setTime(savedTime); // Associa o integrante ao time

            // Salva o integrante no banco de dados
            Integrante savedIntegrante = integranteRepository.save(integrante);

            // Cria uma composição de tempo associando o time ao integrante
            ComposicaoTime composicaoTime = new ComposicaoTime();
            composicaoTime.setTime(savedTime);
            composicaoTime.setIntegrante(savedIntegrante);

            // Salva a composição de tempo no banco de dados
            composicaoTimeRepository.save(composicaoTime);

            // Adiciona a composição à lista de composições
            composicoes.add(composicaoTime);
        }

        // Define as composições de tempo no time
        savedTime.setComposicaoTime(composicoes);

        // Retorna o DTO do time criado com os integrantes associados
        return convertToDTO(savedTime);
    }

    public TimeDTO buscarTimePorId(Long id) {
        Time time = timeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Time não encontrado"));
        return convertToDTO(time);
    }

    public List<TimeDTO> listarTodosTimes() {
        List<Time> times = timeRepository.findAll();
        return times.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TimeDTO atualizarTime(Long id, TimeDTO timeDTO) {
        Time time = timeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Time não encontrado"));
        time.setNome(timeDTO.getNome());
        time.setData(timeDTO.getData());
        Time updatedTime = timeRepository.save(time);
        return convertToDTO(updatedTime);
    }

    public void deletarTime(Long id) {
        timeRepository.deleteById(id);
    }

    private TimeDTO convertToDTO(Time time) {
        TimeDTO timeDTO = new TimeDTO();
        timeDTO.setId(time.getId());
        timeDTO.setNome(time.getNome());
        timeDTO.setData(time.getData());
        return timeDTO;
    }

    @Override
    public TimeDTO timeDaData(LocalDate data) {
        List<Time> todosTimes = timeRepository.findAll();

        Time time = todosTimes.stream().filter(t -> t.getData().equals(data)).findFirst().orElse(null);

       if(time == null) {
           return null;
       }
        // Cria e retorna o DTO com os dados necessários
        TimeDTO timeDto = new TimeDTO();
        timeDto.setId(time.getId());
        timeDto.setData(time.getData());

        List<IntegranteDTO> integranteDtos = time.getComposicaoTime().stream()
                .map(composicaoTime -> {
                    Integrante integrante = composicaoTime.getIntegrante();
                    IntegranteDTO integranteDto = new IntegranteDTO();
                    integranteDto.setId(integrante.getId());
                    integranteDto.setFranquia(integrante.getFranquia());
                    integranteDto.setNome(integrante.getNome());
                    integranteDto.setFuncao(integrante.getFuncao());
                    return integranteDto;
                })
                .collect(Collectors.toList());

        timeDto.setIntegrantes(integranteDtos);

        return timeDto;
    }

    @Override
    public List<String> timeMaisComum(LocalDate dataInicial, LocalDate dataFinal) {
        List<Time> todosOsTimes = timeRepository.findAll();

        // Filtra os times pelo intervalo de datas
        List<Time> timesNoIntervalo = todosOsTimes.stream()
                .filter(time -> isDateInRange(time.getData(), dataInicial, dataFinal))
                .toList();

        // Filtra integrantes dos times no período e conta a ocorrência de cada nome de integrante
        Map<String, Long> nomeIntegranteCountMap = timesNoIntervalo.stream()
                .flatMap(time -> time.getComposicaoTime().stream())
                .map(composicaoTime -> composicaoTime.getIntegrante().getNome())
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

        // Retorna os nomes de integrantes que ocorreram pelo menos uma vez
        return nomeIntegranteCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Método auxiliar para verificar se uma data está dentro do intervalo
    private boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

}
