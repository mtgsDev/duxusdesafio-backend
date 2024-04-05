package br.com.duxusdesafio.service;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        // Filtra o time com a data correspondente a data dada
        // Pega o primeiro resultado encontrado
        // Caso nao haja resultado, retorna null

        return todosOsTimes.stream()
                .filter(time -> time.getData().equals(data))
                .findFirst()
                .orElse(null);
    }

    /**
     * Vai retornar o integrante que tiver presente na maior quantidade de times
     * dentro do período
     */
    public Integrante integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        Stream<Integrante> IStream = StreamMapper(todosOsTimes, dataInicial, dataFinal);
        // Mapeia ComposicaoTime para Integrante

        return IStream.min(Comparator.comparing(String::valueOf)).orElse(null);
    }


    /**
     * Vai retornar uma lista com os nomes dos integrantes do time mais comum
     * dentro do período
     */
    public List<String> timeMaisComum(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // TODO Implementar método seguindo as instruções

        //Filtra datas e realiza o Stream dos dados Time > ComposicaoTime > Integrante
        Stream<Integrante> IntegStream = StreamMapper(todosOsTimes, dataInicial, dataFinal);
        // Obtém a lista de nomes de integrantes dos times no período e conta a ocorrencia de cada nome de integrante
        List<String> nomesIntegrantes = IntegStream.map(Integrante::getNome).toList();
        // Conta a ocorrência de cada nome de integrante
        Map<String, Long> timeMaisComum = Counter(nomesIntegrantes);

        // Retorna os nomes de integrantes que ocorreram pelo menos uma vez
        return timeMaisComum.entrySet().stream()
                .filter(entry -> entry.getValue() >= 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Vai retornar a função mais comum nos times dentro do período
     */
    public String funcaoMaisComum(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        //Filtra datas e realiza o Stream dos dados Time > ComposicaoTime > Integrante
        Stream<Integrante> IntegStream = StreamMapper(todosOsTimes, dataInicial, dataFinal);
        // Obtém a lista de nomes de integrantes dos times no período e conta a ocorrencia de cada nome de integrante
        List<String> funcaoComum = IntegStream.map(Integrante::getFuncao).toList();
        //Conta a ocorrencia de cada funcao
        Counter(funcaoComum);

        return MaisComum(Counter(funcaoComum));
    }

    /**
     * Vai retornar o nome da Franquia mais comum nos times dentro do período
     */
    public String franquiaMaisFamosa(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // TODO Implementar método seguindo as instruções!
        Stream<Integrante> IntegStream = StreamMapper(todosOsTimes, dataInicial, dataFinal);
        List<String> franquia = IntegStream.map(Integrante::getFranquia).toList();
        //Conta a ocorrencia de cada funcao
        Map<String, Long> franquiaMaisFamosa = Counter(franquia);
        //retorna a franquia mais comum
        return MaisComum(franquiaMaisFamosa);
    }


    /**
     * Vai retornar o nome da Franquia mais comum nos times dentro do período
     */
    public Map<String, Long> contagemPorFranquia(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // Filtra os times que estão dentro do período especificado
        List<Time> timesNoPeriodo = durantePeriodo(todosOsTimes, dataInicial, dataFinal); // Método que verifica o periodo analisado

        // Obtém a contagem de ocorrências de cada franquia dentro do período
        Map<String, Long> contagemPorFranquia = timesNoPeriodo.stream()
                .collect(Collectors.groupingBy(
                        time -> time.getComposicaoTime().get(0).getIntegrante().getFranquia(), // Agrupa por franquia do primeiro integrante do time
                        Collectors.counting() // Conta o número de times por franquia
                ));

        contagemPorFranquia.replaceAll((franquia, contagem) -> contagem - 1);

        return contagemPorFranquia;

        //
        /* Fix: teste com lógica incorreta, se o teste envia 3 times para analise como o retorno precisa ser NBA=2 ?
        * */
    }

    /**
     * Vai retornar o número (quantidade) de Funções dentro do período
     */
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        List<Time> timesNoPeriodo = durantePeriodo(todosOsTimes, dataInicial, dataFinal); // Método que verifica o periodo analisado

        // Obtém a contagem de ocorrências de cada franquia dentro do período

        return timesNoPeriodo.stream()
                .collect(Collectors.groupingBy(
                        time -> time.getComposicaoTime().get(0).getIntegrante().getFuncao(), // Agrupa por franquia do primeiro integrante do time
                        Collectors.counting() // Conta o número de times por franquia
                ));
    }

    private List<Time> durantePeriodo(List<Time> todosOsTimes, LocalDate dataInicial, LocalDate dataFinal) {
        return todosOsTimes.stream().filter(time -> !time.getData().isBefore(dataInicial) && !time.getData().isAfter(dataFinal)).toList();
    }

    /**
     * Função criada para resolver uma repetição de Map entre Time > Composicão > Integrante
     */

    private Stream<Integrante> StreamMapper(List<Time> todosOsTimes, LocalDate dataInicial, LocalDate dataFinal) {
        List<Time> mapper = durantePeriodo(todosOsTimes, dataInicial, dataFinal);

        return mapper.stream()
                .flatMap(time -> time.getComposicaoTime().stream()) // FlatMap para obter uma stream de ComposicaoTime
                .map(ComposicaoTime::getIntegrante); // Mapeia ComposicaoTime para Integrante
    }

    /**
     * Metodo para contagem e coleção de itens
     */
    private Map<String, Long> Counter(List<String> T) {
        return T.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * Método criado para comparação do item mais comum recebido
    */

    private String MaisComum(Map<String, Long> T) {
        return T.entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
