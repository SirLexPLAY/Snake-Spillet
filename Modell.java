import java.util.ArrayList;
import java.lang.Math;

public class Modell {
    // parametere for spillet
    GUI gui;
    boolean spillStatus = false; // true: spillet har startet, false: spillet har ikke startet eller ble avbrutt

    // parametere for banen
    int baneHooyde;
    int baneBredde;
    
    // slange.get(0) er alltid halen, siste elementet er hodet
    ArrayList<Integer[]> slange = new ArrayList<>();
    int[] retning = {1,0}; // {1,0}: sør, {-1,0}: nord, {0,1}: øst, {0,-1}: vest

    // [rad][kolonne], true: har eple; false: har ikke eple
    boolean[][] epler;
    int maksAntallEpler;
    int score = 0;


    Modell(GUI gui) {
        this.gui = gui;
    }

    // flyttingen skjer vellykket - true; flyttingen fører til tap - false
    // forlenge parameteren: legger kun til nytt element i enden av slangen
    public boolean flyttSlangen(boolean forlenge) {
        Integer[] hode = slange.get(slange.size()-1);
        int hodeRad = hode[0];
        int hodeKolonne = hode[1];
        /*
        if (hodeRad + retning[0] < 0 || hodeRad + retning[0] >= baneHooyde || hodeKolonne + retning[1] < 0 || hodeKolonne + retning[1] >= baneBredde) {
            spillStatus = false;
            return false;
        }*/

        Integer[] nyHode = new Integer[2];
        nyHode[0] = hodeRad + retning[0];
        nyHode[1] = hodeKolonne + retning[1];

        if (!forlenge) {slange.remove(0);}
        slange.add(nyHode);
        return true;
    }

    // sjekker hvis hodet er en plass hvor eplet befinner seg
    // har slangen spist et eple: return true til kontrolleren, ellers false
    // -> boolean-verdien brukes videre i flyttSlangen() metoden i kontrolleren
    public boolean sjekkEple() {
        Integer[] hode = slange.get(slange.size()-1);
        int hodeRad = hode[0];
        int hodeKolonne = hode[1];

        if (epler[hodeRad][hodeKolonne] == true) {            
            boolean trukketUt = false;
            score++;

            // trekker et nytt eple
            while (!trukketUt) {
                int nyEpleRad = trekk(0, baneHooyde);
                int nyEpleKolonne = trekk(0, baneBredde);
                if (epler[nyEpleRad][nyEpleKolonne] == false) {
                    epler[nyEpleRad][nyEpleKolonne] = true;
                    trukketUt = true;
                }
            }
            return true;
        }
        return false;
    }

    // Trekk et tilfeldig heltall i intervallet [a...b]
    static int trekk(int a, int b) {
        return (int)(Math.random()*(b-a+1)+a);
    }

    public void trekkEpler() {
        int teller = 0;
        while (teller < maksAntallEpler) {
            int rad = trekk(0, baneHooyde-1);
            int kol = trekk(0, baneBredde-1);
            if (!epler[rad][kol]) {
                epler[rad][kol] = true;
                teller++;
            }
        }
    }

    // Trekker startposisjonen (til hodet) slik at den ikke er i et eple, og heller ikke inntil
    // kanten slik at spilleren for sjanse til å redde seg ut.
    public void trekkStartPos() {
        boolean klar = false;
        while (!klar) {
            int rad = trekk(1, baneHooyde-2);
            int kol = trekk(1, baneHooyde-2);
            if (!epler[rad][kol]) {
                Integer[] startPos = {rad, kol};
                slange.add(startPos);
                klar = true;
            }
        }
    }
}