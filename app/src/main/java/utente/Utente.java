package utente;

import banca.ContoBancario;

public class Utente {
    public Utente(String name, String surname, String telephone, String address, ContoBancario contoBancario) {
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.address = address;
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

    public String getAddress() {
        return address;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public void num3() {System.out.println("2");}

    public void num4() {System.out.println("3");}

    public void hey() {System.out.println("prova2");}

    public void num6() {System.out.println("6");}

    public void setContoBancario(ContoBancario contoBancario) {
        this.contoBancario = contoBancario;
    }

    private String name;
    private String surname;
    private String telephone;
    private String address;
    private ContoBancario contoBancario;
}
