package br.com.britolmbs.screenmatch.principal;

import br.com.britolmbs.screenmatch.model.*;
import br.com.britolmbs.screenmatch.service.ConsumoApi;
import br.com.britolmbs.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();

    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=599ea6a9";

    private List<DadosSerie> dadosSeries = new ArrayList<>();

    public void exibeMenu() {

        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1- Buscar Séries
                    2- Buscar episódios
                    3- Listar séries Buscadas
                    0- Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção invalida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        dadosSeries.add(dados);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {

        System.out.println("Digite o nome da Serie: ");
        var nomeSerie = leitura.nextLine();

        String json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        return dados;
    }


        private void buscarEpisodioPorSerie(){
        DadosSerie dadosSerie =getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
           var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);
        }
        private void  listarSeriesBuscadas(){
        List<Serie> series = new ArrayList<>();
        series = dadosSeries.stream()
                        .map(d -> new Serie(d))
                                .collect(Collectors.toList());
       series.stream()
               .sorted(Comparator.comparing(Serie::getGenero))
               .forEach(System.out::println);
        }
}



////        for (int i = 0; i < dados.totalTemporadas(); i++) {
////            List<DadosEpisodio> episodiosTemporadas = temporadas.get(i).episodios();
////            for (int j = 0; j < episodiosTemporadas.size(); j++) {
////                System.out.println(episodiosTemporadas.get(j).titulo());
////            }
////
////        }
//        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
//
//        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
////                .collect(Collectors.toList());
//                .toList();
//
////        System.out.println("\nTop 5 Episodios: ");
////        dadosEpisodios.stream()
////                .filter(e -> !e.avalicao().equalsIgnoreCase("N/A"))
////                .peek(e -> System.out.println("Primeiro Filtro(N/A): " + e))
////                .sorted(Comparator.comparing(DadosEpisodio::avalicao).reversed())
////                .peek(e -> System.out.println("Ordenação: " + e))
////                .limit(10)
////                .peek(e -> System.out.println("Limite: " + e))
////                .forEach(System.out::println);
////
//        List<Episodio> episodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream()
//                        .map(d -> new Episodio(t.numero(), d))
//                ).toList();
//
//        episodios.forEach(System.out::println);
//
////        System.out.println("Digite o episodio desejado: ");
////
////        var trechoTitulo = leitura.nextLine();
////        Optional<Episodio> episodioBuscado = episodios.stream()
////                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
////                .findFirst();
////
////        if (episodioBuscado.isPresent()){
////            System.out.println("Episodio Encontrado!");
////            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
////        }else {
////            System.out.println("Episodio não encontrado!");
////        }
//
//
////        System.out.println("A partir de qye ano você deseja ver os episodios? ");
////        var ano = leitura.nextInt();
////        leitura.nextLine();
////
////        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
////
////        DateTimeFormatter formator = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////        episodios.stream()
////                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
////        .forEach(e -> System.out.println(
////                "Temporada: " + e.getTemporada() +
////                        " Episódio: " + e.getTitulo() +
////                        " Data de Lançamento: " + e.getDataLancamento().format(formator)
////        ));
//
//        Map<Integer, Double> avalicacoesPorTemporadas = episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada,
//                        Collectors.averagingDouble(Episodio::getAvaliacao)));
//
//        System.out.println(avalicacoesPorTemporadas);
//
//        DoubleSummaryStatistics est = episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
//
//        System.out.println("Média: " + est.getAverage());
//        System.out.println("Melhor Episodio: " + est.getMax());
//        System.out.println("Pior Episodio: " + est.getMin());
//        System.out.println("Quantidades de Episodios: " + est.getCount());
//

