# missionary-crossing

This project attempts to solve the "Missionaries &amp; Cannibals" problem using a brute force approach.

Basically all possible moves are tried, working moves are pushed onto the solution stack.  When a dead-end occurs the last frame is popped off the stack.

At the end of the program, if a solution is found, the solution is cleaned up to remove loops (going back to a puzzle
state that we've been in before).

### Building

```
mvn clean package
```

### Running

```
java -jar target/missionary-crossing-1.0-SNAPSHOT.jar
```
