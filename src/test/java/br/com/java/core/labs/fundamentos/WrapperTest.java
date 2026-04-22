package br.com.java.core.labs.fundamentos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Estude Autoboxing e Unboxing: como o Java converte automaticamente entre tipos primitivos e wrappers.
// Entenda os impactos em performance e comparação de objetos.
public class WrapperTest {

    @Test
    public void testAutoboxingImplicito() {
        Integer wrapperA = 42;
        Integer wrapperB = 42;

        assertTrue(wrapperA == wrapperB, "Mesmo objeto para valores entre -128 e 127");
    }

    @Test
    public void testAutoboxingForaDoCacheQuebraIdentidade(){

        Integer wrapperA = 200;
        Integer wrapperB = 200;

        assertFalse(wrapperA == wrapperB, "Fora do cache para valores entre -128 e 127");
        assertEquals(wrapperA, wrapperB, "equals() compara valor, não referência");
    }

    @Test
    public void testUnboxingNullLancaNullPointerException(){
        Integer valorNulo = null;

        assertThrows(NullPointerException.class, () -> {
           int primitivo = valorNulo;
            System.out.println(primitivo); // nunca chegará aqui
        }, "Unboxing de null lança NullPointerException");
    }

    @Test
    public void testConversaoEntreWrappers(){
        Double d = 9.99;
        int truncado = d.intValue();

        assertEquals(truncado, 9, "intValue() trunca, não arredonda");
    }

    @Test
    public void testParseMetodos(){
        String textToInt = "123";
        String textToDouble = "3.14";

        int parsedInt = Integer.parseInt(textToInt);
        double parsedDouble = Double.parseDouble(textToDouble);

        assertEquals(123, parsedInt);
        assertEquals(3.14, parsedDouble, 0.001);
    }

    @Test
    public void testConstantesUteis(){
        System.out.printf("Integer.MIN_VALUE: %d%n", Integer.MIN_VALUE);
        System.out.printf("Integer.MAX_VALUE: %d%n", Integer.MAX_VALUE);
        System.out.printf("Long.MAX_VALUE   : %d%n", Long.MAX_VALUE);

        assertTrue(Integer.MAX_VALUE > 0);
        assertTrue(Integer.MIN_VALUE < 0);
        assertTrue(Integer.MAX_VALUE < Long.MAX_VALUE);
    }

    @Test
    public void testCompareToEntreWrappers(){
        Integer menor = 10;
        Integer maior = 20;

        assertTrue(menor.compareTo(maior) < 0,  "10 é menor que 20");
        assertTrue(maior.compareTo(menor) > 0,  "20 é maior que 10");
        assertEquals(0, menor.compareTo(10), "Mesmo valor → compareTo retorna 0");
    }
}
