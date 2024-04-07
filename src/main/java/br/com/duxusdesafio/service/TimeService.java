package br.com.duxusdesafio.service;

import br.com.duxusdesafio.dto.TimeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TimeService {
    TimeDTO criarTime(TimeDTO timeDTO);

    TimeDTO buscarTimePorId(Long id);

    List<TimeDTO> listarTodosTimes();

    TimeDTO atualizarTime(Long id, TimeDTO timeDTO);

    void deletarTime(Long id);
}
