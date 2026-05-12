package br.com.java.core.labs.datetime;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

// Estude now, plus/minus, isBefore/isAfter, DateTimeFormatter e parse.
// Aprenda a formatar e analisar datas de forma segura e correta para diferentes fusos e padrões.
public class FormattingTest {

    @Test
    public void testFormatterIsoDate() {
        LocalDate data = LocalDate.of(2026, 4,21);

        String formatada = data.format(DateTimeFormatter.ISO_LOCAL_DATE);
        assertEquals(formatada, "2026-04-21");
    }

    @Test
    public void testFormatterIsoDateTime() {
        LocalDateTime data = LocalDateTime.of(2026,4,21,14,30,0);

        String formatada = data.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertEquals(formatada, "2026-04-21T14:30:00");
    }

    @Test
    public void testFormatterCustomizadoBrasileiro() {
        LocalDate data = LocalDate.of(2026, 4, 21);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        assertEquals("21/04/2026", data.format(fmt));
    }

    @Test
    public void testFormatterComHoraMinuto() {
        LocalDateTime data = LocalDateTime.of(2026,4,21,14,30,0);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        assertEquals("21/04/2026 14:30:00", data.format(fmt));
    }

    @Test
    public void testFormatterComLocale() {
        LocalDate data = LocalDate.of(2026,4,21);
        DateTimeFormatter ptBr = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("pt-BR"));

        String resultado = data.format(ptBr);
        assertEquals("21 de abril de 2026", resultado);
    }

    @Test
    public void testParseLocalDate() {
        String texto = "21/04/2026";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate data = LocalDate.parse(texto, fmt);

        assertEquals(21, data.getDayOfMonth());
        assertEquals(4, data.getMonthValue());
        assertEquals(2026, data.getYear());
    }

    @Test
    public void testParseLocalDateTime() {
        String texto = "2026-04-21T14:30:00";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LocalDateTime data = LocalDateTime.parse(texto, fmt);

        assertEquals(21, data.getDayOfMonth());
        assertEquals(4, data.getMonthValue());
        assertEquals(2026, data.getYear());
        assertEquals(14, data.getHour());
        assertEquals(30, data.getMinute());
    }

    @Test
    public void testParseInvalidoLancaExcecao() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        assertThrows(DateTimeParseException.class,
                () -> LocalDate.parse("32/04/2026", fmt)
        );

        assertThrows(DateTimeParseException.class,
                () -> LocalDate.parse("2026-04-21", fmt)
        );
    }

    @Test
    public void testNowComFuso() {
        ZoneId brasilia = ZoneId.of("America/Sao_Paulo");
        ZonedDateTime agora = ZonedDateTime.now(brasilia);

        assertEquals("America/Sao_Paulo", agora.getZone().getId());
    }

    @Test
    public void testPlusMinusEmLocalDate() {
        LocalDate base = LocalDate.of(2026,1,31);

        LocalDate proxMes = base.plusMonths(1);
        assertEquals(proxMes, LocalDate.of(2026,2,28));

        LocalDate semanaSeguinte = base.plusWeeks(1);
        assertEquals(semanaSeguinte, LocalDate.of(2026,2,7));
    }

    @Test
    public void testComparacaoDeDatasSemErro() {
        LocalDate prazo = LocalDate.of(2026,5,1);
        LocalDate entrega = LocalDate.of(2026,4,28);

        assertTrue(entrega.isBefore(prazo));
        assertFalse(entrega.isAfter(prazo));
        assertFalse(entrega.isEqual(prazo));
    }

    @Test
    public void testZonedDateTimeFormatadoComFusoOffset() {
        ZonedDateTime evento = ZonedDateTime.of(
          LocalDateTime.of(2026,4,21,19,0,0),
          ZoneId.of("America/Sao_Paulo")
        );

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm xxx");
        String resultado = evento.format(fmt);

        assertTrue(resultado.contains("21/04/2026"));
        assertTrue(resultado.contains("19:00"));
        assertTrue(resultado.contains("-03:00"));
    }
}
