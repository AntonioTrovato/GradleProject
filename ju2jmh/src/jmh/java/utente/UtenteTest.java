package utente;

import banca.ContoBancario;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UtenteTest {

    private Utente utente;

    private ContoBancario mockContoBancario;

    @Before
    public void setUp() {
        mockContoBancario = Mockito.mock(ContoBancario.class);
        utente = new Utente("John", "Doe", "1234567890", mockContoBancario);
    }

    @Test
    public void testGetName() {
        assertEquals("John", utente.getName());
    }

    @Test
    public void testGetSurname() {
        assertEquals("Doe", utente.getSurname());
    }

    @Test
    public void testGetContoBancario() {
        assertNotNull(utente.getContoBancario());
    }

    @Test
    public void testSetName() {
        utente.setName("Jane");
        assertEquals("Jane", utente.getName());
    }

    @Test
    public void testSetSurname() {
        utente.setSurname("Smith");
        assertEquals("Smith", utente.getSurname());
    }

    @Test
    public void testSetContoBancario() {
        ContoBancario nuovoConto = Mockito.mock(ContoBancario.class);
        utente.setContoBancario(nuovoConto);
        assertEquals(nuovoConto, utente.getContoBancario());
    }
}
