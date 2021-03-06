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
  
## Installation

### Downloads

You can download a prepackaged version with eclipse-mat-easy plugin already installed : [Windows(x86_64)](https://github.com/qlefevre/eclipse-mat-easy/releases/download/v1.2/MemoryAnalyzer-1.8.1.20180910-win32.win32.x86_64-plugin-1.2.zip) [Linux(x86_64)](https://github.com/qlefevre/eclipse-mat-easy/releases/download/v1.2/MemoryAnalyzer-1.8.1.20180910-linux.gtk.x86_64-plugin-1.2.zip)

### Update site

**Note for Memory Analyzer 1.7.0**: you must disable "Eclipse Oxygen" update site before installing plugins
otherwise your MAT installation will get broken.

To disable Eclipse Oxygen in MAT 1.7.0, perform the following:
1. Open MAT Settings
1. Open `Install/Update`, `Available Software Sites`
1. Uncheck `Eclipse Oxygen`
1. Click `Ok`

Use the following update repository to install the latest released version: http://github.com/qlefevre/eclipse-mat-easy/raw/master/com.github.qlefevre.eclipse.mat.easy.updatesite

To install Eclipse MAT Easy plugin, perform the following:
1. Open `Help`, `Install New Software...`
1. **For MAT 1.7.0**: in case you have not disabled Eclipse Oxygen update site, click `Available Software Sites` and disable Oxygen there
1. Click `Add`, it will open a `Add Repository` window
1. Type `Eclipse MAT Easy update site` to the `Name` field
1. Type `http://github.com/qlefevre/eclipse-mat-easy/raw/master/com.github.qlefevre.eclipse.mat.easy.updatesite` to the `Location` field
1. Click `Ok`
1. All the checkboxes can be left by default (`Show only latest version`, `Group items by category`, ...)
1. Check `Eclipse MAT Easy !` category
1. Click `Next` (Available Software)
1. Click `Next` (Installation Details)
1. Accept License
1. Click `Finish` and restart MAT

The following update site can be used to get development builds: http://github.com/qlefevre/eclipse-mat-easy/raw/master/com.github.qlefevre.eclipse.mat.easy.updatesite

## Supported collection implementations

| Package | Class | Type | Extract Size | Library | 
|----------------------|----------------------|----------|------|---------------|
| ![alt text](/doc/package.gif "package") com.google.common.collect | ![alt text](/doc/class.gif "class") ImmutableList | ![alt text](/doc/list.gif "List") List | ![alt text](/doc/ok.png "ok") **Yes** | Guava |
| ![alt text](/doc/package.gif "package") com.google.common.collect | ![alt text](/doc/class.gif "class") ImmutableSet | ![alt text](/doc/set.gif "Set") Set | ![alt text](/doc/ok.png "ok") **Yes** | Guava |
| ![alt text](/doc/package.gif "package") com.google.common.collect | ![alt text](/doc/class.gif "class") EnumBiMap<br>![alt text](/doc/class.gif "class") EnumHashBiMap<br>![alt text](/doc/class.gif "class") HashBiMap<br> ![alt text](/doc/class.gif "class") ImmutableMap<br> ![alt text](/doc/class.gif "class") ImmutableSortedMap | ![alt text](/doc/map.gif "Map") Map | ![alt text](/doc/ok.png "ok") **Yes** | Guava |
| ![alt text](/doc/package.gif "package") gnu.trove.map.hash | ![alt text](/doc/class.gif "class") THashSet<br> ![alt text](/doc/class.gif "class") TLinkedHashSet | ![alt text](/doc/set.gif "set") Set | ![alt text](/doc/ok.png "ok") **Yes** | Trove4J |
| ![alt text](/doc/package.gif "package") gnu.trove.map.hash | ![alt text](/doc/class.gif "class") THashMap | ![alt text](/doc/map.gif "Map") Map | ![alt text](/doc/ok.png "ok") **Yes** | Trove4J |
| ![alt text](/doc/package.gif "package") java.util | ![alt text](/doc/class.gif "class") ArrayList<br> ![alt text](/doc/class.gif "class") LinkedList<br> ![alt text](/doc/class.gif "class") Arrays$ArrayList | ![alt text](/doc/list.gif "list") List | ![alt text](/doc/ok.png "ok") **Yes** | JDK |
| ![alt text](/doc/package.gif "package") java.util | ![alt text](/doc/class.gif "class") HashSet<br> ![alt text](/doc/class.gif "class") LinkedHashSet<br> ![alt text](/doc/class.gif "class") TreeSet | ![alt text](/doc/set.gif "set") Set | ![alt text](/doc/ok.png "ok") **Yes** | JDK |
| ![alt text](/doc/package.gif "package") java.util.concurrent | ![alt text](/doc/class.gif "class") ConcurrentSkipListSet | ![alt text](/doc/set.gif "set") Set | No | JDK |
| ![alt text](/doc/package.gif "package") java.util | ![alt text](/doc/class.gif "class")HashMap<br> ![alt text](/doc/class.gif "class")IdentityHashMap<br> ![alt text](/doc/class.gif "class")LinkedHashMap<br> ![alt text](/doc/class.gif "class")TreeMap<br> ![alt text](/doc/class.gif "class")WeakHashMap | ![alt text](/doc/map.gif "Map") Map | ![alt text](/doc/ok.png "ok") **Yes** | JDK |
| ![alt text](/doc/package.gif "package") java.util.concurrent | ![alt text](/doc/class.gif "class")ConcurrentHashMap<br> ![alt text](/doc/class.gif "class")ConcurrentSkipListMap | ![alt text](/doc/map.gif "Map") Map |  No | JDK |
| ![alt text](/doc/package.gif "package") org.apache.commons.collections.list<br> ![alt text](/doc/package.gif "package") org.apache.commons.collections4.list | ![alt text](/doc/class.gif "class")CursorableLinkedList<br> ![alt text](/doc/class.gif "class")FixedSizeList<br> ![alt text](/doc/class.gif "class")GrowthList<br> ![alt text](/doc/class.gif "class")LazyList<br> ![alt text](/doc/class.gif "class")NodeCachingLinkedList<br> ![alt text](/doc/class.gif "class")PredicatedList<br> ![alt text](/doc/class.gif "class")SetUniqueList<br> ![alt text](/doc/class.gif "class")TransformedList<br> ![alt text](/doc/class.gif "class")TreeList<br> ![alt text](/doc/class.gif "class")UnmodifiableList  | ![alt text](/doc/list.gif "List") List | ![alt text](/doc/ok.png "ok") **Yes** | Apache<br> Commons<br> Collections |
| ![alt text](/doc/package.gif "package") org.apache.commons.collections.set<br> ![alt text](/doc/package.gif "package") org.apache.commons.collections4.set | ![alt text](/doc/class.gif "class")ListOrderedSet<br> ![alt text](/doc/class.gif "class")Unmodifiable<br> ![alt text](/doc/class.gif "class")UnmodifiableSortedSet | ![alt text](/doc/set.gif "Set") Set | ![alt text](/doc/ok.png "ok") **Yes** | Apache<br> Commons<br> Collections |
| ![alt text](/doc/package.gif "package") org.apache.commons.collections.map<br> ![alt text](/doc/package.gif "package") org.apache.commons.collections4.map | ![alt text](/doc/class.gif "class")DefaultedMap<br> ![alt text](/doc/class.gif "class")FixedSizeMap<br> ![alt text](/doc/class.gif "class")FixedSizeSortedMap<br> ![alt text](/doc/class.gif "class")Flat3Map<br> ![alt text](/doc/class.gif "class")HashedMap<br> ![alt text](/doc/class.gif "class")LazyMap<br> ![alt text](/doc/class.gif "class")LinkedMap<br> ![alt text](/doc/class.gif "class")ListOrderedMap<br> ![alt text](/doc/class.gif "class")LRUMap<br> ![alt text](/doc/class.gif "class")MultiValueMap<br> ![alt text](/doc/class.gif "class")PredicatedMap<br> ![alt text](/doc/class.gif "class")ReferenceIdentityMap<br> ![alt text](/doc/class.gif "class")TransformedMap<br> ![alt text](/doc/class.gif "class")UnmodifiableMap<br> ![alt text](/doc/class.gif "class")UnmodifiableOrderedMap<br> ![alt text](/doc/class.gif "class")UnmodifiableSortedMap | ![alt text](/doc/map.gif "Map") Map | ![alt text](/doc/ok.png "ok") **Yes** | Apache<br> Commons<br> Collections |
| ![alt text](/doc/package.gif "package") org.apache.commons.collections.map<br> ![alt text](/doc/package.gif "package") org.apache.commons.collections4.map | ![alt text](/doc/class.gif "class")StaticBucketMap | ![alt text](/doc/map.gif "Map") Map | No | Apache<br> Commons<br> Collections |

