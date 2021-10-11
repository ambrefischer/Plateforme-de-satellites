package Controller;

import java.io.IOException;
import java.util.Scanner;

public class ArchivageMain_interactif {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        boolean fin = false;

        // Lecture de l'ordre de l'utilisateur
        Scanner sc = new Scanner(System.in);

        Archivage a = new Archivage();

        while (fin == false) {
            System.out.println(
                    "Pour effectuer une recherche indiquez la date de dédut de l'intervalle, la date de fin de l'intervalle, le nom du satellite ainsi que le type de donnée sous la forme :");
            System.out.println("AAAA/MM/JJ HH:MM:SS,AAAA/MM/JJ HH:MM:SS,SATELLITE,TYPE");
            System.out.println("Pour qu'un critère soit inactif tapez 0 à la place de celui ci");
            System.out.println("Pour arrêter les recherches tapez stop");
            String order = sc.nextLine();

            try {
                String[] commande = order.split(",");
                // Commande qui permet d'arrêter d'envoyer des ordres
                if (order.equals("stop")) {
                    fin = true;
                } else {
                    a.recherche(commande[0], commande[1], commande[2], commande[3]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Format d'instruction incorrecte");
            }
        }
        sc.close();
    }
}
