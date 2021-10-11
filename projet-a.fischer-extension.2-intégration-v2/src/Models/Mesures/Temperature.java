package Models.Mesures;

import java.text.DecimalFormat;
import java.util.Random;

public class Temperature extends Mesure {

    private static final long serialVersionUID = 1L;
    private double temperature;
    private Random rand;

    /**
     * Constructeur de la classe Temperature
     */
    public Temperature() {
        super();
        rand = new Random();
        temperature = 0.0;

    }

    /**
     * Permet de prendre la mesure, ici une temperature aléatoire en kelvin (entre 0
     * et 1000 kelvin)
     */
    public void take_mesure() {
        temperature = 1000 * (rand.nextDouble());
    }

    /**
     * Permet d'afficher la température au centième
     */
    public void print_mesure() {
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println(df.format(temperature));
    }

}
