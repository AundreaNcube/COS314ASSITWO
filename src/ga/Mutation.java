package ga;
import common.*;
import java.util.Random;


public class Mutation
{
/////////////{[][][]}\\\\\\\\\\\\\

    public void mutate(Solution solu, double pm, KnapsackInstance inst, Random rand_gene)
    {
        for (int i = 0; i < inst.numItems; i++)
        {
            if (rand_gene.nextDouble() < pm)
            {
                solu.flipItem(i, inst); // flipItem updates totals incrementally*
            }
        }
    }
}
