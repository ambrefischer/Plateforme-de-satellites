package Models.SatelliteFamilies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import Models.Mesures.*;

public class AdaptateurSAT {
    /**
     * nom du satellite pour savoir quel channel utiliser
     */
    String nomSat;

    /**
     * Construction de l'objet à partir du nom du satellite (XSAT par exemple)
     * 
     * @param nom
     * @throws IOException
     */
    public AdaptateurSAT(String nom) {
        nomSat = nom;
        File dir1 = new File("channels/" + nom);
        dir1.mkdir();
        /*
         * File f = new File("src/DATA/" + nomSat); if(f.isDirectory() == false){ String
         * filename = "src/DATA/" + nomSat + "/NEXTSEQNUM.txt"; File dir = new
         * File("src/DATA/" + nomSat); dir.mkdir();
         * 
         * File file = new File(filename); file.createNewFile();
         * 
         * PrintWriter out = new PrintWriter(file); out.println("000000000");
         * out.close(); }
         */
    }

    /**
     * Crée et écrit dans le fichier downlink du satellite correspondant
     * 
     * @param state
     * @throws FileNotFoundException
     */
    public void post_downlink(String state) {

        PrintWriter out;
        try {
            out = new PrintWriter("channels/" + nomSat + "/Downlink.txt");
            out.println(state);
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("please check your directories");
        }

    }

    /**
     * Crée et écrit dans le fichier downlink du satellite correspondant
     * 
     * @param state
     * @throws IOException
     */
    public void post_datalink(Mesure state) throws IOException {

        String name = "channels/" + nomSat + "/Datalink.txt";
        File data = new File(name);
        ObjectOutputStream out;
        FileOutputStream out2 = new FileOutputStream(data);
        try {
            out = new ObjectOutputStream(out2);
            out.writeObject(state);
            out2.close();
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("please check your directories");
        }

    }

    /**
     * Récupère le string dans le fichier uplink du satellite correspondant
     * 
     * @return
     * @throws IOException
     */
    public String get_uplink() throws IOException {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader("channels/" + nomSat + "/Uplink.txt"));
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
     * Supprime le fichier uplink du satellite correspondant
     * 
     * @param nomSat
     */
    public void delete(String nomSat) {
        File file = new File("channels/" + nomSat + "/Uplink.txt");
        file.delete();
    }
}
