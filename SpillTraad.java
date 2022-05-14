import javax.swing.Timer;
import java.awt.*;  
import java.awt.event.*;  

public class SpillTraad implements Runnable, ActionListener {
    private Kontroll kontroll;
    private Modell modell;
    private Timer timer = new Timer(2000, this);

    SpillTraad(Kontroll kontroll, Modell modell) {
        this.kontroll = kontroll;
        this.modell = modell;
    }

    @Override
    public void run() {
        System.out.println("Lets go");
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action performed");
        aaUtfoore();
    }

    public void aaUtfoore() {
        System.out.println("Aautføre");
        kontroll.flyttSlangen();
        kontroll.oppdaterRutenett();
    }

}

