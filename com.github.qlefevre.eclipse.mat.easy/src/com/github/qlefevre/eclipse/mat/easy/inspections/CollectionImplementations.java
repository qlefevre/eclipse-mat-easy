/*******************************************************************************
 * Copyright (c) 2018 Quentin Lefèvre and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.github.qlefevre.eclipse.mat.easy.inspections;

public class CollectionImplementations {

	public static final String JAVA_UTIL_ARRAYLIST = "java.util.ArrayList";
	public static final String JAVA_UTIL_LINKEDLIST = "java.util.LinkedList";
	public static final String JAVA_UTIL_ARRAYS_ARRAYLIST = "java.util.Arrays$ArrayList";

	public static final String JAVA_UTIL_HASHMAP = "java.util.HashMap";
	public static final String JAVA_UTIL_TREEMAP = "java.util.TreeMap";
	public static final String JAVA_UTIL_LINKEDHASHMAP = "java.util.LinkedHashMap";
	public static final String JAVA_UTIL_WEAKHASHMAP = "java.util.WeakHashMap";
	public static final String JAVA_UTIL_IDENTITYHASHMAP = "java.util.IdentityHashMap";
	public static final String JAVA_UTIL_CONCURRENT_CONCURRENTHASHMAP = "java.util.concurrent.ConcurrentHashMap";
	public static final String JAVA_UTIL_CONCURRENT_CONCURRENTSKIPLISTMAP = "java.util.concurrent.ConcurrentSkipListMap";

	public static final String JAVA_UTIL_HASHSET = "java.util.HashSet";
	public static final String JAVA_UTIL_TREESET = "java.util.TreeSet";
	public static final String JAVA_UTIL_LINKEDHASHSET = "java.util.LinkedHashSet";
	public static final String JAVA_UTIL_CONCURRENT_CONCURRENTSKIPLISTSET = "java.util.concurrent.ConcurrentSkipListSet";

	public static final String ORG_APACHE_COMMONS_COLLECTIONS_LIST_CURSORABLELINKEDLIST = "org.apache.commons.collections.list.CursorableLinkedList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_LIST_NODECACHINGLINKEDLIST = "org.apache.commons.collections.list.NodeCachingLinkedList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_LIST_TREELIST = "org.apache.commons.collections.list.TreeList";

	public static final String ORG_APACHE_COMMONS_COLLECTIONS_LIST_GROWTHLIST = "org.apache.commons.collections.list.GrowthList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_LIST_FIXEDSIZELIST = "org.apache.commons.collections.list.FixedSizeList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_LIST_LAZYLIST = "org.apache.commons.collections.list.LazyList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_LIST_SETUNIQUELIST = "org.apache.commons.collections.list.SetUniqueList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_LIST_UNMODIFIABLELIST = "org.apache.commons.collections.list.UnmodifiableList";

	public static final String ORG_APACHE_COMMONS_COLLECTIONS_SET_LISTORDEREDSET = "org.apache.commons.collections.set.ListOrderedSet";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_SET_UNMODIFIABLESET = "org.apache.commons.collections.set.UnmodifiableSet";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_SET_UNMODIFIABLESORTEDSET = "org.apache.commons.collections.set.UnmodifiableSortedSet";

	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_FLAT3MAP = "org.apache.commons.collections.map.Flat3Map";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_LRUMAP = "org.apache.commons.collections.map.LRUMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_REFERENCEIDENTITYMAP = "org.apache.commons.collections.map.ReferenceIdentityMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_STATICBUCKETMAP = "org.apache.commons.collections.map.StaticBucketMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_HASHEDMAP = "org.apache.commons.collections.map.HashedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_LINKEDMAP = "org.apache.commons.collections.map.LinkedMap";

	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_DEFAULTEDMAP = "org.apache.commons.collections.map.DefaultedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_FIXEDSIZEMAP = "org.apache.commons.collections.map.FixedSizeMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_LAZYMAP = "org.apache.commons.collections.map.LazyMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_LISTORDEREDMAP = "org.apache.commons.collections.map.ListOrderedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_MULTIVALUEMAP = "org.apache.commons.collections.map.MultiValueMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_UNMODIFIABLEMAP = "org.apache.commons.collections.map.UnmodifiableMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_FIXEDSIZESORTEDMAP = "org.apache.commons.collections.map.FixedSizeSortedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_UNMODIFIABLESORTEDMAP = "org.apache.commons.collections.map.UnmodifiableSortedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_UNMODIFIABLEORDEREDMAP = "org.apache.commons.collections.map.UnmodifiableOrderedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_TRANSFORMEDMAP = "org.apache.commons.collections.map.TransformedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS_MAP_PREDICATEDMAP = "org.apache.commons.collections.map.PredicatedMap";

	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_LIST_CURSORABLELINKEDLIST = "org.apache.commons.collections4.list.CursorableLinkedList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_LIST_NODECACHINGLINKEDLIST = "org.apache.commons.collections4.list.NodeCachingLinkedList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_LIST_TREELIST = "org.apache.commons.collections4.list.TreeList";

	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_LIST_GROWTHLIST = "org.apache.commons.collections4.list.GrowthList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_LIST_FIXEDSIZELIST = "org.apache.commons.collections4.list.FixedSizeList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_LIST_LAZYLIST = "org.apache.commons.collections4.list.LazyList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_LIST_SETUNIQUELIST = "org.apache.commons.collections4.list.SetUniqueList";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_LIST_UNMODIFIABLELIST = "org.apache.commons.collections4.list.UnmodifiableList";

	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_SET_LISTORDEREDSET = "org.apache.commons.collections4.set.ListOrderedSet";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_SET_UNMODIFIABLESET = "org.apache.commons.collections4.set.UnmodifiableSet";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_SET_UNMODIFIABLESORTEDSET = "org.apache.commons.collections4.set.UnmodifiableSortedSet";

	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_FLAT3MAP = "org.apache.commons.collections4.map.Flat3Map";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_LRUMAP = "org.apache.commons.collections4.map.LRUMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_REFERENCEIDENTITYMAP = "org.apache.commons.collections4.map.ReferenceIdentityMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_STATICBUCKETMAP = "org.apache.commons.collections4.map.StaticBucketMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_HASHEDMAP = "org.apache.commons.collections4.map.HashedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_LINKEDMAP = "org.apache.commons.collections4.map.LinkedMap";

	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_DEFAULTEDMAP = "org.apache.commons.collections4.map.DefaultedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_FIXEDSIZEMAP = "org.apache.commons.collections4.map.FixedSizeMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_LAZYMAP = "org.apache.commons.collections4.map.LazyMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_LISTORDEREDMAP = "org.apache.commons.collections4.map.ListOrderedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_MULTIVALUEMAP = "org.apache.commons.collections4.map.MultiValueMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_UNMODIFIABLEMAP = "org.apache.commons.collections4.map.UnmodifiableMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_FIXEDSIZESORTEDMAP = "org.apache.commons.collections4.map.FixedSizeSortedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_UNMODIFIABLESORTEDMAP = "org.apache.commons.collections4.map.UnmodifiableSortedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_UNMODIFIABLEORDEREDMAP = "org.apache.commons.collections4.map.UnmodifiableOrderedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_TRANSFORMEDMAP = "org.apache.commons.collections4.map.TransformedMap";
	public static final String ORG_APACHE_COMMONS_COLLECTIONS4_MAP_PREDICATEDMAP = "org.apache.commons.collections4.map.PredicatedMap";

}
