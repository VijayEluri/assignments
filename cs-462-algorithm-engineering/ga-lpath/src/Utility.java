

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author abuzaher
 */
public class Utility {

    static int getRandomNodeIndex(ArrayList<Double> probabilityDistribution) {
        ArrayList<Double> cumulativeDistribution = new ArrayList<Double>();
        Double rand;
        double sum;
        int index = 0;

        cumulativeDistribution.add(0.0);
        sum = 0;
        for (int i = 0; i < probabilityDistribution.size(); i++) {
            sum += probabilityDistribution.get(i);
            cumulativeDistribution.add(sum);
        }

        rand = Math.random();
        for (int i = 0; i < cumulativeDistribution.size() - 1; i++) {
            if (rand > cumulativeDistribution.get(i) && rand <= cumulativeDistribution.get(i + 1)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
