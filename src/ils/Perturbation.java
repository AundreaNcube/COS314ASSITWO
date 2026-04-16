package ils;
import common.KnapsackInstance;
import common.Solution;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Perturbation class for Iterated Local Search (ILS) algorithm.
 * This class provides a method to perturb a given solution by flipping the selection of a certain number of items.
 * The strength of the perturbation is determined by the k, which specifies how many items to flip.
 * Strategy — random remove-then-add perturbation:
 *   1. Randomly remove k/2 currently included items.
 *   2. Randomly add    k/2 currently excluded items.
 *   3. Repair if the result is infeasible (overweight).
 */

public class Perturbation {
    
    private final int k;

    public Perturbation() {
        this.k = 4;     //default perturbation strength
    }

    public Perturbation(int k) {    //k is the total bit flips - must be even and >= 2
        if(k < 2 || k % 2 != 0)
            throw new IllegalArgumentException("k must be an even integer greater than or equal to 2. Instead, got: " + k);
            
        this.k = k;
        
    }

    /**
     * 
     * @param curr the current best solution to perturb
     * @param instance the knapsack problem instance
     * @param rng   seeded Random instance shared across the ILS run
     * @param ls    helper to repair 
     */
    public Solution perturb(Solution curr, KnapsackInstance instance, Random rng, LocalSearch ls) {
        Solution perturbed = curr.copy();
        ArrayList<Integer> included = new ArrayList<>();
        ArrayList<Integer> excluded = new ArrayList<>();

        for(int i =  0; i< instance.numItems; i++){
            if(perturbed.selected[i]){
                included.add(i);
            } else {
                excluded.add(i);
            }
        }

        Collections.shuffle(included, rng);
        Collections.shuffle(excluded, rng);

        int removeCnt = Math.min(k / 2, included.size());
        for(int i =0; i < removeCnt; i++){
            perturbed.flipItem(included.get(i), instance);
        }

        int addCnt = Math.min(k / 2, excluded.size());
        for(int i =0; i < addCnt; i++){
            perturbed.flipItem(excluded.get(i), instance);
        }

        if(!perturbed.isValid(instance)){
            ls.repair(perturbed, instance);
        }
        return perturbed;
    }

    public int getK() { //return perturbation strength  k
        return k;
    }
}
