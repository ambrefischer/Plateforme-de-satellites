package Models;

import java.util.ArrayList;

import Models.Mesures.Mesure;
import Models.SubSystems.SubSystem;

public class OnBoardSystem {

    /**
     * liste des sous-systèmes du satellite gérés par le contrôleur
     */
    private ArrayList<SubSystem> equipments;

    /**
     * Constructeur avec equipements
     * 
     * @param equipments
     */
    public OnBoardSystem(ArrayList<SubSystem> equipments) {
        this.equipments = equipments;
    }

    /**
     * Getter
     * 
     * @return equipments
     */
    public ArrayList<SubSystem> getEquipments() {
        return this.equipments;
    }

    /**
     * Cette méthode permet de récupérer le nom de la commande envoyée (ON, OFF ou
     * DATA). C'est une simple boucle while qui s'arrête dès le premier ":". Le
     * string envoyé est celui de droite.
     * 
     * @param command
     * @return le nom de la commande en string
     */
    public String getCommandToEquipement(String command) {
        int c = 0;
        boolean deuxpoints = false;
        while (c < command.length() && deuxpoints == false) {
            if (command.substring(c, c + 1).compareTo(":") == 0) {
                deuxpoints = true;
            }
            c = c + 1;
        }
        return command.substring(c);
    }

    /**
     * Cette méthode permet de récupérer le nom de l'équipement contenu dans la
     * commande. C'est une simple boucle while qui s'arrête dès le premier ":". Le
     * string envoyé est celui de gauche.
     * 
     * @param command
     * @return le nom de l'équipement en string
     */
    public String getEquipementName(String command) {
        int c = 0;
        boolean deuxpoints = false;
        while (c < command.length() && deuxpoints == false) {
            if (command.substring(c, c + 1).compareTo(":") == 0) {
                deuxpoints = true;
            }
            c = c + 1;
        }
        return command.substring(0, c - 1);
    }

    /**
     * Vérifie si le sous-système existe dans sa liste equipments
     * 
     * @param subsysOperator
     * @param doesOperator
     * @return "OK" si il existe ou "KO" (avec spécifications) sinon
     */
    private String existEquipment(String subsysOperator, String doesOperator) {

        // Parcours de la liste des sous-systèmes appartenant au satellite
        for (SubSystem subsys : equipments) {

            // Si le sous systeme demandé correspond au sous-système regardé dans le
            // parcours de la boucle for
            if (subsys.getName().equals(subsysOperator)) {

                // Pour une T/C : on regarde si la commande est possible : la commande doit
                // changer le statut du sous-système
                if ((doesOperator.equals("ON") && subsys.getStatus() == false)
                        || (doesOperator.equals("OFF") && subsys.getStatus() == true)) {
                    // alors on pourra effectuer la commande T/C.
                    return "OK";
                }

                // Pour une T/M : on regarde si le sous-système est en ON
                else if ((doesOperator.equals("DATA") && subsys.getStatus() == true)) {
                    // alors on pourra effectuer la mesure.
                    return "OK";
                }

                // Sinon la T/C ou T/M n'est pas possible.
                return "KO";

            }
        }
        // Si le sous-système demandé n'est pas dans la liste equipments, on renvoie
        // "KO..."
        return "KO";
    }

    /**
     * Procède à l'exécution de la commande ou de la mesure
     * 
     * @param subsysOperator
     * @param doesOperator
     * @param mesureCompt
     * @return
     */
    /**
     * Procède à l'exécution de la commande ou de la mesure
     * 
     * @param subsysOperator
     * @param doesOperator
     * @param mesureCompt
     * @return
     */
    public String invokeCommand(String subsysOperator, String doesOperator) {
        String response = null;

        // Afin de faire la T/C ou T/M, on doit s'assurer que cela est possible
        if (existEquipment(subsysOperator, doesOperator).equals("OK")) {

            // On retrouve le bon sous-système
            int nbSS = equipments.size();
            SubSystem sous_systeme = null;
            SubSystem subsys;
            for (int i = 0; i < nbSS; i++) {
                subsys = equipments.get(i);
                if ((subsys.getName().equals(subsysOperator))) {
                    sous_systeme = subsys;
                }
            }

            // Si il s'agit d'une T/C
            if (doesOperator.equals("ON") || doesOperator.equals("OFF")) {

                response = sous_systeme.command();

            }

        }

        // Si la T/C n'est pas possible, on renvoie "KO..."
        else {
            response = existEquipment(subsysOperator, doesOperator);
        }

        return response;

    }

    public Mesure invokeCommand_m(String subsysOperator, String doesOperator) {

        // Afin de faire T/M, on doit s'assurer que cela est possible
        if (existEquipment(subsysOperator, doesOperator).equals("OK")) {

            /// On retrouve le bon sous-système
            int nbSS = equipments.size();
            SubSystem sous_systeme = null;
            SubSystem subsys;
            for (int i = 0; i < nbSS; i++) {
                subsys = equipments.get(i);
                if ((subsys.getName().equals(subsysOperator))) {
                    sous_systeme = subsys;
                }
            }
            Mesure reponse = sous_systeme.createData();
            return reponse;
        } else {
            return null;
        }
    }

    /**
     * Visualise la liste des equipements du satellite
     */
    @Override
    public String toString() {
        return " equipments='" + getEquipments() + "'" + "}";
    }

}