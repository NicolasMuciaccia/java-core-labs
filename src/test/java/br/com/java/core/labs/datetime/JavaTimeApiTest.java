package br.com.java.core.labs.datetime;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

// Estude a API java.time: LocalDate, LocalTime, LocalDateTime, ZonedDateTime, Instant, Period e Duration.
// Entenda como representar datas, horas e intervalos sem os problemas das APIs antigas.
public class JavaTimeApiTest {

    @Test
    public void testLocalDateCriacao() {
        LocalDate natal = LocalDate.of(2024, Month.DECEMBER, 25);

        assertEquals(25, natal.getDayOfMonth());
        assertEquals(Month.DECEMBER, natal.getMonth());
        assertEquals(2024, natal.getYear());
        assertEquals(DayOfWeek.WEDNESDAY, natal.getDayOfWeek());
    }

    @Test
    public void testLocalDateAritmetica() {
        LocalDate hoje = LocalDate.of(2026,5,12);

        LocalDate amanha = hoje.plusDays(1);
        LocalDate daquiUmMes = hoje.plusMonths(1);
        LocalDate daquiUmAno = hoje.plusYears(1);

        assertEquals(13, amanha.getDayOfMonth());
        assertEquals(daquiUmMes.getMonthValue(), 6);
        assertEquals(2027, daquiUmAno.getYear() );
    }

    @Test
    public void testLocalDateIsBeforeIsAfter() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim    = LocalDate.of(2026, 12, 31);

        assertTrue(inicio.isBefore(fim));
        assertTrue(fim.isAfter(inicio));
        assertFalse(inicio.isAfter(fim));
    }

    @Test
    public void testLocalTimeCriacao() {
        LocalTime reuniao = LocalTime.of(14,30,0);

       assertEquals(14, reuniao.getHour());
       assertEquals(30, reuniao.getMinute());
       assertEquals(0, reuniao.getSecond());
    }

    @Test
    public void testLocalTimeAritmetica() {
        // 8:30 de duração
        LocalTime inicio = LocalTime.of(9,0);
        LocalTime fim = inicio.plusHours(8).plusMinutes(30);

        assertEquals(LocalTime.of(17,30,0), fim);
        assertTrue(fim.isAfter(inicio));
    }

    @Test
    public void testLocalDateTimeCombina() {
        LocalDate data = LocalDate.of(2026,4,21);
        LocalTime hora = LocalTime.of(14,15,55);
        LocalDateTime dataEHora = LocalDateTime.of(data, hora);

        assertEquals(2026, dataEHora.getYear());
        assertEquals(14, dataEHora.getHour());
        assertEquals(55, dataEHora.getSecond());
    }

    @Test
    public void testLocalDateTimeParseEFormato() {
        LocalDateTime ldt = LocalDateTime.parse("2026-04-21T10:30:00");

        assertEquals(21, ldt.getDayOfMonth());
        assertEquals(10, ldt.getHour());
    }

    @Test
    public void testZonedDateTimeComFuso() {
        ZoneId brasilia = ZoneId.of("America/Sao_Paulo");
        ZonedDateTime agora = ZonedDateTime.of(
                LocalDateTime.of(2026, 5, 12, 14, 0, 30),
                brasilia
        );

        assertEquals("America/Sao_Paulo",  agora.getZone().getId());

        ZonedDateTime emUtc = agora.withZoneSameInstant(ZoneOffset.UTC);
        assertEquals(17, emUtc.getHour());
    }

    @Test
    public void testInstantRepresentaPontNoTempo() {
        Instant t1 = Instant.now();
        Instant t2 = t1.plusSeconds(60);

        assertTrue(t2.isAfter(t1));
        assertEquals(60, ChronoUnit.SECONDS.between(t1,t2)); // 60
        assertNotEquals(60, ChronoUnit.SECONDS.between(t2,t1)); // -60
    }

    @Test
    public void testPeriodEntreDatas() {
        LocalDate nascimento = LocalDate.of(1995, 6, 15);
        LocalDate hoje = LocalDate.of(2026,6,15);

        Period idade = Period.between(nascimento, hoje);

        assertEquals(31, idade.getYears());
        assertEquals(0, idade.getMonths());
    }

    @Test
    public void testPeriodOf() {
        Period trimestre = Period.ofMonths(3);
        LocalDate inicio = LocalDate.of(2026,1,1);
        LocalDate fim = inicio.plusMonths(3);

        assertEquals(LocalDate.of(2026,4,1), fim);
    }

    @Test
    public void testDurationEntreInstants() {
        Instant inicio = Instant.parse("2026-04-21T10:00:00Z");
        Instant fim = Instant.parse("2026-04-21T12:30:00Z");

        Duration duracao = Duration.between(inicio, fim);

        assertEquals(2, duracao.toHours());
        assertEquals(150, duracao.toMinutes());
    }

    @Test
    public void testDurationEntreLocalTimes() {
        LocalTime atendimentoInicio = LocalTime.of(9,0);
        LocalTime atendimentoFim = LocalTime.of(12,0);

        Duration tempoAtendimento = Duration.between(atendimentoInicio, atendimentoFim);

        assertEquals(180, tempoAtendimento.toMinutes());
        assertTrue(atendimentoFim.isAfter(atendimentoInicio));
    }
}
