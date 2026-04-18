# COS314 Assignment 2 — Knapsack Metaheuristics

> Comparing a **Genetic Algorithm** (population-based) against **Iterated Local Search** (trajectory-based) on 11 instances of the 0/1 Knapsack Problem.

---

## Team

| Name | Student Num |
|---|---|
| AKT | u23539764 |
| Aundrea Ncube | u22747363|
| David Kalu | u23534975 |

---

## Quick Start

```bash
# Compile and run
make run

# Build JAR
make jar

# Run JAR (must be in project root)
java -jar AAD_Assignment2.jar
```

When prompted, choose:
- `1` — single run with a seed you enter
- `2` — 30-seed batch run for statistical analysis

---

## Project Structure

```
COS314ASSITWO/
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
├── data/                        # 11 knapsack problem instances
├── results/                     # CSV output 
├── Assignment2Report.pdf
├── Makefile
└── README.md
```

---

## Make Targets

| Command | Description |
|---|---|
| `make` | Compile all sources |
| `make run` | Compile and run |
| `make jar` | Build executable JAR |
| `make wilcoxon` | Run Wilcoxon test on saved batch data |
| `make batch-wilcoxon` | Run 30-seed batch then Wilcoxon test |
| `make run-seed SEED=42` | Single run with a specific seed |
| `make clean` | Remove compiled classes |
| `make clean-all` | Remove compiled classes and results |
| `make rebuild` | Clean then recompile |

---

## Output

Results are printed to the terminal and saved to `results/results.csv`:

| Column | Description |
|---|---|
| Instance | Problem instance name |
| Algorithm | GA or ILS |
| Seed | Seed used for the run |
| BestValue | Best solution value found |
| KnownOptimum | Known optimal value |
| Runtime(s) | Wall-clock time in seconds |

---

## Statistical Analysis

After a batch run, execute the Wilcoxon Signed-Rank Test (one-tailed, α = 0.05):

```bash
make wilcoxon
```

Two tests are reported:
- **Solution quality** - is there a significant difference in best values found?
- **Runtime** - is ILS significantly faster than GA?

Batch data is saved to `results/wilcoxon_data.csv`.

---

## Data Format

Each file in `data/` follows:

```
n W
v1 w1
v2 w2
...
```

Where `n` = items, `W` = capacity, `vi` / `wi` = value and weight per item.

> `f5_l-d_kp_15_375` contains decimal values. The reader auto-detects this and scales all values, weights and capacity by ×1000 to keep integer arithmetic throughout.

---

## Notes

- Seed is requested at runtime - all randomness is reproducible
- Known optimums are used for reporting only, never inside algorithm logic
- The JAR must be run from the project root so it can locate `data/`
