package utente.personale;

public class Tecnico {
    public Tecnico(String name, String surname, String profession) {
        this.name = name;
        this.surname = surname;
        this.profession = profession;
    }

    public String getName() {
        return name;
    }

    public String getProfession() {
        return profession;
    }

    public String setName(String name, String surname) {
        this.name = name;
        return name + " " + surname;
    }

    public void setProfessione(String profession) {
        this.profession = profession;
    }

    public String added_method_1 (String string) {
        System.out.println(string);
        return "added_method_1";
    }

    public String added_method_2 (String string) {return string;}

    private String name;
    private String surname;
    private String profession;
}
