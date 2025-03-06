JAVAC = javac
JAVA = java
SRC_DIR = src
BIN_DIR = bin

# Trouver tous les fichiers .java dans src/
SOURCES = $(shell find $(SRC_DIR) -name "*.java")
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

.PHONY: all run clean

all: $(CLASSES)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	mkdir -p $(dir $@)
	$(JAVAC) -d $(BIN_DIR) -sourcepath $(SRC_DIR) $(SOURCES)

run: all
	$(JAVA) -cp $(BIN_DIR) TapasDebugger

clean:
	rm -rf $(BIN_DIR)
