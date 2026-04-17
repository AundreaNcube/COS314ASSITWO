import java.io.*;
import java.util.*;
import common.*;

public class Main
{
    // Genetic Algorithm
    private static Solution ga(KnapsackInstance inst, Random rand)
    {
        return new ga.geneticAlgorithm().run(inst, rand);
    }

    // Iterated Local Search
    private static Solution ils(KnapsackInstance instance, Random random)
    {
        return new ils.iteratedLocalSearch().run(instance, random);
    }
    
    //loads the instances from the data folder
    private static void loadInstances(int seed)
    {
        try
        {
            // first load the instances
            KnapsackInstance[] instances = KnapsackReader.readAll("data/");
            System.out.println("Loaded " + instances.length + " instances\n");
            
            // then prepare results storage
            List<String[]> results = new ArrayList<>();
            results.add(new String[]{"Instance", "Algorithm", "Seed", "BestValue", "KnownOptimum", "Runtime(s)"});
            
            //then run the alogrithms
            for (KnapsackInstance instance : instances)
            {
                System.out.println("Processing: " + instance.name);
                System.out.println("Capacity: " + instance.capacity + ", Items: " + instance.numItems);
                
                //Genetics Algorithm here
                Random gaRandom = new Random(seed);
                long gaStart = System.nanoTime();
                Solution gaSolution = ga(instance, gaRandom);
                long gaEnd = System.nanoTime();
                long gaRuntime = (gaEnd - gaStart) / 1_000_000;
                double gaRuntimeSec = gaRuntime / 1000.0;
                
                //Iterated Local Search here
                Random ilsRandom = new Random(seed);
                long ilsStart = System.nanoTime();
                Solution ilsSolution = ils(instance, ilsRandom);
                long ilsEnd = System.nanoTime();
                long ilsRuntime = (ilsEnd - ilsStart) / 1_000_000;
                double ilsRuntimeSec = ilsRuntime / 1000.0;
                
                //Store results
                results.add(new String[]{instance.name, "GA", String.valueOf(seed),String.valueOf(gaSolution.fitness(instance)), 
                String.valueOf(instance.knownOptimum), String.format("%.3f", gaRuntimeSec)});
                results.add(new String[]{instance.name, "ILS", String.valueOf(seed), String.valueOf(ilsSolution.fitness(instance)), 
                String.valueOf(instance.knownOptimum), String.format("%.3f", ilsRuntimeSec)});
                
                System.out.printf("  GA:  %d/%d (runtime: %.3f s)%n",gaSolution.fitness(instance), instance.knownOptimum, gaRuntimeSec);
                System.out.printf("  ILS: %d/%d (runtime: %.3f s)%n%n", ilsSolution.fitness(instance), instance.knownOptimum, ilsRuntimeSec);
            }
            
            //then print the results in a table
            printTable(results);
            
            //then update the CSV
            writeCSV("results/results.csv", results);            
        }
        catch(IOException e)
        {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //print the results in a table format
    private static void printTable(List<String[]> results)
    {
        System.out.println("\n" + "=".repeat(100));
        System.out.printf("%-25s %-10s %-8s %-12s %-12s %-10s%n", 
            "Instance", "Algorithm", "Seed", "BestValue", "KnownOptimum", "Runtime(s)");
        System.out.println("=".repeat(100));
        
        for (int i = 1; i < results.size(); i++) {
            String[] row = results.get(i);
            System.out.printf("%-25s %-10s %-8s %-12s %-12s %-10s%n", 
                row[0], row[1], row[2], row[3], row[4], row[5]);
        }
        System.out.println("=".repeat(100));
    }

    //writes to the csv file
    private static void writeCSV(String filePath, List<String[]> results) throws IOException {
        // create parent directory if it doesn't exist
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String[] row : results) {
                writer.println(String.join(",", row));
            }
        }
    }


    // will run both ga nd ils with multiple seeds and writes out to a csv file so we can have enough data to draw a conclusion
    private static void runMultiSeeds(int[] seeds) throws IOException
    {
        KnapsackInstance[] instances = KnapsackReader.readAll("data/");
        System.out.println("Fetched " + instances.length + " instances for batch run\n");

        List<String[]> batch = new ArrayList<>();
        batch.add(new String[]{"Instance", "Seed", "GA_Value", "ILS_Value", "GA_Runtime(s)", "ILS_Runtime(s)"});

        for (int seed : seeds)
        {
            System.out.println("_______Seed: " + seed + "_________");
            for (KnapsackInstance inst : instances)
            {
                Random gaRng = new Random(seed);
                long gaStart = System.nanoTime();
                Solution gaS = ga(inst, gaRng);
                double gaTime = (System.nanoTime() - gaStart) / 1_000_000_000.0;

                Random ilsRng = new Random(seed);
                long ilsStart = System.nanoTime();
                Solution ilsS = ils(inst, ilsRng);
                double ilsTime = (System.nanoTime() - ilsStart) / 1_000_000_000.0;

                System.out.printf("  %-25s GA=%d  ILS=%d%n", inst.name, gaS.fitness(inst), ilsS.fitness(inst));

                batch.add(new String[]
                {
                    inst.name,
                    String.valueOf(seed),
                    String.valueOf(gaS.fitness(inst)),
                    String.valueOf(ilsS.fitness(inst)),
                    String.format("%.4f", gaTime),
                    String.format("%.4f", ilsTime)
                });
            }
        }

        writeCSV("results/wilcoxon_data.csv", batch);
        System.out.println("\ncomplete.");
    }



    public static void main(String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("select mode:");
        System.out.println(" 1 - run once(enter one seed)");
        System.out.println(" 2 - run more than once(30 seeds for wilcoxon test)");
        System.out.print("choice: ");
        int choice = scanner.nextInt();

        if (choice == 2)
        {
            int[] seeds = 
            {
                233,383,12,34,45,83,293,123,244,22,
                333,445,143,5,42,66,31,90,78,374,
                242,109,127,132,11,3,74,490,471,153
            };
            runMultiSeeds(seeds);
        }
        else
        {
            System.out.print("enter random seed: ");
            int seed = scanner.nextInt();
            loadInstances(seed);
        }
        scanner.close();
    }
}
