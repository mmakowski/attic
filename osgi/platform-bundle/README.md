Running (in Apache Felix):

1. build: `mvn package` -- this will produce a jar in target dir
2. start felix: `java -jar bin/felix.jar`
3. start the bundle: `start file:/path/to/jar/in/target/dir`

