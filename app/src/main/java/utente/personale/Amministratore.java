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

    public String setDepartment(String department) {
        System.out.println();
        this.department = department;
        return department;
    }

    public static void added_method () {}

    private String name;
    private String surname;
    private String department;
}
