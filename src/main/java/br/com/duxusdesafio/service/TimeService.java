package br.com.duxusdesafio.service;

import br.com.duxusdesafio.dto.TimeDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface TimeService {
    //CRUD
    TimeDTO criarTime(TimeDTO timeDTO);

    TimeDTO buscarTimePorId(Long id);

    List<TimeDTO> listarTodosTimes();

    TimeDTO atualizarTime(Long id, TimeDTO timeDTO);

    void deletarTime(Long id);

    //MÉTODOS
    TimeDTO criarTimeComIntegrantes(TimeDTO timeDTO);

    TimeDTO timeDaData(LocalDate data);

    List<String> timeMaisComum(LocalDate dataInicial, LocalDate dataFinal);

    String funcaoMaisComum(LocalDate dataInicial, LocalDate dataFinal);

    String franquiaMaisFamosa(LocalDate dataInicial, LocalDate dataFinal);

    Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal);

    Map<String, Long> contagemPorFranquia(LocalDate dataInicial, LocalDate dataFinal);
}
