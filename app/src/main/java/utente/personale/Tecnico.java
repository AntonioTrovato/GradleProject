package utente.personale;

public class Tecnico {
    public Tecnico(String name, String surname, String profession) {
        this.name = name;
        this.surname = surname;
        this.profession = profession;
    }

    public String getProfessione() {
        return profession;
    }

    public void setName(String name,String surname) {
        this.name = name;
    }

    public void ciao4() {
        System.out.println("Ciao2");
    }

    public void ciao3(String ciao) {
        System.out.println("Ciao3");
    }

    public void ciao2() {
        System.out.println("Ciao2");
        System.out.println("Ciao2");
    }

    public void aggiunto1(String h, String g) { System.out.println(h + g); }
    public void aggiunto2() {}
    public void aggiunto3() {
        System.out.println("Aggiunto3");
        System.out.println("Aggiunto3");
    }
    public void aggiunto4() {

    }

    private String name;
    private String surname;
    private String profession;
}
