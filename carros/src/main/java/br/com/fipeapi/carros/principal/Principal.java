package br.com.fipeapi.carros.principal;

import br.com.fipeapi.carros.model.Dados;
import br.com.fipeapi.carros.model.Modelos;
import br.com.fipeapi.carros.service.ConsumoApi;
import br.com.fipeapi.carros.service.ConverteDados;
import org.springframework.boot.web.reactive.context.GenericReactiveWebApplicationContext;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {
    Scanner leitura = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private ConverteDados conversor = new ConverteDados();
    private ConsumoApi consumoApi = new ConsumoApi();
    public void exibeMenu(){
        String menu = """
                *******OPÇÕES*****
                    Carro
                    Moto
                    Caminhão
                ******************
                Digite uma das opções para consulta:
                """;
        System.out.println(menu);
        String opcao = leitura.nextLine();
        String endereco;

        if (opcao.toLowerCase().contains("carr")){
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco= URL_BASE + "motos/marcas";
        }else{
            endereco = URL_BASE + "caminhoes/marcas";
        }
        String json = consumoApi.obterDados(endereco);
        System.out.println(json);
        List<Dados> marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);
        System.out.println("Informe o código da marca para consulta:");
        String codigoMarca = leitura.nextLine();

        endereco = endereco + "/" +codigoMarca + "/modelos";
        json= consumoApi.obterDados(endereco);
        var modeloList = conversor.obterDados(json, Modelos.class);

        System.out.println("\nModelos dessa Marca: ");
        modeloList.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);
    }
}
