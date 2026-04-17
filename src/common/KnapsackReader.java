package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class KnapsackReader
{
    // Hardcoded known-optimums based on the intances in the .7z folder provided on Clickup
    private static final Map<String, Integer> KNOWN_OPTIMUMS = new HashMap<>();
    
    static
    {
        KNOWN_OPTIMUMS.put("f1_l-d_kp_10_269", 295);
        KNOWN_OPTIMUMS.put("f2_l-d_kp_20_878", 1024);
        KNOWN_OPTIMUMS.put("f3_l-d_kp_4_20", 35);
        KNOWN_OPTIMUMS.put("f4_l-d_kp_4_11", 23);
        KNOWN_OPTIMUMS.put("f5_l-d_kp_15_375", 481); // decimal inst optimum which will is caled at read time*
        KNOWN_OPTIMUMS.put("f6_l-d_kp_10_60", 52);
        KNOWN_OPTIMUMS.put("f7_l-d_kp_7_50", 107);
        KNOWN_OPTIMUMS.put("f8_l-d_kp_23_10000", 9767);
        KNOWN_OPTIMUMS.put("f9_l-d_kp_5_80", 130);
        KNOWN_OPTIMUMS.put("f10_l-d_kp_20_879", 1025);
        KNOWN_OPTIMUMS.put("knapPI_1_100_1000_1", 9147);
    }
    
    // Returns true if any value/weight token in the file contains a decimal point
    private static boolean hasDecimals(String filePath) throws IOException
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.trim().isEmpty()) continue;
                if (line.contains(".")) return true;
            }
        }
        return false;
    }

    // Reads a single instance file
    public static KnapsackInstance readInstance(String filePath) throws IOException
    {
        // Extract instance name from file path
        Path path = Paths.get(filePath);
        String name = path.getFileName().toString();

        // Detect whether this instance uses decimal values - if so we scale by 1000
        boolean decimal = hasDecimals(filePath);
        int scale = decimal ? 1000 : 1;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read header line
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new IOException("Empty file: " + filePath);
            }
            
            String[] headerParts = headerLine.trim().split("\\s+");
            int numItems = Integer.parseInt(headerParts[0]);
            int capacity = (int) Math.round(Double.parseDouble(headerParts[1]) * scale);
            
            // Initialize arrays
            int[] values = new int[numItems];
            int[] weights = new int[numItems];
            
            // Parse as double first to handle decimal instances ,
            // scale by 1000 for decimal files, leave integer files unchanged
            for (int i = 0; i < numItems; i++)
            {
                String line = reader.readLine();
                if (line == null) {
                    throw new IOException("Unexpected end of file: expected " + numItems + " items, got " + i);
                }
                
                String[] parts = line.trim().split("\\s+");
                values[i]  = (int) Math.round(Double.parseDouble(parts[0]) * scale);
                weights[i] = (int) Math.round(Double.parseDouble(parts[1]) * scale);
            }
            
            // Get known optimum (or -1 if not found)
            // For decimal instances the optimum is also scaled
            int knownOptimum = KNOWN_OPTIMUMS.getOrDefault(name, -1);
            if (knownOptimum == -1) {
                System.err.println("Warning: No known optimum found for instance: " + name);
            } else if (decimal) {
                knownOptimum = knownOptimum * scale;
            }
            
            return new KnapsackInstance(name, capacity, numItems, weights, values, knownOptimum);
        }
    }
    
    // Reads all instances from a directory
    public static KnapsackInstance[] readAll(String dataDirectory) throws IOException
    {
        List<KnapsackInstance> instances = new ArrayList<>();
        
        // if the order needs to change i'll change it and commit
        String[] filenames =
        {
            "f1_l-d_kp_10_269",
            "f2_l-d_kp_20_878",
            "f3_l-d_kp_4_20",
            "f4_l-d_kp_4_11",
            "f5_l-d_kp_15_375",
            "f6_l-d_kp_10_60",
            "f7_l-d_kp_7_50",
            "f8_l-d_kp_23_10000",
            "f9_l-d_kp_5_80",
            "f10_l-d_kp_20_879",
            "knapPI_1_100_1000_1"
        };
        
        for (String filename : filenames)
        {
            String filePath = dataDirectory + filename;
            try
            {
                KnapsackInstance instance = readInstance(filePath);
                instances.add(instance);
                System.out.println("Loaded: " + filename);
            } 
            catch(IOException e)
            {
                System.err.println("Error loading " + filename + ": " + e.getMessage());
                // Re-throw to indicate failure
                throw new IOException("Failed to load instance: " + filename, e);
            }
        }
        
        return instances.toArray(new KnapsackInstance[0]); // this is the array now with the instances
    }
}