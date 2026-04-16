package ils;

import common.KnapsackInstance;
import common.Solution;

public class LocalSearch {
    
    public Solution run(Solution curr, KnapsackInstance inst)
    {
        Solution sol = curr.copy();

        if(!sol.isValid(inst)){
            repair(sol, inst);
        }

        boolean improved = true;
        while(improved){
            improved = false;

            int bestOne = 0;
            int bestFlip = -1;
            int bestIN = -1;
            int bestOUT = -1;

            for(int i = 0; i< inst.numItems; i++){
            
                sol.flipItem(i, inst);
                if(sol.isValid(inst)){
                    int gain = sol.selected[i] ? inst.values[i] : -inst.values[i];

                    if(gain > bestOne){
                        bestOne = gain;
                        bestFlip = i;
                        bestIN =  -1;
                        bestOUT =  1;
                    }
                }

                sol.flipItem(i, inst); //flip back
            }

            for(int out=0; out<inst.numItems; out++){
                if(!sol.selected[out]) continue;

                for(int in = 0; in<inst.numItems; in++){
                    if(sol.selected[in]) continue;

                    sol.flipItem(out, inst);    //remove swap then add in
                    sol.flipItem(in, inst);

                    if(sol.isValid(inst)){
                        int gain = inst.values[in] - inst.values[out];
                        if(gain > bestOne){
                            bestOne = gain;
                            bestFlip = -1;
                            bestIN = in;
                            bestOUT = out;
                        }
                    }
                    sol.flipItem(in, inst);    //flip back
                    sol.flipItem(out, inst);
                }
            }
            if(bestFlip != -1){
                sol.flipItem(bestFlip, inst);
                improved = true;
            }
            else if(bestIN != -1){
                sol.flipItem(bestOUT, inst);
                sol.flipItem(bestIN, inst);
                improved = true;
            }
        }
        return sol;
    }

    void repair(Solution s, KnapsackInstance inst)
    {

        while(!s.isValid(inst))
        {
            double worstRatio = Double.MAX_VALUE;
            int worstIdx = -1;

            for(int i =0; i< inst.numItems; i++){
                if(s.selected[i]){
                    double w = inst.weights[i];
                    double ratio = (w==0) ? Double.MAX_VALUE : (double) inst.values[i] / w;
                    if(ratio < worstRatio){
                        worstRatio = ratio;
                        worstIdx = i;
                    }
                }
            }
            if(worstIdx == -1) break; //should not necessarily happen, but just in case
            s.flipItem(worstIdx, inst);
        }
    }


}
