package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class PlanszaPanel extends JPanel {
    private Plansza p;
    private Rectangle2D[][] pole = new Rectangle2D[Plansza.getRozmiarPlanszy()][Plansza.getRozmiarPlanszy()];
    private boolean cheat;

    public PlanszaPanel(Plansza p) {
        setPreferredSize(new Dimension(350, 330));
        this.p = p;
        cheat = false;

        for (int i = 0; i < Plansza.getRozmiarPlanszy(); i++) {
            for (int j = 0; j < Plansza.getRozmiarPlanszy(); j++) {
                pole[i][j] = new Rectangle2D.Double(25 + i*30, 10 + j*30, 28, 28);

            }
        }

    }

    public void setCheat(boolean c) {
        cheat = c;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        for (int i = 0; i < Plansza.getRozmiarPlanszy(); i++) {
            for (int j = 0; j < Plansza.getRozmiarPlanszy(); j++) {
                g2.setColor(new Color(140, 230, 251));
                g2.fill(pole[i][j]);
                g2.setColor(Color.BLACK);
                g2.draw(pole[i][j]);



                if (p.getStatus(i, j).equals(FieldStatus.STATEK)) {
                    g2.fill(pole[i][j]);
                }
                if (p.getStatus(i, j).equals(FieldStatus.PUDLO)) {
                    g2.setColor(Color.GRAY.brighter());
                    g2.fill(pole[i][j]);
                    g2.setColor(Color.BLACK);
                    g2.draw(pole[i][j]);
                }
                if (p.getStatus(i, j).equals(FieldStatus.TRAFIONY)) {
                    g2.setColor(Color.RED.darker());
                    g2.fill(pole[i][j]);
                    g2.setColor(Color.BLACK);
                    g2.draw(pole[i][j]);
                }
                if (p.getStatus(i, j).equals(FieldStatus.ZATOPIONY)) {
                    g2.setColor(Color.RED);
                    g2.fill(pole[i][j]);
                    g2.setColor(Color.BLACK);
                    g2.draw(pole[i][j]);
                }
                if (cheat == true) {
                    if (p.getStatus(i, j).equals(FieldStatus.UKRYTY)) {
                        g2.setColor(Color.YELLOW);
                        g2.fill(pole[i][j]);
                        g2.setColor(Color.BLACK);
                        g2.draw(pole[i][j]);
                    }
                }

            }
        }
    }

}