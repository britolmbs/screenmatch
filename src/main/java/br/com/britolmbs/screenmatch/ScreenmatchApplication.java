package br.com.britolmbs.screenmatch;

import br.com.britolmbs.screenmatch.model.DadosEpisodio;
import br.com.britolmbs.screenmatch.model.DadosSerie;
import br.com.britolmbs.screenmatch.model.DadosTemporada;
import br.com.britolmbs.screenmatch.service.ConsumoApi;
import br.com.britolmbs.screenmatch.service.ConverteDado;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoApi consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=Breaking+Bad&apikey=599ea6a9");
		System.out.println(json);

		ConverteDado conversor = new ConverteDado();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		json = consumoApi.obterDados("https://www.omdbapi.com/?t=Breaking+Bad&season=1&episode=2&apikey=599ea6a9");

		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);

		List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i<= dados.totalTemporadas(); i++){
		json = consumoApi.obterDados("https://www.omdbapi.com/?t=Breaking+Bad&season="+ i +"&apikey=599ea6a9");
		DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
		temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);



	}
}
