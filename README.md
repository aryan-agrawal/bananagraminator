# bananagraminator
Tool that creates valid bananagrams style crosswords given a set of letters.

To use, compile and run with arguments for side length of the resultant board and a list of letters, separated only by spaces.

Format: ```$java Main [side length] [letters]```

Ex:     ```$java Main 31 "a b a c d e e f g g h i j k l m n o o p p q r r r s t t u u v w x y z"```

Utilizes a maximizing game tree with a custom heuristic for a BananaBoard class that allows the program to detect and make the moves that lead towards the best possible board. It is also capable of immediately forcing a completed board if moves are detected that will achieve such a result.
