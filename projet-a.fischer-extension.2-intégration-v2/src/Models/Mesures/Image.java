package Models.Mesures;

import java.util.Arrays;
import java.util.Random;

public class Image extends Mesure {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int size;
    private Random rand;
    private int[][] map;

    /**
     * Constructeur de la classe Image
     */
    public Image() {
        super();
        size = 2;
        rand = new Random();
        map = new int[size][size];
    }

    /**
     * Permet de prendre la mesure, ici un tableau de nombre aléatoire
     */
    public void take_mesure() {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = rand.nextInt(256);
            }
        }

    }

    public void print_mesure() {
        System.out.println(Arrays.deepToString(map));
    }

}
