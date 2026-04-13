package ga;
import common.*;
import java.util.Random;

//single-point****
public class Crossover
{

////////////{akt was here}\\\\\\\\\\\\\

    public Solution[] crossover(Solution p1, Solution p2, double pc, KnapsackInstance instance, Random rng)
    {
        /// not enough items to perform c_o
        if (instance.numItems <= 1)
        {
            return new Solution[]{p1.copy(), p2.copy()};
        }

        // skip c_o if rand draw >= pc
        if (rng.nextDouble() >= pc)
        {
            return new Solution[]{p1.copy(), p2.copy()};
        }

        //choose crossover point in range 1 -> numItems-1
        int cp = 1 + rng.nextInt(instance.numItems - 1);

        Solution offspring1 = new Solution(instance.numItems);
        Solution offspring2 = new Solution(instance.numItems);

        for (int i = 0; i < instance.numItems; i++)
        {
            offspring1.selected[i] = (i < cp) ? p1.selected[i] : p2.selected[i];
            offspring2.selected[i] = (i < cp) ? p2.selected[i] : p1.selected[i];
        }

        offspring1.recalculateTotals(instance);
        offspring2.recalculateTotals(instance);

        return new Solution[]{offspring1, offspring2};
    }
}
