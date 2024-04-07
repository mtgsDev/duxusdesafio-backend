package br.com.duxusdesafio.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeDTO {
    private Long id;
    @NotNull
    private String nome;
    @NotNull
    private LocalDate data;
}
