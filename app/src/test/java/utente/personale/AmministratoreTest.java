package utente.personale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AmministratoreTest {

    @Test
    public void testGetName() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        assertEquals("John", amministratore.getNome());
    }

    @Test
    public void testGetDepartment() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        assertEquals("HR", amministratore.getDepartment());
    }

    @Test
    public void testSetName() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        amministratore.setName("Jane");
        assertEquals("Jane", amministratore.getNome());
    }

    @Test
    public void testSetDepartment() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        amministratore.setDepartment("IT");
        assertEquals("IT", amministratore.getDepartment());
    }
}
