package br.com.java.core.labs.fundamentos;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Estude as palavras-chave final, static, this, super e overload.
// Elas controlam imutabilidade, contexto de classe/instância e hierarquia de métodos.
public class PalavrasChaveTest {

    static int contadorGlobal = 0;

    static class Instancia {
        static int total = 0;
        final String nome;

        Instancia(String nome) {
            this.nome = nome;
            total++;
        }
    }

    @Test
    public void testStaticPertenceAClasse() {
        Instancia a = new Instancia("Alpha");
        Instancia b = new Instancia("Beta");

        assertEquals(2, Instancia.total);
    }

    @Test
    public void testFinalEmVariavelLocal() {
        final int limite = 100;

        // Tentar reatribuir causaria erro de compilação: limite = 200;
        assertEquals(100, limite);
    }

    @Test
    public void testFinalNaoImpedeAlteracaoDoConteudo() {
        final List<String> lista = new ArrayList<>();

        lista.add("Java");
        lista.add("Angular");

        // lista = new ArrayList<>();  // Erro de compilação
        assertEquals(2, lista.size());
    }


    // OVERLOAD

    static class Calculadora {
        int somar(int a, int b) {
            return a + b;
        }

        double somar(double a, double b) {
            return a + b;
        }

        int somar (int a, int b, int c) {
            return a + b + c;
        }
    }

    @Test
    public void testOverloadResolucaoPelaAssinatura() {
        Calculadora calc = new Calculadora();

        assertEquals(5, calc.somar(2,3));
        assertEquals(5.5, calc.somar(2.2,3.3), 0.001);
        assertEquals(6, calc.somar(2, 2,2));
    }


    static class Veiculo {
        String tipo;
        int velocidadeMax;

        Veiculo(String tipo, int velocidadeMax) {
            this.tipo = tipo;
            this.velocidadeMax = velocidadeMax;
        }
    }

    static class Carro extends Veiculo {
        String modelo;

        Carro(String modelo) {
            super("Automóvel", 200);
            this.modelo = modelo;
        }

        Carro() {
            this("Modelo Padrão");
        }
    }

    @Test
    public void testSuperDelegaAoPai(){
        Carro carro = new Carro("Fusca");

        assertEquals("Automóvel", carro.tipo);
        assertEquals(200, carro.velocidadeMax);
        assertEquals("Fusca", carro.modelo);
    }

    @Test
    public void testThisDelegaParaOutroConstrutorDaMesmaClasse(){
        Carro carro = new Carro();

        assertEquals("Automóvel", carro.tipo);
        assertEquals(200, carro.velocidadeMax);
        assertEquals("Modelo Padrão", carro.modelo);
    }
}



















