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

    public String setSurname(String surname, String name) {
        this.surname = surname;
        return name;
    }

    public void setDipartmento(String department) {
        this.department = department;
    }

    public String added_method_1 (String string) {
        System.out.println(string);
        return "added_method_1";
    }

    public String added_method_2 (String string) {return string;}

    private String name;
    private String surname;
    private String department;
}
