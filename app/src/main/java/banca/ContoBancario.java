package banca;

import utente.Utente;

public class ContoBancario {
    public ContoBancario(String idz, int saldo_iniziale) {
        this.id = idz;
        this.saldo = saldo_iniziale;
    }

    public String getId() {
        return id;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public void setSaldo2(int saldo) {
        ciao();
        Utente utente = new Utente("name","surname","telephone","address",this);
        utente.getName();
        this.saldo = saldo;
    }

    public void versamento(int quota) {
        this.saldo += quota;//commento
    }

    public void ciao() {
        System.out.println("Ciao");
        System.out.println("Saldo: " + this.saldo);
        System.out.println("Ciao");
    }

    public int prelievo(int quota) {
        ciao();
        Utente utente = new Utente("name","surname","telephone","address",this);
        utente.getName();
        //e commento
        if (this.saldo < quota)
            return 0;
        else
            this.saldo -= quota;
        return 1;
    }

    private String id;
    private int saldo;

}