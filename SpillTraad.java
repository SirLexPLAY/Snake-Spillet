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
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Noe gikk galt!");
            }
            kontroll.flyttSlangen();
            kontroll.oppdaterRutenett();
        }
    }
}

