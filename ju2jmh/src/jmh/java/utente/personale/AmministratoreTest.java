package utente.personale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AmministratoreTest {

    @Test
    public void testGetName() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        assertEquals("John", amministratore.getName("ciao"));
    }

    /*@Test
    public void testGetSurname() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        assertEquals("Doe", amministratore.getSurname());
    }*/
    @Test
    public void testGetDepartment() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        assertEquals("HR", amministratore.getDepartment());
    }

    @Test
    public void testSetName() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        amministratore.setName("Jane");
        assertEquals("Jane", amministratore.getName("ciao"));
    }

    /*@Test
    public void testSetSurname() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        amministratore.setSurname("Smith");
        assertEquals("Smith", amministratore.getSurname());
    }*/
    @Test
    public void testSetDepartment() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        amministratore.setDepartment("IT");
        assertEquals("IT", amministratore.getDepartment());
    }

    @Test
    public void testAddedMethod() {
        String input = "Hello, world!";
        String actualOutput = Amministratore.added_method(input);
        assertEquals(input, actualOutput);
    }
}
