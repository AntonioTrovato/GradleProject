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

    public void setProfessione(String profession) {
        this.profession = profession;
    }

    public static         void metodo_strano() {}

    public  static   <T>  int   altro_metodo(T prame, T prame2) {
        return 3;
    }

    public static void added_method () {}

    private String name;
    private String surname;
    private String profession;
}
