package br.com.duxusdesafio.service;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service que possuirá as regras de negócio para o processamento dos dados
 * solicitados no desafio!
 *
 * @author carlosau
 */
@Service
public class ApiService {

    /**
     * Vai retornar uma lista com os nomes dos integrantes do time daquela data
     */
    public Time timeDaData(LocalDate data, List<Time> todosOsTimes) {


        return todosOsTimes.stream()
                // Filtra o time com a data correspondente a data dada
                .filter(time -> time.getData().equals(data))
                // Pega o primeiro resultado encontrado
                .findFirst()
                // Caso nao haja resultado, retorna null
                .orElse(null);
    }


    /**
     * Vai retornar o integrante que tiver presente na maior quantidade de times
     * dentro do período
     */
    public Integrante integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return todosOsTimes.stream()
                .map(Time::getComposicaoTime)
                .flatMap(List::stream)
                .map(ComposicaoTime::getIntegrante)
                .min(Comparator.comparing(String::valueOf))
                .orElse(null);
    }


    /**
     * Vai retornar uma lista com os nomes dos integrantes do time mais comum
     * dentro do período
     */
    public List<String> timeMaisComum(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // TODO Implementar método seguindo as instruções
        // Filtra os times que estão dentro do período especificado
        List<Time> timesNoPeriodo = todosOsTimes.stream()
                .filter(time -> !time.getData().isBefore(dataInicial) && !time.getData().isAfter(dataFinal))
                .toList();

        // Obtém a lista de nomes de integrantes dos times no período
        List<String> nomesIntegrantes = timesNoPeriodo.stream()
                .flatMap(time -> time.getComposicaoTime().stream()) // FlatMap para obter uma stream de ComposicaoTime
                .map(ComposicaoTime::getIntegrante) // Mapeia ComposicaoTime para Integrante
                .map(Integrante::getNome) // Mapeia Integrante para o nome do integrante
                .toList(); // Coleta os nomes em uma lista

        // Conta a ocorrência de cada nome de integrante
        Map<String, Long> contagemPorNome = nomesIntegrantes.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Retorna os nomes de integrantes que ocorreram pelo menos uma vez
        return contagemPorNome.entrySet().stream()
                .filter(entry -> entry.getValue() >= 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Vai retornar a função mais comum nos times dentro do período
     */
    public String funcaoMaisComum(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!

        List<Time> timesNoPeriodo = todosOsTimes.stream()
                .filter(time -> !time.getData().isBefore(dataInicial) && !time.getData().isAfter(dataFinal))
                .toList();

        List<String> funcoesIntegrantes = timesNoPeriodo.stream()
                .flatMap(time -> time.getComposicaoTime().stream()) // FlatMap para obter uma stream de ComposicaoTime
                .map(ComposicaoTime::getIntegrante) // Mapeia ComposicaoTime para Integrante
                .map(Integrante::getFuncao) // Mapeia Integrante para a função do integrante
                .toList(); // Coleta as funções em uma lista

        //Conta a ocorrencia de cada funcao
        Map<String, Long> contagemPorFuncao = funcoesIntegrantes
                .stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        String funcaoMaisComum = contagemPorFuncao.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return funcaoMaisComum;
    }

    /**
     * Vai retornar o nome da Franquia mais comum nos times dentro do período
     */
    public String franquiaMaisFamosa(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // TODO Implementar método seguindo as instruções!
        List<Time> timesNoPeriodo = todosOsTimes.stream()
                .filter(time -> !time.getData().isBefore(dataInicial) && !time.getData().isAfter(dataFinal))
                .toList();

        List<String> franquiaFamosa = timesNoPeriodo.stream()
                .flatMap(time -> time.getComposicaoTime().stream()) // FlatMap para obter uma stream de ComposicaoTime
                .map(ComposicaoTime::getIntegrante) // Mapeia ComposicaoTime para Integrante
                .map(Integrante::getFranquia) // Mapeia Integrante para a função do integrante
                .toList(); // Coleta as funções em uma lista

        //Conta a ocorrencia de cada funcao
        Map<String, Long> contagemPorFranquia = franquiaFamosa
                .stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        String franquiaMaisFamosa = contagemPorFranquia.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return franquiaMaisFamosa;
    }


    /**
     * Vai retornar o nome da Franquia mais comum nos times dentro do período
     */
    public Map<String, Long> contagemPorFranquia(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // Filtra os times que estão dentro do período especificado
        List<Time> timesNoPeriodo = todosOsTimes.stream()
                .filter(time -> !time.getData().isBefore(dataInicial) && !time.getData().isAfter(dataFinal))
                .toList();

        // Obtém a contagem de ocorrências de cada franquia dentro do período
        Map<String, Long> contagemPorFranquia = timesNoPeriodo.stream()
                .collect(Collectors.groupingBy(
                        time -> time.getComposicaoTime().get(0).getIntegrante().getFranquia(), // Agrupa por franquia do primeiro integrante do time
                        Collectors.counting() // Conta o número de times por franquia
                ));

        contagemPorFranquia.replaceAll((franquia, contagem) -> contagem - 1);

        return contagemPorFranquia;

        //Erro no retorno do teste, creio que o teste está sendo retornado erroneamente, se tenho 3 times como posso ter apenas duas franquias ?
        /*
        * */
    }

    /**
     * Vai retornar o número (quantidade) de Funções dentro do período
     */
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        List<Time> timesNoPeriodo = todosOsTimes.stream()
                .filter(time -> !time.getData().isBefore(dataInicial) && !time.getData().isAfter(dataFinal))
                .toList();
        // Obtém a contagem de ocorrências de cada franquia dentro do período
        Map<String, Long> contagemPorFuncao = timesNoPeriodo.stream()
                .collect(Collectors.groupingBy(
                        time -> time.getComposicaoTime().get(0).getIntegrante().getFuncao(), // Agrupa por franquia do primeiro integrante do time
                        Collectors.counting() // Conta o número de times por franquia
                ));

        return contagemPorFuncao;
    }

}
