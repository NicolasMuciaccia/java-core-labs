package br.com.java.core.labs.strings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Estude manipulação de strings: split, substring, replace, trim, toLowerCase/toUpperCase,
// contains, startsWith e endsWith.
// Aprenda quando usar String imutável e como evitar operações custosas.
public class StringManipulationTest {

    @Test
    public void testSplitAndSubstring(){
        String logLine = "INFO:2026-04-12:User login failed";

        String[] parts = logLine.split(":");
        assertEquals(3, parts.length);
        assertEquals("INFO", parts[0]);

        String datePart = logLine.substring(5, 15);
        assertEquals("2026-04-12", datePart);
    }

    @Test
    public void testReplaceVsReplaceAll() {
        String data = "user.name@gmail.com";

        // .replace() utiliza caracteres literais, em que "." é de fato apenas um "."
        String safeData = data.replace(".", "[DOT]");
        assertEquals("user[DOT]name@gmail[DOT]com", safeData);

        // .replaceAll() utiliza regex, em que "." é um coringa, logo substitui tudo
        String destroytedData = data.replaceAll(".", "X");
        assertEquals("XXXXXXXXXXXXXXXXXXX", destroytedData);
        assertNotSame("user[DOT]name@gmail.com", destroytedData);
    }

    @Test
    public void testTrimmingAndCasing(){
        String input = "     Java 25     ";

        // Legado, remove apenas espaços básicos "tab, enter, " ")
        assertEquals("Java 25", input.trim());

        // Recente, utiliza Character.isWhitespace(), muito mais completo
        assertEquals("Java 25", input.strip());

        // \u3000 representa espaço feito por um teclado chinês por exemplo
        String inputWithChineseSpace = "\u3000Java 25\u3000";

        // .trim() NÃO fará a remoção desse espaçamento
        assertEquals("\u3000Java 25\u3000", inputWithChineseSpace.trim());
        assertNotEquals("Java 25", inputWithChineseSpace.trim());

        // .strip() FARÁ a remoção desse espaçamento
        assertEquals("Java 25", inputWithChineseSpace.strip());
        assertNotEquals("\u3000Java 25\u3000", inputWithChineseSpace.strip());
    }

    @Test
    public void testVerificationMethods() {
        String payload = """
                {
                "status":"ACTIVE"
                }""";

        // Métodos de checagem retornam boolean e são case-sensitive.
        assertTrue(payload.startsWith("{"));
        assertTrue(payload.endsWith("}"));
        assertTrue(payload.contains("ACTIVE"));
        assertFalse(payload.contains("active"));
    }
}
