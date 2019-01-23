
all: Helloworld
	java $^


go: Helloworld
	java $^ >test.log
	

Helloworld: Helloworld.class


%.class: %.java
	javac $^

clean:
	rm -rf *.class

