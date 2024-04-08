package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.IntegranteDTO;
import br.com.duxusdesafio.dto.TimeDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.service.IntegranteService;
import br.com.duxusdesafio.service.TimeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class MetodoController {

    @Autowired
    private IntegranteService integranteService;
    private final TimeService timeService;

    @GetMapping("/IntegranteMaisUsado")
    public ResponseEntity<IntegranteDTO> getAllIntegrante(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        IntegranteDTO integrantes = integranteService.integranteMaisComum(dataInicial, dataFinal);

        return new ResponseEntity<>(integrantes, HttpStatus.OK);
    }

    @GetMapping("/TimeDaData")
    public ResponseEntity<TimeDTO> getTimeDaData(
            @RequestParam("data")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        TimeDTO timeDto = timeService.timeDaData(data);

        // Adiciona a chave "data" com o valor da data solicitada
        if (timeDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Retorna a resposta com os times na data solicitada
        return new ResponseEntity<>(timeDto, HttpStatus.OK);
    }

    @GetMapping("/TimeMaisComum")
    public ResponseEntity<List<String>> getTimeMaisComum(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<String> nomesIntegrantesMaisComuns = timeService.timeMaisComum(dataInicial, dataFinal);

        return new ResponseEntity<>(nomesIntegrantesMaisComuns, HttpStatus.OK);
    }

    @GetMapping("/FranquiaMaisFamosa")
    public  ResponseEntity<Map<String, String>> getFranquiaFamosa(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        String franquiaMaisFamosa = timeService.franquiaMaisFamosa(dataInicial, dataFinal);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Franquia", franquiaMaisFamosa);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/FuncaoMaisComum")
    public ResponseEntity<Map<String, String>> getFuncaoMaisComum(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        String funcaoMaisComum = timeService.funcaoMaisComum(dataInicial, dataFinal);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Função", funcaoMaisComum);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/ContagemPorFranquia")
    public ResponseEntity<Map<String, Long>> getContagemFranquia(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        Map<String, Long> contagemDeFranquia = timeService.contagemPorFranquia(dataInicial, dataFinal);

        return new ResponseEntity<>(contagemDeFranquia, HttpStatus.OK);
    }


    @GetMapping("/ContagemPorFuncao")
    public ResponseEntity<Map<String, Long>> getContagemFuncao(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        Map<String, Long> contagemDeFuncao = timeService.contagemPorFuncao(dataInicial, dataFinal);

        return new ResponseEntity<>(contagemDeFuncao, HttpStatus.OK);
    }
}
