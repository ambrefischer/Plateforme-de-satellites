package Controller.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import Controller.ControlCenter;
import View.Gui;

import java.awt.Color;

public class ButtonListener implements ActionListener {
    /**
     * Permettent de faire la T/C ou T/M
     */
    protected ControlCenter cc;
    protected String nameSat;
    protected String nameProcedure;
    protected String message;

    /**
     * Permettent de changer la couleur dans l'ihm
     */
    protected Gui view;
    protected JLabel label;

    /**
     * Constructeur
     * 
     * @param cc
     * @param nameSat
     * @param nameSS
     * @param message
     * @param view
     * @param label
     */
    public ButtonListener(ControlCenter cc, String nameSat, String nameSS, String message, Gui view, JLabel label) {
        this.cc = cc;
        this.nameSat = nameSat;
        this.nameProcedure = nameSS;
        this.message = message;
        this.view = view;
        this.label = label;
    }

    @Override
    /**
     * Permet de faire effectuer la commande au control center et de demander au
     * view de se mettre à jour (label et historique)
     */
    public void actionPerformed(ActionEvent e) {

        // fait la commande
        try {
            cc.command(nameSat + ":" + "src/Procedures/" + nameProcedure + "/" + message);
        } catch (Exception exc) {
            System.out.println("pb");
        }

        // Si la commande est valide, on renvoie true pour que le controlleur change
        // l'interface de l'ihm
        if (cc.getStateCommand().equals("OK")) {
            view.refresh(label, Color.GREEN, nameSat + ":" + nameProcedure + ":" + message);
        } else {
            view.refresh(label, Color.RED, nameSat + ":" + nameProcedure + ":" + message);
        }

    }

}
