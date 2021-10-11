package Models.SatelliteFamilies;

import java.io.IOException;

import Models.OnBoardSystem;

public class ISAESatellite extends Satellite {

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
    public ISAESatellite(String name, OnBoardSystem satelliteControl) {
        super("ISAE" + name, satelliteControl);
        this.name = "ISAE" + name;
        this.adapt = new AdaptateurSAT("ISAE" + name);
    }

    /**
     * Getter
     * 
     * @return name
     */
    public String getName() {
        return this.name;
    }

    public AdaptateurSAT getAdapt() {
        return this.adapt;
    }

}
