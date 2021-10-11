package Models.SatelliteFamilies;

import java.io.IOException;

import Models.OnBoardSystem;

public class XSatellite extends Satellite {

    /**
     * nom du satellite
     */
    private String name;
    private AdaptateurSAT adapt;

    /**
     * Constructeur qui ajoute "X" en préfixe du nom du satellite pour faire
     * référence à sa famille d'appartenance et reprend les attributs name et
     * satelliteControl de la super-classe Satellite
     * 
     * @throws IOException
     */
    public XSatellite(String name, OnBoardSystem satelliteControl) {
        super("X" + name, satelliteControl);
        this.name = "X" + name;
        this.adapt = new AdaptateurSAT("X" + name);
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
     * @return adapt
     */
    public AdaptateurSAT getAdapt() {
        return adapt;
    }
}
