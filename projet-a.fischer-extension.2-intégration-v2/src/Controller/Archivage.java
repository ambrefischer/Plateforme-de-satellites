package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import Models.Mesures.*;

public class Archivage {
    private TreeMap<Date, ArrayList<String>> time;
    private HashMap<String, ArrayList<String>> sat;
    private HashMap<String, ArrayList<String>> type_donnee;

    /**
     * Contructeur de Archivage
     */
    public Archivage() {
        time = new TreeMap<Date, ArrayList<String>>();
        sat = new HashMap<String, ArrayList<String>>();
        type_donnee = new HashMap<String, ArrayList<String>>();
    }

    /**
     * Méthode permettant de mettre à jour la base de donnée de l'archive,
     * c'est-à-dire qu'elle récupère toutes les données du dossier DATA et qu'elle
     * les trie suivant la date, le nom du satellite et le type de mesure.
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void mettre_a_jour() throws IOException, ClassNotFoundException {

        time.clear();
        sat.clear();
        type_donnee.clear();

        // Liste des dossiers (noms des satellites) de DATA
        File repertoire = new File("DATA");
        String liste[] = repertoire.list();

        // Recherche dans tous les dossiers nom de satellite
        for (String name_sat : liste) {

            // Liste des fichiers contenant les données du satellite
            ArrayList<String> liste_sat = new ArrayList<>();
            File sous_repertoire = new File("DATA/" + name_sat);
            String liste_data[] = sous_repertoire.list();

            for (String file : liste_data) {

                if (file.equals("NEXTSEQNUM.txt") == false) {

                    // On ajoute chaque fichier à la liste des fichiers correspondant au satellite
                    // name_sat
                    String data_file_i = "DATA/" + name_sat + "/" + file;
                    liste_sat.add(data_file_i);

                    // Récupération de la donnée de mesure
                    FileInputStream fi = new FileInputStream(data_file_i);
                    ObjectInputStream oi = new ObjectInputStream(fi);
                    Mesure m = (Mesure) oi.readObject();
                    if (m != null) {
                        // Tri en fonction du type de donnée
                        if (type_donnee.containsKey(m.getClass().getName())) {
                            ArrayList<String> liste_mesures = type_donnee.get(m.getClass().getName());
                            liste_mesures.add(data_file_i);
                            type_donnee.put(m.getClass().getName(), liste_mesures);
                        } else {
                            ArrayList<String> liste_mesures = new ArrayList<>();
                            liste_mesures.add(data_file_i);
                            type_donnee.put(m.getClass().getName(), liste_mesures);
                        }
                        // Tri en fonction de la date
                        Date d = m.getTimeStamp();
                        if (time.containsKey(d)) {
                            ArrayList<String> liste_date = time.get(d);
                            liste_date.add(data_file_i);
                            time.put(d, liste_date);
                        } else {
                            ArrayList<String> liste_date = new ArrayList<>();
                            liste_date.add(data_file_i);
                            time.put(d, liste_date);

                        }
                    }
                    oi.close();
                    fi.close();

                }
            }
            // Tri en fonction du satellite
            sat.put(name_sat, liste_sat);

        }
    }

    /**
     * Méthode permettant de rechercher et d'afficher toutes les données stockées si leur nombre est
     * inférieur à 20.
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void recherche() throws ClassNotFoundException, IOException {
        mettre_a_jour();
        Collection<ArrayList<String>> list = sat.values();
        ArrayList<String> result = new ArrayList<String>();
        for (ArrayList<String> donnee : list) {
            for (String donnee_2 : donnee) {
                result.add(donnee_2);
            }
        }
        afficher(result);
    }

    /**
     * Méthode permettant d'effectuer une recherche selon trois paramètre :
     * intervalle de temps, satellite et type de mesure. Pour qu'un paramètre soit
     * inactif, il suffit de mettre "0".
     * 
     * @param depart   date de début de l'intervalle format "yyyy/MM/dd HH:mm:ss"
     * @param arrive   date de fin de l'intervalle format "yyyy/MM/dd HH:mm:ss"
     * @param satel    nom du satellite
     * @param t_donnee type de donnée
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws ParseException
     */
    public void recherche(String depart, String arrive, String satel, String t_donnee1)
            throws ClassNotFoundException, IOException {

        if (depart.equals("0") & arrive.equals("0") & t_donnee1.equals("0") & satel.equals("0")) {
            recherche();
        } else if ((depart.equals("0") & (arrive.equals("0") == false))
                || (arrive.equals("0") & (depart.equals("0") == false))) {
            System.out.println("Merci d'indiquer un début ET une fin d'intervalle");
        } else {
            mettre_a_jour();
            String t_donnee = "Models.Mesures." + t_donnee1;
            System.out.print("Recherche des mesures ");

            // Création en amont de certaines variables très redondantes
            ArrayList<String> list_sat = sat.get(satel);
            ArrayList<String> list_type = type_donnee.get(t_donnee);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            ArrayList<String> result = new ArrayList<String>();

            // On commence par traiter les cas où 2 des 3 critères sont nuls

            if (depart.equals("0") & arrive.equals("0") & t_donnee1.equals("0")) {
                System.out.println("du satellite " + satel);
                System.out.println(" ");
                result = list_sat;
                afficher(result);

            } else if (depart.equals("0") & arrive.equals("0") & satel.equals("0")) {
                System.out.println("de type " + t_donnee1);
                System.out.println(" ");
                result = list_type;
                afficher(result);

            } else if (satel.equals("0") & t_donnee1.equals("0")) {
                System.out.println("sur un intervalle entre " + depart + " et " + arrive);
                System.out.println(" ");

                try {
                    Date beg = sdf.parse(depart);
                    Date fin = sdf.parse(arrive);
                    try {
                        SortedMap<Date, ArrayList<String>> l_time = time.subMap(beg, fin);
                        Collection<ArrayList<String>> list_time = l_time.values();
                        for (ArrayList<String> donnee_time : list_time) {
                            for (String donnee_time2 : donnee_time) {
                                result.add(donnee_time2);
                            }
                        }
                        afficher(result);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Ordre des dates incorrecte");
                    }
                } catch (ParseException e) {
                    System.out.println("Format de date incorrecte");
                }
                // Puis on traite les cas où un seul critère est nul

            } else if (depart.equals("0") & arrive.equals("0")) {
                System.out.println("de type " + t_donnee1 + " sur le satellite " + satel);
                System.out.println(" ");
                if ((list_sat != null) & (list_type != null)) {
                    for (String donnee_sat : list_sat) {
                        if (list_type.contains(donnee_sat)) {
                            result.add(donnee_sat);
                        }
                    }
                } else {
                    result = null;
                }
                afficher(result);
            } else if (t_donnee1.equals("0")) {
                System.out.println(
                        "sur un intervalle entre " + depart + " et " + arrive + " et sur le satellite" + satel);
                System.out.println(" ");
                if (list_sat != null) {
                    try {
                        Date beg = sdf.parse(depart);
                        Date fin = sdf.parse(arrive);
                        try {
                            SortedMap<Date, ArrayList<String>> l_time = time.subMap(beg, fin);
                            Collection<ArrayList<String>> list_time = l_time.values();
                            for (String donnee_sat : list_sat) {
                                for (ArrayList<String> donnee_time : list_time) {
                                    if (donnee_time.contains(donnee_sat)) {
                                        result.add(donnee_sat);
                                    }
                                }
                            }
                            afficher(result);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Ordre des dates incorrecte");
                        }
                    } catch (ParseException e) {
                        System.out.println("Format de date incorrecte");
                    }
                } else {
                    afficher(null);
                }

            } else if (satel.equals("0")) {
                System.out.println("sur un intervalle entre " + depart + " et " + arrive + " et de type " + t_donnee1);
                System.out.println(" ");
                if (list_type != null) {
                    try {
                        Date beg = sdf.parse(depart);
                        Date fin = sdf.parse(arrive);
                        try {
                            SortedMap<Date, ArrayList<String>> l_time = time.subMap(beg, fin);
                            Collection<ArrayList<String>> list_time = l_time.values();
                            for (String donnee_type : list_type) {
                                for (ArrayList<String> donnee_time : list_time) {
                                    if (donnee_time.contains(donnee_type)) {
                                        result.add(donnee_type);
                                    }
                                }
                            }
                            afficher(result);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Ordre des dates incorrecte");
                        }
                    } catch (ParseException e) {
                        System.out.println("Format de date incorrecte");
                    }
                } else {
                    afficher(null);
                }
                // Enfin on traite le cas où tous les critères sont non nuls.
            } else {
                System.out.println("sur un intervalle entre " + depart + " et " + arrive + " sur le satellite " + satel
                        + " et de type " + t_donnee1);
                System.out.println(" ");
                if ((list_sat != null) & (list_type != null)) {
                    try {
                        Date beg = sdf.parse(depart);
                        Date fin = sdf.parse(arrive);
                        try {
                            SortedMap<Date, ArrayList<String>> l_time = time.subMap(beg, fin);
                            Collection<ArrayList<String>> list_time = l_time.values();
                            for (String donnee_sat : list_sat) {
                                if (list_type.contains(donnee_sat)) {
                                    for (ArrayList<String> donnee_time : list_time) {
                                        if (donnee_time.contains(donnee_sat)) {
                                            result.add(donnee_sat);
                                        }
                                    }
                                }
                            }
                            afficher(result);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Ordre des dates incorrecte");
                        }
                    } catch (ParseException e) {
                        System.out.println("Format de date incorrecte");
                    }
                } else {
                    afficher(null);
                }
            }
        }
    }

    /**
     * Méthode permettant d'afficher les résultats de la recherche sous la forme :
     * Nom du fichier    Date et heure     Nom du satellite    Type de donnée   Donnée
     * 
     * @param result liste contenant les fichiers correspondant à la recherche
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void afficher(ArrayList<String> result) throws IOException, ClassNotFoundException {

        if (result != null) {
            if ((result.size() != 0)) {

                // Traitement différent suivant le nombre de fichiers trouvé
                if (result.size() < 20) {

                    System.out.println(
                            "Nom du fichier                          Date et heure                          Nom du satellite            Type de donnée                       Donnée");
                    for (String donnee : result) {

                        // Récupération de la mesure contenue dans le fichier
                        FileInputStream fi = new FileInputStream(donnee);
                        ObjectInputStream oi = new ObjectInputStream(fi);
                        Mesure m = (Mesure) oi.readObject();

                        if (m != null) {
                            String[] tokens = donnee.split("DATA/");
                            String name = tokens[1].trim();
                            // Récupération du nom du satellite
                            String[] tokens2 = name.split("/");
                            String name2 = tokens2[0].trim();

                            // Récupération du nom du fichier sans src et .txt
                            String[] split2 = donnee.split(".txt");
                            String nom_fichier = split2[0].trim();

                            // Récupération du nom de la classe
                            String classe = m.getClass().getName();
                            String[] split3 = classe.split("Mesures.");
                            String nom_classe = split3[1].trim();

                            // Affichage
                            System.out.print(nom_fichier);
                            if (name2.equals("ISAESAT")) {
                                System.out.print("                  ");
                            } else {
                                System.out.print("                     ");
                            }
                            System.out.print(m.getTimeStamp());
                            System.out.print("                  ");
                            System.out.print(name2);
                            if (name2.equals("ISAESAT")) {
                                System.out.print("                  ");
                            } else {
                                System.out.print("                     ");
                            }
                            System.out.print(nom_classe);
                            System.out.print("                      ");
                            m.print_mesure();
                        }

                        fi.close();
                        oi.close();
                    }
                    System.out.println(" ");
                } else {
                    System.out.print("Nombre de mesures trouvé : ");
                    System.out.println(result.size());
                }
            } else {
                System.out.println("Aucun résultat trouvé");
            }
        } else {
            System.out.println("Aucun résultat trouvé");
        }
    }
}
