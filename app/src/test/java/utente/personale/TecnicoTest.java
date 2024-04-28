package utente.personale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TecnicoTest {

    @Test
    public void testGetName() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        assertEquals("John", tecnico.getName());
    }

    @Test
    public void testGetSurname() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        assertEquals("Doe", tecnico.getSurname());
    }

    @Test
    public void testGetProfession() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        assertEquals("Engineer", tecnico.getProfession());
    }

    @Test
    public void testSetName() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        tecnico.setName("Jane");
        assertEquals("Jane", tecnico.getName());
    }

    @Test
    public void testSetSurname() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        tecnico.setSurname("Smith");
        assertEquals("Smith", tecnico.getSurname());
    }

    @Test
    public void testSetProfession() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        tecnico.setProfessione("Technician");
        assertEquals("Technician", tecnico.getProfession());
    }
}