package ga;
import common.*;
import java.util.Random;

public class Selection
{

////////////{:":":":":":":}\\\\\\\\\\\\

    public Solution select(Solution[] pop, int tour_size, KnapsackInstance inst, Random rand_gene) 
    {
        Solution best = null;
        int bestFit = Integer.MIN_VALUE;

        for (int i = 0; i < tour_size; i++)
        {
            int idx = rand_gene.nextInt(pop.length);
            Solution candidate = pop[idx];
            int canidateFit = candidate.fitness(inst);

            if (best == null || canidateFit > bestFit)
            {
                best = candidate;
                bestFit = canidateFit;
            }
        }

        return best;
    }
}
