package Models.SubSystems;

import Models.Mesures.Mesure;
import Models.Mesures.Temperature;

public class Thermometre extends SubSystem {
    /**
     * Constructeur, reprend les attributs de la super classe SubSystem : name et
     * status
     */
    public Thermometre(String name, boolean status) {
        super(name, status);
    }

    /**
     * Crée une mesure de type Bipmap accompagnée de sa date et la renvoie en format
     * string afin qu'elle puisse être archivée
     */
    public Mesure createData() {
        // Création de la mesure
        Mesure m = new Temperature();
        m.take_mesure();
        return m;
    }
}
