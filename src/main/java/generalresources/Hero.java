package generalresources;

import java.io.Serializable;

public class Hero implements Serializable {

    private String name;
    private int rank;

    public Hero(String n, int r) {
        this.name = n;
        this.rank = r;
    }

    public synchronized int getRank() {
        return rank;
    }

    public synchronized void setRank(int r) {
        this.rank = r;
    }

    public synchronized String getNome() {
        return name;
    }

    public synchronized void setNome(String nome) {
        this.name = nome;
    }

}