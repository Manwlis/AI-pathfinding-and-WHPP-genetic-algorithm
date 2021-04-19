# AI-pathfinding-and-WHPP-genetic-algorithm
 
Και τα δύο μέρη έχουν υλοποιηθεί σε java με OpenJDK 11.

## how to run:
### Μέρος Α
compile: javac GridGenerator.java

run: java GridGenerator arguments

πχ: java GridGenerator -i world_examples/easy.world

Θα εμφανιστεί μενού για την επιλογή αλγόριθμου.

Δημιουργείται η εικόνα του κόσμου με και χωρίς το μονοπάτι και στο terminal εμφανίζεται το κόστος του μονοπατιού και του αλγόριθμου.

### Μέρος B
compile: javac WHPP.java

run: java WHPP arguments(6)

πχ: java WHPP 1500 500 0.022 0.5 0.028 0.001

Θα εμφανιστούν μενού για την επιλογή αλγόριθμων.

Εκτυπώνει αριθμό γενιάς, βέλτιστο σκορ και μέσο όρο.
Αφού τελειώσει εκτυπώνει το βέλτιστο χρωμόσωμα της τελευταίας γενιάς μαζί με το feasibility και το σκορ του.
