package br.com.duxusdesafio.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntegranteDTO {
    private Long id;
    @NotBlank
    private String franquia;
    @NotBlank
    private String nome;
    @NotBlank
    private String funcao;
}
