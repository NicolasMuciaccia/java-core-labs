package br.com.java.core.labs.functional;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Estude Optional: of, ofNullable, empty, isPresent, ifPresent, orElse e orElseThrow.
// Use Optional para tratar valores que podem estar ausentes e evitar NullPointerException.
public class OptionalTest {

    @Test
    public void testOfExigeValorNaoNulo() {
        Optional<String> opt = Optional.of("Java 21");

        assertTrue(opt.isPresent());
        assertEquals("Java 21", opt.get());

        assertThrows(NullPointerException.class, () -> Optional.of(null));
    }

    @Test
    public void testOfNullableAceitaNulo() {
        Optional<String> comValor = Optional.ofNullable("Kotlin");
        Optional<String> semValor = Optional.ofNullable(null);

        assertTrue(comValor.isPresent());
        assertTrue(semValor.isEmpty());
    }

    @Test
    public void testEmptyCriaOptionalVazio() {
        Optional<String> vazio = Optional.empty();
        assertFalse(vazio.isPresent());
    }

    @Test
    public void testIfPresentExecutaApenasSePresente() {
        Optional<String> opt = Optional.of("mensagem");
        List<String> log = new ArrayList<String>();

        opt.ifPresent(v -> log.add("Recebido: " + v));
        Optional.empty().ifPresent(v -> log.add("Não deveria adicionar"));

        assertEquals(1, log.size());
        assertEquals("Recebido: mensagem", log.getFirst());
    }

    @Test
    public void testIfPresentOrElseJava9() {
        List<String> acoes = new ArrayList<>();

        Optional.of("presente").ifPresentOrElse(
        v -> acoes.add("valor: " + v),
              () -> acoes.add("estava vazio")
        );

        Optional.<String>empty().ifPresentOrElse(
                v -> acoes.add("não deveria"),
                () -> acoes.add("estava vazio")
        );

        assertEquals("valor: presente", acoes.getFirst());
        assertEquals("estava vazio", acoes.get(1));
    }

    @Test
    public void testOrElseRetornaDefaultSeVazio() {
        String resultado = Optional.<String>empty().orElse("Padrão");
        assertEquals(resultado, "Padrão");

        String presente = Optional.of("Java").orElse("Padrão");
        assertEquals(presente, "Java");
    }

    @Test
    public void testOrElseGetAvaliaLazily() {
        List<String> chamadas = new ArrayList<>();

        String response = Optional.of("Java").orElseGet(() -> {
            chamadas.add("não deveria");
            return "Padrão lazy";
        });

        assertEquals(0, chamadas.size());
        assertEquals(response, "Java");

        String fallback = Optional.<String>empty().orElseGet(() -> {
           chamadas.add("Default padrão");
           return "Padrão lazy";
        });

        assertEquals(1, chamadas.size());
        assertEquals("Padrão lazy", fallback);
    }

    @Test
    public void testOrElseThrowLancaExcecaoSeVazio() {
        assertThrows(NoSuchElementException.class,
                () -> Optional.empty().orElseThrow());
        assertThrows(IllegalStateException.class,
                () -> Optional.<String>empty()
                        .orElseThrow(() -> new IllegalStateException("Banco sem retorno")));
        // Se presente, retorna o valor sem lançar.
        String valor = Optional.of("ok").orElseThrow();
        assertEquals("ok", valor);
    }

    @Test
    public void testMapTransformaOValor() {
        Optional<String> nome = Optional.of("  java  ");
        Optional<String> normalizado = nome.map(String::trim).map(String::toUpperCase);
        assertEquals("JAVA", normalizado.orElseThrow());
    }

    @Test
    public void testMapEmOptionalVazioNaoExecuta() {
        Optional<String> resultado = Optional.<String>empty()
                .map(String::toUpperCase);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testFlatMapEvitaOptionalAninhado() {
        Optional<String> usuario = Optional.of("user123");
        Optional<String> email = usuario.flatMap(u ->
                u.equals("user123")
                        ? Optional.of("user123@empresa.com")
                        : Optional.empty()
        );
        assertEquals("user123@empresa.com", email.orElseThrow());
    }

    @Test
    public void testFilter() {
        Optional<Integer> nota = Optional.of(75);
        Optional<Integer> aprovado = nota.filter(n -> n >= 60);
        Optional<Integer> reprovado = nota.filter(n -> n >= 90);
        assertTrue(aprovado.isPresent());
        assertTrue(reprovado.isEmpty());
    }

    @Test
    public void testUsoConcretoEmServico() {
        Optional<String> produto = buscarProdutoPorSku("SKU-999");
        String nome = produto
                .map(String::toUpperCase)
                .orElse("PRODUTO NÃO ENCONTRADO");
        assertEquals("PRODUTO NÃO ENCONTRADO", nome);
    }

    private Optional<String> buscarProdutoPorSku(String sku) {
        if ("SKU-001".equals(sku)) return Optional.of("Caneta Azul");
        return Optional.empty();
    }
}
