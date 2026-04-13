import java.io.*;
import java.util.*;
import common.*;
import ga.geneticAlgorithm;

public class Main
{
    // Greedy placeholder algorithm (by value-to-weight ratio)
    // this one is the one like the manual grid method... very greedy !
    private static Solution greedyPlaceholder(KnapsackInstance instance, Random random)
    {
        Solution solution = new Solution(instance.numItems);
        
        // Create list of indices sorted by value/weight ratio (descending)
        Integer[] indices = new Integer[instance.numItems];
        for (int i = 0; i < instance.numItems; i++) {
            indices[i] = i;
        }
        
        Arrays.sort(indices, (a, b) -> {
            double ratioA = (double) instance.values[a] / instance.weights[a];
            double ratioB = (double) instance.values[b] / instance.weights[b];
            return Double.compare(ratioB, ratioA);
        });
        
        // Greedy selection
        for (int i : indices) {
            if (solution.totalWeight + instance.weights[i] <= instance.capacity) {
                solution.selected[i] = true;
                solution.totalValue += instance.values[i];
                solution.totalWeight += instance.weights[i];
            }
        }
        
        return solution;
    }
    
    // Genetic Algorithm
    private static Solution ga(KnapsackInstance inst, Random rand)
    {
        return new ga.geneticAlgorithm().run(inst, rand);
    }
    
    // Placeholder for ILS (just calls greedy for now while I await AKT & Andrea to finish GA)
    private static Solution ils(KnapsackInstance instance, Random random)
    {
        return greedyPlaceholder(instance, random);
    }

    // I containerized the reading input so main is easy to follow
    private static int readUserInput()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter random seed: ");
        int seed = scanner.nextInt();
        scanner.close();
        return seed;
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
            results.add(new String[]{"Instance", "Algorithm", "Seed", "BestValue", "KnownOptimum", "Runtime(ms)"});
            
            //then run the alogrithms
            for (KnapsackInstance instance : instances)
            {
                System.out.println("Processing: " + instance.name);
                System.out.println("  Capacity: " + instance.capacity + ", Items: " + instance.numItems);
                
                //Genetics Algorithm here
                Random gaRandom = new Random(seed);
                long gaStart = System.nanoTime();
                Solution gaSolution = ga(instance, gaRandom);
                long gaEnd = System.nanoTime();
                long gaRuntime = (gaEnd - gaStart) / 1_000_000; // Convert to milliseconds
                
                //Iterated Local Search here
                Random ilsRandom = new Random(seed);
                long ilsStart = System.nanoTime();
                Solution ilsSolution = ils(instance, ilsRandom);
                long ilsEnd = System.nanoTime();
                long ilsRuntime = (ilsEnd - ilsStart) / 1_000_000;
                
                //Store results
                results.add(new String[]{instance.name, "GA", String.valueOf(seed), 
                    String.valueOf(gaSolution.fitness(instance)), String.valueOf(instance.knownOptimum), String.valueOf(gaRuntime)});
                results.add(new String[]{instance.name, "ILS", String.valueOf(seed), 
                    String.valueOf(ilsSolution.fitness(instance)), String.valueOf(instance.knownOptimum), String.valueOf(ilsRuntime)});
                
                System.out.printf("  GA: %d/%d (runtime: %d ms)%n", 
                    gaSolution.fitness(instance), instance.knownOptimum, gaRuntime);
                System.out.printf("  ILS: %d/%d (runtime: %d ms)%n%n", 
                    ilsSolution.fitness(instance), instance.knownOptimum, ilsRuntime);
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
            "Instance", "Algorithm", "Seed", "BestValue", "KnownOptimum", "Runtime(ms)");
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
    public static void main(String[] args)
    {
        boolean straightRun = true; //change this to false if you want to manually add the seed value
        int seed = 100;

        if(straightRun == true)
        {
            loadInstances(seed);
        }
        else
        {
            seed = readUserInput();
            loadInstances(seed);
        }        
    }
}
