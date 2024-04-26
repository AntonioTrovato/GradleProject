package utente.personale;

public class Amministratore {
    public Amministratore(String name, String surname, String department) {
        this.name = name;
        this.surname = surname;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDepartment() {
        return department;
    }

    public String setName(String nome) {
        this.name = nome;
        return nome;
    }

    public void setSurnamo(String surname) {
        this.surname = surname;
    }

    public void setDepartment(String department) {
        System.out.println();
        this.department = department;
    }

    public    <T extends Tecnico>  int ciao     (int p) {return p;}

    private String name;
    private String surname;
    private String department;
}
