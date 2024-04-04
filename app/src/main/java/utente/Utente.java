package utente;

import banca.ContoBancario;

public class Utente {
    public Utente(String name, String surname, ContoBancario contoBancario) {
        this.name = name;
        this.surname = surname;
        this.contoBancario = contoBancario;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public ContoBancario getContoBancario() {
        return contoBancario;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setContoBancario(ContoBancario contoBancario) {
        this.contoBancario = contoBancario;
    }

    private String name;
    private String surname;
    private ContoBancario contoBancario;
}
