JAVAC = javac
JAVA = java
SRC_DIR = src
BIN_DIR = bin
LIB_DIR = third-parties

# Trouver tous les fichiers .java dans src/
SOURCES = $(shell find $(SRC_DIR) -name "*.java")
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Classpath = tous les JARs de third-parties + dossier bin
space:=
space+=
JARS = $(subst $(space),:,$(wildcard $(LIB_DIR)/*.jar))
CLASSPATH = $(JARS):$(BIN_DIR)

.PHONY: all run clean

all: $(CLASSES)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	mkdir -p $(dir $@)
	$(JAVAC) -cp $(CLASSPATH) -d $(BIN_DIR) -sourcepath $(SRC_DIR) $(SOURCES)

run: all
	$(JAVA) -cp $(CLASSPATH) TapasDebugger

clean:
	rm -rf $(BIN_DIR)
