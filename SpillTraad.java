import javax.swing.Timer;

public class SpillTraad implements Runnable {
    private Kontroll kontroll;
    private Modell modell;

    SpillTraad(Kontroll kontroll, Modell modell) {
        this.kontroll = kontroll;
        this.modell = modell;
    }

    @Override
    public void run() {
        while (modell.spillStatus) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println("Noe gikk galt!");
            }
            kontroll.flyttSlangen();
            kontroll.oppdaterRutenett();
        }
    }
}

