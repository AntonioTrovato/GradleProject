package utente.personale;

import java.util.List;

public class Admin {

    public Admin(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static <Z> List<Z> hey(List<Z> c) {
        return c;
    }

    private String nome;
    private String email;
}
