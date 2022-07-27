main:
	javac ./codigo_fonte/*.java

docs:
	javadoc ./codigo_fonte/*.java -d documentacao

clean:
	rm ./codigo_fonte/*.class