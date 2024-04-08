package br.com.duxusdesafio.service.impl;

import br.com.duxusdesafio.dto.ComposicaoTimeDTO;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.ComposicaoTimeRepository;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ComposicaoTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComposicaoTimeServiceImpl implements ComposicaoTimeService {
    @Autowired
    private final ComposicaoTimeRepository composicaoTimeRepository;
    @Autowired
    private IntegranteRepository integranteRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    public ComposicaoTimeServiceImpl(ComposicaoTimeRepository composicaoTimeRepository, TimeRepository timeRepository, IntegranteRepository integranteRepository) {
        this.composicaoTimeRepository = composicaoTimeRepository;
        this.timeRepository = timeRepository;
        this.integranteRepository = integranteRepository;
    }

    // Get todas composições, metodo utiliza findAll() da JpaRepository, recebe o obj, realiza um stream e salva em uma lista.
    @Override
    public List<ComposicaoTimeDTO> listarComposicoes() {
        List<ComposicaoTime> composicoes = composicaoTimeRepository.findAll();
        return composicoes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // Get por id, metodo utiliza findById() da JpaRepository, recebe o obj, realiza um stream e salva em uma lista.
    @Override
    public ComposicaoTimeDTO buscarComposicaoPorId(Long id) {
        ComposicaoTime composicao = composicaoTimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Composição não encontrada"));
        return convertToDTO(composicao);
    }

    @Override
    public ComposicaoTimeDTO criarComposicao(ComposicaoTimeDTO composicaoTimeDTO) {
        Long timeId = composicaoTimeDTO.getTimeId();
        Long integranteId = composicaoTimeDTO.getIntegranteId();

        // Buscar o Time e o Integrante pelo ID
        Time time = timeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("Time não encontrado com o ID: " + timeId));

        Integrante integrante = integranteRepository.findById(integranteId)
                .orElseThrow(() -> new EntityNotFoundException("Integrante não encontrado com o ID: " + integranteId));

        // Criar a ComposicaoTime com os objetos Time e Integrante
        ComposicaoTime composicaoTime = new ComposicaoTime();
        composicaoTime.setTime(time);
        composicaoTime.setIntegrante(integrante);

        // Salvar a ComposicaoTime no banco de dados
        composicaoTime = composicaoTimeRepository.save(composicaoTime);

        // Retornar DTO da ComposicaoTime criada
        return convertToDTO(composicaoTime);
    }

    @Override
    public void deletarComposicao(Long id) {
        composicaoTimeRepository.deleteById(id);
    }

    // Método auxiliar para converter ComposicaoTime em ComposicaoTimeDTO
    private ComposicaoTimeDTO convertToDTO(ComposicaoTime composicaoTime) {
        ComposicaoTimeDTO dto = new ComposicaoTimeDTO();
        dto.setId(composicaoTime.getId());
        dto.setTimeId(composicaoTime.getTime().getId());
        dto.setIntegranteId(composicaoTime.getIntegrante().getId());
        return dto;
    }

    // Método auxiliar para converter ComposicaoTimeDTO em ComposicaoTime
    private ComposicaoTime convertToEntity(ComposicaoTimeDTO composicaoTimeDTO) {
        ComposicaoTime composicaoTime = new ComposicaoTime();
        Long composicaoTimeId = composicaoTimeDTO.getId();
        if (composicaoTimeId != null) {
            composicaoTime.setId(composicaoTimeId.longValue());
        }
        // Outras atribuições e lógica de conversão...
        return composicaoTime;
    }
}
