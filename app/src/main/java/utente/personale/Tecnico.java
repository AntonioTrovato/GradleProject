package utente.personale;

public class Tecnico {
    public Tecnico(String name, String surname, String profession) {
        this.name = name;
        this.surname = surname;
        this.profession = profession;
    }

    public String getName() {
        System.out.println();
        return name;
    }

    public void getSurname() {
        System.out.println(surname);
    }

    public String getProfession() {
        return profession;
    }

    public String setName(String name, String surname, String profession) {
        this.name = name;
        System.out.println(profession);
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setProfessione(String profession) {
        this.profession = profession;
    }

    public static         void metodo_strano() {}

    public  static   <T>  int   altro_metodo(T prame) {
        return 3;
    }

    private String name;
    private String surname;
    private String profession;
}
