package game;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Game extends JFrame {


    private static final String version = "beta";

    private GameType typGry;
    private GameStatus stanGry;
    private Wspolrzedna lastShot;
    private boolean strzelono;
    private Wspolrzedna[] lastTrafiony = new Wspolrzedna[3];
    private boolean czyOstatnioTrafiony;
    private int licznikSprawdzonych;
    private boolean cheatWin;
    private boolean cheatLose;

    private int licznikZatopionychGracza;
    private int licznikZatopionychKomputera;

    private Plansza PlanszaKomputera;
    private Plansza PlanszaGracza;
    private Statek[] okretyKomputera = new Statek[10];
    private Statek[] okretyGracza = new Statek[10];
    private JPanel obrazPlanszyKomputera;
    private JPanel obrazPlanszyGracza;

    private JLabel napisGracza;
    private JLabel napisKomp;
    private JTextArea infoArea;
    private JTextArea chatField;
    private JTextArea chatHistory;
    private JButton bMulti;
    private JButton bSingle;
    private JButton bChangeMap;
    private JButton bStart;


    public Game() {
        typGry = GameType.SINGLE;
        stanGry = GameStatus.NIE_ROZPOCZETA;
        lastShot = new Wspolrzedna();
        strzelono = false;
        for(int i = 0; i < 3; i++) {
            lastTrafiony[i] = new Wspolrzedna();
        }
        czyOstatnioTrafiony = false;
        licznikSprawdzonych = 0;
        licznikZatopionychGracza = 0;
        licznikZatopionychKomputera = 0;
        cheatWin = false;
        cheatLose = false;

        for (int i = 0; i < 10; i++) {
            if (i == 0) {okretyGracza[i] = new Statek(4);}
            if (i == 1 || i == 2) {okretyGracza[i] = new Statek(3);}
            if (i == 3 || i == 4 || i == 5) {okretyGracza[i] = new Statek(2);}
            if (i == 6 || i == 7 || i == 8 || i == 9) {okretyGracza[i] = new Statek(1);}
        }

        setTitle("Okręty by Pawełek " + version);
        setSize(700,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new WindowListener());

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints GBClabelKomp = new GridBagConstraints();
        GBClabelKomp.gridx = 2;
        GBClabelKomp.gridy = 0;
        GBClabelKomp.gridwidth = 2;
        GBClabelKomp.gridheight = 1;

        GridBagConstraints GBClabelGracz = new GridBagConstraints();
        GBClabelGracz.gridx = 0;
        GBClabelGracz.gridy = 0;
        GBClabelGracz.gridwidth = 2;
        GBClabelGracz.gridheight = 1;

        GridBagConstraints GBCplKomp = new GridBagConstraints(); // GridBagConstraintsPlanszaKomputera
        GBCplKomp.gridx = 2;
        GBCplKomp.gridy = 1;
        GBCplKomp.gridwidth = 2;
        GBCplKomp.gridheight = 2;

        GridBagConstraints GBCplGracz = new GridBagConstraints(); // GridBagConstraintsPlanszaGracza
        GBCplGracz.gridx = 0;
        GBCplGracz.gridy = 1;
        GBCplGracz.gridwidth = 2;
        GBCplGracz.gridheight = 2;

        GridBagConstraints GBCbuttonPanel = new GridBagConstraints();
        GBCbuttonPanel.gridx = 0;
        GBCbuttonPanel.gridy = 3;
        GBCbuttonPanel.gridwidth = 2;
        GBCbuttonPanel.gridheight = 2;

        GridBagConstraints GBCchatPanel = new GridBagConstraints();
        GBCchatPanel.gridx = 2;
        GBCchatPanel.gridy = 3;
        GBCchatPanel.gridwidth = 2;
        GBCchatPanel.gridheight = 2;


        PlanszaGracza = new Plansza();
        PlanszaKomputera = new Plansza();

        MyszaListener Mysza = new MyszaListener();

        obrazPlanszyKomputera = new PlanszaPanel(PlanszaKomputera);
        obrazPlanszyGracza = new PlanszaPanel(PlanszaGracza);
        obrazPlanszyKomputera.addMouseListener(Mysza);

        add(obrazPlanszyKomputera, GBCplKomp);
        add(obrazPlanszyGracza, GBCplGracz);

        napisGracza = new JLabel("Twoja Plansza");
        napisGracza.setFont(new Font(null, Font.BOLD, 25));
        napisGracza.setBounds(10, 10, 100, 20);
        add(napisGracza, GBClabelGracz);
        napisKomp = new JLabel("Plansza Komputera");
        napisKomp.setFont(new Font(null, Font.BOLD, 25));
        napisKomp.setBounds(10, 10, 100, 20);
        add(napisKomp, GBClabelKomp);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(350, 200));
        buttonPanel.setLayout(null);
        bStart = new JButton("Start Gry");
        bStart.setBounds(25, 0, 145, 30);
        bStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                startGry();
                setMessage("neutral", " Gra rozpoczęła się.\n\n Strzelaj!\n Twoja Kolej");
                bStart.setEnabled(false);
                bChangeMap.setEnabled(false);
            }
        });
        buttonPanel.add(bStart);
        bChangeMap = new JButton("Zmień ustawienie");
        bChangeMap.setBounds(180, 0, 145, 30);
        bChangeMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graczUstawia();
            }
        });
        buttonPanel.add(bChangeMap);
        infoArea = new JTextArea("");
        infoArea.setBounds(25, 40, 300, 100);
        infoArea.setBorder(BorderFactory.createEtchedBorder());
        infoArea.setFont(new Font(null, Font.BOLD, 16));
        setMessage("neutral", " Witaj w grze okręty\n Aby rozpocząć naciśnij start");
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        buttonPanel.add(infoArea);
        bSingle = new JButton("Single Player");
        bSingle.setBounds(25, 150, 145, 30);
        bSingle.setEnabled(false);
        buttonPanel.add(bSingle);
        bMulti = new JButton("Informacje");  // chwilowo przycisk informacji
        bMulti.setBounds(180, 150, 145, 30);
        bMulti.setEnabled(true);
        bMulti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wynik = "" + JOptionPane.showInputDialog(null, " Paweł Grzegórzko 2G \n czerwiec 2015 \n\n Wszelkie prawa zastrzeżone !!! \n\n Wpisz kod:", "Informacje", JOptionPane.INFORMATION_MESSAGE);
                wynik = wynik.trim().toLowerCase();
                if (wynik.equals("show")) {
                    ((PlanszaPanel) obrazPlanszyKomputera).setCheat(true);
                    obrazPlanszyKomputera.repaint();
                }
                if (wynik.equals("hide")) {
                    ((PlanszaPanel) obrazPlanszyKomputera).setCheat(false);
                    obrazPlanszyKomputera.repaint();
                }
                if (stanGry.equals(GameStatus.NIE_ROZPOCZETA) == false) {
                    if (wynik.equals("win")) {
                        for (int i = 0; i < 10; i++) {
                            okretyKomputera[i].setZatopiony();
                        }
                    }
                    if (wynik.equals("lose")) {
                        for (int i = 0; i < 10; i++) {
                            okretyGracza[i].setZatopiony();
                        }
                    }
                }


            }
        });
        buttonPanel.add(bMulti);
        add(buttonPanel, GBCbuttonPanel);

        JPanel chatPanel = new JPanel();
        chatPanel.setPreferredSize(new Dimension(350, 200));
        chatPanel.setLayout(null);
        JLabel napisChat = new JLabel("Chat:");
        napisChat.setBounds(25, 0, 300, 20);
        napisChat.setFont(new Font(null, Font.BOLD, 24));
        chatPanel.add(napisChat);
        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatHistory);
        chatScroll.setBounds(25, 30, 300, 120);
        chatPanel.add(chatScroll);
        chatField = new JTextArea();
        chatField.setEditable(false);
        chatField.setFont(new Font(null, Font.PLAIN, 14));
        JScrollPane messageScroll = new JScrollPane(chatField);
        messageScroll.setBounds(25, 155, 300, 25);
        chatPanel.add(messageScroll);

        add(chatPanel, GBCchatPanel);


        pack();

    }



    public void startGry() {
        if (typGry.equals(GameType.MULTI) || typGry.equals(GameType.SINGLE)) {
            stanGry = GameStatus.ROZPOCZETA;
        }
    }

    public void komputerUstawia() {
        if (stanGry.equals(GameStatus.ROZPOCZETA)) {


            stanGry = GameStatus.KOMP_USTAWIA;
            Random r = new Random();

            for (int i = 0; i < 10; i++) {
                if (i == 0) {okretyKomputera[i] = new Statek(4);}
                if (i == 1 || i == 2) {okretyKomputera[i] = new Statek(3);}
                if (i == 3 || i == 4 || i == 5) {okretyKomputera[i] = new Statek(2);}
                if (i == 6 || i == 7 || i == 8 || i == 9) {okretyKomputera[i] = new Statek(1);}

                for (int j = 0; j < okretyKomputera[i].getRozmiarStatku(); j++) {

                    boolean czyMożliwe; // czy możliwe jest ustawienie statku w danym miejscu
                    int x,y;

                    do {
                        x = r.nextInt(Plansza.getRozmiarPlanszy());
                        y = r.nextInt(Plansza.getRozmiarPlanszy());

                        if (PlanszaKomputera.getStatus(x, y).equals(FieldStatus.PUSTY) || PlanszaKomputera.getStatus(x, y).equals(FieldStatus.DOSTEPNY) ) {
                            czyMożliwe = true;
                        }
                        else {czyMożliwe = false;}

                    }while(czyMożliwe == false);

                    okretyKomputera[i].setWspółrzędna(j, new Wspolrzedna(x, y));

                    for (int a = 0; a < Plansza.getRozmiarPlanszy(); a++) {
                        for (int b = 0; b < Plansza.getRozmiarPlanszy(); b++) {
                            if (PlanszaKomputera.getStatus(a, b).equals(FieldStatus.STATEK) == false &&  PlanszaKomputera.getStatus(a, b).equals(FieldStatus.DOSTEPNY) == false && PlanszaKomputera.getStatus(a, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                                PlanszaKomputera.setStatus(a, b, FieldStatus.NIEDOSTEPNY);
                            }
                        }
                    }

                    PlanszaKomputera.setStatus(x, y, FieldStatus.STATEK);

                    if (PlanszaKomputera.getStatus(x + 1, y).equals(FieldStatus.STATEK) == false && PlanszaKomputera.getStatus(x + 1, y).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                        PlanszaKomputera.setStatus(x + 1, y, FieldStatus.DOSTEPNY);
                    }
                    if (PlanszaKomputera.getStatus(x - 1 , y).equals(FieldStatus.STATEK) == false  && PlanszaKomputera.getStatus(x - 1, y).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                        PlanszaKomputera.setStatus(x - 1, y, FieldStatus.DOSTEPNY);
                    }
                    if (PlanszaKomputera.getStatus(x, y + 1).equals(FieldStatus.STATEK) == false  && PlanszaKomputera.getStatus(x, y + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                        PlanszaKomputera.setStatus(x, y + 1, FieldStatus.DOSTEPNY);
                    }
                    if (PlanszaKomputera.getStatus(x, y - 1).equals(FieldStatus.STATEK) == false  && PlanszaKomputera.getStatus(x, y - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                        PlanszaKomputera.setStatus(x, y - 1, FieldStatus.DOSTEPNY);
                    }

                }

                for (int a = 0; a < Plansza.getRozmiarPlanszy(); a++) {
                    for (int b = 0; b < Plansza.getRozmiarPlanszy(); b++) {
                        if (PlanszaKomputera.getStatus(a, b).equals(FieldStatus.DOSTEPNY)) {
                            PlanszaKomputera.setStatus(a, b, FieldStatus.SUPER_NIEDOSTEPNY);
                        }

                        if (PlanszaKomputera.getStatus(a, b).equals(FieldStatus.STATEK) == false && PlanszaKomputera.getStatus(a, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                            PlanszaKomputera.setStatus(a, b, FieldStatus.PUSTY);
                        }
                    }
                }

                /// Łatanie dziur
                if (i == 1 || i == 2) {
                    for (int a = 0; a < Plansza.getRozmiarPlanszy(); a++) {
                        for (int b = 0; b < Plansza.getRozmiarPlanszy(); b++) {
                            if (PlanszaKomputera.getStatus(a, b).equals(FieldStatus.PUSTY)) {
                                if ((PlanszaKomputera.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                        PlanszaKomputera.getStatus(a - 2, b).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a + 2, b).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a, b - 2).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a, b + 2).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a - 1, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a - 1, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a - 1, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a + 1, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a + 1, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a + 1, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a - 1, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a + 1, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                        (PlanszaKomputera.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                                PlanszaKomputera.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY))) {
                                }
                            }
                        }
                    }
                }


                if (i == 3 || i == 4 || i == 5) {
                    for (int a = 0; a < Plansza.getRozmiarPlanszy(); a++) {
                        for (int b = 0; b < Plansza.getRozmiarPlanszy(); b++) {
                            if (PlanszaKomputera.getStatus(a, b).equals(FieldStatus.PUSTY)) {
                                if (PlanszaKomputera.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) &&
                                        PlanszaKomputera.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) &&
                                        PlanszaKomputera.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) &&
                                        PlanszaKomputera.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) {
                                    PlanszaKomputera.setStatus(a, b, FieldStatus.NIEDOSTEPNY);
                                }
                            }
                        }
                    }
                }
                // koniec łatania
            }
            for (int a = 0; a < Plansza.getRozmiarPlanszy(); a++) {
                for (int b = 0; b < Plansza.getRozmiarPlanszy(); b++) {
                    if (PlanszaKomputera.getStatus(a, b).equals(FieldStatus.STATEK) == false) {
                        PlanszaKomputera.setStatus(a, b, FieldStatus.PUSTY);
                    }
                    if (PlanszaKomputera.getStatus(a, b).equals(FieldStatus.STATEK)) {
                        PlanszaKomputera.setStatus(a, b, FieldStatus.UKRYTY);
                    }
                }
            }
            obrazPlanszyKomputera.repaint();
            stanGry = GameStatus.KOMP_USTAWIL;
        }
    }

    public void graczUstawia() {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                PlanszaGracza.setStatus(i, j, FieldStatus.PUSTY);
            }
        }
        obrazPlanszyGracza.repaint();

        stanGry = GameStatus.GRACZ_USTAWIA;
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < okretyGracza[i].getRozmiarStatku(); j++) {
				/*try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
                obrazPlanszyGracza.repaint();
                boolean czyMożliwe; // czy możliwe jest ustawienie statku w danym miejscu
                int x,y;

                do {
                    x = r.nextInt(Plansza.getRozmiarPlanszy());
                    y = r.nextInt(Plansza.getRozmiarPlanszy());

                    if (PlanszaGracza.getStatus(x, y).equals(FieldStatus.PUSTY) || PlanszaGracza.getStatus(x, y).equals(FieldStatus.DOSTEPNY) ) {
                        czyMożliwe = true;
                    }
                    else {czyMożliwe = false;}

                }while(czyMożliwe == false);

                okretyGracza[i].setWspółrzędna(j, new Wspolrzedna(x, y));

                for (int a = 0; a < Plansza.getRozmiarPlanszy(); a++) {
                    for (int b = 0; b < Plansza.getRozmiarPlanszy(); b++) {
                        if (PlanszaGracza.getStatus(a, b).equals(FieldStatus.STATEK) == false &&  PlanszaGracza.getStatus(a, b).equals(FieldStatus.DOSTEPNY) == false && PlanszaGracza.getStatus(a, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                            PlanszaGracza.setStatus(a, b, FieldStatus.NIEDOSTEPNY);
                        }
                    }
                }

                PlanszaGracza.setStatus(x, y, FieldStatus.STATEK);

                if (PlanszaGracza.getStatus(x + 1, y).equals(FieldStatus.STATEK) == false && PlanszaGracza.getStatus(x + 1, y).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                    PlanszaGracza.setStatus(x + 1, y, FieldStatus.DOSTEPNY);
                }
                if (PlanszaGracza.getStatus(x - 1 , y).equals(FieldStatus.STATEK) == false  && PlanszaGracza.getStatus(x - 1, y).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                    PlanszaGracza.setStatus(x - 1, y, FieldStatus.DOSTEPNY);
                }
                if (PlanszaGracza.getStatus(x, y + 1).equals(FieldStatus.STATEK) == false  && PlanszaGracza.getStatus(x, y + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                    PlanszaGracza.setStatus(x, y + 1, FieldStatus.DOSTEPNY);
                }
                if (PlanszaGracza.getStatus(x, y - 1).equals(FieldStatus.STATEK) == false  && PlanszaGracza.getStatus(x, y - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                    PlanszaGracza.setStatus(x, y - 1, FieldStatus.DOSTEPNY);
                }

            }

            for (int a = 0; a < Plansza.getRozmiarPlanszy(); a++) {
                for (int b = 0; b < Plansza.getRozmiarPlanszy(); b++) {
                    if (PlanszaGracza.getStatus(a, b).equals(FieldStatus.DOSTEPNY)) {
                        PlanszaGracza.setStatus(a, b, FieldStatus.SUPER_NIEDOSTEPNY);
                    }

                    if (PlanszaGracza.getStatus(a, b).equals(FieldStatus.STATEK) == false && PlanszaGracza.getStatus(a, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) == false) {
                        PlanszaGracza.setStatus(a, b, FieldStatus.PUSTY);
                    }
                }
            }

            /// Łatanie dziur
            if (i == 1 || i == 2) {
                for (int a = 0; a < Plansza.getRozmiarPlanszy(); a++) {
                    for (int b = 0; b < Plansza.getRozmiarPlanszy(); b++) {
                        if (PlanszaGracza.getStatus(a, b).equals(FieldStatus.PUSTY)) {
                            if ((PlanszaGracza.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                    PlanszaGracza.getStatus(a - 2, b).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a + 2, b).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a, b - 2).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a, b + 2).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a - 1, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a - 1, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a - 1, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a + 1, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a + 1, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a + 1, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a - 1, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a + 1, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) ||
                                    (PlanszaGracza.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) ||
                                            PlanszaGracza.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY))) {
                            }
                        }
                    }
                }
            }
            if (i == 3 || i == 4 || i == 5) {
                for (int a = 0; a < Plansza.getRozmiarPlanszy(); a++) {
                    for (int b = 0; b < Plansza.getRozmiarPlanszy(); b++) {
                        if (PlanszaGracza.getStatus(a, b).equals(FieldStatus.PUSTY)) {
                            if (PlanszaGracza.getStatus(a - 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) &&
                                    PlanszaGracza.getStatus(a + 1, b).equals(FieldStatus.SUPER_NIEDOSTEPNY) &&
                                    PlanszaGracza.getStatus(a, b - 1).equals(FieldStatus.SUPER_NIEDOSTEPNY) &&
                                    PlanszaGracza.getStatus(a, b + 1).equals(FieldStatus.SUPER_NIEDOSTEPNY)) {
                                PlanszaGracza.setStatus(a, b, FieldStatus.NIEDOSTEPNY);
                            }
                        }
                    }
                }
            }
            // koniec łatania

        }
        for (int a = 0; a < Plansza.getRozmiarPlanszy(); a++) {
            for (int b = 0; b < Plansza.getRozmiarPlanszy(); b++) {
                if (PlanszaGracza.getStatus(a, b).equals(FieldStatus.STATEK) == false) {
                    PlanszaGracza.setStatus(a, b, FieldStatus.PUSTY);
                }
            }
        }
        obrazPlanszyGracza.repaint();
        stanGry = GameStatus.GRACZ_USTAWIL;
        stanGry = GameStatus.NIE_ROZPOCZETA;
    }

    public boolean komputerStrzela() {
        stanGry = GameStatus.KOMP_STRZELA;
        boolean wynik = false;

        ////
        int z = 0;
        for (int i = 0; i < 10; i++) {
            if (okretyGracza[i].getZatopiony() == true) {
                z++;
            }
        }
        licznikZatopionychGracza = z;
        ////

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean czyMożliwy = false; // czy możliwy jest strzał w dane miejsce
        Random r = new Random();
        int x,y;
        do {
            if (czyOstatnioTrafiony == true) {
                if (licznikSprawdzonych == 0) {
                    x = lastTrafiony[0].getWspX() + 1;
                    y = lastTrafiony[0].getWspY();
                }
                else if (licznikSprawdzonych == 1) {
                    x = lastTrafiony[0].getWspX() - 1;
                    y = lastTrafiony[0].getWspY();
                }
                else if (licznikSprawdzonych == 2) {
                    x = lastTrafiony[0].getWspX();
                    y = lastTrafiony[0].getWspY() + 1;
                }
                else if (licznikSprawdzonych == 3) {
                    x = lastTrafiony[0].getWspX();
                    y = lastTrafiony[0].getWspY() - 1;
                }
                else if (licznikSprawdzonych == 4) {
                    x = lastTrafiony[1].getWspX() + 1;
                    y = lastTrafiony[1].getWspY();
                }
                else if (licznikSprawdzonych == 5) {
                    x = lastTrafiony[1].getWspX() - 1;
                    y = lastTrafiony[1].getWspY();
                }
                else if (licznikSprawdzonych == 6) {
                    x = lastTrafiony[1].getWspX();
                    y = lastTrafiony[1].getWspY() + 1;
                }
                else if (licznikSprawdzonych == 7) {
                    x = lastTrafiony[1].getWspX();
                    y = lastTrafiony[1].getWspY() - 1;
                }
                else if (licznikSprawdzonych == 8) {
                    x = lastTrafiony[2].getWspX() + 1;
                    y = lastTrafiony[2].getWspY();
                }
                else if (licznikSprawdzonych == 9) {
                    x = lastTrafiony[2].getWspX() - 1;
                    y = lastTrafiony[2].getWspY();
                }
                else if (licznikSprawdzonych == 10) {
                    x = lastTrafiony[2].getWspX();
                    y = lastTrafiony[2].getWspY() + 1;
                }
                else if (licznikSprawdzonych == 11) {
                    x = lastTrafiony[2].getWspX();
                    y = lastTrafiony[2].getWspY() - 1;
                }
                else {
                    x = r.nextInt(Plansza.getRozmiarPlanszy());
                    y = r.nextInt(Plansza.getRozmiarPlanszy());
                }
                licznikSprawdzonych += 1;
            }
            else {
                x = r.nextInt(Plansza.getRozmiarPlanszy());
                y = r.nextInt(Plansza.getRozmiarPlanszy());
            }





            if (PlanszaGracza.getStatus(x, y).equals(FieldStatus.PUSTY) || PlanszaGracza.getStatus(x, y).equals(FieldStatus.STATEK)) {
                czyMożliwy = true;
            }
        }while(czyMożliwy == false);

        if (PlanszaGracza.getStatus(x, y).equals(FieldStatus.STATEK)) {
            PlanszaGracza.setStatus(x, y, FieldStatus.TRAFIONY);
            setMessage("negatyw", " Komputer trafił twój statek!\n Komputer strzela jeszcze raz");
            czyOstatnioTrafiony = true;
            licznikSprawdzonych = 0;
            wynik = true;

            lastTrafiony[2] = new Wspolrzedna(lastTrafiony[1].getWspX(), lastTrafiony[1].getWspY());
            lastTrafiony[1] = new Wspolrzedna(lastTrafiony[0].getWspX(), lastTrafiony[0].getWspY());
            lastTrafiony[0] = new Wspolrzedna(x, y);
			/*
			System.out.println("1. (" + lastTrafiony[0].getWspX() + "," + lastTrafiony[0].getWspY() + ")");
			System.out.println("2. (" + lastTrafiony[1].getWspX() + "," + lastTrafiony[1].getWspY() + ")");
			System.out.println("3. (" + lastTrafiony[2].getWspX() + "," + lastTrafiony[2].getWspY() + ")\n");
			*/
        }


        if (PlanszaGracza.getStatus(x, y).equals(FieldStatus.PUSTY)) {
            PlanszaGracza.setStatus(x, y, FieldStatus.PUDLO);
            setMessage("neutral", " Strzelaj!\n Twoja kolej...");
            wynik = false;
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < okretyGracza[i].getRozmiarStatku(); j++) {
                if (okretyGracza[i].getWspółrzędna(j).getWspX() == x && okretyGracza[i].getWspółrzędna(j).getWspY() == y) {
                    okretyGracza[i].setTrafiony(j, true);
                }
            }
        }

        int pomocniczy_licznik = 0;
        for (int i = 0; i < 10; i++) {
            boolean pomocnicza_zmienna = true;
            for (int j = 0; j < okretyGracza[i].getRozmiarStatku(); j++) {
                if (okretyGracza[i].getTrafiony(j) == false) pomocnicza_zmienna = false;
            }
            if (pomocnicza_zmienna == true) {
                okretyGracza[i].setZatopiony();
                pomocniczy_licznik += 1;
            }
        }

        if (pomocniczy_licznik > licznikZatopionychGracza) {
            setMessage("negatyw", " Komputer zatopił twój statek!\n Komputer strzela jeszcze raz");
            czyOstatnioTrafiony = false;
        }
        if (pomocniczy_licznik == 10) {
            wynik = false;
        }


        for (int i = 0; i < 10; i++) {
            if (okretyGracza[i].getZatopiony() == true) {
                for (int j = 0; j < okretyGracza[i].getRozmiarStatku(); j++) {
                    PlanszaGracza.setStatus(okretyGracza[i].getWspółrzędna(j).getWspX(), okretyGracza[i].getWspółrzędna(j).getWspY(), FieldStatus.ZATOPIONY);

                    if (PlanszaGracza.getStatus(okretyGracza[i].getWspółrzędna(j).getWspX() + 1, okretyGracza[i].getWspółrzędna(j).getWspY()).equals(FieldStatus.PUSTY)) {
                        PlanszaGracza.setStatus(okretyGracza[i].getWspółrzędna(j).getWspX() + 1, okretyGracza[i].getWspółrzędna(j).getWspY(), FieldStatus.PUDLO);
                    }
                    if (PlanszaGracza.getStatus(okretyGracza[i].getWspółrzędna(j).getWspX() - 1, okretyGracza[i].getWspółrzędna(j).getWspY()).equals(FieldStatus.PUSTY)) {
                        PlanszaGracza.setStatus(okretyGracza[i].getWspółrzędna(j).getWspX() - 1, okretyGracza[i].getWspółrzędna(j).getWspY(), FieldStatus.PUDLO);
                    }
                    if (PlanszaGracza.getStatus(okretyGracza[i].getWspółrzędna(j).getWspX(), okretyGracza[i].getWspółrzędna(j).getWspY() + 1).equals(FieldStatus.PUSTY)) {
                        PlanszaGracza.setStatus(okretyGracza[i].getWspółrzędna(j).getWspX(), okretyGracza[i].getWspółrzędna(j).getWspY() + 1, FieldStatus.PUDLO);
                    }
                    if (PlanszaGracza.getStatus(okretyGracza[i].getWspółrzędna(j).getWspX(), okretyGracza[i].getWspółrzędna(j).getWspY() - 1).equals(FieldStatus.PUSTY)) {
                        PlanszaGracza.setStatus(okretyGracza[i].getWspółrzędna(j).getWspX(), okretyGracza[i].getWspółrzędna(j).getWspY() - 1, FieldStatus.PUDLO);
                    }
                }
            }
        }
        obrazPlanszyGracza.repaint();
        return wynik;
    }

    public boolean graczStrzela() {
        boolean wynik = false;

        int z = 0;
        for (int i = 0; i < 10; i++) {
            if (okretyKomputera[i].getZatopiony() == true) {
                z++;
            }
        }
        licznikZatopionychKomputera = z;

        boolean czyMożliwy = false; // czy strzał w dane pole jest możliwy
        do {
            do {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(strzelono == false);
            if (PlanszaKomputera.getStatus(lastShot.getWspX(), lastShot.getWspY()).equals(FieldStatus.PUSTY) || PlanszaKomputera.getStatus(lastShot.getWspX(), lastShot.getWspY()).equals(FieldStatus.UKRYTY)) {
                czyMożliwy = true;
            }
            else {
                czyMożliwy = false;
                strzelono = false;
            }

        }while(czyMożliwy == false);

        //JOptionPane.showMessageDialog(null, lastShot.getWspX() + "," + lastShot.getWspY());
        strzelono = false;
        if (PlanszaKomputera.getStatus(lastShot.getWspX(), lastShot.getWspY()).equals(FieldStatus.UKRYTY)) {
            PlanszaKomputera.setStatus(lastShot.getWspX(), lastShot.getWspY(), FieldStatus.TRAFIONY);
            setMessage("pozytyw", " Trafiłeś statek komputera!\n Strzelaj jeszcze raz");
            wynik = true;
        }
        if (PlanszaKomputera.getStatus(lastShot.getWspX(), lastShot.getWspY()).equals(FieldStatus.PUSTY)) {
            PlanszaKomputera.setStatus(lastShot.getWspX(), lastShot.getWspY(), FieldStatus.PUDLO);
            setMessage("neutral", " Komputer strzela!\n Uważaj...");
            wynik = false;
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < okretyKomputera[i].getRozmiarStatku(); j++) {
                if(okretyKomputera[i].getWspółrzędna(j).getWspX() == lastShot.getWspX() && okretyKomputera[i].getWspółrzędna(j).getWspY() == lastShot.getWspY()) {
                    okretyKomputera[i].setTrafiony(j, true);
                }
            }
        }

        int pomocniczy_licznik = 0;
        for (int i = 0; i < 10; i++) {
            boolean pomocnicza_zmienna = true;
            for (int j = 0; j < okretyKomputera[i].getRozmiarStatku(); j++) {
                if (okretyKomputera[i].getTrafiony(j) == false) pomocnicza_zmienna = false;
            }
            if (pomocnicza_zmienna == true) {
                okretyKomputera[i].setZatopiony();
                pomocniczy_licznik += 1;
            }
        }

        if (pomocniczy_licznik > licznikZatopionychKomputera) {
            setMessage("pozytyw", " Zatopiłeś statek komputera!\n Strzelaj jeszcze raz");
        }
        if (pomocniczy_licznik == 10) {
            wynik = false;
        }



        for (int i = 0; i < 10; i++) {
            if (okretyKomputera[i].getZatopiony() == true) {
                for (int j = 0; j < okretyKomputera[i].getRozmiarStatku(); j++) {
                    PlanszaKomputera.setStatus(okretyKomputera[i].getWspółrzędna(j).getWspX(), okretyKomputera[i].getWspółrzędna(j).getWspY(), FieldStatus.ZATOPIONY);

                    if (PlanszaKomputera.getStatus(okretyKomputera[i].getWspółrzędna(j).getWspX() + 1, okretyKomputera[i].getWspółrzędna(j).getWspY()).equals(FieldStatus.PUSTY)) {
                        PlanszaKomputera.setStatus(okretyKomputera[i].getWspółrzędna(j).getWspX() + 1, okretyKomputera[i].getWspółrzędna(j).getWspY(), FieldStatus.PUDLO);
                    }
                    if (PlanszaKomputera.getStatus(okretyKomputera[i].getWspółrzędna(j).getWspX() - 1, okretyKomputera[i].getWspółrzędna(j).getWspY()).equals(FieldStatus.PUSTY)) {
                        PlanszaKomputera.setStatus(okretyKomputera[i].getWspółrzędna(j).getWspX() - 1, okretyKomputera[i].getWspółrzędna(j).getWspY(), FieldStatus.PUDLO);
                    }
                    if (PlanszaKomputera.getStatus(okretyKomputera[i].getWspółrzędna(j).getWspX(), okretyKomputera[i].getWspółrzędna(j).getWspY() + 1).equals(FieldStatus.PUSTY)) {
                        PlanszaKomputera.setStatus(okretyKomputera[i].getWspółrzędna(j).getWspX(), okretyKomputera[i].getWspółrzędna(j).getWspY() + 1, FieldStatus.PUDLO);
                    }
                    if (PlanszaKomputera.getStatus(okretyKomputera[i].getWspółrzędna(j).getWspX(), okretyKomputera[i].getWspółrzędna(j).getWspY() - 1).equals(FieldStatus.PUSTY)) {
                        PlanszaKomputera.setStatus(okretyKomputera[i].getWspółrzędna(j).getWspX(), okretyKomputera[i].getWspółrzędna(j).getWspY() - 1, FieldStatus.PUDLO);
                    }
                }
            }
        }

        obrazPlanszyKomputera.repaint();
        return wynik;
    }

    public int sprawdzWygrana() {
        boolean graczWin = true;
        boolean kompWin = true;

        for (int i = 0; i < 10; i++) {
            if (okretyGracza[i].getZatopiony() == false) {
                kompWin = false;
            }
        }
        for (int i = 0; i < 10; i++) {
            if (okretyKomputera[i].getZatopiony() == false) {
                graczWin = false;
            }
        }


        if (graczWin == true) return 1;
        else if (kompWin == true) return 2;
        else return 0;
    }

    public GameStatus getGameStatus() {
        return stanGry;
    }

    private void findPole(int x, int y) {
        x = x - 25;
        y = y - 10;

        x = x/30;
        y = y/30;

        lastShot.setWspX(x);
        lastShot.setWspY(y);

        strzelono = true;

    }

    public void setMessage(String rodzaj, String text) {
        Color neutralny = new Color(255, 255, 196);
        Color pozytywny = new Color(196, 255, 196);
        Color negatywny = new Color(255, 196, 196);
        if (rodzaj.equals("pozytyw")) {
            infoArea.setBackground(pozytywny);
        }
        if (rodzaj.equals("negatyw")) {
            infoArea.setBackground(negatywny);
        }
        if (rodzaj.equals("neutral")) {
            infoArea.setBackground(neutralny);
        }
        infoArea.setText(text);
    }

    private class MyszaListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            findPole(e.getX(), e.getY());


        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

    }

    private class WindowListener extends WindowAdapter{
        public void windowClosing(WindowEvent e) {
            int odp = JOptionPane.showConfirmDialog(null, "Czy napewno chcesz wyjść?", "Uwaga!", JOptionPane.YES_NO_OPTION);
            if (odp == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        }
    }
}
