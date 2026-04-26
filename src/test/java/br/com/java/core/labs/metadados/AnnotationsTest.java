package br.com.java.core.labs.metadados;

import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Estude @Override, @Deprecated e @SuppressWarnings.
// Compreenda como metadados podem documentar e influenciar o compilador.
public class AnnotationsTest {

    static class Animal {
        public String fazerSom() {
            return "...";
        }

        public String toString() {
            return "Animal genérico.";
        }
    }

    static class Cachorro extends Animal {
        @Override
        public String fazerSom() {
            return "Au.. Au..";
        }

        @Override
        public String toString() {
            return "Cachorro";
        }
    }

    @Test
    public void testOverrideRedefineSomDoAnimal() {
        Animal animal = new Cachorro();

        assertEquals("Au.. Au..", animal.fazerSom());
        assertEquals("Cachorro", animal.toString());
    }


    static class ServicoLegado {
        @Deprecated(since = "2.0", forRemoval = true)
        public int calcular(int a, int b) {
            return a + b;
        }

        public int calcularNovo(int a, int b) {
            return Integer.sum(a, b);
        }
    }

    @Test
    @SuppressWarnings("deprecation") // suprime o aviso de uso de método deprecated
    public void testDeprecatedAindaFunciona() {
        ServicoLegado svc = new ServicoLegado();

        assertEquals(10, svc.calcular(5,5));
        assertEquals(10, svc.calcularNovo(5,5));
    }

    @Test
    public void testDeprecatedComReflection() throws Exception {
        Method metodo = ServicoLegado.class.getMethod("calcular", int.class, int.class);
        // A anotação @Deprecated é retida em runtime e acessível via Reflection.
        assertTrue(metodo.isAnnotationPresent(Deprecated.class));
        Deprecated dep = metodo.getAnnotation(Deprecated.class);
        assertEquals("2.0", dep.since());
        assertTrue(dep.forRemoval());
    }

    @Test
    @SuppressWarnings("unchecked") // Suprime alertas de cast genérico inseguro
    public void testSuppresWarningsUnchecked() {
        List lista = new ArrayList();
        lista.add("item sem generics");

        List<String> listaTipada = (List<String>) lista;
        assertEquals("item sem generics", listaTipada.getFirst());
    }

    @Retention(RetentionPolicy.RUNTIME) // disponível em runtime
    @Target(ElementType.METHOD) // aplicável apenas em métodos
    @interface Autor {
        String nome();
        String versao() default "1.0";
    }

    static class Servico {
        @Autor(nome = "Felipe", versao = "2.1")
        public String processar() {
            return "processado";
        }

        @Autor(nome = "Ana")
        public String validar() {
            return "validado";
        }
    }

    @Test
    public void testAnotacaoCustomizadaLidaEmRuntime() throws Exception {
        Method processar = Servico.class.getMethod("processar");
        Autor autor = processar.getAnnotation(Autor.class);
        assertNotNull(autor, "Anotação @Autor presente no método processar");
        assertEquals("Felipe", autor.nome());
        assertEquals("2.1",    autor.versao());
    }
    @Test
    public void testValorDefaultDaAnotacao() throws Exception {
        Method validar = Servico.class.getMethod("validar");
        Autor autor = validar.getAnnotation(Autor.class);
        assertEquals("Ana", autor.nome());
        assertEquals("1.0", autor.versao(), "Versão padrão (default) foi usada");
    }

    @Test
    public void testMetodoSemAnotacao() throws Exception {
        class SemAnotacao {
            public void qualquerMetodo() {}
        }

        Method m = SemAnotacao.class.getMethod("qualquerMetodo");
        assertNull(m.getAnnotation(Autor.class));
    }

    @FunctionalInterface // garante que a interface tenha exatamente UM metodo abstrato
    interface Transformador<T> {
        T transformar(T entrada);
        // Adicionar um segundo metodo abstrato aqui causaria erro de compilação.
    }

    @Test
    public void testFunctionalInterfaceAceitaLambda() {
        // @FunctionalInterface habilita uso de lambda e method reference.
        Transformador<String> maiusculas = String::toUpperCase;
        assertEquals("JAVA", maiusculas.transformar("java"));

        Transformador<Integer> dobrar = n -> n * 2;
        assertEquals(42, dobrar.transformar(21));
    }
}





















