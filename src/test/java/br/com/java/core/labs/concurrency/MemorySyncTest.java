package br.com.java.core.labs.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

// Estude synchronized e volatile.
// Entenda como sincronizar acesso à memória entre threads.
public class MemorySyncTest {

    static class ContadorSincronizado {
        private int valor = 0;

        // synchronized: apenas UMA thread por vez executa este método no mesmo objeto.
        public synchronized void incrementar() {
            valor++;
        }
        public synchronized int getValor() {
            return valor;
        }
    }

    @Test
    public void testSynchronizedGaranteConsistencia() throws InterruptedException {
        ContadorSincronizado contador = new ContadorSincronizado();

        int iteracoes = 100_000;

        Runnable tarefa = () -> {
          for (int i = 0; i < iteracoes; i ++){
              contador.incrementar();
          }
        };

        Thread t1 = new Thread(tarefa);
        Thread t2 = new Thread(tarefa);

        t1.start(); t2.start();
        t1.join(); t2.join();

        // Evitou race condition
        assertEquals(200_000, contador.getValor());
    }

    static class BancoSincronizado {
        private double saldo;
        private final Object lock = new Object(); // monitor explícito

        BancoSincronizado(double saldoInicial) {
            this.saldo = saldoInicial;
        }

        public void depositar(double valor) {
            // Bloco synchronized: mais granular que o método inteiro.
            synchronized (lock) {
                saldo += valor;
            }
        }
        public double getSaldo() {
            synchronized (lock) { return saldo; }
        }
    }

    @Test
    public void testSynchronizedEmBloco() throws InterruptedException {
        BancoSincronizado conta = new BancoSincronizado(0.0);

        Runnable depositar100 = () -> {
          for (int i = 0; i < 1000; i++) {
              conta.depositar(100.0);
          }
        };

        Thread t1 = new Thread(depositar100);
        Thread t2 = new Thread(depositar100);

        t1.start(); t2.start();
        t2.join(); t1.join();
        assertEquals(200_000.0, conta.getSaldo(), 0.001);
    }

    static class SinalVolatil {
        // volatile: garante visibilidade entre threads.
        // Leituras/escritas vão diretamente à memória principal (sem cache duplicado de CPU da variável).
        private volatile boolean ativo = true;
        public void desligar() { ativo = false; }
        public boolean estaAtivo() { return ativo; }
    }

    @Test
    public void testVolatileGaranteVisibilidade () throws InterruptedException {
        SinalVolatil sinal = new SinalVolatil();
        AtomicInteger contadorIteracoes = new AtomicInteger(0);

        Thread worker = new Thread( () -> {
           while (sinal.estaAtivo()){
               contadorIteracoes.incrementAndGet();
           }
        });

        worker.start();
        Thread.sleep(50);
        sinal.desligar();
        worker.join(500);

        assertEquals(Thread.State.TERMINATED, worker.getState());
        assertTrue(contadorIteracoes.get() > 0);
    }

    @Test
    public void testVolatileNaoGaranteAtomicidade () throws InterruptedException {
        AtomicInteger atomico = new AtomicInteger(0);

        Runnable inc = () -> {
          for(int i = 0; i < 5_000; i++) {
              atomico.incrementAndGet();
          }
        };

        Thread t1 = new Thread(inc);
        Thread t2 = new Thread(inc);

        t1.start(); t2.start();
        t1.join(); t2.join();

        assertEquals(10_000, atomico.get(), "AtomicInteger corrige onde volatile não basta");
    }

    @Test
    public void testComparacaoSynchronizedVsVolatile () throws InterruptedException {
        // Regra de ouro:
        //  • Apenas UMA thread escreve, várias lêem → volatile é suficiente
        //  • Múltiplas threads escrevem        → synchronized ou Atomic* é necessário
        CountDownLatch latch = new CountDownLatch(1);
        MyVolatileFlag volatileFlag= new MyVolatileFlag();

        Thread escritor = new Thread(() -> {
            volatileFlag.valor = true;
            latch.countDown();
        });

        escritor.start();
        latch.await();

        assertTrue(volatileFlag.valor);
    }

    static class MyVolatileFlag {
        volatile boolean valor = false;
    }
}
