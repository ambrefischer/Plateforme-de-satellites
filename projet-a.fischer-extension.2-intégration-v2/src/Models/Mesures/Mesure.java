package Models.Mesures;

import java.io.Serializable;
import java.util.Date;

public abstract class Mesure implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Date Time;

    /**
     * Constructeur de Mesure, fixe la date et l'heure de la mesure
     */
    public Mesure() {
        Time = new Date();
    }

    /**
     * Permet d'obtenir l'estampille temporelle
     * 
     * @return l'estampille temporelle
     */
    public Date getTimeStamp() {
        return this.Time;
    }

    /**
     * Permet de faire la mesure, dépend du type de mesure
     */
    public abstract void take_mesure();

    /**
     * Permet d'afficher la donnée mesurée, dépend du type de mesure
     */
    public abstract void print_mesure();

}
