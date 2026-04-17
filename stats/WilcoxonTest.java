package stats;
import java.io.*;
import java.util.*;


public class WilcoxonTest
{
    public static void main(String[] args) throws IOException
    {
        String csvPath = (args.length > 0) ? args[0] : "results/wilcoxon_data.csv";
        System.out.println("reading: " + csvPath);
        System.out.println();


        // load paired dat
        Map<String,List<Double>> gaValByInst = new LinkedHashMap<>();
        Map<String,List<Double>> ilsValByInst = new LinkedHashMap<>();
        Map<String,List<Double>> gaRtByInst = new LinkedHashMap<>();
        Map<String,List<Double>> ilsRtByInst = new LinkedHashMap<>();

        List<Double> gaAllVal = new ArrayList<>();
        List<Double> ilsAllVal = new ArrayList<>();
        List<Double> gaAllRt = new ArrayList<>();
        List<Double> ilsAllRt = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(csvPath)))
        {
            String header = br.readLine();
            if (header == null) { System.err.println("empty file."); return; }

            String line;
            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                String inst = parts[0].trim();
                double gaVal = Double.parseDouble(parts[2].trim());
                double ilsVal = Double.parseDouble(parts[3].trim());
                double gaRt = Double.parseDouble(parts[4].trim());
                double ilsRt = Double.parseDouble(parts[5].trim());

                gaValByInst.computeIfAbsent(inst, k -> new ArrayList<>()).add(gaVal);
                ilsValByInst.computeIfAbsent(inst, k -> new ArrayList<>()).add(ilsVal);
                gaRtByInst.computeIfAbsent(inst, k -> new ArrayList<>()).add(gaRt);
                ilsRtByInst.computeIfAbsent(inst, k -> new ArrayList<>()).add(ilsRt);

                gaAllVal.add(gaVal);  ilsAllVal.add(ilsVal);
                gaAllRt .add(gaRt);   ilsAllRt .add(ilsRt);
            }
        }


        // as per instance summary -> solution quality*
        System.out.println("=".repeat(90));
        System.out.printf("%-30s %10s %10s %8s %10s %10s %8s%n","Instance", "GA_val", "ILS_val", "ValDiff", "GA_rt(s)", "ILS_rt(s)", "RtDiff");
        System.out.println("=".repeat(90));
        for (String inst : gaValByInst.keySet())
        {
            double gaMeanVal= mean(gaValByInst.get(inst));
            double ilsMeanVal = mean(ilsValByInst.get(inst));
            double gaMeanRt = mean(gaRtByInst.get(inst));
            double ilsMeanRt = mean(ilsRtByInst.get(inst));
            System.out.printf("%-30s %10.1f %10.1f %8.1f %10.4f %10.4f %8.4f%n", inst, gaMeanVal, ilsMeanVal, ilsMeanVal - gaMeanVal,
            gaMeanRt, ilsMeanRt, ilsMeanRt - gaMeanRt);
        }
        System.out.println("=".repeat(90));
        System.out.println();


        //////// {test 1-> solution quality }\\\\\\\\\\\\

        System.out.println("______TEST 1:  SOLUTION QUALITY________");
        System.out.println("H0: GA and ILS find equivalent solution values");
        System.out.println("H1: ILS finds better values than GA"); // one-tailed, alpha=0.05
        System.out.println();
        runWilcoxon(gaAllVal, ilsAllVal, "GA", "ILS");

        System.out.println();
        System.out.println("_".repeat(90));
        System.out.println();

        /////////{ test 2 -> runtime }\\\\\\\\\\\

        System.out.println("_______TEST 2 - RUNTIME_________");
        System.out.println("H0: GA and ILS have equivalent runtimes");
        System.out.println("H1: ILS is faster than GA");
        System.out.println();

        //// note : w+  means ga runtime > ils runtime basically confirming ils is faster
        runWilcoxon(ilsAllRt, gaAllRt, "ILS", "GA");
    }


    
    public static void runWilcoxon(List<Double> listA, List<Double> listB, String nameA, String nameB)
    {
        if (listA.size() != listB.size())
        {
            System.err.println("Lists must be the same length.");
            return;
        }

        int n = listA.size();

        //// cal differences d_i = B - A
        double[] diff = new double[n];
        for (int i = 0; i < n; i++)
            diff[i] = listB.get(i) - listA.get(i);

        //// remov zero diffs
        List<Double> nonZero = new ArrayList<>();
        for (double d : diff)
            if (d != 0.0) nonZero.add(d);

        int m = nonZero.size();
        System.out.println("Total pairs: " + n);
        System.out.println("Non-zero diffs: " + m);

        if (m == 0)
        {
            System.out.println("All differences are zero both GA and ILS perform identically.");
            System.out.println("We do not reject H0.");
            return;
        }


        //// rank by abs difference then will be taking the average rank in the cases that theres ties
        double[] absDiff = new double[m];
        for (int i = 0; i < m; i++) absDiff[i] = Math.abs(nonZero.get(i));

        Integer[] idx = new Integer[m];
        for (int i = 0; i < m; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> Double.compare(absDiff[a], absDiff[b]));

        double[] ranks = new double[m];
        int i = 0;
        while (i < m)
        {
            int j = i;
            while (j < m && absDiff[idx[j]] == absDiff[idx[i]]) j++; // finding those ties
            double avgRank = (i + 1 + j) / 2.0;
            for (int k = i; k < j; k++) ranks[idx[k]] = avgRank;
            i = j;
        }


        //// sum pos nd neg ranks
        double Wplus  = 0, Wminus = 0;
        for (int k = 0; k < m; k++)
        {
            if (nonZero.get(k) > 0) Wplus  += ranks[k];
            else Wminus += ranks[k];
        }

        System.out.printf("W+ (%s better): %.1f%n", nameB, Wplus);
        System.out.printf("W- (%s better): %.1f%n", nameA, Wminus);


        ///// test stat which is the minimum between wplus and minus
        double T = Math.min(Wplus, Wminus);
        System.out.printf("T (test stat): %.1f%n", T);


        //// normal approx  true/holds for m >= 10

        // mean and var  of W under null hyp.
        double mu = m * (m + 1) / 4.0;
        double sigma = Math.sqrt(m * (m + 1) * (2 * m + 1) / 24.0);
        double z = (Wplus - mu) / sigma; // since its one-tailed ,testing Wplus is large

        System.out.printf("z-score: %.4f%n", z);

        // critical z ->  alpha=0.05 which is 1.645 (i miss doing stats i wont lie lol)
        double zCritical = 1.645;
        System.out.printf("z-critical: %.3f%n", zCritical);
        System.out.println();

        if (m < 10)
        {
            System.out.println("Note: m < 10 leads to normal approximation not being unreliable."); //need to use exact wilcoxon table for small samples
        }

        if (z > zCritical)
        {
            System.out.println("RESULT: REJECT H0");
            System.out.printf("z=%.4f > %.3f - %s is significantly faster than %s (p < 0.05, one-tailed)%n",z, zCritical, nameA, nameB);
        }
        else
        {
            System.out.println("RESULT: FAIL TO REJECT H0");
            System.out.printf("z=%.4f <= %.3f - No significant difference between %s and %s%n",z, zCritical, nameA, nameB);
        }
    }

    private static double mean(List<Double> vals)
    {
        double sum = 0;
        for (double v : vals) sum += v;
        return sum / vals.size();
    }
}
