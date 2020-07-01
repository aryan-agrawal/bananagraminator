# bananagraminator
Tool that creates valid bananagrams style crosswords given a set of letters.

Utilizes a maximizing game tree with a custom heuristic for a BananaBoard class that allows the program to detect and make the moves that lead towards the best possible board. It is also capable of immediately forcing a completed board if moves are detected that will achieve such a result.

I am continuing to update the heuristic algorithms over time to be more effective in guiding the program to a completed board. I am also experimenting with different ways to alter the game tree algorithm to allow for variable search depths based on need, and perhaps sorting the possible move list so that it will be reasonable to break the recursion after a certain number of moves are checked.

Furthermore, expect future versions of the program to allow for the addition of extra characters onto an already completed board, and the capability for the program to make reversals and adjustments of already placed words.

Finally, thanks to Jeff Boulter for the list of words on their website, which can be found here: http://boulter.com/scrabble/words/words.txt. Also, thanks to Luis Von Ahn's research group for posting a collection of resources including a list of profane, unacceptable words on their website, which can be found at cs.cmu.edu. I filtered Boulter's list with the list of unacceptable, terrible words to ensure no awful words made their way anywhere near this program. Thanks again for the resources. I would like to make clear that I am not, and will not profit off of them in any way.

Questions or Concerns? Contact me at agrawalaryanm@gmail.com, or (469) 803-7906.
