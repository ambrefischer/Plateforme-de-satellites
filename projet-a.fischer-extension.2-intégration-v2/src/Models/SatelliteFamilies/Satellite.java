package Models.SatelliteFamilies;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import Models.OnBoardSystem;
import Models.Mesures.Mesure;
import Models.SubSystems.SubSystem;

abstract public class Satellite {

    /**
     * Full name, les sous-classes changent le nom
     */
    private String name;

    /**
     * Controleur de satellite : Classe, est le même quelque soit la famille de
     * satellites
     */
    private OnBoardSystem satelliteControl;

    /**
     * Constructeur avec name et satelliteControl
     */
    public Satellite(String name, OnBoardSystem satelliteControl) {
        this.name = name;
        this.satelliteControl = satelliteControl;
        File f = new File("DATA/" + name);
        if (f.isDirectory() == false) {
            String filename = "DATA/" + name + "/NEXTSEQNUM.txt";
            File dir = new File("DATA/" + name);
            dir.mkdir();

            File file = new File(filename);
            try {
                file.createNewFile();
                PrintWriter out = new PrintWriter(file);
                out.println("000000000");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

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
     * @return satelliteControl
     */
    public OnBoardSystem getSatelliteControl() {
        return this.satelliteControl;
    }

    /**
     * Visualise le nom et les sous-systèmes du satellite
     */
    @Override
    public String toString() {
        return "{" + " name='" + getName() + "'," + getSatelliteControl() + "'" + "}";
    }

    /**
     * Méthode abstraite commune aux satellites, permet de récupérer l'adaptateur du
     * satellite
     * 
     * @return
     */
    abstract public AdaptateurSAT getAdapt();

    /**
     * Effectue la simulation du satellite
     */
    public void simSat() throws IOException {
        // Récupère l'ordre
        String order = getAdapt().get_uplink();
        // Supprime le fichier
        getAdapt().delete(getName());

        // Récupère l'ordre sans le satellite
        order = getSatelliteControl().getCommandToEquipement(order);
        // Récupère le nom du sous système concerné
        String subsysOperator = getSatelliteControl().getEquipementName(order);
        // Récupère la commande (ON, OFF ou DATA)
        String doesOperator = getSatelliteControl().getCommandToEquipement(order);
        // Récupère la réponse à renvoyer au centre de controle (soit OK, KO, ou la
        // mesure)
        if (doesOperator.equals("DATA")) {
            Mesure tosend = getSatelliteControl().invokeCommand_m(subsysOperator, doesOperator);
            if (tosend != null) {
                getAdapt().post_datalink(tosend);
            } else {
                getAdapt().post_downlink("KO");
            }
        }

        else {
            String tosend = getSatelliteControl().invokeCommand(subsysOperator, doesOperator);
            getAdapt().post_downlink(tosend);
        }
    }

    /**
     * Permet de créer un fichier par satellite contenant le nom des ses
     * sous-systèmes
     * 
     * @param sat
     * @throws IOException
     */
    public void createFile() {
        String filename = "src/ARCHI/" + name;
        File file = new File(filename);
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);
            int nbSS = satelliteControl.getEquipments().size();
            for (int i = 0; i < nbSS; i++) {
                SubSystem ss = satelliteControl.getEquipments().get(i);
                pw.println(ss.getName());
            }
            pw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
