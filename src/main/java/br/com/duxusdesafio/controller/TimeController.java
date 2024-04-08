package br.com.duxusdesafio.controller;


import br.com.duxusdesafio.dto.TimeDTO;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.service.ApiService;
import br.com.duxusdesafio.service.IntegranteService;
import br.com.duxusdesafio.service.TimeService;
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


@RestController
@RequestMapping("api/times")
public class TimeController {

    private final TimeService timeService;
    private final IntegranteService integranteService;
    private final ApiService apiService;

    @Autowired
    public TimeController(TimeService timeService, IntegranteService integranteService, ApiService apiService) {
        this.timeService = timeService;
        this.apiService = apiService;
        this.integranteService = integranteService;
    }

    @PostMapping
    public ResponseEntity<TimeDTO> criarTime(@RequestBody TimeDTO timeDTO) {
        TimeDTO novoTime = timeService.criarTime(timeDTO);
        return new ResponseEntity<>(novoTime, HttpStatus.CREATED);
    }

    @PostMapping("/criar-com-integrantes")
    public ResponseEntity<TimeDTO> criarTimeComIntegrantes(@RequestBody TimeDTO timeDTO) {
        TimeDTO createdTime = timeService.criarTimeComIntegrantes(timeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTime);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TimeDTO> buscarTimePorId(@PathVariable Long id) {
        TimeDTO time = timeService.buscarTimePorId(id);
        return ResponseEntity.ok(time);
    }

    @GetMapping
    public ResponseEntity<List<TimeDTO>> listarTodosTimes() {
        List<TimeDTO> times = timeService.listarTodosTimes();
        return ResponseEntity.ok(times);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeDTO> atualizarTime(@PathVariable Long id, @RequestBody TimeDTO timeDTO) {
        TimeDTO timeAtualizado = timeService.atualizarTime(id, timeDTO);
        return ResponseEntity.ok(timeAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTime(@PathVariable Long id) {
        timeService.deletarTime(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/TimeDaData")
    public ResponseEntity<TimeDTO> getTimeDaData(
            @RequestParam("data")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        // Chama o serviço para obter os times na data especificada
        TimeDTO timeDto = timeService.timeDaData(data);

        // Adiciona a chave "data" com o valor da data solicitada
        if (timeDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Retorna a resposta com os times na data solicitada

        return new ResponseEntity<>(timeDto, HttpStatus.OK);
    }

    @GetMapping("/timemaiscomum")
    public ResponseEntity<Map<String, Object>> timeMaiscomum(
            // Pegando a data dos parâmetros da requisição
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataFinal) {

        // Verifica se a primeira data é nula
        LocalDate dataInicialReal = dataInicial.orElse(LocalDate.MIN);

        // Verifica se a segunda data é nula
        LocalDate dataFinalReal = dataFinal.orElse(LocalDate.MAX);

        // Coloca todos os times em uma lista de Time
//        List<Time> times = timeService.findAll();
        // Chama o método timeDaData e passa data e a lista de times como argumento
//        List<String> integrantes = apiService.timeMaisComum(dataInicialReal, dataFinalReal, times);
        // Criando um Map que irá receber o integrante mais usado
        Map<String, Object> timeMaisComum = new HashMap<>();

        // Passando a key "integrante mais usado" e pegando o nome do integrante
//        timeMaisComum.put("Time mais comum:", integrantes);

        //Retorno da resposta
        return new ResponseEntity<>(timeMaisComum, HttpStatus.OK);
    }

    @GetMapping("/funcaomaiscomum")
    public ResponseEntity<Map<String, Object>> funcaoMaisComum(
            // Pegando a data dos parâmetros da requisição
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataFinal) {

        // Verifica se a primeira data é nula
        LocalDate dataInicialReal = dataInicial.orElse(LocalDate.MIN);

        // Verifica se a segunda data é nula
        LocalDate dataFinalReal = dataFinal.orElse(LocalDate.MAX);

        // Coloca todos os times em uma lista de Time
//        List<Time> times = timeService.findAll();
        // Chama o método timeDaData e passa data e a lista de times como argumento
//        String funcao = apiService.funcaoMaisComum(dataInicialReal, dataFinalReal, times);
        // Criando um Map que irá receber o integrante mais usado
        Map<String, Object> funcaoMaisComum = new HashMap<>();

        // Passando a key "integrante mais usado" e pegando o nome do integrante
//        funcaoMaisComum.put("Função mais comum:", funcao);

        //Retorno da resposta
//        return new ResponseEntity<>(funcaoMaisComum, HttpStatus.OK);
        return null;

    }


    @GetMapping("/franquiamaisfamosa")
    public ResponseEntity<Map<String, Object>> franquiaMaisFamosa(
            // Pegando a data dos parâmetros da requisição
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataFinal) {

        // Verifica se a primeira data é nula
        LocalDate dataInicialReal = dataInicial.orElse(LocalDate.MIN);

        // Verifica se a segunda data é nula
        LocalDate dataFinalReal = dataFinal.orElse(LocalDate.MAX);

        // Coloca todos os times em uma lista de Time
//        List<Time> times = timeService.findAll();
        // Chama o método timeDaData e passa data e a lista de times como argumento
//        String franquia = apiService.franquiaMaisFamosa(dataInicialReal, dataFinalReal, times);
        // Criando um Map que irá receber o integrante mais usado
        Map<String, Object> franquiaMaisFamosa = new HashMap<>();

        // Passando a key "integrante mais usado" e pegando o nome do integrante
//        franquiaMaisFamosa.put("Franquia mais famosa:", franquia);

        //Retorno da resposta
//        return new ResponseEntity<>(franquiaMaisFamosa, HttpStatus.OK);
        return null;

    }

    @GetMapping("/contagemfranquia")
    public ResponseEntity<Map<String, Long>> contagemPorFranquia(
            // Pegando a data dos parâmetros da requisição
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataFinal) {

        // Verifica se a primeira data é nula
        LocalDate dataInicialReal = dataInicial.orElse(LocalDate.MIN);

        // Verifica se a segunda data é nula
        LocalDate dataFinalReal = dataFinal.orElse(LocalDate.MAX);

        // Coloca todos os times em uma lista de Time
//        List<Time> times = timeService.findAll();
        // Chama o método timeDaData e passa data e a lista de times como argumento
//        Map<String, Long> franquia = apiService.contagemPorFranquia(dataInicialReal, dataFinalReal, times);

        //Retorno da resposta
//        return new ResponseEntity<>(franquia, HttpStatus.OK);
        return null;

    }

    @GetMapping("/contagemfuncao")
    public ResponseEntity<Map<String, Long>> contagemPorFuncao(
            // Pegando a data dos parâmetros da requisição
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dataFinal) {

        // Verifica se a primeira data é nula
        LocalDate dataInicialReal = dataInicial.orElse(LocalDate.MIN);

        // Verifica se a segunda data é nula
        LocalDate dataFinalReal = dataFinal.orElse(LocalDate.MAX);

        // Coloca todos os times em uma lista de Time
//        List<Time> times = timeService.findAll();
        // Chama o método timeDaData e passa data e a lista de times como argumento
//        Map<String, Long> franquia = apiService.contagemPorFuncao(dataInicialReal, dataFinalReal, times);

        //Retorno da resposta
//        return new ResponseEntity<>(franquia, HttpStatus.OK);
        return null;
    }
}
