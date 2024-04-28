package banca;

import banca.ContoBancario;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContoBancarioTest {

    @Test
    public void testVersamento() {
        ContoBancario conto = new ContoBancario("123", 100);
        conto.versamento(50);
        assertEquals(150, conto.getSaldo());
    }

    @Test
    public void testPrelievo_SufficienteSaldo() {
        ContoBancario conto = new ContoBancario("123", 100);
        int result = conto.prelievo(50);
        assertEquals(1, result);
        assertEquals(50, conto.getSaldo());
    }

    @Test
    public void testPrelievo_InsufficienteSaldo() {
        ContoBancario conto = new ContoBancario("123", 100);
        int result = conto.prelievo(150);
        assertEquals(0, result);
        assertEquals(100, conto.getSaldo());
    }





    @Test
    public void testGetSaldo() {
        ContoBancario conto = new ContoBancario("123", 100);
        assertEquals(100, conto.getSaldo());
    }

    @Test
    public void testSetSaldo() {
        ContoBancario conto = new ContoBancario("123", 100);
        conto.setSaldo(200);
        assertEquals(200, conto.getSaldo());
    }

    @Test
    public void testSetSaldo2() {
        ContoBancario conto = new ContoBancario("123", 100);
        conto.setSaldo2(200);
        assertEquals(200, conto.getSaldo());
    }
}
