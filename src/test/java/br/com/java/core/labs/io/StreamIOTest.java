package br.com.java.core.labs.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

// Estude BufferedReader, BufferedWriter, InputStream e OutputStream.
// Pratique leitura e escrita de dados em diferentes fluxos.
public class StreamIOTest {

    @TempDir
    Path tempDir;

    @Test
    public void testFileOutputStreamEInputStream() throws IOException {
        File arquivo = tempDir.resolve("bytes.bin").toFile();
        byte[] dados = {74, 97, 118, 97};

        try(OutputStream out = new FileOutputStream(arquivo)) {
            out.write(dados);
        }

        try(InputStream in = new FileInputStream(arquivo)) {
            byte[] lidos = in.readAllBytes();
            assertArrayEquals(dados, lidos);
            assertEquals("Java", new String(lidos, StandardCharsets.US_ASCII));
        }
    }

    @Test
    public void testBufferedOutputStreamReduceSistemaDeArquivo() throws IOException {
        // Buffered*: acumula dados em memória e escreve em blocos — muito mais eficiente
        // que escrever byte a byte diretamente no disco.
        File arquivo = tempDir.resolve("buffered.txt").toFile();

        try(BufferedOutputStream bufferedOut = new BufferedOutputStream(new FileOutputStream(arquivo))) {
            String linha = "Linha escrita com buffer.\n";
            bufferedOut.write(linha.getBytes(StandardCharsets.UTF_8));
        }

        try(BufferedInputStream bufferedInput = new BufferedInputStream(new FileInputStream(arquivo))) {
            String conteudo = new String(bufferedInput.readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(conteudo.startsWith("Linha escrita com buffer."));
        }
    }

    @Test
    public void testBufferedWriterEBufferedReader() throws IOException {
        File arquivo = tempDir.resolve("texto.txt").toFile();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, StandardCharsets.UTF_8))) {
            writer.write("Primeira linha");
            writer.newLine();
            writer.write("Segunda linha");
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(arquivo, StandardCharsets.UTF_8))) {
            assertEquals("Primeira linha", reader.readLine());
            assertEquals("Segunda linha", reader.readLine());
            assertNull(reader.readLine(), "Fim do arquivo");
        }
    }

    @Test
    public void testBufferedReaderLinhas() throws IOException {
        File arquivo = tempDir.resolve("multiplas.txt").toFile();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, StandardCharsets.UTF_8))) {
            writer.write("Alpha\nBeta\nGamma\nDelta");
        }

        long qtdLinhas;
        try(BufferedReader reader = new BufferedReader(new FileReader(arquivo, StandardCharsets.UTF_8))) {
            qtdLinhas = reader.lines().count();
        }

        assertEquals(4, qtdLinhas);
    }

    @Test
    public void testStringWriterCapturaSaida() throws IOException {
        StringWriter sw = new StringWriter();

        try(PrintWriter pw = new PrintWriter(sw)) {
            pw.println("Linha 1");
            pw.print("Linha 2");
        }

        String resultado = sw.toString();
        assertTrue(resultado.contains("Linha 1"));
        assertTrue(resultado.contains("Linha 2"));
    }

    @Test
    public void testStringReaderLeDaString() throws IOException {
        String fonte = "Conteúdo em memória";
        StringBuilder capturado = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new StringReader(fonte))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                capturado.append(linha);
            }
        }

        assertEquals("Conteúdo em memória", capturado.toString());
    }

    @Test
    public void testByteArrayOutputStreamCapturaBytesEmMemoria() throws IOException {
        // ByteArrayOutputStream: similar ao StringWriter, mas para bytes.
        // Extremamente útil para serialização e testes.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try(DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeInt(42);
            dos.writeDouble(3.14);
            dos.writeUTF("Java");
        }

        byte[] buffer = baos.toByteArray();

        // Alterar a ordem de leitura quebraria
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buffer))) {
            assertEquals(42, dis.readInt());
            assertEquals(3.14, dis.readDouble());
            assertEquals("Java", dis.readUTF());
        }
    }

    @Test
    public void testPrintStreamParaArquivo() throws IOException {
        File arquivo = tempDir.resolve("print.txt").toFile();
        // PrintStream: fornece print/println/printf — System.out é um PrintStream.
        try (PrintStream ps = new PrintStream(arquivo, StandardCharsets.UTF_8)) {
            ps.println("Linha impressa via PrintStream");
            ps.printf("Valor formatado: %d%n", 21);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo, StandardCharsets.UTF_8))) {
            assertEquals("Linha impressa via PrintStream", reader.readLine());
            assertTrue(reader.readLine().contains("21"));
        }
    }

    @Test
    public void testTryWithResourcesFechaAutomaticamente() throws IOException {
        // Qualquer Closeable/AutoCloseable é fechado automaticamente ao sair do bloco.
        // Elimina o risco de vazamento de recursos (resource leaks).
        File arquivo = tempDir.resolve("auto.txt").toFile();

        try (FileWriter fw = new FileWriter(arquivo, StandardCharsets.UTF_8);
                 BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("Escrito com try-with-resources");
        }
        // fw e bw foram fechados automaticamente — sem finally necessário

        String conteudo = Files.readString(arquivo.toPath());
        assertEquals("Escrito com try-with-resources", conteudo);
    }
}














