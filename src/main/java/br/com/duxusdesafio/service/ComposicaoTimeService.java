package br.com.duxusdesafio.service;

import br.com.duxusdesafio.dto.ComposicaoTimeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ComposicaoTimeService {

    List<ComposicaoTimeDTO> listarComposicoes();

    ComposicaoTimeDTO buscarComposicaoPorId(Long id);

    ComposicaoTimeDTO criarComposicao(ComposicaoTimeDTO composicaoTimeDTO);

    void deletarComposicao(Long id);
}
