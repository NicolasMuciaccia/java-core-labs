package br.com.java.core.labs.fundamentos;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Estude o comportamento de equals vs ==, o papel de hashCode, e como toString ajuda na depuração.
// Veja também como usar instanceof e @Override de forma correta.
public class MetodosPadraoTest {

    static class Produto {
        String sku;
        String nome;
        double preco;

        Produto(String sku, String nome, double preco){
            this.sku = sku;
            this.nome = nome;
            this.preco = preco;
        }

        @Override
        public boolean equals(Object o){
            if(this == o) return true; // Mesma referênciana memória
            if (!(o instanceof Produto p)) return false; // pattern matching
            return Objects.equals(sku, p.sku);
        }

        @Override
        public int hashCode() {
            // Consistente com o equals, deve usar "sku" também
            return Objects.hash(sku);
        }

        @Override
        public String toString(){
            // Muito usados pra chamar em logs
            return "Produto{sku='%s', nome='%s', preco=%.2f}".formatted(sku, nome, preco);
        }
    }

    @Test
    public void testIgualdadeDeReferencia(){
        Produto p1 = new Produto("SKU-001", "Caneta", 2.50);
        Produto p2 = p1;

        assertTrue(p1 == p2);
    }

    @Test
    public void testIgualdadeDeValorComEquals(){
        Produto p1 = new Produto("SKU-001", "Caneta", 2.50);
        Produto p2 = new Produto("SKU-001", "Caneta BIC", 3.00);

        assertFalse(p1 == p2);
        assertEquals(p1, p2);
    }

    @Test
    public void testStringEqualsNaoUsaOperadorDeIgualdade(){
        String a = new String("Java");
        String b = new String("Java");

        assertNotSame(a, b); // Objetos diferentes na memória, uso de "New"
        assertEquals(a, b); // Contudo, contém o mesmo valor, são iguais .equals()
    }

    @Test
    public void testContratoHashCode(){

        Produto p1 = new Produto("SKU-001", "Caneta", 2.50);
        Produto p2 = new Produto("SKU-001", "Caneta BIC", 3.00);

        assertEquals(p1.hashCode(), p2.hashCode()); // hashCode() é gerado pelo campo sku, logo devem ser iguais
    }

    @Test
    public void testHashCodePermiteUsarEmHashSet(){
        Set<Produto> catalogo = new HashSet<>();
        catalogo.add(new Produto("SKU-001", "Caneta", 2.50));
        catalogo.add(new Produto("SKU-001", "Caneta BIC", 3.00)); // Duplicata, pelo Sku

        assertEquals(1, catalogo.size()); // Só inseriu um elemento
    }

    @Test
    public void testToStringLegivel(){
        Produto p = new Produto("SKU-999", "Monitor", 1299.99);
        String representacao = p.toString();

        assertTrue(representacao.contains("SKU-999"));
        assertTrue(representacao.contains("Monitor"));
        assertTrue(representacao.contains("1299.99") || representacao.contains("1299,99"));
    }

    @Test
    public void testSemToStringExibeEnderecoDeMemoria(){
        Object anonimo = new Object();
        String representacao = anonimo.toString();

        // O formato padrão inclui "@" e o hash hexadecimal
        assertTrue(representacao.contains("@"));
    }

    @Test
    public void testInstanceofComPatternMatching(){
        Object obj = new Produto("SKU-777", "Teclado", 250.50);

        if(obj instanceof Produto p) {
            assertEquals("SKU-777", p.sku);
        } else {
            fail("Deveria ser um Produto");
        }
    }

    @Test
    public void testInstanceofRetornaFalseParaNulo(){
        Object nulo  = null;
        assertFalse(nulo instanceof Produto);
    }
}
