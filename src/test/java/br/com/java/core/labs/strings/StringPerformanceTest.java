package br.com.java.core.labs.strings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Estude diferenças de performance e thread-safety entre String, StringBuilder e StringBuffer.
public class StringPerformanceTest {

    @Test
    public void testImmutabilityOverhead(){
        String texto = "Texto imutável";
        String referenciaOriginal = texto;

        texto += " ou mutável.";

        // NOT SAME
        assertNotSame(referenciaOriginal, texto, "São objetos completamente diferentes na memória.");
        assertEquals("Texto imutável", referenciaOriginal);
        assertEquals("Texto imutável ou mutável.", texto);
    }

    @Test
    public void testStringBuilderMutability(){
        StringBuilder builder = new StringBuilder("Texto inicial com espaço extra pra append");
        StringBuilder referenciaOriginal = builder;

        builder.append(", o append funcionou!");

        // SAME
        assertSame(referenciaOriginal, builder, "Apontam para o mesmo objeto na memória.");
        // Transforma do objeto "StringBuilder" em "String" de fato
        assertEquals("Texto inicial com espaço extra pra append, o append funcionou!", builder.toString());
    }

    @Test
    public void testStringBufferThreadSafety(){
        StringBuffer buffer = new StringBuffer("Texto inicial com thread safety");
        buffer.append(" e seguro.");

        assertEquals("Texto inicial com thread safety e seguro.", buffer.toString());
    }
}
