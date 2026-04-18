# COS314 Assignment 2

The purpose of this assignment is to compare the effectiveness of a **population-based meta-heuristic (Genetic Algorithm)** and a **trajectory-based meta-heuristic (Iterated Local Search)** on 11 instances of the 0/1 Knapsack Problem.

---

## Team Members
- Member 1 — Amantle Temo
- Member 2 — Aundrea Ncube
- Member 3 — David Kalu

---

## Project Overview

Comparing Genetic Algorithm (GA) and Iterated Local Search (ILS) on 11 Knapsack Problem instances.

---

## File Structure

```
COS314ASSITWO/
├── README.md
├── Makefile
├── report/
│   └── report.pdf
├── data/
│   ├── f1_l-d_kp_10_269
│   ├── f2_l-d_kp_20_878
│   ├── f3_l-d_kp_4_20
│   ├── f4_l-d_kp_4_11
│   ├── f5_l-d_kp_15_375        
│   ├── f6_l-d_kp_10_60
│   ├── f7_l-d_kp_7_50
│   ├── f8_l-d_kp_23_10000
│   ├── f9_l-d_kp_5_80
│   ├── f10_l-d_kp_20_879
│   ├── knapPI_1_100_1000_1
│   └── Known Optimums.xlsx
├── src/
│   ├── Main.java
│   ├── common/
│   │   ├── KnapsackInstance.java
│   │   ├── KnapsackReader.java
│   │   └── Solution.java
│   ├── ga/
│   │   ├── geneticAlgorithm.java
│   │   ├── Selection.java
│   │   ├── Crossover.java
│   │   └── Mutation.java
│   └── ils/
│       ├── iteratedLocalSearch.java
│       ├── LocalSearch.java
│       └── Perturbation.java
├── stats/
│   └── WilcoxonTest.java
├── results/
│   ├── results.csv
│   └── wilcoxon_data.csv
└── out/                       
```

---

## How to Compile and Run

### Using Make

```bash
# Compile
make

# Run (prompts for mode and seed)
make run

# Run Wilcoxon statistical test on existing batch data
make wilcoxon

# Run 30-seed batch then immediately run Wilcoxon test
make batch-wilcoxon

# Clean compiled files
make clean
```

### Manual compilation

```bash
mkdir -p out
javac -d out src/common/*.java src/ga/*.java src/ils/*.java src/Main.java stats/*.java
java -cp out Main
```

### Build and run as JAR

```bash
# Build JAR
jar cfe AAD_Assignment2.jar r Main -C out .

# Run JAR (from root)
java -jar AAD_Assignment2.jar 
```

---

## Program Modes

When you run the program it will prompt:

```
select mode:
1 - run once (enter one seed)
2 - run more than once (30 seeds for Wilcoxon test)
choice:
```

**Mode 1** — Single run. Enter any integer seed. Outputs the results table and saves to `results/results.csv`.

**Mode 2** — Multpile run across 30 fixed seeds. Saves paired GA/ILS data to `results/wilcoxon_data.csv` for statistical analysis.

---

## Output Format

The results table contains:

| Column | Description |
|---|---|
| Instance | Problem instance name |
| Algorithm | GA or ILS |
| Seed | Random seed used |
| BestValue | Best solution value found |
| KnownOptimum | Known optimal value for comparison |
| Runtime(s) | Execution time in seconds |

Results are saved to `results/results.csv`.

---

## Statistical Analysis (Wilcoxon Test)

Run after a batch to perform the one-tailed Wilcoxon Signed-Rank Test at α = 0.05:

```bash
make wilcoxon
```

Two tests are performed:
- **Test 1** — Solution quality: is there a significant difference in best values found?
- **Test 2** — Runtime: is ILS significantly faster than GA?

---

## Data Files

Problem instances are in the `data/` folder. Each file follows this format:

```
n W
v1 w1
v2 w2
...
vn wn
```

Where `n` = number of items, `W` = knapsack capacity, `vi` and `wi` = value and weight of item i.

Instance `f5_l-d_kp_15_375` contains decimal values. The reader auto-detects this and scales all values, weights and capacity by 1000 to preserve integer arithmetic throughout.

---

## Notes

- All randomness is controlled by a seed passed at runtime via `new Random(seed)`.
- Known optimum values are used only for reporting - never inside the algorithm logic.
- The JAR must be run from the project root directory so it can locate the `data/` folder.
- The `out/` directory is created automatically by `make compile`.
