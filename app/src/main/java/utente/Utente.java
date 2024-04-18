package utente;

import banca.ContoBancario;

public class Utente {
    public Utente(String name, String surname, String telephone, ContoBancario contoBancario) {
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.contoBancario = contoBancario;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getTelephone() {
        return telephone;
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

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setContoBancario(ContoBancario contoBancario) {
        this.contoBancario = contoBancario;
    }

    private String name;
    private String surname;
    private String telephone;
    private ContoBancario contoBancario;
}
