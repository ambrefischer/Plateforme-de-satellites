package Models;

import java.io.IOException;
import java.util.ArrayList;

import Models.SatelliteFamilies.XSatellite;
import Models.SubSystems.Imager;
import Models.SubSystems.SubSystem;

public class XSATMain {

    public static void main(String[] args) throws IOException {

        // Création du satellite XSAT
        SubSystem eqX1 = new Imager("IMAGER1", false);
        SubSystem eqX2 = new Imager("IMAGER2", false);
        ArrayList<SubSystem> equiX = new ArrayList<>();
        equiX.add(eqX1);
        equiX.add(eqX2);
        OnBoardSystem onBoardX = new OnBoardSystem(equiX);
        XSatellite XSAT = new XSatellite("SAT", onBoardX);
        XSAT.createFile();
        // Core
        boolean stop = true;
        while (stop) {

            // Les satellites attendent de recevoir un ordre dans uplink
            while (XSAT.getAdapt().get_uplink().compareTo("") == 0) {
            }

            // Effectue la simulation du satellite XSAT, renvoie false si la commande reçue
            // est terminate, true sinon
            XSAT.simSat();

        }
    }
}
