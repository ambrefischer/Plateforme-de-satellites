package Controller;

import java.io.IOException;
import java.text.ParseException;

public class ArchivageMain {
        public static void main(String[] args) throws ClassNotFoundException, IOException, ParseException {

                Archivage a = new Archivage();
                // Recherche avec intervalle de temps incorrecte
                System.out.println(
                                "/////////////////////////////////////////////////////////////////////// RECHERCHE 1 /////////////////////////////////////////////////////////////////////////////");
                a.recherche("2020/11/06 21:00:00", "2020/11/06 20:00:00", "0", "Image");

                // Recherche avec un nom de satellite inconnu
                System.out.println(
                                "/////////////////////////////////////////////////////////////////////// RECHERCHE 2 /////////////////////////////////////////////////////////////////////////////");
                a.recherche("0", "0", "IS", "0");

                // Recherche des mesures de ISAESAT
                System.out.println(
                                "/////////////////////////////////////////////////////////////////////// RECHERCHE 3 /////////////////////////////////////////////////////////////////////////////");
                a.recherche("0", "0", "ISAESAT", "0");

                // Recherche des mesures de XSAT
                System.out.println(
                                "/////////////////////////////////////////////////////////////////////// RECHERCHE 4 /////////////////////////////////////////////////////////////////////////////");
                a.recherche("0", "0", "XSAT", "0");

                // Recherche des mesures de type Image
                System.out.println(
                                "/////////////////////////////////////////////////////////////////////// RECHERCHE 5 /////////////////////////////////////////////////////////////////////////////");
                a.recherche("0", "0", "0", "Image");

                // Recherche des mesures de type Image du satellite XSAT du 2020/11/06 21:00:00
                // au 2020/11/06 22:00:00
                System.out.println(
                                "/////////////////////////////////////////////////////////////////////// RECHERCHE 6 /////////////////////////////////////////////////////////////////////////////");
                a.recherche("2020/11/14 14:46:00", "2020/11/14 14:48:00", "XSAT", "Image");

                // Recherche des mesures de type Temperature
                System.out.println(
                                "/////////////////////////////////////////////////////////////////////// RECHERCHE 7 /////////////////////////////////////////////////////////////////////////////");
                a.recherche("0", "0", "0", "Temperature");
        }
}
