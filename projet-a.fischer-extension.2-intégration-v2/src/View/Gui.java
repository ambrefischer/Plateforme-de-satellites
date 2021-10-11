package View;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Gui {
    /** fenêtre de l'ihm */
    private JFrame window;

    /** sauvegarde les noms de satellites */
    private ArrayList<String> constellation;

    /** garde en mémoire tous les boutons et les labels dans des HashMap */
    private HashMap<String, HashMap<String, HashMap<String, JButton>>> satMap;
    private HashMap<String, HashMap<String, JLabel>> labelSatMap;
    private HashMap<String, JButton> procecomplMap;
    private HashMap<String, JLabel> labelprocecomplMap;

    /** Historique */
    private JTextPane textPane;

    /**
     * constructeur de l'Ihm qui va initialiser la fenêtre avec les onglets et les
     * boutons
     * 
     * @throws IOException
     */
    public Gui(ArrayList<String> constellation) throws IOException {
        this.constellation = constellation;

        // fenêtre de l'ihm
        JFrame window = new JFrame("Control Center");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ajout de la console de sortie
        EmptyBorder eb = new EmptyBorder(new Insets(50, 50, 50, 50));
        textPane = new JTextPane();
        textPane.setBorder(eb);
        JScrollPane scrollPane = new JScrollPane(textPane);

        // ajout des onglets
        JTabbedPane tabs = new JTabbedPane();

        // satMap contient la HashMap des sous-systèmes contenant également chaque
        // bouton avec comme clé le satellite correspondant
        satMap = new HashMap<String, HashMap<String, HashMap<String, JButton>>>();
        // labelSatMap contient la HashMap des sous-systèmes contenant également le
        // label avec comme clé le satellite correspondant
        labelSatMap = new HashMap<String, HashMap<String, JLabel>>();

        // prececomplMap permet de garder en mémoire les boutons des procédures
        // complexes
        procecomplMap = new HashMap<String, JButton>();
        // labelprocecomplMap permet de garder le label procédures complexes en mémoire
        labelprocecomplMap = new HashMap<String, JLabel>();

        // pour chaque satellite :
        for (int i = 0; i < constellation.size(); i++) {
            String satellite = constellation.get(i);

            // on récupère les noms des sous-systèmes dans le dossier ARCHI
            ArrayList<String> subsystems = readFile("src/ARCHI/" + satellite);
            int nbSS = subsystems.size();

            // on crée un panneau par sous-système
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(nbSS + 1, 4));

            // ssSysMap contient la HashMap des boutons avec comme clés les noms des
            // sous-systèmes
            HashMap<String, HashMap<String, JButton>> ssSysMap = new HashMap<String, HashMap<String, JButton>>();
            // labelSsSysMap contient le label correspond au nom du sous-système
            HashMap<String, JLabel> labelSsSysMap = new HashMap<String, JLabel>();

            // pour chaque sous-système de satellite :
            for (int j = 0; j < nbSS; j++) {

                // On récupère le nom depuis la liste des sous-systèmes
                String ssName = subsystems.get(j);
                JLabel label = new JLabel(ssName);

                // sauvegarde du label
                labelSsSysMap.put(ssName, label);

                // Création de boutons
                JButton button_ON = new JButton("ON");
                JButton button_OFF = new JButton("OFF");
                JButton button_DATA = new JButton("DATA");
                JButton button_REPEAT = new JButton("REPEAT");

                // sauvegarde des boutons
                HashMap<String, JButton> buttonsMap = new HashMap<String, JButton>();
                buttonsMap.put("ON", button_ON);
                buttonsMap.put("OFF", button_OFF);
                buttonsMap.put("DATA", button_DATA);
                buttonsMap.put("REPEAT", button_REPEAT);

                // on met dans la HashMap
                ssSysMap.put(ssName, buttonsMap);

                // Assemblage
                panel.add(label);
                panel.add(button_ON);
                panel.add(button_OFF);
                panel.add(button_DATA);
                panel.add(button_REPEAT);
            }

            // implémentation des procédures complexes
            JLabel label = new JLabel("procédures complexes");
            JButton button_REDUNDANT = new JButton("PRENDRE LA MESURE DU DERNIER SOUS-SYSTEME");
            JButton button_ALLMESURES = new JButton("PRENDRE LES MESURES DES 2 PREMIERS SOUS-SYSTEMES");

            // sauvegarde des procédures complexes boutons et label
            procecomplMap.put(satellite + "dernieremesure", button_REDUNDANT);
            procecomplMap.put(satellite + "2sous-systeme", button_ALLMESURES);
            labelprocecomplMap.put(satellite, label);

            // on met dans les HashMap
            labelSatMap.put(satellite, labelSsSysMap);
            satMap.put(satellite, ssSysMap);

            // Assemblages des procédures complexes
            panel.add(label);
            panel.add(button_REDUNDANT, BorderLayout.SOUTH);
            panel.add(button_ALLMESURES, BorderLayout.SOUTH);

            // on met le tout dans l'onglet
            tabs.addTab(satellite, panel);
        }

        // on met le tout dans la fenêtre principale
        window.add(tabs, BorderLayout.NORTH);
        window.add(scrollPane);

        window.pack();
        window.setVisible(true);

    }

    /**
     * Getter
     * 
     * @return window
     */
    public JFrame getWindow() {
        return this.window;
    }

    /**
     * Setter
     * 
     * @param constelString
     * @return constelString
     */
    public Gui constelString(ArrayList<String> constelString) {
        this.constellation = constelString;
        return this;
    }

    /**
     * Getter
     * 
     * @return constelString
     */
    public ArrayList<String> getConstelString() {
        return this.constellation;
    }

    /**
     * Getter
     * 
     * @return satMap
     */
    public HashMap<String, HashMap<String, HashMap<String, JButton>>> getSatMap() {
        return this.satMap;
    }

    /**
     * Getter
     * 
     * @return labelSatMap
     */
    public HashMap<String, HashMap<String, JLabel>> getLabelSatMap() {
        return this.labelSatMap;
    }

    public ArrayList<String> getConstellation() {
        return this.constellation;
    }

    public HashMap<String, JButton> getProcecomplMap() {
        return this.procecomplMap;
    }

    public HashMap<String, JLabel> getLabelprocecomplMap() {
        return this.labelprocecomplMap;
    }

    /**
     * Ajoute du texte sur le textPane avec la couleur color et met le label de la
     * même couleur
     * 
     * @param label
     * @param color
     * @param text
     */
    public void refresh(JLabel label, Color color, String text) {
        appendToPane(text + "\n", color);
        label.setForeground(color);
    }

    public void setButtonColor(JButton button_ON, JButton button_OFF, String message) {
        switch (message) {
            // si le bouton ON est clické alors seulenemnt lui apparaît en bleu
            case "ON":
                button_ON.setForeground(Color.BLUE);
                button_OFF.setForeground(Color.BLACK);
                break;

            // si le bouton OFF est clické alors seulenemnt lui apparaît en bleu
            case "OFF":
                button_OFF.setForeground(Color.BLUE);
                button_ON.setForeground(Color.BLACK);
                break;

            default:
                break;
        }
    }

    /**
     * ajoute la T/C ou T/M la couleur dans la console de sortie et lui met une
     * couleur
     * 
     * @param textPane
     * @param msg
     * @param c
     */
    public void appendToPane(String msg, Color c) {
        textPane.setEditable(true);
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        int len = textPane.getDocument().getLength();
        textPane.setCaretPosition(len);
        textPane.setCharacterAttributes(aset, false);
        textPane.replaceSelection(msg);
        textPane.setEditable(false);
    }

    /**
     * Permet de lire les fichiers contenant les noms des sous-systèmes des
     * satellites et de les mettre dans une liste.
     * 
     * @param filename
     * @return ArrayList de String
     * @throws IOException
     */
    private ArrayList<String> readFile(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader in = new BufferedReader(new FileReader(file));
        ArrayList<String> subsystems = new ArrayList<String>();
        String line = in.readLine();
        while (line != null) {
            subsystems.add(line);
            line = in.readLine();
        }
        in.close();

        return subsystems;
    }

}
