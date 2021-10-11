package Controller.ActionListener;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

import Controller.ControlCenter;
import View.Gui;

import java.awt.Color;

public class ButtonONOFFListener extends ButtonListener {

    /** bouton ON */
    private JButton button_ON;

    /** bouton OFF */
    private JButton button_OFF;

    /**
     * Constructeur hérite de ClickedButton
     * 
     * @param cc
     * @param nameSat
     * @param nameProcedure
     * @param message
     * @param label
     * @param textPane
     * @param button_ON
     * @param button_OFF
     */
    public ButtonONOFFListener(ControlCenter cc, String nameSat, String nameSS, String message, Gui view, JLabel label,
            JButton button_ON, JButton button_OFF) {
        super(cc, nameSat, nameSS, message, view, label);
        this.button_ON = button_ON;
        this.button_OFF = button_OFF;
    }

    /**
     * Permet de faire effectuer la commande au control center et de changer la
     * couleur et de mettre la couleur du bouton bleu si il est actif.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // envoie la commande
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

        // met la couleur bleu sur le bouton clické
        view.setButtonColor(button_ON, button_OFF, message);
    }

}
