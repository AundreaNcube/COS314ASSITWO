# Compiler and flags
JAVAC = javac
JAVA = java
FLAGS = -d

# Directories
SRC_DIR = src
OUT_DIR = out
DATA_DIR = data
RESULTS_DIR = results

# Source files (includes common package)
SOURCES = $(SRC_DIR)/common/*.java \
          $(SRC_DIR)/ga/*.java \
          $(SRC_DIR)/ils/*.java \
          $(SRC_DIR)/Main.java

# Default target
all: compile

# Compile everything
compile:
	mkdir -p $(OUT_DIR)
	$(JAVAC) $(FLAGS) $(OUT_DIR) $(SOURCES)

# Run the program
run: compile
	$(JAVA) -cp $(OUT_DIR) Main

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
	@echo "$(SEED)" | $(JAVA) -cp $(OUT_DIR) Main

# Help
help:
	@echo "Available targets:"
	@echo "  make          - compile"
	@echo "  make run      - compile and run"
	@echo "  make clean    - remove compiled classes"
	@echo "  make clean-all- remove compiled classes and results"
	@echo "  make rebuild  - clean then compile"
	@echo "  make run-seed SEED=42 - run with a specific seed"

.PHONY: all compile run clean clean-all rebuild run-seed help