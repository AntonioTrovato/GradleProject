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

    public String getSurname() {
        return surname;
    }

    public String getProfession() {
        return profession;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setProfessione(String profession) {
        this.profession = profession;
    }

    public String getString(String string){
        return string;
    }

    private String name;
    private String surname;
    private String profession;
}
