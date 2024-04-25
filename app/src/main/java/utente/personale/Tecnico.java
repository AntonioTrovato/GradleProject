package utente.personale;

public class Tecnico {
    public Tecnico(String name, String surname, String profession) {
        this.name = name;
        this.surname = surname;
        this.profession = profession;
    }

    public String getProfession() {
        return profession;
    }

    public void setNome(String name) {
        this.name = name;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void printProfession() {
        System.out.println(this.profession);
    }

    public void ciao4() {
        System.out.println("Ciao2");
    }

    public void ciao3() {
        System.out.println("Ciao3");
    }

    private String name;
    private String surname;
    private String profession;
}
