# Eclipse MAT Easy!

From my own experience, OutOfMemoryError are often due to a "Collection" instance retaining all objects in Java Heap.
We all have our recipe to deal with OOME. Mine is quite simple, I get the dominator tree view, and I go down all the levels as long as the retained heap is high. And I often found an API Collection instance as *java.util.List*, *java.util.Set* or *java.util.Map*. 

This plugin provides a new view **Collection tree**
![alt text](/doc/collection_tree_view.png "Collection tree view")

You can access it with this new action in toolbar: ![alt text](/doc/collection_tree.gif "Collection tree view")

This view is quite similar to the Dominator Tree ![alt text](/doc/dominator_tree.gif "Dominator tree view")
For each object, you have its size if it's a Collection, retained heap and percentage of total heap used. 

View uses default *IClassSpecificNameResolver* instances to display name as in the Dominator Tree.
However *"Collection"* instances like *java.util.List*, *java.util.Set* or *java.util.Map* are displayed differently to show the refered field in the problematic class. 

![alt text](/doc/collection_tree_view_example.png "HashSet<String> instance example")

In example : You can see that field Collection<String> values in class *com.github.qlefevre.eclipse.mat.test.heapdump.BagB* retains *37.81%* of total heap. Instance *java.util.HashSet<String>* has *63404* elements and retains *8.24 MB*.
  
## Supported collection implementations

| Package | Class | Type | Extract Size | Library | 
|--------|-----------------|-----------------|---------------|---------------| 
| ![alt text](/doc/package.gif "package") java.util | ![alt text](/doc/class.gif "class") ArrayList<br> ![alt text](/doc/class.gif "class") LinkedList<br> ![alt text](/doc/class.gif "class") Arrays$ArrayList | ![alt text](/doc/list.gif "list") List | ![alt text](/doc/ok.png "ok") **Yes** | JDK |
| ![alt text](/doc/package.gif "package") java.util | ![alt text](/doc/class.gif "class") HashSet<br> ![alt text](/doc/class.gif "class") LinkedHashSet<br> ![alt text](/doc/class.gif "class") TreeSet | ![alt text](/doc/set.gif "set") Set | ![alt text](/doc/ok.png "ok") **Yes** | JDK |
| ![alt text](/doc/package.gif "package") java.util.concurrent | ![alt text](/doc/class.gif "class") ConcurrentSkipListSet | ![alt text](/doc/set.gif "set") Set | No | JDK |
| ![alt text](/doc/package.gif "package") java.util | ![alt text](/doc/class.gif "class")HashMap<br> ![alt text](/doc/class.gif "class")IdentityHashMap<br> ![alt text](/doc/class.gif "class")LinkedHashMap<br> ![alt text](/doc/class.gif "class")TreeMap<br> ![alt text](/doc/class.gif "class")WeakHashMap | ![alt text](/doc/map.gif "Map") Map | ![alt text](/doc/ok.png "ok") **Yes** | JDK |
| ![alt text](/doc/package.gif "package") java.util.concurrent | ![alt text](/doc/class.gif "class")ConcurrentHashMap<br> ![alt text](/doc/class.gif "class")ConcurrentSkipListMap | ![alt text](/doc/map.gif "Map") Map |  No | JDK |
| ![alt text](/doc/package.gif "package") org.apache.commons.collections.list<br> ![alt text](/doc/package.gif "package") org.apache.commons.collections4.list | ![alt text](/doc/class.gif "class")CursorableLinkedList<br> ![alt text](/doc/class.gif "class")FixedSizeList<br> ![alt text](/doc/class.gif "class")GrowthList<br> ![alt text](/doc/class.gif "class")LazyList<br> ![alt text](/doc/class.gif "class")NodeCachingLinkedList<br> ![alt text](/doc/class.gif "class")PredicatedList<br> ![alt text](/doc/class.gif "class")SetUniqueList<br> ![alt text](/doc/class.gif "class")TransformedList<br> ![alt text](/doc/class.gif "class")TreeList<br> ![alt text](/doc/class.gif "class")UnmodifiableList  | ![alt text](/doc/list.gif "List") List | ![alt text](/doc/ok.png "ok") **Yes** | Apache Commons Collections |
| ![alt text](/doc/package.gif "package") org.apache.commons.collections.set<br> ![alt text](/doc/package.gif "package") org.apache.commons.collections4.set | ![alt text](/doc/class.gif "class")ListOrderedSet<br> ![alt text](/doc/class.gif "class")Unmodifiable<br> ![alt text](/doc/class.gif "class")UnmodifiableSortedSet | ![alt text](/doc/set.gif "Set") Set | ![alt text](/doc/ok.png "ok") **Yes** | Apache Commons Collections |




