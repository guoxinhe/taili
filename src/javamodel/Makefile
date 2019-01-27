
all: DongziYear

x: DongziYear
	java $^

go: DongziYear
	java $^ >test.log
	
DongziYear: DongziYear.class

%.class: %.java
	javac $^

clean:
	rm -rf *.class
