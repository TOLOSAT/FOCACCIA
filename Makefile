JAVAC = javac
JAVA = java
SRC_DIR = src
BIN_DIR = bin
LIB_DIR = third-parties

SOURCES = $(shell find $(SRC_DIR) -name "*.java")

space:=
space+=
JARS = $(subst $(space),:,$(wildcard $(LIB_DIR)/*.jar))
CLASSPATH = $(JARS):$(BIN_DIR)

.PHONY: all run clean resources compile build build-fat

all: resources compile

resources:
	mkdir -p $(BIN_DIR)
	rsync -av --exclude='*.java' --exclude='*.class' $(SRC_DIR)/ $(BIN_DIR)/

compile:
	mkdir -p $(BIN_DIR)
	$(JAVAC) -cp $(CLASSPATH) -d $(BIN_DIR) -sourcepath $(SRC_DIR) $(SOURCES)

run: all
	$(JAVA) -cp $(CLASSPATH) MainApplication

build: all
	rm -rf $(BIN_DIR)/tmp-fat
	mkdir -p $(BIN_DIR)/tmp-fat
	for jar in $(wildcard $(LIB_DIR)/*.jar); do \
		(cd $(BIN_DIR)/tmp-fat && jar xf ../../$$jar); \
	done
	cp -r $(BIN_DIR)/* $(BIN_DIR)/tmp-fat/
	jar cfm $(BIN_DIR)/focaccia.jar $(SRC_DIR)/MANIFEST.MF -C $(BIN_DIR)/tmp-fat .
	rm -rf $(BIN_DIR)/tmp-fat

clean:
	rm -rf $(BIN_DIR)
