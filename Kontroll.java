import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Kontroll {
    GUI gui;
    Modell modell;
    Thread spillTraad;

    // Retninger
    final int NORD = 0;
    final int OOST = 1;
    final int SOOR = 2;
    final int VEST = 3;

    Kontroll() {
        gui = new GUI(this);
        modell = new Modell(gui);
        spillTraad = new Thread(new SpillTraad(this, modell));
        klargjoorSpillet();
    }

    // Gjør spillet klart 
    public void klargjoorSpillet() {
        oppdaterBaneDim();
        modell.trekkEpler();
        modell.trekkStartPos();
        gui.oppdaterRutenett(modell.slange, modell.epler);
        
    }

    public void flyttSlangen() {
        modell.flyttSlangen(modell.sjekkEple());
    }

    public void oppdaterRutenett() {
        gui.oppdaterRutenett(modell.slange, modell.epler);
    }

    public void oppdaterBaneDim() {
        modell.baneHooyde = gui.baneHooyde;
        modell.baneBredde = gui.baneBredde;
        modell.epler = new boolean[modell.baneHooyde][modell.baneBredde];
        int antallEpler = (int) Math.ceil(((float) modell.baneHooyde * (float) modell.baneBredde)/20);
        modell.maksAntallEpler = antallEpler;
    }

    // 0 - nord; 1 - øst; 2 - sør; 3 - vest
    public void skifRetning(int retning) {
        int[] nyRetning = new int[2];
        if (retning == 0) {nyRetning[0] = -1; nyRetning[1] = 0;}
        else if (retning == 1) {nyRetning[0] = 0; nyRetning[1] = 1;}
        else if (retning == 2) {nyRetning[0] = 1; nyRetning[1] = 0;}
        else if (retning == 3) {nyRetning[0] = 0; nyRetning[1] = -1;}
        else {nyRetning[0] = 0; nyRetning[1] = 0;}
        modell.retning = nyRetning;
    }

    public void spillStatusPaa() {
        modell.spillStatus = true;
    }

}
