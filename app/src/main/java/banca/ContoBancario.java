package banca;

public class ContoBancario {
    public ContoBancario(String idz, int saldo_iniziale) {
        this.id = idz;
        this.saldo = saldo_iniziale;
    }

    public static    <T> int prova () {
        return 0;
    }

    public void  getSaldi() {}

    public void setId(String id,String ciao) {
        this.id = id;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public void setSaldo2(int saldo) { this.saldo = saldo; }

    public void versamento(int quota) {
        this.saldo += quota;
    }

    public int prelievo(int quota) {
        if (this.saldo < quota)
            return 0;
        else
            this.saldo -= quota;
        return 1;
    }

    private String id;
    private int saldo;

}