package Models.SubSystems;

import Models.Mesures.Mesure;

public abstract class SubSystem {

    /** nom du sous-système */
    private String name;

    /** statut en ON (true) ou OFF (false) */
    private boolean status;

    /**
     * Constructeur avec name et status
     * 
     * @param name
     * @param status
     */
    public SubSystem(String name, boolean status) {
        this.name = name;
        this.status = status;
    }

    /**
     * Getter
     * 
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter
     * 
     * @return status
     */
    public boolean getStatus() {
        return this.status;
    }

    /**
     * échange le statut de false en true ou de true en false
     * 
     * @return "OK"
     */
    public String command() {
        this.status = !this.status;
        return "OK";
    }

    /**
     * Chaque sous système doit avoir un créateur de mesure
     * 
     * 
     * @return
     */
    public abstract Mesure createData();

    /**
     * Visualise les sous-sytèmes avec leurs noms et leur status
     */
    @Override
    public String toString() {
        return "{" + " name='" + getName() + "'" + ", status='" + getStatus() + "'" + "}";
    }

}
