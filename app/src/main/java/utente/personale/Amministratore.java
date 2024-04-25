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

    public void setName(String name, String surname, String department) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDepartment(String department) {
        this.department = department;
        System.out.println(department);
    }

    public void ciao() {
        System.out.println("Ciao");
    }

    private String name;
    private String surname;
    private String department;
}
