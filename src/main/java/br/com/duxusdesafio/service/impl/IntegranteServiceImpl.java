package br.com.duxusdesafio.service.impl;
import br.com.duxusdesafio.dto.IntegranteDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.service.IntegranteService;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class IntegranteServiceImpl implements IntegranteService {

    private final IntegranteRepository integranteRepository;

    public IntegranteServiceImpl(IntegranteRepository integranteRepository) {
        this.integranteRepository = integranteRepository;
    }

    public IntegranteDTO criarIntegrante(IntegranteDTO integranteDTO) {
        if (integranteJaExistente(integranteDTO)) {
            throw new IllegalArgumentException("Integrante com os mesmos atributos já existe.");
        }

        Integrante integrante = new Integrante();
        integrante.setNome(integranteDTO.getNome());
        integrante.setFuncao(integranteDTO.getFuncao());
        integrante.setFranquia(integranteDTO.getFranquia());

        Integrante novoIntegrante = integranteRepository.save(integrante);
        return toDTO(novoIntegrante);
    }

    public IntegranteDTO buscarIntegrantePorId(Long id) {
        Optional<Integrante> integranteOptional = integranteRepository.findById(id);
        return integranteOptional.map(this::toDTO).orElse(null);
    }

    public IntegranteDTO atualizarIntegrante(Long id, IntegranteDTO integranteDTO) {
        Optional<Integrante> integranteOptional = integranteRepository.findById(id);
        if (integranteOptional.isPresent()) {
            Integrante integrante = integranteOptional.get();
            integrante.setNome(integranteDTO.getNome());
            integrante.setFuncao(integranteDTO.getFuncao());
            integrante.setFranquia(integranteDTO.getFranquia());

            Integrante integranteAtualizado = integranteRepository.save(integrante);
            return toDTO(integranteAtualizado);
        }
        return null;
    }

    public void deletarIntegrante(Long id) {
        integranteRepository.deleteById(id);
    }

    private IntegranteDTO toDTO(Integrante integrante) {
        IntegranteDTO integranteDTO = new IntegranteDTO();
        integranteDTO.setId(integrante.getId());
        integranteDTO.setNome(integrante.getNome());
        integranteDTO.setFuncao(integrante.getFuncao());
        integranteDTO.setFranquia(integrante.getFranquia());
        return integranteDTO;
    }

    /*
    * erro 500
    * */
//    @Override
//    public List<IntegranteDTO> buscarTodosIntegrantes() {
//        List<Integrante> integrantes = integranteRepository.findAll();
//        return integrantes.stream()
//                .map(this::toDTO)
//                .collect(Collectors.toList());
//    }

    private boolean integranteJaExistente(IntegranteDTO integranteDto) {
        String nome = integranteDto.getNome();
        String funcao = integranteDto.getFuncao();
        String franquia = integranteDto.getFranquia();

        // Consultar se existe algum integrante com os mesmos atributos
        return integranteRepository.findByNomeAndFuncaoAndFranquia(nome, funcao, franquia).isPresent();
    }
}
