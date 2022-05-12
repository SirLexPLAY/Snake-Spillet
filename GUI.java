import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GUI {
    Kontroll kontroll;
    JFrame vindu;
    JPanel panel, konsoll, rutenett;
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
        vindu = new JFrame("Slangespillet");
        vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vindu.setSize(new Dimension(500, 500));

        // Oppretter et hoved JPanel
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        vindu.add(panel);

        // Oppretter et JPanel for konsoll (Poengsum, START knapp)
        konsoll = new JPanel();
        konsoll.setLayout(new BorderLayout());
        panel.add(konsoll, BorderLayout.NORTH);
        konsoll.add(startKnapp, BorderLayout.EAST);
        konsoll.add(new JLabel("Poengsum: 0"));

        // Definerer oppførselen til startknappen
        class Startbehandler implements ActionListener {
            @Override
            public void actionPerformed (ActionEvent e) {
                if (!kontroll.modell.spillStatus) {
                    kontroll.spillStatusPaa();
                    kontroll.spillTraad.start();
                    startKnapp.setText("STOPP");
                } else {
                    System.out.println("Lol");
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
        vindu.addKeyListener(new TastBehandler(kontroll));

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
        System.out.println("oppdatert");
        System.out.println(slange.size());
        System.out.println("rad: " + slange.get(slange.size()-1)[0] + ", kolonne: " + slange.get(slange.size()-1)[1]);
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
            System.out.println("Skiftet nord!");
            kontroll.skifRetning(kontroll.NORD);
        }
        else if (keyCode == KeyEvent.VK_DOWN) {
            System.out.println("Skiftet sør!");
            kontroll.skifRetning(kontroll.SOOR);
        }
        else if (keyCode == KeyEvent.VK_LEFT) {
            System.out.println("Skiftet vest!");
            kontroll.skifRetning(kontroll.VEST);
        }
        else if (keyCode == KeyEvent.VK_RIGHT) {
            System.out.println("Skifter øst!");
            kontroll.skifRetning(kontroll.OOST);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}