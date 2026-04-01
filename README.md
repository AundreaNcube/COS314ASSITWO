# COS314ASSITWO
The purpose of this AI assignment two is to compare the effectiveness of a population-based meta-heuristic (Genetic Algorithm) and a trajectory-based meta-heuristic (Iterated Local Search) to solve a provided problem instances.
---

## Team Members
- Member 1 — tbd(Genetic Algorithm)
- Member 2 — tbd(Iterated Local Search)
- Member 3 — tbd(Infrastructure, Integration & Analysis)

---

## Project Overview

Comparing a population-based metaheuristic (Genetic Algorithm) and a trajectory-based metaheuristic (Iterated Local Search) on 10 instances of the 0/1 Knapsack Problem.

---

## File Structure

cos314-assignment2/
│
├── README.md
├── report/
│ └── report.pdf
│
├── data/ -extracted from the 7z file
│ ├── f1_l-d_kp_10_269
│ ├── f2_l-d_kp_20_878
│ ├── f3_l-d_kp_4_20
│ ├── f4_l-d_kp_4_11
│ ├── f5_l-d_kp_15_375
│ ├── f6_l-d_kp_10_60
│ ├── f7_l-d_kp_7_50
│ ├── f8_l-d_kp_23_10000
│ ├── f9_l-d_kp_5_80
│ └── f10_l-d_kp_20_879
│
├── src/
│ ├── KnapsackInstance.java
│ ├── Solution.java
│ ├── KnapsackReader.java
│ ├── Main.java
│ │
│ ├── ga/
│ │ ├── GeneticAlgorithm.java
│ │ ├── Selection.java
│ │ ├── Crossover.java
│ │ └── Mutation.java
│ │
│ └── ils/
│ ├── IteratedLocalSearch.java
│ ├── LocalSearch.java
│ └── Perturbation.java
│
├── results/
│ └── results_table.csv
│
└── stats/
└── WilcoxonTest.java


---

## How to Compile
# From the project root, compile all .java files
javac -d out src/*.java src/ga/*.java src/ils/*.java src/stats/*.java

# Package into a JAR
jar cfe Assignment2.jar Main -C out .
How to Run
java -jar Assignment2.jar

The program will prompt:

Enter seed value: 

Enter any integer seed. The program will then run both GA and ILS on all 10 problem instances and output results to the console and to results/results_table.csv.

Output Format
Problem Instance	Algorithm	Seed	Best Solution	Known Optimum	Runtime (s)
f1_l-d_kp_10_269	GA	...	...	...	...
f1_l-d_kp_10_269	ILS	...	...	...	...
...	...	...	...	...	...
Data Files

Problem instances are located in the data/ folder. Each file follows the format:

n W
v1 w1
v2 w2
...
vn wn

Where n = number of items, W = knapsack capacity, and each subsequent line is a value-weight pair.
---
All randomness is seeded via new Random(seed) passed into each algorithm — no algorithm creates its own Random instance internally
The known optimum values are used only for reporting in the results table, never inside the algorithm logic
Tested on Java 17+
