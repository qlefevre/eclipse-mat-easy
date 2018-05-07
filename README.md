# Eclipse MAT Easy!

From my own experience, OutOfMemoryError are often due to a "Collection" instance retaining all objects in Java Heap.
We all have our recipe to deal with OOME. Mine is quite simple, I get the dominator tree view, and I go down all the levels as long as the retained heap is high. And I often found an API Collection instance as java.util.List, java.util.Set or java.util.Map. 

This plugin provides a new view **Collection tree**

