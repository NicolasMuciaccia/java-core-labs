package br.com.java.core.labs.functional;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

// Estude interfaces funcionais: Predicate, Function, Consumer e Supplier.
// Elas são a base para lambdas e programação reativa em Java.
public class FunctionalInterfacesTest {

    @Test
    public void testPredicateBasico() {
        Predicate<String> isEmail = s -> s.contains("@") && s.contains(".");

        assertTrue(isEmail.test("user@email.com"));
        assertFalse(isEmail.test("nao-e-email"));
    }

    @Test
    public void testPredicateComposicao() {
        Predicate<Integer> positivo = n -> n > 0;
        Predicate<Integer> menor100 = n -> n < 100;

        Predicate<Integer> entre0e100 = positivo.and(menor100);
        Predicate<Integer> foraDaFaixa = entre0e100.negate();

        assertTrue(entre0e100.test(50));
        assertFalse(entre0e100.test(150));
        assertTrue(foraDaFaixa.test(-1));
    }

    @Test
    public void testPredicateEmFilter() {
        List<String> nomes = List.of("Ana", "Bruno", "Amanda", "Carlos", "Alice");
        Predicate<String> comecaComA = s -> s.startsWith("A");

        List<String> filtrado = nomes.stream()
                .filter(comecaComA)
                .toList();

        assertEquals(List.of("Ana", "Amanda", "Alice"), filtrado);
    }

    @Test
    public void testFunctionBasica() {
        Function<String, Integer> tamanho = String::length;

        assertEquals(4, tamanho.apply("Java"));
        assertEquals(0, tamanho.apply(""));
    }

    @Test
    public void testFunctionComposicaoAndThen() {
        Function<String, String>  trimmer    = String::trim;
        Function<String, String>  upperCase  = String::toUpperCase;
        Function<Integer, String> intToStr   = Object::toString;

        Function<String, String> normalizar = trimmer.andThen(upperCase);

        assertEquals("JAVA CORE", normalizar.apply("    jAvA cORe   "));
    }

    @Test
    public void testFunctionComposicaoCompose() {
        Function<Integer, Integer> vezesDois = x -> x * 2;
        Function<Integer, Integer> maisCinco = x -> x + 5;

        Function<Integer, Integer> multiplicaSoma = maisCinco.compose(vezesDois);
        Function<Integer, Integer> somaMultiplica = vezesDois.compose(maisCinco);

        assertEquals(15, multiplicaSoma.apply(5));
        assertEquals(20, somaMultiplica.apply(5));
    }

    @Test
    public void testConsumerBasico() {
        List<String> log = new ArrayList<>();
        Consumer<String> registrar = mensagem -> log.add("[LOG] " + mensagem);

        registrar.accept("Início do processo");
        registrar.accept("Fim do processo");

        assertEquals(2, log.size());
        assertTrue(log.get(0).startsWith("[LOG] "));
    }

    @Test
    public void testConsumerAndThen() {
        List<String> acoes = new ArrayList<>();
        Consumer<String> salvar = s -> acoes.add("salvo: " + s);
        Consumer<String> auditar = s -> acoes.add("auditado: " + s);

        Consumer<String> pipeline = salvar.andThen(auditar);
        pipeline.accept("Pedido#123");

        assertEquals(2, acoes.size());
        assertEquals("salvo: Pedido#123", acoes.get(0));
        assertEquals("auditado: Pedido#123", acoes.get(1));
    }

    @Test
    public void testSupplierBasico() {
        Supplier<List<String>> novaLista = ArrayList::new;

        List<String> l1 = novaLista.get();
        List<String> l2 = novaLista.get();

        assertNotSame(l1, l2, "Instâncias diferentes");
    }

    @Test
    public void testSupplierLazyEvaluation() {
        // orElseGet() aceita Supplier: o valor só é calculado se necessário.
        // orElse() avaliaria a expressão SEMPRE, mesmo se Optional não estiver vazio.
        Optional<String> opt = Optional.of("Java");

        List<String> chamadas = new ArrayList<>();
        Supplier<String> caroDeCalcular = () -> {
          chamadas.add("calculado");
          return "Padrão caro";
        };

        String resultado = opt.orElseGet(caroDeCalcular);

        assertEquals("Java", resultado);
        assertEquals(0, chamadas.size());
    }

    @Test
    public void testBiFunctionDoisParametros() {
        BiFunction<String, Integer, String> repetir =
                (texto, vezes) -> texto.repeat(vezes);

        assertEquals("Java Java Java ", repetir.apply("Java ", 3));
    }

    @Test
    public void testUnaryOperatorEspecializacao() {
        UnaryOperator<String> criptografar = s -> new StringBuilder(s).reverse().toString();
        assertEquals("avaJ", criptografar.apply("Java"));
    }
}