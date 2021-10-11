package Controller;

import java.io.IOException;
import java.util.ArrayList;

import View.Gui;

public class SolMain {

    public static void main(String[] args) throws IOException {
        // Init constellation
        ArrayList<String> constellation = new ArrayList<>();
        constellation.add("XSAT");
        constellation.add("ISAESAT");

        // construction de la view
        Gui view = new Gui(constellation);

        // Init CC

        ControlCenter cc = new ControlCenter(constellation, view);

        // Supprime les fichiers de channel si il en reste
        cc.deleteAll();

        cc.initController();
    }
}
