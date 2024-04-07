package br.com.duxusdesafio.service.impl;

import br.com.duxusdesafio.dto.TimeDTO;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeServiceImpl implements TimeService {

    private final TimeRepository timeRepository;

    @Autowired
    public TimeServiceImpl(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
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


}
