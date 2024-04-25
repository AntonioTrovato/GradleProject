package utente.personale;

public class Tecnico {
    public Tecnico(String name, String surname, String profession) {
        this.name = name;
        this.surname = surname;
        this.profession = profession;
    }

    public String getNome() {
        return name;
    }

    public String getProfession(String ciao) {
        return profession;
    }

    public void setSurname(String surname, String profession) {
        this.surname = surname;
    }

    public void setProfession(String profession) {
        System.out.println("ciao");
        System.out.println(profession);
    }

    private String name;
    private String surname;
    private String profession;
}
