package br.com.java.core.labs.fundamentos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Estude os pilares de Orientação a Objetos: Classes, Objetos, Encapsulamento,
// Herança, Polimorfismo, Abstração e Interfaces.
// Entenda como a JVM gerencia referências e por que a modelagem OO é a base para desenvolvimento Java.
public class OrientacaoObjetosTest {


    class ClassePai {
        int valor = 7;
    }

    class ClasseFilha extends ClassePai {
        int valor = 20;

        public  int getSoma() {
            return this.valor + super.valor;
        }
    }

    @Test
    public void testThisESuper() {
        ClasseFilha obj = new ClasseFilha();
        assertEquals(27, obj.getSoma());
    }

    @Test
    public void test(){

    }

    @Test
    public void testAutoboxingOverhead() {
        System.out.printf("%nINÍCIO: TESTE AUTOBOXING/UNBOXING C/ 100.000.000 %n");
        // Wrapper (Objeto) sobre o tipo primitivo "long"
        Long sumObject = 0L;
        long start = System.nanoTime();

        for (int i = 0; i < 100000000; i++) {
            // Aqui a JVM faz Autoboxing/Unboxing 1000x
            // transformando Long em long para realizar a soma, e cria um NOVO Long após, equivalente à "Long.valueOf(x)" a cada iteração
            sumObject += i;
        }
        long timeObject = System.nanoTime() - start;
        System.out.printf("Tempo com Objeto (Long)   : %,d nanosegundos%n", timeObject);

        // Tipo primitivo direto, sem wrapper.
        long primitiveSum = 0L;
        start = System.nanoTime();

        for (int i = 0; i < 100000000; i++) {
            // A soma é realizada diretamente, sem Autoboxing/Unboxing.
            primitiveSum += i;
        }
        long primitiveTime = System.nanoTime() - start;
        System.out.printf("Tempo com Primitivo (long): %,d nanosegundos %n%n", primitiveTime);

        assertTrue(primitiveTime < timeObject, "Primitivos evitam alocação no Heap");
    }
}
