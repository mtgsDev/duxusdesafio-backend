package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.IntegranteDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
import br.com.duxusdesafio.service.IntegranteService;
import br.com.duxusdesafio.service.TimeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/integrantes")
public class IntegranteController {

    @Autowired
    private IntegranteService integranteService;
    @Autowired
    private ApiService apiService;
    @Autowired
    private TimeRepository timeRepository;

    /*
    * erro 500;
    * */
//    @GetMapping
//    public ResponseEntity<List<IntegranteDTO>> getAllIntegrantes() {
//        try {
//            List<IntegranteDTO> integrantes = integranteService.buscarTodosIntegrantes();
//            return ResponseEntity.ok(integrantes);
//        } catch (Exception e) {
//            // Em caso de exceção, retornar uma mensagem de erro com status 500
//            String errorMessage = "Erro ao buscar todos os integrantes: " + e.getMessage();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<IntegranteDTO> buscarIntegrantePorId(@PathVariable Long id) {
        IntegranteDTO integranteDTO = integranteService.buscarIntegrantePorId(id);
        if (integranteDTO != null) {
            return ResponseEntity.ok(integranteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntegranteDTO> atualizarIntegrante(@PathVariable Long id, @RequestBody IntegranteDTO integranteDTO) {
        IntegranteDTO integranteAtualizado = integranteService.atualizarIntegrante(id, integranteDTO);
        if (integranteAtualizado != null) {
            return ResponseEntity.ok(integranteAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarIntegrante(@PathVariable Long id) {
        integranteService.deletarIntegrante(id);
        return ResponseEntity.noContent().build();
    }

    // Criação API REST
    @PostMapping
    public ResponseEntity<Object> createIntegrante(@RequestBody IntegranteDTO integranteDTO) {
        try {
            IntegranteDTO novoIntegrante = integranteService.criarIntegrante(integranteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoIntegrante);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe. Verifique os dados fornecidos."); // Ou outra mensagem de erro
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocorreu um erro inesperado. Detalhes: " + e.getMessage());
    }

    @GetMapping("/integrantemaisusado")
    public ResponseEntity<Object> integranteMaisUsado(
            // Pegando a data dos parâmetros da requisição
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataFinal)
    {
        LocalDate dataInicialReal = dataInicial.orElse(LocalDate.MIN);
        LocalDate dataFinalReal = dataFinal.orElse(LocalDate.MAX);

        // Coloca todos os times em uma lista de Time
        List<Time> times = timeRepository.findAll();
        // Chama o método timeDaData e passa data e a lista de times como argumento
        Integrante integrante = apiService.integranteMaisUsado(dataInicialReal, dataFinalReal, times);
        // Criando um Map que irá receber o integrante mais usado
        Map<String, Object> integranteMaisUsado = new HashMap<>();

        // Passando a key "integrante mais usado" e pegando o nome do integrante
        integranteMaisUsado.put("Integrante Mais Usado", integrante.getNome());

        //Retorno da resposta
        return new ResponseEntity<>(integranteMaisUsado, HttpStatus.OK);
    }
}