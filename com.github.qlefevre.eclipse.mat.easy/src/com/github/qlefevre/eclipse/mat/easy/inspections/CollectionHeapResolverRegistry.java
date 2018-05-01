/**
 * 
 */
package com.github.qlefevre.eclipse.mat.easy.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.model.IObject;

import com.github.qlefevre.eclipse.mat.easy.extension.ICollectionHeapResolver;

/**
 * @author Quentin
 *
 */
public final class CollectionHeapResolverRegistry implements ICollectionHeapResolver{
	
	public static final byte TYPE_OBJECT = 0;
	public static final byte TYPE_LIST = 1;
	public static final byte TYPE_SET = 2;
	public static final byte TYPE_MAP = 3;
	public static final byte TYPE_ARRAY = 4;

	private static CollectionHeapResolverRegistry instance;
	
	private CollectionHeapResolverRegistry() {
	}
	
	public static synchronized CollectionHeapResolverRegistry getInstance() {
		if(instance == null) {
			instance = new CollectionHeapResolverRegistry();
		}
		return instance;
	}
	
	@Override
	public long getCollectionHeapSize(IObject object) throws SnapshotException {
		// TODO Auto-generated method stub
		if(object == null)
			return 0;
		if(isCollection(object)) {
		return new ListCollectionHeapResolver().getCollectionHeapSize(object);
		}else {
			return object.getRetainedHeapSize();
		}
	}

	@Override
	public int getCollectionSize(IObject object) throws SnapshotException {
		// TODO Auto-generated method stub
		if(object == null)
			return -1;
		if(!isCollection(object))
			return -1;
		return new ListCollectionHeapResolver().getCollectionSize(object);
	}

	@Override
	public String getSourceCodeReference(IObject object) throws SnapshotException {
		// TODO Auto-generated method stub
		if(object == null) {
			return "";
		}
		return new ListCollectionHeapResolver().getSourceCodeReference(object);
	}
	
	public String getDisplayName(IObject object) {
		return object.getClazz().getName();
	}
	
	public byte getType(IObject object) {
		if(object == null)
			return TYPE_OBJECT;
		if(object.getClazz().isArrayType())
			return TYPE_ARRAY;
		String classname = object.getClazz().getName().toLowerCase();
		if(classname.contains("list")) {
			return TYPE_LIST;
		}
		return TYPE_OBJECT;
	}
	
	private boolean isCollection(IObject object) {
		try {
			String classname = object.getClazz().getName().toLowerCase();
			return classname.contains("list") && !classname.contains("$");
		}catch(Exception vEx) {
			return false;
		}
	}

}
