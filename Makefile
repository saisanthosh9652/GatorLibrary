# Initialize class name to variable
GREEDY_CLASS = GatorLibrary

.SUFFIXES: .java .class

.java.class:
	@javac $<

CLASSES = \
	GatorLibrary.java \
	Node.java \
	Book.java \
	MinHeap.java \
	Patron.java \
	RedBlackTree.java

compile:
	javac $(GREEDY_CLASS).java

default: classes

classes: $(CLASSES:.java=.class)

run: classes
	@java $(GREEDY_CLASS) $(arg)
	@$(MAKE) clean

clean:
	@$(RM) *.class

.PHONY: clean compile default
