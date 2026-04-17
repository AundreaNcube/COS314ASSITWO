package ils;
import common.KnapsackInstance;
import common.Solution;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Perturbation
{
    
    private final int k;

    public Perturbation() {
        this.k = 4;     //default perturbation strength
    }

    public Perturbation(int k) {    //k is the total bit flips - must be even and >= 2
        if(k < 2 || k % 2 != 0)
            throw new IllegalArgumentException("k must be an even integer greater than or equal to 2. Instead, got: " + k);
            
        this.k = k;
        
    }

    
    
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
