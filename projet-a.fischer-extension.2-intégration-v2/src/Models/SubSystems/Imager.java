package Models.SubSystems;

import Models.Mesures.Image;
import Models.Mesures.Mesure;

public class Imager extends SubSystem {

    /**
     * Constructeur, reprend les attributs de la super classe SubSystem : name et
     * status
     */
    public Imager(String name, boolean status) {
        super(name, status);
    }

    /**
     * Crée une mesure de type Bipmap accompagnée de sa date et la renvoie en format
     * string afin qu'elle puisse être archivée
     */
    public Mesure createData() {
        // Création de la mesure
        Mesure m = new Image();
        m.take_mesure();
        return m;
    }

    /*
     * String mesure = String.valueOf(m.getTimeStamp()); mesure = mesure +
     * " : mesure "; for (int x = 0; x < m.getMesure().length; x++) { for (int y =
     * 0; y < m.getMesure().length; y++) { double el = m.getMesure()[x][y]; mesure =
     * mesure + ", " + String.valueOf(el); } } return mesure;
     */

}
