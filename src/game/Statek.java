package game;

public class Statek {
    private static int liczbaStatków = 0;

    private int rozmiarStatku;
    private Wspolrzedna[] wspStatku;
    private boolean trafiony[];
    private boolean zatopiony;


    public Statek(int s) {
        rozmiarStatku = s;
        wspStatku = new Wspolrzedna[s];
        for (int i = 0; i < s; i++) {
            wspStatku[i] = new Wspolrzedna();
        }

        trafiony = new boolean[s];
        for (int i = 0; i < s; i++) {
            trafiony[i] = false;
        }
        zatopiony = false;

        liczbaStatków += 1;
    }

    public int getRozmiarStatku() {
        return rozmiarStatku;
    }

    public void setWspółrzędna(int n, Wspolrzedna wsp) {
        wspStatku[n] = wsp;
    }

    public Wspolrzedna getWspółrzędna(int n) {
        return wspStatku[n];
    }

    public void setTrafiony(int n, boolean t) {
        trafiony[n] = t;
    }
    public boolean getTrafiony(int n) {
        return trafiony[n];
    }

    public void setZatopiony() {
        zatopiony = true;
    }

    public boolean getZatopiony() {
        return zatopiony;
    }

    public static int getLiczbaStatkow() {
        return liczbaStatków;
    }

}
