package game;

import javax.swing.JOptionPane;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        int q; // warunek ponownej gry
        do {
            Game gra = new Game();
            gra.setVisible(true);
            gra.graczUstawia();

            do{
                Thread.sleep(10);
            }while(gra.getGameStatus().equals(GameStatus.ROZPOCZETA) == false);

            gra.komputerUstawia();

            boolean graczTrafił;
            boolean kompTrafił;
            do {
                do {
                    graczTrafił = gra.graczStrzela();

                }while(graczTrafił == true);

                if (gra.sprawdzWygrana() == 1) break;

                do {
                    kompTrafił = gra.komputerStrzela();

                }while(kompTrafił == true);

            }while(gra.sprawdzWygrana() == 0);


            if (gra.sprawdzWygrana() == 1) {
                gra.setMessage("pozytyw", " Wygrałeś!!!\n\n BRAWO!!!   :D");
                JOptionPane.showMessageDialog(null, "Wygrałeś grę! Brawo!", "Zwycięstwo!", JOptionPane.PLAIN_MESSAGE, null);

            }
            if (gra.sprawdzWygrana() == 2) {
                gra.setMessage("negatyw", " Przegrałeś");
                JOptionPane.showMessageDialog(null, "Przegrałeś grę! Smuteczek :(", "Porażka!", JOptionPane.PLAIN_MESSAGE, null);

            }

            q = JOptionPane.showConfirmDialog(null, "Czy chcesz zagrać jeszcze raz?", "Koniec gry.", JOptionPane.YES_NO_OPTION);
            if (q == JOptionPane.YES_OPTION) gra.dispose();
            if (q == JOptionPane.NO_OPTION) System.exit(0);
        }while(q == JOptionPane.YES_OPTION);


    }

}
