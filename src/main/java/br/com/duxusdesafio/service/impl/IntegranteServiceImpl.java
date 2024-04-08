package br.com.duxusdesafio.service.impl;
import br.com.duxusdesafio.dto.ComposicaoTimeDTO;
import br.com.duxusdesafio.dto.IntegranteDTO;
import br.com.duxusdesafio.dto.TimeDTO;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.IntegranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class IntegranteServiceImpl implements IntegranteService {
    @Autowired
    private final IntegranteRepository integranteRepository;
    @Autowired
    private final TimeRepository timeRepository; // Repositório para acesso às composições de time


    public IntegranteServiceImpl(IntegranteRepository integranteRepository, TimeRepository timeRepository) {
        this.integranteRepository = integranteRepository;
        this.timeRepository = timeRepository;
    }

    @Override
    public List<IntegranteDTO> listarTodosIntegrantes() {
        List<Integrante> integrante = integranteRepository.findAll();
        return integrante.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IntegranteDTO criarIntegrante(IntegranteDTO integranteDTO) {
        if (integranteJaExistente(integranteDTO)) {
            throw new IllegalArgumentException("Integrante com os mesmos atributos já existe.");
        }

        Integrante integrante = new Integrante();
        integrante.setNome(integranteDTO.getNome());
        integrante.setFuncao(integranteDTO.getFuncao());
        integrante.setFranquia(integranteDTO.getFranquia());

        Integrante novoIntegrante = integranteRepository.save(integrante);
        return convertToDTO(novoIntegrante);
    }

    @Override
    public IntegranteDTO buscarIntegrantePorId(Long id) {
        Optional<Integrante> integranteOptional = integranteRepository.findById(id);
        return integranteOptional.map(this::convertToDTO).orElse(null);
    }
    @Override
    public IntegranteDTO atualizarIntegrante(Long id, IntegranteDTO integranteDTO) {
        Optional<Integrante> integranteOptional = integranteRepository.findById(id);
        if (integranteOptional.isPresent()) {
            Integrante integrante = integranteOptional.get();
            integrante.setNome(integranteDTO.getNome());
            integrante.setFuncao(integranteDTO.getFuncao());
            integrante.setFranquia(integranteDTO.getFranquia());

            Integrante integranteAtualizado = integranteRepository.save(integrante);
            return convertToDTO(integranteAtualizado);
        }
        return null;
    }

//    public IntegranteDTO integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal) {
//        // Recupera todos os times dentro do período especificado
//        List<Time> times = timeRepository.findByDataBetween(dataInicial, dataFinal);
//
//        // Mapeia os integrantes de todos os times dentro do período
//        List<Integrante> integrantes = times.stream()
//                .flatMap(time -> time.getComposicaoTime().stream())
//                .map(ComposicaoTime::getIntegrante)
//                .filter(Objects::nonNull) // Filtra integrantes não nulos
//                .toList();
//
//        // Conta a ocorrência de cada integrante
//        Map<Integrante, Long> countByIntegrante = integrantes.stream()
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//
//        // Encontra o integrante com mais ocorrências
//        Integrante integranteMaisUsado = countByIntegrante.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .map(Map.Entry::getKey)
//                .orElse(null);
//
//        // Converte o integrante mais usado para DTO
//        if (integranteMaisUsado != null) {
//            return convertToDTO(integranteMaisUsado);
//        } else {
//            return null;
//        }
//    }

    private IntegranteDTO toIntegranteDTO(ComposicaoTimeDTO composicaoTimeDTO) {
        IntegranteDTO integranteDTO = new IntegranteDTO();
        integranteDTO.setId(composicaoTimeDTO.getIntegranteId());
        // Outros mapeamentos de campos, se necessário
        return integranteDTO;
    }

    private List<TimeDTO> filterTimesByPeriod(List<TimeDTO> todosOsTimes, LocalDate dataInicial, LocalDate dataFinal) {
        return todosOsTimes.stream()
                .filter(timeDTO -> !timeDTO.getData().isBefore(dataInicial) && !timeDTO.getData().isAfter(dataFinal))
                .collect(Collectors.toList());
    }


    public void deletarIntegrante(Long id) {
        integranteRepository.deleteById(id);
    }

    private boolean integranteJaExistente(IntegranteDTO integranteDto) {
        String nome = integranteDto.getNome();
        String funcao = integranteDto.getFuncao();
        String franquia = integranteDto.getFranquia();

        // Consultar se existe algum integrante com os mesmos atributos
        return integranteRepository.findByNomeAndFuncaoAndFranquia(nome, funcao, franquia).isPresent();
    }
    @Override
    public IntegranteDTO integranteMaisComum(LocalDate dataInicial, LocalDate dataFinal) {
        List<Time> timesNoPeriodo = timeRepository.findByDataBetween(dataInicial, dataFinal);

        // Mapa para contar o número de ocorrências de cada integrante
        Map<Integrante, Long> countByIntegrante = new HashMap<>();

        // Percorre os times no período e conta as ocorrências de cada integrante
        for (Time time : timesNoPeriodo) {
            for (ComposicaoTime composicao : time.getComposicaoTime()) {
                Integrante integrante = composicao.getIntegrante();
                Long count = countByIntegrante.getOrDefault(integrante, 0L);
                countByIntegrante.put(integrante, count + 1);
            }
        }

        // Encontra o integrante com mais ocorrências
        Integrante integranteMaisComum = countByIntegrante.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Converte o integrante mais comum para DTO
        if (integranteMaisComum != null) {
            return convertToDTO(integranteMaisComum);
        } else {
            return null;
        }
    }


    @Override
    public Integrante integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal){
        List<Time> todosOsTimes = timeRepository.findAll();
        // TODO Implementar método seguindo as instruções!
        Stream<Integrante> IStream = StreamMapper(todosOsTimes, dataInicial, dataFinal);
        // Mapeia ComposicaoTime para Integrante

        return IStream.min(Comparator.comparing(String::valueOf)).orElse(null);
    }

    private Stream<Integrante> StreamMapper(List<Time> todosOsTimes, LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial == null || dataFinal == null) {
            return todosOsTimes.stream()
                    .flatMap(time -> time.getComposicaoTime().stream()) // FlatMap para obter uma stream de ComposicaoTime
                    .map(ComposicaoTime::getIntegrante);
        }
        List<Time> mapper = durantePeriodo(todosOsTimes, dataInicial, dataFinal);

        return mapper.stream()
                .flatMap(time -> time.getComposicaoTime().stream()) // FlatMap para obter uma stream de ComposicaoTime
                .map(ComposicaoTime::getIntegrante); // Mapeia ComposicaoTime para Integrante
    }

    private List<Time> durantePeriodo(List<Time> todosOsTimes, LocalDate dataInicial, LocalDate dataFinal) {
        return todosOsTimes.stream().filter(time -> !time.getData().isBefore(dataInicial) && !time.getData().isAfter(dataFinal)).toList();
    }


    private IntegranteDTO convertToDTO(Integrante integrante) {
        IntegranteDTO integranteDTO = new IntegranteDTO();
        integranteDTO.setId(integrante.getId());
        integranteDTO.setNome(integrante.getNome());
        integranteDTO.setFuncao(integrante.getFuncao());
        integranteDTO.setFranquia(integrante.getFranquia());
        return integranteDTO;
    }



    //    private IntegranteDTO toIntegranteDTO(Integrante integrante) {
//        if (integrante == null) {
//            return null;
//        }
//        IntegranteDTO integranteDTO = new IntegranteDTO();
//        integranteDTO.setId(integrante.getId());
//        integranteDTO.setFranquia(integrante.getFranquia());
//        integranteDTO.setNome(integrante.getNome());
//        integranteDTO.setFuncao(integrante.getFuncao());
//        return integranteDTO;
//    }
//
//    private List<Time> durantePeriodo(List<Time> todosOsTimes, LocalDate dataInicial, LocalDate dataFinal) {
//        return todosOsTimes.stream().filter(time -> !time.getData().isBefore(dataInicial) && !time.getData().isAfter(dataFinal)).toList();
//    }
//
//    public Stream<IntegranteDTO> StreamMapper(List<TimeDTO> todosOsTimes, LocalDate dataInicial, LocalDate dataFinal) {
//        if (dataInicial == null || dataFinal == null) {
//            return todosOsTimes.stream()
//                    .flatMap(timeDTO -> timeDTO.getComposicaoTime().stream()) // FlatMap para obter uma stream de ComposicaoTimeDTO
//                    .map(this::toIntegranteDTO); // Mapeia ComposicaoTimeDTO para IntegranteDTO
//        }
//
//        List<TimeDTO> filteredTimes = filterTimesByPeriod(todosOsTimes, dataInicial, dataFinal);
//
//        return filteredTimes.stream()
//                .flatMap(timeDTO -> timeDTO.getComposicaoTime().stream()) // FlatMap para obter uma stream de ComposicaoTimeDTO
//                .map(this::toIntegranteDTO); // Mapeia ComposicaoTimeDTO para IntegranteDTO
//    }

}
