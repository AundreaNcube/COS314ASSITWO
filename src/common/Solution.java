package common;

public class Solution
{
    public boolean[] selected;
    public int totalValue;
    public int totalWeight;
    
    public Solution(int numItems)
    {
        this.selected = new boolean[numItems];
        this.totalValue = 0;
        this.totalWeight = 0;
    }
    
    private Solution(boolean[] selected, int totalValue, int totalWeight)
    {
        this.selected = selected.clone();
        this.totalValue = totalValue;
        this.totalWeight = totalWeight;
    }
    
    public void recalculateTotals(KnapsackInstance instance)
    {
        totalValue = 0;
        totalWeight = 0;

        for (int i = 0; i < instance.numItems; i++)
        {
            if (selected[i])
            {
                totalValue  += instance.values[i];
                totalWeight += instance.weights[i];
            }
        }
    }
    
    // true if solution respects capacity constraint, false otherwise
    public boolean isValid(KnapsackInstance instance)
    {
        boolean valid;

        if(totalWeight <= instance.capacity)
        {
            valid = true;
        }
        else
        {
            valid = false;
        }
        return valid;
    }
    
    public int fitness(KnapsackInstance instance)
    {
        int fitness;

        if(isValid(instance) == true)
        {
            fitness = totalValue;
        }
        else
        {
            fitness = 0;
        }

        return fitness;
    }
    
    // Creates a deep copy of this solution, as taught in C++ :)
    public Solution copy()
    {
        boolean[] selectedCopy = new boolean[this.selected.length];

        for (int i=0; i<this.selected.length; i++)
        {
            selectedCopy[i] = this.selected[i];
        }
        
        Solution copy = new Solution(selectedCopy, totalValue, totalWeight);
        return copy;
    }
    
    // Toggles item i and updates totals incrementally
    // so in other words, if the item is in the bag it gets removed
    // otherwise if the item is not in the bag it adds it
    public void flipItem(int i, KnapsackInstance instance)
    {
        if (selected[i]) 
            {
            // Remove item
            totalValue -= instance.values[i];
            totalWeight -= instance.weights[i];
        } else {
            // Add item
            totalValue += instance.values[i];
            totalWeight += instance.weights[i];
        }
        selected[i] = !selected[i];
    }
}