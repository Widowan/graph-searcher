## Java graph searcher
Made for Deeplay 2022 internship, this repo contains solution
for [the task](https://internship.deeplay.io/java) ([pdf (ru)](https://www.pdfhost.net/index.php?Action=Download&File=5b07136bc671440f4752574013a000f2)).

What's here:
* Required `Solution` class
* Abstract graph structure (Graph <- WeightedGraph <- IntGraph)
* Multiple types of vertices (Vertex <- IntVertex)
* **Multiple searchers (namely A\* and Dijkstra)**
* Unit-tests (93% line coverage 😎)
* Input parser that parser command line arguments
* Fully JavaDoc documented code
* **Additional task:** parse graph data from file - from config.properties 
  that bundled with jar or one next to the jar, if such exists

What's not here:
* Quick-and-dirty ugly code :)
