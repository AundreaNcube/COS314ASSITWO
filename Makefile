# Compiler and flags
JAVAC = javac
JAVA = java
FLAGS = -d

# Directories
SRC_DIR = src
STATS_DIR = stats
OUT_DIR = out
DATA_DIR = data
RESULTS_DIR = results

# Source files 
SOURCES = $(SRC_DIR)/common/*.java \
          $(SRC_DIR)/ga/*.java \
          $(SRC_DIR)/ils/*.java \
          $(SRC_DIR)/Main.java

# Default target
all: compile

# Compile everything including stats
compile:
	mkdir -p $(OUT_DIR)
	$(JAVAC) $(FLAGS) $(OUT_DIR) $(SOURCES) $(STATS_DIR)/*.java

# Run the program (single or batch mode)
run: compile
	$(JAVA) -cp $(OUT_DIR) Main

# Run Wilcoxon test on existing batch results
wilcoxon: compile
	$(JAVA) -cp $(OUT_DIR) stats.WilcoxonTest results/wilcoxon_data.csv

# Run batch then immediately run Wilcoxon test
batch-wilcoxon: compile
	@echo "2" | $(JAVA) -cp $(OUT_DIR) Main
	$(JAVA) -cp $(OUT_DIR) stats.WilcoxonTest results/wilcoxon_data.csv

# Clean compiled files only
clean:
	rm -rf $(OUT_DIR)

# Clean everything including results
clean-all: clean
	rm -rf $(RESULTS_DIR)

# Clean and rebuild
rebuild: clean compile

# Run with specific seed (example: make run-seed SEED=42)
run-seed: compile
	@echo "1\n$(SEED)" | $(JAVA) -cp $(OUT_DIR) Main

# Build executable JAR (run from project root where data/ folder exists)
jar: compile
	jar cfe AAD_Assignment2.jar Main -C out .

# Help
help:
	@echo "Available targets:"
	@echo "  make                    - compile"
	@echo "  make run                - compile and run (prompts for mode + seed)"
	@echo "  make wilcoxon           - run Wilcoxon test on existing batch data"
	@echo "  make batch-wilcoxon     - run 30-seed batch then Wilcoxon test"
	@echo "  make jar                - build executable JAR (AAD_Assignment2.jar)"
	@echo "  make clean              - remove compiled classes"
	@echo "  make clean-all          - remove compiled classes and results"
	@echo "  make rebuild            - clean then compile"
	@echo "  make run-seed SEED=42   - single run with specific seed"

.PHONY: all compile run clean clean-all rebuild run-seed wilcoxon batch-wilcoxon jar help
