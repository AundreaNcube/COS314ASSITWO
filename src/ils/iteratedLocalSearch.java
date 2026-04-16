package ils;
import common.Solution;
import java.util.Random;

public class iteratedLocalSearch {
    private static int max_non_improving = 500; //stopping criterion of no improvement after 500 iter
    private static final int perturbation_strength = 4; //number of random flips in perturbation

    public Solution run(KnapsackInstance inst, Random rng) {
        LocalSearch ls = new LocalSearch();
        Perturbation prtb = new Perturbation(perturbation_strength);
    }

    private Solution greedyConstruct(KnapsackInstance inst, Random rng)
    {

    }

    public static int getMaxNoImprove(){
        return max_non_improving;
    }

    public static int getPerturbationk(){
        return perturbation_strength;
    }

}
