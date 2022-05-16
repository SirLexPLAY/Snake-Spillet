import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Vindu extends JFrame implements KeyListener {
    Kontroll kontroll;

    Vindu(String vinduNavn, Kontroll kontroll) {
        super(vinduNavn);
        this.kontroll = kontroll;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(500, 500));
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("XD");
        // TODO Auto-generated method stub
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
    public void keyReleased(KeyEvent e) {}

}
