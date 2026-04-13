package ga;
import common.*;
import java.util.Random;


public class Mutation
{
/////////////{[][][]}\\\\\\\\\\\\\

    public void mutate(Solution solu, double pm,KnapsackInstance inst, Random rng)
    {
        for (int i = 0; i < inst.numItems; i++)
        {
            if (rng.nextDouble() < pm)
            {
                solu.flipItem(i, inst);
            }
        }
        solu.recalculateTotals(inst);
    }
}
