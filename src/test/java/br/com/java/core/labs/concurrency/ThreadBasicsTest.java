package br.com.java.core.labs.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Estude Thread, Runnable e Callable.
// Compreenda como criar e executar tarefas concorrentes no Java.
public class ThreadBasicsTest {

    @Test
    public void testThreadComRunnable() throws InterruptedException {
        AtomicInteger contador = new AtomicInteger(0);

        Runnable tarefa = () -> {
          for (int i = 0; i < 1000; i++) {
              contador.incrementAndGet();
          }
        };

        Thread t1 = new Thread(tarefa, "Worker-1");
        Thread t2 = new Thread(tarefa, "Worker-2");

        t1.start(); // inicia t1: não bloqueia a thread atual
        t2.start();

        t1.join();  // bloqueia até t1 terminar
        t2.join();

        assertEquals(2000, contador.get());
    }

    @Test
    public void testNomeEEstadoDaThread() throws InterruptedException {
        Thread t = new Thread(() -> {
           try {
               Thread.sleep(50);
           } catch (InterruptedException ignored) {}
        }, "Nome da minha thread");

        assertEquals("Nome da minha thread", t.getName());
        assertEquals(Thread.State.NEW, t.getState());

        t.start();
        assertEquals(Thread.State.RUNNABLE, t.getState());

        t.join();
        assertEquals(Thread.State.TERMINATED, t.getState());
    }

    @Test
    public void testThreadDaemon() throws InterruptedException {
        // Thread daemon: morre automaticamente quando todas as threads não-daemon terminam.
        // Usado para tarefas em background (ex.: garbage collector, monitoramento).
        Thread daemon = new Thread( () -> {
           while (true) {
               try {
                   Thread.sleep(100);
               } catch (InterruptedException  e) {
                   break;
               }
           }
        });

        daemon.setDaemon(true);
        assertTrue(daemon.isDaemon());

        daemon.start();
        // Se esta fosse a única thread não-daemon restante, a JVM encerraria o daemon.
        daemon.interrupt(); // interrompemos para não atrasar o teste
    }

    @Test
    public void testThreadSleepNaoLiberaBloqueio() throws InterruptedException {
        long antes = System.currentTimeMillis();
        Thread.sleep(100);
        long depois = System.currentTimeMillis();

        assertTrue(depois - antes >= 100);
    }

    @Test
    public void testRaceconditionSemSincronizacao() throws InterruptedException {
        int[] counter = {0};

        Runnable incrementa = () -> {
          for (int i = 0; i < 10_000; i++) {
              counter[0]++;
          }
        };

        Thread t1 = new Thread(incrementa);
        Thread t2 = new Thread(incrementa);

        t1.start(); t2.start();
        t1.join(); t2.join();


        System.out.printf("Resultado sem sync: %d (esperado 20000)%n", counter[0]);
        assertTrue(counter[0] < 20_000);
    }

    @Test
    public void testAtomicIntegerResolvRecaCondition() throws InterruptedException {
        AtomicInteger atomic = new AtomicInteger(0);

        Runnable incrementa = () -> {
            for (int i = 0; i < 10_000; i++) {
                atomic.incrementAndGet();
            }
        };

        Thread t1 = new Thread(incrementa);
        Thread t2 = new Thread(incrementa);

        t1.start(); t2.start();
        t1.join(); t2.join();


        assertEquals(20_000, atomic.get());
    }

    @Test
    public void testThreadInterrupt() throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5000); // dorme 5s
            } catch (InterruptedException e) {
                // sleep lança InterruptedException quando a thread é interrompida
                Thread.currentThread().interrupt(); // boa prática: repropaga o sinal
            }
        });
        t.start();
        Thread.sleep(50); // deixa a thread entrar no sleep

        t.interrupt(); // sinaliza interrupção
        t.join(500);   // espera até 500ms para terminar

        // Thread terminou rapidamente (não aguardou os 5000ms)
        assertEquals(Thread.State.TERMINATED, t.getState());
    }
}
