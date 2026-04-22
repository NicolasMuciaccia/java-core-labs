package br.com.java.core.labs.fundamentos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JvmMechanicsTest {

    // Por ser "static" pertence à Classe, e não à instância no heap.
    static class Contador {
        static int global = 0;
        int local = 0;

        public Contador(){
            global++;
            local++;
        }
    }

    @Test
    public void testStaticMemory(){
        Contador c1 = new Contador();
        Contador c2 = new Contador();
        Contador c3 = new Contador();

        assertEquals(1, c3.local, "A memória local é isolada para o c1");
        assertEquals(3, Contador.global, "O static é externo à primeira instância, logo, seu valor é agregado pela segunda e terceira também.");
    }

    @Test
    public void testFinalKeyword(){
        // Uso de final em variáveis primitivas impede a alteração de valor
        final int limite = 100;
        // limite = 200; // O compilador rejeita

        // Classes statics impedem apenas a alteração do ponteiro dela, e não do conteúdo
        final java.util.List<String> finalList = new java.util.ArrayList<>();
        finalList.add("Java");
        finalList.add("is");
        finalList.add("good");

        assertEquals(3, finalList.size());
    }

    @Test
    public void testInstanceofModerno(){
        Object apiReturn = "Microsserviço";

        // Java 21+ (Pattern Matching) instancia automaticamente o "s", não precisa de cast manual
        if(apiReturn instanceof String s) {
            assertEquals("MICROSSERVIÇO", s.toUpperCase());
        } else {
            fail("Deveria ser uma Stringa.");
        }
    }
}