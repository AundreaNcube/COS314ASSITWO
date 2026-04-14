package common;

//This is the data container that will store the Knapsack Instances
public class KnapsackInstance
{
    public String name;
    public int capacity;
    public int numItems;
    public int[] weights;
    public int[] values;
    public int knownOptimum;
    
    public KnapsackInstance(String name, int capacity, int numItems, int[] weights, int[] values, int knownOptimum)
    {
        this.name = name;
        this.capacity = capacity;
        this.numItems = numItems;
        this.weights = weights;
        this.values = values;
        this.knownOptimum = knownOptimum;
    }
}