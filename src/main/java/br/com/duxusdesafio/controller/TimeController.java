package br.com.duxusdesafio.controller;


import br.com.duxusdesafio.dto.IntegranteDTO;
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

    @GetMapping("/timeMaisComum")
    public ResponseEntity<List<String>> getTimeMaisComum(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<String> nomesIntegrantesMaisComuns = timeService.timeMaisComum(dataInicial, dataFinal);

        return new ResponseEntity<>(nomesIntegrantesMaisComuns, HttpStatus.OK);
    }

    @GetMapping("/FuncaoMaisComum")
    public ResponseEntity<Map<String, String>> getFuncaoMaisComum(@RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                     @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        String funcaoMaisComum = timeService.funcaoMaisComum(dataInicial, dataFinal);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Função", funcaoMaisComum);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }


}
