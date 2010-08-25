package aco;

import java.util.Vector;
import java.lang.Math;
import java.util.Random;

public class LPath2 {

    public static Vector<Vector<Double>> adjacencyMatrix;
    public static Vector<Vector<Double>> pheromoneMatrix;
    public static Vector<Vector<Integer>> solution;
    public static Vector<Integer> bestSolution;
    public static Vector<Double> cost;
    public static double bestCost = 0;
    public static Vector<Boolean> visited;
    public static double counter = 0;
    public static double initialPheromone = 5;
    public static int ants = 100;
    public static double evaporationRate = 0.5;
    public static double depositRate = 2;
    public static int numberIteration = 100;
    public static int size;                                    //graph size
    public static double alpha = 3;
    public static double beta = 1;

    /*
     * 1. Reads input form the file
     */
    public static void initialize() {
        Input.openFile("longestPath.txt");
//        Input.openFile("input2.txt");
//        Input.openFile("input3.txt");

        // reading input file, Vector vc has the data
        Vector<Double> vc = Input.readFile();
        Input.closeFile();

        // assign size, alpha and beta
        size = (int) Math.sqrt(vc.size() - 2);
        alpha = vc.elementAt(vc.size() - 1);
        beta = vc.elementAt(vc.size() - 2);


        // distance between the vertices
        adjacencyMatrix = new Vector<Vector<Double>>();

        // pheromone in the edges
        pheromoneMatrix = new Vector<Vector<Double>>();

        // solution generated by each ant
        // each ant's solution will contain a list of integers
        // that has all the nodes it visits
        solution = new Vector<Vector<Integer>>();

        // the best solution
        bestSolution = new Vector<Integer>();

        // total path cost for each ant
        cost = new Vector<Double>();

        // marker for each vertex whether it has been visited
        visited = new Vector<Boolean>();


        // create 2D blank adjacency matrix
        for (int i = 0; i < size; i++) {
            adjacencyMatrix.add(new Vector<Double>());
        }

        // create 2D blank pheromone matrix
        for (int i = 0; i < size; i++) {
            pheromoneMatrix.add(new Vector<Double>());
        }

        // create blank soluction vector
        for (int i = 0; i < ants; i++) {
            solution.add(new Vector<Integer>());
        }



        // assign value to the adjacency matrix from Vector vc
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                adjacencyMatrix.elementAt(i).add(vc.elementAt(i * size + j));
            }
        }

        // i is row, j is column
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (adjacencyMatrix.elementAt(i).elementAt(j) > 0) {
                    // if there is an edge between i to j, deplpy initial pheromone
                    // in the pheromone matrix
                    pheromoneMatrix.elementAt(i).add(initialPheromone);
                } else {
                    // else, pheromone 0, so the ant will never go in this way
                    pheromoneMatrix.elementAt(i).add(0.0);
                }
            }
        }

        // mark all the nodes as unvisited
        for (int i = 0; i < size; i++) {
            visited.add(i, false);
        }

        // for each ant, set associated travel cost to 0
        for (int i = 0; i < ants; i++) {
            cost.add(i, 0.0);
        }
    }

    /*
     * finds the local maximum for a starting node and a specific ant
     */
    public static void findMaximalPath(int currentCT, int ant) {
        int counter = 0;
        double currentProbability;
        int currentProbabilityIndex = 0;
        double sum = 0;

        for (int i = 0; i < size; i++) {
            if (i != currentCT) {
                if (adjacencyMatrix.elementAt(currentCT).elementAt(i) > 0) {
                    // for each directed edge CT -> i
                    if (!visited.elementAt(i)) {
                        // if the target node is not visited
                        // sum += pheremone(current, target)^alpha * distance(current, target)^beta
                        sum += Math.pow(pheromoneMatrix.elementAt(currentCT).elementAt(i), alpha) * Math.pow(adjacencyMatrix.elementAt(currentCT).elementAt(i), beta);
                        currentProbabilityIndex = i;
                        counter++;
                    }
                }
            }
        }

        // coutner == 0, means the above code segment did not run
        // i.e. all the nodes are visited, we do not have to run the program anymore
        if (counter == 0) {
            return;
        }

        // we visied some nodes, and calculated the probabilityIndex, so go to following lines
        counter = 0;
        currentProbability = Math.pow(pheromoneMatrix.elementAt(currentCT).elementAt(currentProbabilityIndex), alpha) * Math.pow(adjacencyMatrix.elementAt(currentCT).elementAt(currentProbabilityIndex), beta) / sum;

        for (int i = 0; i < size; i++) {

            if (adjacencyMatrix.elementAt(currentCT).elementAt(i) > 0) {

                if (!visited.elementAt(i)) {
                    double temp = Math.pow(pheromoneMatrix.elementAt(currentCT).elementAt(i), alpha) * Math.pow(adjacencyMatrix.elementAt(currentCT).elementAt(i), beta) / sum;
                    if (temp > currentProbability) {
                        currentProbability = temp;
                        currentProbabilityIndex = i;
                    }

                    counter++;

                    if (temp > Math.random()) {
                        visited.remove(i);
                        visited.add(i, true);
                        solution.elementAt(ant).add(i);
                        double tempCost = cost.elementAt(ant) + adjacencyMatrix.elementAt(currentCT).elementAt(i);
                        cost.remove(ant);
                        cost.add(ant, tempCost);
                        findMaximalPath(i, ant);
                        return;
                    }
                }

            }

        }

        if (counter != 0) {
            visited.remove(currentProbabilityIndex);
            visited.add(currentProbabilityIndex, true);
            solution.elementAt(ant).add(currentProbabilityIndex);
            double tempCost = cost.elementAt(ant) + adjacencyMatrix.elementAt(currentCT).elementAt(currentProbabilityIndex);
            cost.remove(ant);
            cost.add(ant, tempCost);
            findMaximalPath(currentProbabilityIndex, ant);
        }
    }

    public static void pheromoneUpdate() {

        int vertex1;
        int vertex2;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double temp = pheromoneMatrix.elementAt(i).elementAt(j);
                temp = (1 - evaporationRate) * temp;
                pheromoneMatrix.elementAt(i).remove(j);
                pheromoneMatrix.elementAt(i).add(j, temp);
            }
        }

        int currentBestSolutionSize = bestSolution.size();

        for (int i = 0; i < (currentBestSolutionSize - 1); i++) {
            vertex1 = bestSolution.elementAt(i);
            vertex2 = bestSolution.elementAt(i + 1);
            double temp = pheromoneMatrix.elementAt(vertex1).elementAt(vertex2) + depositRate;
            pheromoneMatrix.elementAt(vertex1).remove(vertex2);
            pheromoneMatrix.elementAt(vertex1).add(vertex2, temp);

        }


    }

    /*
     * main function for finding the longest path in an iteration
     */
    public static void findLongestPath() {

        // for each ant
        for (int i = 0; i < ants; i++) {
            // for each of tbe nodes
            for (int j = 0; j < size; j++) {
                // mark the node as unvisited
                visited.remove(j);
                visited.add(j, false);
            }

            Random rg = new Random();
            int currentCT = rg.nextInt(size);

            // visit a random node
            visited.remove(currentCT);
            visited.add(currentCT, true);

            // since we are visiting the node, we need to add this node to our
            // current sulution for ant i
            solution.elementAt(i).add(currentCT);

            // each ant finds the maximal path (local maximum)
            findMaximalPath(currentCT, i);
        }

        double tempCost = cost.elementAt(0);
        int tempCostIndex = 0;
        for (int i = 1; i < ants; i++) {
            if (cost.elementAt(i) > tempCost) {
                tempCost = cost.elementAt(i);
                tempCostIndex = i;
            }
        }

        if (tempCost > bestCost) {
            bestCost = tempCost;
            if (!bestSolution.isEmpty()) {
                bestSolution.clear();
            }
            int currentBestSolutionSize = solution.elementAt(tempCostIndex).size();
            for (int j = 0; j < currentBestSolutionSize; j++) {
                bestSolution.add(solution.elementAt(tempCostIndex).elementAt(j));
            }


        }
        pheromoneUpdate();

    }

    public static void main(String args[]) {

        initialize();
        // for each iteration
        for (int i = 0; i < numberIteration; i++) {
            for (int j = 0; j < ants; j++) {
                // for each ant
                // set its associated cost to zero
                cost.remove(j);
                cost.add(j, 0.0);
                // remove its associated solution
                solution.elementAt(j).removeAllElements();
            }

            // the main function to find the longest path for this iteration
            // NOTE: pheromone states are still conveyed through iterations
            findLongestPath();

            for (int k = 0; k < ants; k++) {
                // for each ant
                int currentSolutionSize = solution.elementAt(k).size();
                // print the solution path
                for (int j = 0; j < currentSolutionSize; j++) {
                    System.out.printf("%d\t", solution.elementAt(k).elementAt(j));
                }
                // print the cost for this solution
                System.out.printf("%f", cost.elementAt(k));
                System.out.printf("\n");
            }
            System.out.printf("PRINTING BEST SOLUTION\n");

            int currentBestSolutionSize = bestSolution.size();

            for (int k = 0; k < currentBestSolutionSize; k++) {
                System.out.printf("%d\t", bestSolution.elementAt(k));
            }
            System.out.printf("\n");
            System.out.printf("cost:%f", bestCost);
            System.out.printf("\n");
            System.out.printf("\n");
        }



        System.out.printf("\n\n");

        // at the end , print the pheromone matrix state
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%f\t", pheromoneMatrix.elementAt(i).elementAt(j));
            }
            System.out.printf("\n");
        }

    }
}
