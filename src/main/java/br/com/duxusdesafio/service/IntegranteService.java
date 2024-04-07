package br.com.duxusdesafio.service;

import br.com.duxusdesafio.dto.IntegranteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IntegranteService {


    IntegranteDTO buscarIntegrantePorId(Long id);
    IntegranteDTO criarIntegrante(IntegranteDTO integranteDto);
    void deletarIntegrante(Long id);
    IntegranteDTO atualizarIntegrante(Long id, IntegranteDTO integranteDTO);

//    List<IntegranteDTO> buscarTodosIntegrantes();
}
