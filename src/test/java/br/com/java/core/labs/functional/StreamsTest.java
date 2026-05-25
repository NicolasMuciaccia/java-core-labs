package br.com.java.core.labs.functional;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Estude Streams: stream, filter, map, flatMap, reduce, collect, Collectors, sorted e matchers.
// Aprenda a processar coleções de forma declarativa e enxuta.
public class StreamsTest {
    record Funcionario(String nome, String depto, double salario) {}

    private List<Funcionario> equipe() {
        return List.of(
                new Funcionario("Ana",    "Tech",     8_500.0),
                new Funcionario("Bruno",  "Tech",     9_200.0),
                new Funcionario("Carla",  "RH",       5_800.0),
                new Funcionario("Diego",  "RH",       6_200.0),
                new Funcionario("Elena",  "Tech",    12_000.0),
                new Funcionario("Fábio",  "Vendas",   7_100.0)
        );
    }

    @Test
    public void testFilter() {
        List<Funcionario> tech = equipe().stream()
                .filter(f -> f.depto.equals("Tech"))
                .toList();

        assertEquals(3, tech.size());
        assertTrue(tech.stream().allMatch(f -> f.depto().equals("Tech")));
    }

    @Test
    public void testMapTransformacao() {
        List<String> nomes = equipe().stream()
                .map(Funcionario::nome)
                .toList();

        assertEquals(6, nomes.size());
        assertEquals("Ana", nomes.get(0));
    }

    @Test
    public void testMapParaTipo() {
        List<Double> salarios = equipe().stream()
                .map(Funcionario::salario)
                .toList();

        assertEquals(6, salarios.size());
        assertTrue(salarios.contains(12_000.0));
    }

    @Test
    public void testFlatMapAchataListas() {
        List<List<Integer>> matriz = List.of(
          List.of(1,2,3),
          List.of(4,5),
          List.of(6,7,8,9)
        );

        List<Integer> plana = matriz.stream()
                .flatMap(Collection::stream)
                .toList();

        assertEquals(9, plana.size());
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), plana);
    }

    @Test
    public void testFlatMapComStrings() {
        List<String> frases = List.of("Java é sensacional","Streams são poderosas");

        List<String> palavras = frases.stream()
                .flatMap(frase -> Arrays.stream(frase.split(" ")))
                .toList();

        assertEquals(6, palavras.size());
        assertTrue(palavras.contains("Java"));
        assertTrue(palavras.contains("poderosas"));
    }

    @Test
    public void testReduceSoma() {
        int soma = IntStream.rangeClosed(1, 10)
                .reduce(0, Integer::sum);

        assertEquals(55, soma);
    }

    @Test
    public void testReduceEncontrarMaior() {
        Optional<Funcionario> maisRico = equipe().stream()
                .reduce((a,b) -> a.salario > b.salario ? a : b);

        assertTrue(maisRico.isPresent());
        assertEquals("Elena", maisRico.get().nome());
    }

    @Test
    public void testCollectorToList() {
        List<String> deptosTech = equipe().stream()
                .filter(f -> f.depto().equals("Tech"))
                .map(Funcionario::nome)
                .collect(Collectors.toList());

        assertEquals(3, deptosTech.size());
    }

    @Test
    public void testCollectorGroupingBy() {
        Map<String, List<Funcionario>> porDepto = equipe().stream()
                .collect(Collectors.groupingBy(Funcionario::depto));

        assertEquals(3, porDepto.get("Tech").size());
        assertEquals(2, porDepto.get("RH").size());
    }

    @Test
    public void testCollectorSummarizingDouble() {
        DoubleSummaryStatistics stats = equipe().stream()
                .collect(Collectors.summarizingDouble(Funcionario::salario));

        assertEquals(6, stats.getCount());
        assertEquals(5_800.0, stats.getMin(), 0.01);
        assertEquals(12_000.0, stats.getMax(), 0.01);
    }

    @Test
    public void testCollectorJoining() {
        String nomes = equipe().stream()
                .map(Funcionario::nome)
                .collect(Collectors.joining(", ", "[", "]"));

        assertTrue(nomes.startsWith("[Ana"));
        assertTrue(nomes.endsWith("]"));
        assertTrue(nomes.contains(","));
    }

    @Test
    public void testAnyMatchAllMatchNoneMatch() {
        List<Funcionario> lista = equipe();

        assertTrue(lista.stream().anyMatch(f -> f.salario() > 10_000));
        assertTrue(lista.stream().allMatch(f -> f.salario() > 0));
        assertTrue(lista.stream().noneMatch(f -> f.salario() < 0));
    }

    @Test
    public void testSortedComComparator() {
        List<Funcionario> porSalario = equipe().stream()
                .sorted(Comparator.comparingDouble(Funcionario::salario).reversed())
                .toList();

        assertEquals("Elena", porSalario.get(0).nome(), "Maior salário primeiro");
        assertEquals("Carla", porSalario.get(5).nome(), "Menor salário último");
    }

    @Test
    public void testDistinct() {
        List<String> deptos = equipe().stream()
                .map(Funcionario::depto)
                .distinct()
                .sorted()
                .toList();

        assertEquals(List.of("RH", "Tech", "Vendas"), deptos);
    }

    @Test
    public void testLimitESkip() {
        List<Funcionario> pagina = equipe().stream()
                .skip(2)
                .limit(3)
                .toList();

        assertEquals(3, pagina.size());
        assertEquals("Carla", pagina.get(0).nome());
        assertEquals("Elena", pagina.get(2).nome());
    }
}
