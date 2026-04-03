# COS314 Assignment 2

The purpose of this assignment is to compare the effectiveness of a **population-based meta-heuristic (Genetic Algorithm)** and a **trajectory-based meta-heuristic (Iterated Local Search)** on 10 instances of the 0/1 Knapsack Problem.

---

## Team Members
- Member 1 — TBD (Genetic Algorithm)
- Member 2 — TBD (Iterated Local Search)
- Member 3 — TBD (Infrastructure, Integration & Analysis)

---

## Project Overview
Comparing Genetic Algorithm (GA) and Iterated Local Search (ILS) on 10 Knapsack Problem instances.

---

## File Structure

```bash
cos314-assignment2/
├── README.md
├── report/
│   └── report.pdf
├── data/                  # Extracted from the provided 7z file
│   ├── f1_l-d_kp_10_269
│   ├── f2_l-d_kp_20_878
│   ├── f3_l-d_kp_4_20
│   ├── f4_l-d_kp_4_11
│   ├── f5_l-d_kp_15_375
│   ├── f6_l-d_kp_10_60
│   ├── f7_l-d_kp_7_50
│   ├── f8_l-d_kp_23_10000
│   ├── f9_l-d_kp_5_80
│   └── f10_l-d_kp_20_879
├── src/
│   ├── KnapsackInstance.java
│   ├── Solution.java
│   ├── KnapsackReader.java
│   ├── Main.java
│   ├── ga/
│   │   ├── GeneticAlgorithm.java
│   │   ├── Selection.java
│   │   ├── Crossover.java
│   │   └── Mutation.java
│   └── ils/
│       ├── IteratedLocalSearch.java
│       ├── LocalSearch.java
│       └── Perturbation.java
├── results/
│   └── results_table.csv
└── stats/
    └── WilcoxonTest.java

How to Compile
Bash# From the project root
javac -d out src/*.java src/ga/*.java src/ils/*.java stats/*.java

# Create executable JAR
jar cfe Assignment2.jar Main -C out .
How to Run
Bashjava -jar Assignment2.jar
The program will prompt you for a seed value.

Output Format
The program generates a table with the following columns:

Problem Instance
Algorithm (GA / ILS)
Seed
Best Solution
Known Optimum
Runtime (s)

Results are saved to results/results_table.csv

Data Files
Problem instances are located in the data/ folder. Each file follows this format:
textn W
v1 w1
v2 w2
...
vn wn
Where:

n = number of items
W = knapsack capacity
vi, wi = value and weight of item i


Notes

All randomness is controlled by a single seed passed via new Random(seed).
Known optimum values are used only for reporting, never inside the algorithm logic.
