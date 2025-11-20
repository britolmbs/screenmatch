package br.com.britolmbs.screenmatch;

import br.com.britolmbs.screenmatch.model.DadosSerie;
import br.com.britolmbs.screenmatch.service.ConsumoApi;
import br.com.britolmbs.screenmatch.service.ConverteDado;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
	}
}
