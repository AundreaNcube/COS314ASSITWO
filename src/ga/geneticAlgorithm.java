package ga;
import common.*;
import java.util.ArrayList;
import java.util.Random;


public class geneticAlgorithm
{

    private static final int    pop_size = 100,  max_gen = 500, tour_size = 3;
    private static final double  pc = 0.8;


///////////{::}\\\\\\\\\\\\\
    public Solution run(KnapsackInstance inst, Random rng) 
    {
        double pm = 1.0 / inst.numItems;
        Selection slctr = new Selection();
        Crossover crossover = new Crossover();
        Mutation mut = new Mutation();


        Solution[] pop = init(inst, rng);
        Solution globalBest = findBest(pop, inst).copy();

        for (int gen = 0; gen < max_gen; gen++)
        {
            Solution best = findBest(pop, inst).copy();
            if (best.fitness(inst) > globalBest.fitness(inst))
            {
                globalBest = best.copy();
            }

            Solution[] nextGen = new Solution[pop_size];
            nextGen[0] = best;

            int i = 1;
            while (i < pop_size)
            {
                Solution p1 = slctr.select(pop, tour_size, inst, rng);
                Solution p2 = slctr.select(pop, tour_size, inst, rng);
                Solution[] offspring = crossover.crossover(p1, p2, pc, inst, rng);

                for (Solution child : offspring)
                {
                    mut.mutate(child, pm, inst, rng);

                    if (!child.isValid(inst))
                    {
                        repair(child, inst, rng);
                    }

                    if (i < pop_size)
                    {
                        nextGen[i++] = child;
                    }
                }
            }

            pop = nextGen;
        }


        
        Solution lastBest = findBest(pop, inst);

        if (lastBest.fitness(inst) > globalBest.fitness(inst))
        {
            globalBest = lastBest.copy();
        }


        return globalBest;
    }

   

/////////{___________}\\\\\\\\\\\\\\\\

    private Solution[] init(KnapsackInstance inst, Random rng)
    {
        Solution[] pop = new Solution[pop_size];

        pop[0] = greedyInit(inst);
        //random
        for (int s = 1; s < pop_size; s++)
        {
            Solution solu = new Solution(inst.numItems);
            for (int i = 0; i < inst.numItems; i++)
            {
                solu.selected[i] = rng.nextBoolean();
            }

            solu.recalculateTotals(inst);

            if (!solu.isValid(inst))
            {
                repair(solu, inst, rng);
            }
            pop[s] = solu;
        }

        return pop;
    }

    private Solution greedyInit(KnapsackInstance inst)
    {
        Integer[] order = new Integer[inst.numItems];
        for (int i = 0; i < inst.numItems; i++) order[i] = i;

        java.util.Arrays.sort(order, (a, b) -> {
            double ra = inst.weights[a] == 0 ? Double.MAX_VALUE : (double) inst.values[a] / inst.weights[a];
            double rb = inst.weights[b] == 0 ? Double.MAX_VALUE : (double) inst.values[b] / inst.weights[b];
            return Double.compare(rb, ra);
        });

        Solution sol = new Solution(inst.numItems);
        for (int i : order)
        {
            if (sol.totalWeight + inst.weights[i] <= inst.capacity)
            {
                sol.flipItem(i, inst);
            }
        }
        return sol;
    }


///////////{::::::}\\\\\\\\\\\\\\\\  

    private void repair(Solution solu, KnapsackInstance inst, Random rng)
    {
    
        ArrayList<Integer> idxSelected = new ArrayList<>();
        for (int i = 0; i < inst.numItems; i++)
        {
            if (solu.selected[i])
            {
                idxSelected.add(i);
            }
        }

        //// this is whats called the Fisher-Yates shuffle *
        for (int i = idxSelected.size() - 1; i > 0; i--)
        {
            int j = rng.nextInt(i + 1);
            int tmp = idxSelected.get(i);
            idxSelected.set(i, idxSelected.get(j));
            idxSelected.set(j, tmp);
        }

        // lastly we deselect items until feasible
        for (int idx : idxSelected)
        {
            if (solu.isValid(inst))
            {
                break;
            }
            solu.flipItem(idx, inst);
        }
        
    }


    
///////////////{""""""""}\\\\\\\\\\\\\\\

    private Solution findBest(Solution[] pop, KnapsackInstance inst)
    {
        Solution best = pop[0];
        int bestFit = best.fitness(inst);

        for (int i = 1; i < pop.length; i++)
        {
            int f = pop[i].fitness(inst);
            if (f > bestFit)
            {
                best = pop[i];
                bestFit = f;
            }
        }

        return best;
    }
}
