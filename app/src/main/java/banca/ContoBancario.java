package banca;

public class ContoBancario {
    public ContoBancario(String id, int saldo_iniziale) {
        this.id = id;
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