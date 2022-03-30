package windows;

import javax.swing.*;

public class Start {
    private static final GUI gui = new GUI();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(gui);
    }
}