package br.com.britolmbs.screenmatch.principal;

import br.com.britolmbs.screenmatch.model.DadosEpisodio;
import br.com.britolmbs.screenmatch.model.DadosSerie;
import br.com.britolmbs.screenmatch.model.DadosTemporada;
import br.com.britolmbs.screenmatch.model.Episodio;
import br.com.britolmbs.screenmatch.service.ConsumoApi;
import br.com.britolmbs.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
   private Scanner leitura = new Scanner(System.in);

   private ConsumoApi consumo = new ConsumoApi();

   private ConverteDados conversor = new ConverteDados();

   private final String ENDERECO = "https://www.omdbapi.com/?t=";
   private final String API_KEY = "&apikey=599ea6a9";

    public void exibeMenu(){
        System.out.println("Digite o nome da Serie: ");
        var nomeSerie = leitura.nextLine();

        String json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++ ){
            json =consumo.obterDados(ENDERECO + nomeSerie.replace(" " , "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporadas = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporadas.size(); j++) {
//                System.out.println(episodiosTemporadas.get(j).titulo());
//            }
//
//        }
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
                .toList();

        System.out.println("\nTop 5 Episodios: ");
        dadosEpisodios.stream()
                .filter(e -> !e.avalicao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avalicao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).toList();

        episodios.forEach(System.out::println);

        System.out.println("A partir de qye ano você deseja ver os episodios? ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formator = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
        .forEach(e -> System.out.println(
                "Temporada: " + e.getTemporada() +
                        " Episódio: " + e.getTitulo() +
                        " Data de Lançamento: " + e.getDataLancamento().format(formator)
        ));
    }
}
