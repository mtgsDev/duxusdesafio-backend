package br.com.duxusdesafio.service;

import br.com.duxusdesafio.dto.IntegranteDTO;
import br.com.duxusdesafio.dto.TimeDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface IntegranteService {


    IntegranteDTO buscarIntegrantePorId(Long id);
    IntegranteDTO criarIntegrante(IntegranteDTO integranteDto);
    IntegranteDTO integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal);
    List<IntegranteDTO> listarTodosIntegrantes();
    void deletarIntegrante(Long id);
    IntegranteDTO atualizarIntegrante(Long id, IntegranteDTO integranteDTO);
}
