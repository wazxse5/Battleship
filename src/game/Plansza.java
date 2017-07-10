package game;

public class Plansza {
    private static final int ROZMIAR_PLANSZY = 10;

    private FieldStatus tablica[][];

    public Plansza() {
        tablica = new FieldStatus[ROZMIAR_PLANSZY][ROZMIAR_PLANSZY];

        for (int i = 0; i < ROZMIAR_PLANSZY; i++) {
            for (int j = 0; j < ROZMIAR_PLANSZY; j++) {
                tablica[i][j] = FieldStatus.PUSTY;
            }
        }
    }

    public void setStatus(int i, int j, FieldStatus s) {
        if (i >= 0 && i < ROZMIAR_PLANSZY && j >= 0 && j < ROZMIAR_PLANSZY){
            tablica[i][j] = s;
        }
    }

    public FieldStatus getStatus(int i, int j) {
        if (i >= 0 && i < ROZMIAR_PLANSZY && j >= 0 && j < ROZMIAR_PLANSZY){
            return tablica[i][j];
        }
        else {
            return FieldStatus.OUT_ERROR;
        }

    }


    public static int getRozmiarPlanszy() {
        return ROZMIAR_PLANSZY;
    }



}
