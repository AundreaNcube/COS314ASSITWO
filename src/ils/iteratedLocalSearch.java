package ils;

import common.KnapsackInstance;
import common.Solution;
import java.util.Random;

public class iteratedLocalSearch {
    private static int max_non_improving = 500; // stopping criterion of no improvement after 500 iter
    private static final int perturbation_strength = 4; // number of random flips in perturbation

    public Solution run(KnapsackInstance inst, Random rng) {
        LocalSearch ls = new LocalSearch();
        Perturbation prtb = new Perturbation(perturbation_strength);

        Solution initial = greedyConstruct(inst, rng);
        Solution global_best = ls.run(initial, inst);

        int no_improve_cnt = 0;
        while (no_improve_cnt < max_non_improving) {
            Solution perturbed = prtb.perturb(global_best, inst, rng, ls);
            Solution candidate = ls.run(perturbed, inst);

            if (candidate.fitness(inst) > global_best.fitness(inst)) {
                global_best = candidate.copy();
                no_improve_cnt = 0;
            } else {
                no_improve_cnt++;
            }
        }
        return global_best;
    }

    private Solution greedyConstruct(KnapsackInstance inst, Random rng) {
        int num = inst.numItems;

        Integer[] order = new Integer[num];
        double[] ratio = new double[num];

        for (int i = 0; i < num; i++) {
            order[i] = i;
            double w = inst.weights[i];

            double base = w == 0 ? Double.MAX_VALUE : (double) inst.values[i] / w;
            ratio[i] = base + base * 0.01 * rng.nextDouble(); // add small random noise
        }

        java.util.Arrays.sort(order, (a, b) -> Double.compare(ratio[b], ratio[a]));

        Solution sol = new Solution(num);
        for (int i : order) {
            if (sol.totalWeight + inst.weights[i] <= inst.capacity) {
                sol.flipItem(i, inst);
            }
        }
        return sol;
    }

    public static int getMaxNoImprove() {
        return max_non_improving;
    }

    public static int getPerturbationk() {
        return perturbation_strength;
    }

}
