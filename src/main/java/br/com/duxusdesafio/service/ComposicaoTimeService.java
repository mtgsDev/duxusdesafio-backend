package br.com.duxusdesafio.service;

import br.com.duxusdesafio.dto.ComposicaoTimeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

//Camada de Interface dos métodos criados para ComposicaoTimeService.
@Service
public interface ComposicaoTimeService {

    List<ComposicaoTimeDTO> listarComposicoes();

    ComposicaoTimeDTO buscarComposicaoPorId(Long id);

    ComposicaoTimeDTO criarComposicao(ComposicaoTimeDTO composicaoTimeDTO);

    void deletarComposicao(Long id);
}
