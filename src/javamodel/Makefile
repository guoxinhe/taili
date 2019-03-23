
PROJ=DongziYear
LOG=test.log

all: $(PROJ)

x: $(PROJ)
	java $^

go: $(PROJ)
	java $^ >$(LOG)

$(PROJ): $(PROJ).class


%.class: %.java
	javac $^

clean:
	rm -rf *.class
