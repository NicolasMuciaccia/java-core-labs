package br.com.java.core.labs.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// Estude File, Path e Files.
// Aprenda a trabalhar com o sistema de arquivos de forma moderna e segura.
public class FileSystemsTest {
    @TempDir
    Path tempDir;

    @Test
    public void testPathOf() {
        Path p = Path.of("src", "main", "java", "Main.java");

        assertEquals("Main.java", p.getFileName().toString());
        assertEquals(Path.of("src", "main", "java"), p.getParent());
    }

    @Test
    public void testPathResolveERelativize() {
        Path base    = Path.of("/projetos/java-core-labs");
        Path arquivo = base.resolve("src/Main.java"); // concatena

        assertTrue(arquivo.toString().replace("\\", "/")
                .endsWith("projetos/java-core-labs/src/Main.java"));

        Path relativo = base.relativize(arquivo);
        assertEquals(Path.of("src", "Main.java"), relativo);
    }

    @Test
    public void testPathNormalize() {
        Path sujo = Path.of("/a/b/../c/./d");
        Path limpo = sujo.normalize(); // remove ".." e "."
    }

    @Test
    public void testWriteAndReadString() throws IOException {
        Path arquivo = tempDir.resolve("leitura.txt");
        String conteudo = "Java 21 é incrível\nLinha dois.";

        // Files.writeString: escreve String diretamente no arquivo.
        Files.writeString(arquivo, conteudo);

        // Files.readString: lê o arquivo inteiro como String.
        String lido = Files.readString(arquivo);
        assertEquals(conteudo, lido);
    }

    @Test
    public void testReadAllLines() throws IOException {
        Path arquivo = tempDir.resolve("linhas.txt");
        Files.writeString(arquivo, "Alpha\nBeta\nGamma");

        List<String> linhas = Files.readAllLines(arquivo);

        assertEquals(3, linhas.size());
        assertEquals("Alpha", linhas.getFirst());
        assertEquals("Gamma", linhas.getLast());
    }


    @Test
    public void testCreateFileEDiretorios() throws IOException {
        Path dir = tempDir.resolve("subpasta/aninhada");
        Path arq = dir.resolve("novo.txt");

        Files.createDirectories(dir);
        assertTrue(Files.isDirectory(dir));

        Files.createFile(arq);
        assertTrue(Files.exists(arq));
        assertTrue(Files.isRegularFile(arq));
    }


    @Test
    public void testCopyEMove() throws IOException {
        Path origem  = tempDir.resolve("original.txt");
        Path destino = tempDir.resolve("copia.txt");
        Path movido  = tempDir.resolve("movido.txt");

        Files.writeString(origem, "conteúdo original");
        Files.copy(origem, destino);

        assertTrue(Files.exists(origem));
        assertTrue(Files.exists(destino));
        assertEquals("conteúdo original", Files.readString(destino));

        Files.move(destino, movido);
        assertFalse(Files.exists(destino));
        assertTrue(Files.exists(movido));
        assertEquals("conteúdo original", Files.readString(movido));
    }


    @Test
    public void testDeleteIfExists() throws IOException {
        Path arq = tempDir.resolve("efemero.txt");
        Files.writeString(arq, "apaga-me");

        assertTrue(Files.exists(arq));
        assertTrue(Files.deleteIfExists(arq));
        assertFalse(Files.exists(arq));
    }


    @Test
    public void testAtributosBasicos() throws IOException {
        Path arq = tempDir.resolve("info.txt");
        Files.writeString(arq, "tamanho é importante");

        BasicFileAttributes attrs = Files.readAttributes(arq, BasicFileAttributes.class);

        assertTrue(attrs.isRegularFile());
        assertFalse(attrs.isDirectory());
        assertTrue(attrs.size() > 0);
        assertNotNull(attrs.creationTime());
    }

    @Test
    public void testWalkListaArquivosRecursivamente() throws IOException {
        // Cria estrutura: tempDir/a/b/c.txt e tempDir/d.txt
        Files.createDirectories(tempDir.resolve("a/b"));
        Files.writeString(tempDir.resolve("a/b/c.txt"), "c");
        Files.writeString(tempDir.resolve("d.txt"), "d");

        try (Stream<Path> stream = Files.walk(tempDir)) {
            long qtdArquivos = stream
                    .filter(Files::isRegularFile)
                    .count();
            assertEquals(2, qtdArquivos);
        }
    }

    @Test
    public void testListDirectChildren() throws IOException {
        Files.writeString(tempDir.resolve("a.txt"), "a");
        Files.writeString(tempDir.resolve("b.txt"), "b");
        Files.createDirectories(tempDir.resolve("pasta"));

        try (Stream<Path> filhos = Files.list(tempDir)) {
            long total = filhos.count();
            assertEquals(3, total);
        }
    }
}
