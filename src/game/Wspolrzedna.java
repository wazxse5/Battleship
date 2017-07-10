package game;

public class Wspolrzedna {
    private int wspX;
    private int wspY;

    public Wspolrzedna(int x, int y) {
        wspX = x;
        wspY = y;
    }
    public Wspolrzedna() {
        this(0,0);
    }


    public void setWspX(int x) {
        wspX = x;
    }
    public void setWspY(int y) {
        wspY = y;
    }


    public int getWspX() {
        return wspX;
    }
    public int getWspY() {
        return wspY;
    }
}
