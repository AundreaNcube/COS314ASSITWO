# Compiler and flags
JAVAC = javac
JAVA = java
FLAGS = -d

# Directories
SRC_DIR = src
OUT_DIR = out
DATA_DIR = data
RESULTS_DIR = results

# Source files
SOURCES = $(SRC_DIR)/KnapsackInstance.java \
          $(SRC_DIR)/Solution.java \
          $(SRC_DIR)/KnapsackReader.java \
          $(SRC_DIR)/Main.java \
          $(SRC_DIR)/ga/*.java \
          $(SRC_DIR)/ils/*.java

# Class files (not needed explicitly, but useful for clean)
CLASSES = $(OUT_DIR)/*.class $(OUT_DIR)/ga/*.class $(OUT_DIR)/ils/*.class

# Default target
all: compile

# Compile everything
compile:
	mkdir -p $(OUT_DIR)
	$(JAVAC) $(FLAGS) $(OUT_DIR) $(SOURCES)

# Run the program
run: compile
	$(JAVA) -cp $(OUT_DIR) Main

# Clean compiled files
clean:
	rm -rf $(OUT_DIR)
	rm -rf $(RESULTS_DIR)

# Clean and rebuild
rebuild: clean compile

# Create results directory
results-dir:
	mkdir -p $(RESULTS_DIR)

# Run with specific seed (example: make run-seed SEED=42)
run-seed: compile
	@echo "$(SEED)" | $(JAVA) -cp $(OUT_DIR) Main

.PHONY: all compile run clean rebuild help run-seed results-dir