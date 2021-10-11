package Models;

import java.io.IOException;
import java.util.ArrayList;

import Models.SatelliteFamilies.ISAESatellite;
import Models.SubSystems.Imager;
import Models.SubSystems.SubSystem;
import Models.SubSystems.Thermometre;

public class ISAESATMain {
    public static void main(String[] args) throws IOException {

        // Création du satellite ISAESAT
        SubSystem eqISAE1 = new Imager("IMAGER1", false);
        SubSystem eqISAE2 = new Imager("IMAGER2", false);
        SubSystem eqISAE3 = new Thermometre("THERMO1", false);
        ArrayList<SubSystem> equiISAE = new ArrayList<>();
        equiISAE.add(eqISAE1);
        equiISAE.add(eqISAE2);
        equiISAE.add(eqISAE3);
        OnBoardSystem onBoardISAE = new OnBoardSystem(equiISAE);
        ISAESatellite ISAESAT = new ISAESatellite("SAT", onBoardISAE);
        ISAESAT.createFile();
        // Core
        boolean stop = true;
        while (stop) {

            // Les satellites attendent de recevoir un ordre dans uplink
            while (ISAESAT.getAdapt().get_uplink().compareTo("") == 0) {
            }

            // Effectue la simulation du satellite ISAESAT, renvoie false si la commande
            // reçue est terminate, true sinon
            ISAESAT.simSat();

        }
    }

}
