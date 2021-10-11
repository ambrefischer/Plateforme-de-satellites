package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;

import Controller.ActionListener.ButtonListener;
import Controller.ActionListener.ButtonONOFFListener;
import Models.Mesures.*;
import View.Gui;

public class ControlCenter {
    private Adaptateur_CC adapt;

    /** ensemble des satellites visible par le ControlCenter */
    private ArrayList<String> constellation;

    /**
     * Cet attribut montre si l'operation precedente a ete un succes ou un echec. Si
     * c'est un echec, alors stateCommand est egal a "KO", sinon, il est egal a
     * "OK".
     */
    private String stateCommand = "";

    /** interface graphique : view */
    private Gui view;

    /**
     * Constructeur, avec archive et constellation
     * 
     * @param archive
     * @param constellation
     */
    public ControlCenter(ArrayList<String> constellation, Gui view) {
        this.constellation = constellation;
        this.adapt = new Adaptateur_CC();
        this.view = view;
    }

    public String getStateCommand() {
        return this.stateCommand;
    }

    /**
     * Getter
     * 
     * @return adapt
     */
    public Adaptateur_CC getAdapt() {
        return this.adapt;
    }

    /**
     * Getter
     * 
     * @return constellation
     */
    public ArrayList<String> getConstellation() {
        return this.constellation;
    }

    @Override
    public String toString() {
        return "{" + ", constellation='" + constellation + "}";
    }

    /**
     * Supprime les fichiers txt de communication de tout les satellites
     */
    public void deleteAll() {
        for (String str : constellation) {
            adapt.delete("Uplink", str);
            adapt.delete("Downlink", str);
            adapt.delete("Datalink", str);
        }
    }

    /**
     * Effectue la simulation du centre de contrôle. Renvoie false si la commande
     * est "terminate" ce qui a pour effet d'arrêter le SolMain et le BordMain.
     * 
     * @param scanner
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public String simCC(String order, String satOperator) throws ClassNotFoundException, IOException {

        try {
            getAdapt().post_uplink(order, satOperator);
            // Attends une réponse du satellite
            Mesure mes = null;
            while (mes == null && getAdapt().get_downlink(satOperator).compareTo("") == 0) {
                mes = getAdapt().get_datalink(satOperator);
            }
            // Si la réponse est dans le datalink, il archive la mesure renvoyée et renvoie
            // "OK" dans le terminal pour indiquer que tout s'est bien passé. Il affiche
            // également l'archive mesure.
            if (mes != null) {
                SaveData(mes, satOperator);
                getAdapt().delete("Datalink", satOperator);
                return "OK";
            }
            // Si la réponse est dans le downlink, il affiche cette réponse.
            else {
                String response = getAdapt().get_downlink(satOperator);
                getAdapt().delete("Downlink", satOperator);
                return response;

            }
        } catch (FileNotFoundException e) {
            System.out.println("please check your channel directory");
        }

        return ("KO");

    }

    /**
     * Méthode permettant de sauvegarder une mesure sous forme d'un fichier texte
     * 
     * @param my_mesure mesure que l'on souhaite sauvegarder
     * @param sat       satellite ayant effectué la mesure
     * @throws IOException
     */
    public static void SaveData(Mesure my_mesure, String name_sat) throws IOException {

        String file_name_1 = "DATA/" + name_sat + "/NEXTSEQNUM.txt";

        // Lecture du fichier Nextseqnum
        BufferedReader in = new BufferedReader(new FileReader(file_name_1));
        String number = in.readLine();
        in.close();

        // Création du nouveau fichier et écriture de la mesure
        String name = "DATA/" + name_sat + "/" + number + ".txt";
        File file = new File(name);

        if (file.createNewFile()) {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(name));
            out.writeObject(my_mesure);
            out.close();
        }

        // Ecriture du nouveau numéro de nextseqnum
        PrintWriter out2 = new PrintWriter(file_name_1);
        int new_number = Integer.parseInt(number);
        new_number = new_number + 1;
        String num = String.valueOf(new_number);
        while (num.length() != 9) {
            num = "0" + num;
        }
        out2.println(num);
        out2.close();

    }

    /**
     * Recupere un ordre compose d'un nom de satellite et d'un chemin vers le
     * fichier texte correspondant a la procedure souhaitee. Si le chemin menant au
     * fichier texte n'est pas correct, le programme l'indique a l'utilisateur.
     * 
     *
     * @param message Ordre envoye par l'Ihm.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void command(String message) throws IOException, ClassNotFoundException {

        // Lecture de l'ordre de l'utilisateur
        Scanner sc = new Scanner(message);
        String order = sc.next();

        Scanner s = new Scanner(order).useDelimiter(":");
        String satOperator = s.next();
        // Commande qui permet d'arrêter d'envoyer des ordres
        if (satOperator.equals("stop")) {

            System.exit(0);
        }
        try {
            String filename = s.next();
            analysefichierstextes(filename, satOperator);
        } catch (FileNotFoundException e) {
            System.out.println("Le nom du fichier est incorrect");
            stateCommand = "KO";
        }
        sc.close();
        s.close();

    }

    /**
     * C'est la methode qui analyse la procedure et envoie les ordres necessaires a
     * simCC pour mener a bien la procedure. Si une ligne de la procedure est
     * incorrecte, le programme l'indique a l'operateur. De la meme maniere, si la
     * procedure invoque une autre procedure qui comporte un chemin errone, le
     * programme l'indique egalement a l utilisateur.
     * 
     * 
     * @param procedure   Chemin qui mene au fichier texte (la procedure) a
     *                    analyser.
     * @param satOperator Nom du satellite demande par l'operateur.
     * @throws IOException
     * @throws ClassNotFoundException
     */

    private void analysefichierstextes(String procedure, String satOperator)
            throws IOException, ClassNotFoundException {

        // On vérifie que l'ordre possède plus que juste le satellite, sinon on
        // redemmande l'ordre

        FileReader in = new FileReader(procedure);
        BufferedReader bin = new BufferedReader(in);
        // Correspond a la liste des operations de la procedure.
        ArrayList<String> listeoperations = new ArrayList<>();
        // Tant que le fichier texte comporte des lignes non vides, on ajoute les
        // differentes operations de la procedure a la liste d'operations de la
        // procedure.
        while (bin.ready()) {

            String line = bin.readLine();
            listeoperations.add(line);
        }

        // Par la suite, on va analyser les operations une par une et ordonner leur
        // execution.

        for (int index = 0; index < listeoperations.size(); index++) {
            // Si l'operation est la chaine de caractere "ANDTHEN " et qu'il ne s'agit pas
            // de la derniere ligne de la procedure, on effectue l'operation suivante
            if (listeoperations.get(index).equals("ANDTHEN") == true && index < listeoperations.size() - 1) {
                // On regarde l'operation suivante.
                index++;
                if (stateCommand.equals("OK") == true) {
                    ex_andthen_orelse(index, listeoperations, procedure, satOperator);
                } else {

                }

                // Si l'operation est la chaine de caractere "ORELSE " et qu'il ne s'agit pas de
                // la derniere ligne de la procedure, on effectue l'operation suivante
            } else if (listeoperations.get(index).equals("ORELSE") == true && index < listeoperations.size() - 1) {
                // On regarde l'operation suivante.
                index++;
                if (stateCommand.equals("KO") == true) {
                    ex_andthen_orelse(index, listeoperations, procedure, satOperator);
                } else {

                }

            }

            // Si l'operation est une TC ou une TM, on l'execute.

            else if (listeoperations.get(index).contains(":") == true) {
                ex_operation_TC_ou_TM(listeoperations, index, satOperator);

            }

            // Si l'operation suivante est une nouvelle procedure, alors on l'execute.
            else if (listeoperations.get(index).contains("src/Procedures") == true) {
                try {
                    analysefichierstextes(listeoperations.get(index), satOperator);
                } catch (FileNotFoundException ex1) {
                    System.out.println("Le nom du fichier" + procedure + " est incorrect");
                    stateCommand = "KO";

                }
            }
            // Si l'operation suivante contient l'instruction "WAIT n", alors on l'execute.
            else if (listeoperations.get(index).contains("WAIT ") == true) {
                ex_wait(listeoperations.get(index));

            }

            // Si la ligne est en fait un commentaire ou une ligne vide, alors on la passe.

            else if (listeoperations.get(index).contains(";") == true || listeoperations.get(index).isEmpty() == true) {

            }
            // Si l'operation est un "REPEAT n", alors on l'execute.
            else if (listeoperations.get(index).contains("REPEAT ") == true && index < listeoperations.size() - 1) {

                ex_repeat(listeoperations, index, satOperator, procedure);

            }
            // Si l'operation n'a pas ete comprise, on l'indique a l'operateur.
            else {
                int j = index + 1;
                System.out.println("L'ordre situe a la ligne " + j + " du fichier " + procedure + " est incorrect");
            }

        }
        bin.close();

    }

    /**
     * Cette methode permet d'effectuer une operation TC ou TM sur un satellite
     * donne en invoquant la methode simCC.
     * 
     * 
     * 
     * @param listeoperations Represente la liste des lignes de la procedure.
     * @param index           Correspond au numero de la ligne auquel on s'interesse
     *                        actuellement.
     * @param satOperator     Correspond au nom du satellite demande par
     *                        l'utilisateur.
     * @throws IOException
     * @throws ClassNotFoundException
     */

    private void ex_operation_TC_ou_TM(ArrayList<String> listeoperations, int index, String satOperator)
            throws ClassNotFoundException, IOException {

        String ordre = listeoperations.get(index);
        // On effectue l'operation en invoquant la methode simCC.
        stateCommand = simCC(satOperator + ":" + ordre, satOperator);

    }

    /**
     * Cette methode permet de repeter n fois l'operation suivante.
     * 
     * 
     * @param listeoperations Liste des operations de la procedure.
     * @param index           Numero de l'operation a laquelle on s'interesse.
     * @param satOperator     Nom du satellite demande par l'operateur.
     * @param procedure       Procedure qui nous interesse.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void ex_repeat(ArrayList<String> listeoperations, int index, String satOperator, String procedure)
            throws IOException, ClassNotFoundException {
        // Dans la boucle suivante, on s'affranchit des commentaires et des lignes
        // vides.
        while (listeoperations.get(index).contains(";") || listeoperations.get(index).isEmpty() == true) {
            if (index < listeoperations.size() - 1) {
                index++;
            } else {
                int k = index + 1;
                System.out.println("Erreur logique a la ligne " + k + "de la procedure " + procedure
                        + ": L'instruction REPEAT n doit etre suivie d'une operation.");
            }
        }
        String linerepeat = listeoperations.get(index);
        String[] tokensrepeat = linerepeat.split(" ");
        int compteur = Integer.parseInt(tokensrepeat[1].trim());
        index++;
        // Si l'operation suivante est une TC ou une TM, on l'execute le nombre de fois
        // necessaire.
        if (listeoperations.get(index).contains(":") == true) {

            String ordre = listeoperations.get(index);

            for (int r = 1; r < compteur; r++) {
                stateCommand = simCC(satOperator + ":" + ordre, satOperator);
            }

        }
        // S'il faut repeter une procedure, alors on effectue l'operation suivante.
        else if (listeoperations.get(index).contains("src/Procedures") == true) {
            try {
                for (int r = 1; r < compteur; r++) {
                    analysefichierstextes(listeoperations.get(index), satOperator);
                }
            } catch (FileNotFoundException ex1) {
                System.out.println("Le nom du fichier" + procedure + " est incorrect");
                stateCommand = "KO";

            }

        }

        // Si l'operation suivante contient l'instruction "WAIT n", alors on l'effectue.
        else if (listeoperations.get(index).contains("WAIT ") == true) {
            ex_wait(listeoperations.get(index));
        }

    }

    /**
     * Effectue l'operation ANDTHEN ou l'operation ORELSE (a ce stade, cela revient
     * au meme).
     * 
     * 
     * 
     * @param index           Index qui nous interesse dans le tableau des
     *                        operations
     * @param listeoperations Tableau des operations
     * @param procedure       Fichier texte ou sont consignees les operations.
     * @param satOperator     Nom du satellite demande par l'operateur
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void ex_andthen_orelse(int index, ArrayList<String> listeoperations, String procedure, String satOperator)
            throws IOException, ClassNotFoundException {

        // Dans la boucle while suivante, on s'affranchit des commentaires et des lignes
        // vides.
        while (listeoperations.get(index).contains(";") || listeoperations.get(index).isEmpty() == true) {
            if (index < listeoperations.size() - 1) {
                index++;
            } else {
                int k = index + 1;
                System.out.println("Probleme a la ligne " + k + "de la procedure" + procedure
                        + ": le AND THEN doit etre suivi d'une operation");
                stateCommand = "KO";
            }
        }
        // Si l'operation suivante est une TC ou une TM, on l'execute
        if (listeoperations.get(index).contains(":") == true) {
            ex_operation_TC_ou_TM(listeoperations, index, satOperator);
        }
        // Si l'operation suivante est une nouvelle procedure, on l'execute.
        else if (listeoperations.get(index).contains("src/Procedures") == true) {
            try {
                analysefichierstextes(listeoperations.get(index), satOperator);
            } catch (FileNotFoundException ex1) {
                System.out.println("Le nom du fichier" + procedure + " est incorrect");
                stateCommand = "KO";

            }

        }
        // Si l'operation suivante est "REPEAT n" alors on l'execute.

        else if (listeoperations.get(index).contains("REPEAT ")) {
            ex_repeat(listeoperations, index, satOperator, procedure);

        }

        // Si l'operation n'a pas ete reconnue, alors on l'indique a l'utilisateur.
        else {
            System.out.println("Operation non logique");
            stateCommand = "KO";
        }

    }

    /**
     * Si l'operation demandee est un "WAIT n", alors on attend n millisecondes.
     * 
     * @param ordre
     */

    private void ex_wait(String ordre) {
        String[] tokens = ordre.split(" ");
        int time1 = Integer.parseInt(tokens[1].trim());

        try {
            Thread.sleep(time1);

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    /**
     * permet de relier les signaux des boutons aux actions correspondantes
     */
    protected void initController() {
        // contient les boutons et les labels
        HashMap<String, HashMap<String, HashMap<String, JButton>>> satMap = view.getSatMap();
        HashMap<String, HashMap<String, JLabel>> labelSatMap = view.getLabelSatMap();

        // pour chaque satellite
        for (String satKey : satMap.keySet()) {
            HashMap<String, HashMap<String, JButton>> ssSysMap = satMap.get(satKey);
            HashMap<String, JLabel> labelSsSysMap = labelSatMap.get(satKey);

            // pour chaque sous-système
            for (String ssSysKey : ssSysMap.keySet()) {
                HashMap<String, JButton> buttonsMap = ssSysMap.get(ssSysKey);

                // on récupère tous les boutons existants de la HashMap du view pour leur
                // associer un ButtonListener en spécifiant le nom du satellite et le nom du
                // sous-système correspondand à chaque fois
                ButtonListener onListener = new ButtonONOFFListener(this, satKey, ssSysKey, "ON", view,
                        labelSsSysMap.get(ssSysKey), buttonsMap.get("ON"), buttonsMap.get("OFF"));
                buttonsMap.get("ON").addActionListener(onListener);

                ButtonListener offListener = new ButtonONOFFListener(this, satKey, ssSysKey, "OFF", view,
                        labelSsSysMap.get(ssSysKey), buttonsMap.get("ON"), buttonsMap.get("OFF"));
                buttonsMap.get("OFF").addActionListener(offListener);

                ButtonListener dataListener = new ButtonListener(this, satKey, ssSysKey, "DATA", view,
                        labelSsSysMap.get(ssSysKey));
                buttonsMap.get("DATA").addActionListener(dataListener);

                ButtonListener repeatListener = new ButtonListener(this, satKey, ssSysKey, "REPEAT", view,
                        labelSsSysMap.get(ssSysKey));
                buttonsMap.get("REPEAT").addActionListener(repeatListener);

            }

            /// on récupère tous les boutons des procédures complexes du view pour leur
            // associer un ButtonListener en spécifiant le nom du satellite et le nom du
            // de la procédure.
            HashMap<String, JButton> procecomplMap = view.getProcecomplMap();
            HashMap<String, JLabel> labelprocecomplMap = view.getLabelprocecomplMap();

            ButtonListener dernieremesureListener = new ButtonListener(this, satKey, "PROCEDURESCOMPLEXES", "REDUNDANT",
                    view, labelprocecomplMap.get(satKey));
            procecomplMap.get(satKey + "dernieremesure").addActionListener(dernieremesureListener);

            ButtonListener deuxssListener = new ButtonListener(this, satKey, "PROCEDURESCOMPLEXES", "PLUSIEURSMESURES",
                    view, labelprocecomplMap.get(satKey));
            procecomplMap.get(satKey + "2sous-systeme").addActionListener(deuxssListener);

        }

    }
}
