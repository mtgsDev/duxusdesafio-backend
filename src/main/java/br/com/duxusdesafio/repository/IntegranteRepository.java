package br.com.duxusdesafio.repository;

import br.com.duxusdesafio.model.Integrante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntegranteRepository extends JpaRepository<Integrante, Long> {
    Optional<Integrante> findByNomeAndFuncaoAndFranquia(String nome, String funcao, String franquia);
}
