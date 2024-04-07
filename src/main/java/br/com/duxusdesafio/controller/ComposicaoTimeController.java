package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.ComposicaoTimeDTO;
import br.com.duxusdesafio.service.ComposicaoTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/composicoes-time")
public class ComposicaoTimeController {

    private final ComposicaoTimeService composicaoTimeService;

    @Autowired
    public ComposicaoTimeController(ComposicaoTimeService composicaoTimeService) {
        this.composicaoTimeService = composicaoTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ComposicaoTimeDTO>> listarComposicoes() {
        List<ComposicaoTimeDTO> composicoes = composicaoTimeService.listarComposicoes();
        return ResponseEntity.ok(composicoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComposicaoTimeDTO> buscarComposicaoPorId(@PathVariable Long id) {
        ComposicaoTimeDTO composicao = composicaoTimeService.buscarComposicaoPorId(id);
        return ResponseEntity.ok(composicao);
    }

    @PostMapping
    public ResponseEntity<ComposicaoTimeDTO> criarComposicao(@RequestBody ComposicaoTimeDTO composicaoTimeDTO) {
        ComposicaoTimeDTO novaComposicao = composicaoTimeService.criarComposicao(composicaoTimeDTO);
        return new ResponseEntity<>(novaComposicao, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarComposicao(@PathVariable Long id) {
        composicaoTimeService.deletarComposicao(id);
        return ResponseEntity.noContent().build();
    }
}
