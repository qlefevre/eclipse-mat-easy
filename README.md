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

In example : You can see that field Collection<String> values in class com.github.qlefevre.eclipse.mat.test.heapdump.BagB retains 37.81% of total heap. Instance HashSet<String> has 63404 elements and retains 8.24 MB.
