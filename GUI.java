import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GUI {
    Kontroll kontroll;
    Vindu vindu;
    JPanel panel, konsoll, rutenett, styreKnapper;
    JButton[] knapper; // 0 - nord; 1 - øst, 2 - sør, 3 - vest
    JLabel[][] ruter;
    JButton startKnapp = new JButton("START");
    int baneHooyde;
    int baneBredde;

    GUI(Kontroll kontroll) {
        this.kontroll = kontroll;

        // Ber brukeren å oppgi banedimensjoner
        boolean utfylt = false;
        String userInput;
        String[] parametere;
        while (!utfylt) {
            userInput = JOptionPane.showInputDialog(
                                    vindu, 
                                    "Vennligst oppgi parametere for banen din på formen\n <antall rader> <antall kolonner>\n(Sepparert med mellomrom)",
                                    "Snakespillet - Innstillinger",
                                    JOptionPane.QUESTION_MESSAGE
                                    );

            if (userInput == null) {System.exit(0);}
            
            try {
                parametere = userInput.split(" ");
            } catch (Exception e) {continue;}

            // Kun to parametere er gyldige
            if (parametere.length != 2) {continue;}

            // Kun heltall er lov som parametere
            try {
                baneHooyde = Integer.parseInt(parametere[0]);
                baneBredde = Integer.parseInt(parametere[1]);
            } catch (NumberFormatException e) {continue;}
            
            // Hverken baneHooyden eller baneBredden kan være mindre enn 1
            if (baneHooyde < 1 || baneBredde < 1) {
                continue;
            }

            utfylt = true;
        }
        
        // Tilpasser grensesnittet
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {System.exit(0);}

        // Oppretter et nytt vindu
        vindu = new Vindu("Slangespillet", kontroll);
        vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vindu.setSize(new Dimension(500, 500));

        // Oppretter et hoved JPanel
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        vindu.add(panel);

        // Oppretter et JPanel for konsoll (Poengsum, START knapp, styreknapper)
        konsoll = new JPanel();
        konsoll.setLayout(new BorderLayout());
        panel.add(konsoll, BorderLayout.NORTH);
        konsoll.add(startKnapp, BorderLayout.EAST);
        konsoll.add(new JLabel("Poengsum: 0"), BorderLayout.WEST);

        //
        class styreKnapp implements ActionListener {
            int retning;

            styreKnapp(int retning) {
                this.retning = retning;
            }

            @Override
            public void actionPerformed (ActionEvent e) {
                if (retning == 0) {
                    kontroll.skifRetning(kontroll.NORD);
                } else if (retning == 1) {
                    kontroll.skifRetning(kontroll.OOST);
                } else if (retning == 2) {
                    kontroll.skifRetning(kontroll.SOOR);
                } else if (retning == 3) {
                    kontroll.skifRetning(kontroll.VEST);
                }
            }
        }

        // Oppretter styreKnapper
        knapper = new JButton[4];
        knapper[0] = new JButton(); // nord
        knapper[1] = new JButton(); // øst
        knapper[2] = new JButton(); // sør
        knapper[3] = new JButton(); // vest

        knapper[0].addActionListener(new styreKnapp(0));
        knapper[1].addActionListener(new styreKnapp(1));
        knapper[2].addActionListener(new styreKnapp(2));
        knapper[3].addActionListener(new styreKnapp(3));

        knapper[0].setText("Opp");
        knapper[1].setText("Høyre");
        knapper[2].setText("Ned");
        knapper[3].setText("Venstre");

        styreKnapper = new JPanel();
        styreKnapper.setLayout(new BorderLayout());
        styreKnapper.add(knapper[0], BorderLayout.NORTH);
        styreKnapper.add(knapper[1], BorderLayout.EAST);
        styreKnapper.add(knapper[2], BorderLayout.SOUTH);
        styreKnapper.add(knapper[3], BorderLayout.WEST);

        konsoll.add(styreKnapper, BorderLayout.CENTER);

        // Definerer oppførselen til startknappen
        class Startbehandler implements ActionListener {
            @Override
            public void actionPerformed (ActionEvent e) {
                if (!kontroll.modell.spillStatus) {
                    kontroll.spillStatusPaa();
                    kontroll.spillTraad.start();
                    startKnapp.setText("STOPP");
                } else {
                }
            }
        }
        startKnapp.addActionListener(new Startbehandler());

        // Oppretter et JPanel for banen
        rutenett = new JPanel();
        rutenett.setLayout(new GridLayout(baneHooyde, baneBredde));
        ruter = new JLabel[baneHooyde][baneBredde];
        for (int rx = 0; rx < baneHooyde; rx++) {
            for (int kx = 0; kx < baneBredde; kx++) {
                JLabel rute = new JLabel("", SwingConstants.CENTER);
                ruter[rx][kx] = rute;
                rute.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
                rute.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                rute.setPreferredSize(new Dimension(40,40));
                rute.setOpaque(true);
                rutenett.add(rute);
            }
        }
        panel.add(rutenett, BorderLayout.SOUTH);
        vindu.setFocusable(true);
        //vindu.addKeyListener(new TastBehandler(kontroll));

        // Gjør alt synlig
        vindu.pack();
        vindu.setVisible(true);
    }

    // Metoden oppdaterer rutenettet. 
    // input: slangen og eplene
    public void oppdaterRutenett(ArrayList<Integer[]> slange, boolean[][] epler) {
        int slangeLengde = slange.size();
        for (int rx = 0; rx < baneHooyde; rx++) {
            for (int kx = 0; kx < baneBredde; kx++) {
                JLabel ruten = ruter[kx][rx];
                if (epler[kx][rx]) {
                    ruter[kx][rx].setText("");
                    continue;
                }
                ruten.setText("");
                ruten.setBackground(Color.WHITE);
            }
        }
        for (Integer[] slangeRute : slange) {
            JLabel ruten = ruter[slangeRute[0]][slangeRute[1]];
            if (slangeRute == slange.get(slangeLengde-1)) {
                ruten.setText("o");
                ruten.setBackground(Color.GREEN);
            } else {
                ruten.setText("+");
                ruten.setBackground(Color.GREEN);
            }
        }
    }   
}

// Konfigurerer keyListener
class TastBehandler implements KeyListener {
    Kontroll kontroll;

    TastBehandler(Kontroll kontroll) {
        this.kontroll = kontroll;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP) {
            kontroll.skifRetning(kontroll.NORD);
        }
        else if (keyCode == KeyEvent.VK_DOWN) {
            kontroll.skifRetning(kontroll.SOOR);
        }
        else if (keyCode == KeyEvent.VK_LEFT) {
            kontroll.skifRetning(kontroll.VEST);
        }
        else if (keyCode == KeyEvent.VK_RIGHT) {
            kontroll.skifRetning(kontroll.OOST);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}

// Styreknapper
