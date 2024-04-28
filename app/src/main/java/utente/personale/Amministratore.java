package utente.personale;

public class Amministratore {
    public Amministratore(String name, String surname, String department) {
        this.name = name;
        this.surname = surname;
        this.department = department;
    }

    public String getNome() {
        return this.name;
    }

    public String getDepartment() {
        return department;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public static      <T> String      added_method(String str) {
        return str;
    }

    private String name;
    private String surname;
    private String department;
}
