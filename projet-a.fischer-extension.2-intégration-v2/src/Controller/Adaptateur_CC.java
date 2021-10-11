package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import Models.Mesures.*;

public class Adaptateur_CC {
    /**
     * Crée et écrit dans le fichier uplink du satellite correspondant
     * 
     * @param command
     * @param nomSat
     * @throws FileNotFoundException
     */
    public void post_uplink(String command, String nomSat) throws FileNotFoundException {

        PrintWriter out = new PrintWriter("channels/" + nomSat + "/Uplink.txt");
        out.println(command);
        out.close();

    }

    /**
     * Récupère le string dans le fichier downlink du satellite correspondant
     * 
     * @param nomSat
     * @return
     * @throws IOException
     */
    public String get_downlink(String nomSat) throws IOException {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader("channels/" + nomSat + "/Downlink.txt"));
        } catch (FileNotFoundException e) {
            return "";
        }
        String str = in.readLine();
        in.close();
        if (str != null) {
            return str;
        }
        return "";
    }

    /**
     * Récupère le string dans le fichier datalink du satellite correspondant
     * 
     * @param nomSat
     * @return
     * @throws ClassNotFoundException
     */
    public Mesure get_datalink(String nomSat) throws ClassNotFoundException {

        String donnee = "channels/" + nomSat + "/Datalink.txt";
        FileInputStream fi;
        try {
            fi = new FileInputStream(donnee);
            ObjectInputStream oi;
            try {
                oi = new ObjectInputStream(fi);
                Mesure m = (Mesure) oi.readObject();
                oi.close();
                fi.close();
                return m;
            } catch (IOException e) {
                fi.close();
                return null;
            }
        } catch (IOException e) {
            return null;
        }

    }

    /**
     * Supprime le fichier downlink ou datalink du satellite correspondant
     * 
     * @param dir
     * @param nomSat
     */
    public void delete(String dir, String nomSat) {
        File file = new File("channels/" + nomSat + "/" + dir + ".txt");
        file.delete();
    }
}
