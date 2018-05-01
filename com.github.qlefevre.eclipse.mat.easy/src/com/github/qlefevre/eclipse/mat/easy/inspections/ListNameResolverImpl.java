/*
 * Copyright 2018  Quentin Lefèvre
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.qlefevre.eclipse.mat.easy.inspections;

import static com.github.qlefevre.eclipse.mat.easy.inspections.ListImplementations.JAVA_UTIL_ARRAYLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.ListImplementations.JAVA_UTIL_ARRAYS_ARRAYLIST;
import static com.github.qlefevre.eclipse.mat.easy.inspections.ListImplementations.JAVA_UTIL_LINKEDLIST;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.query.BytesFormat;
import org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;
/**
 * @author Quentin
 *
 */
@Subjects(value={JAVA_UTIL_ARRAYLIST,
		JAVA_UTIL_LINKEDLIST,
		JAVA_UTIL_ARRAYS_ARRAYLIST})
public class ListNameResolverImpl implements IClassSpecificNameResolver {

	private final CollectionHeapResolverRegistry collectionHeapResolver;
	/**
	 * Default constructor
	 */
	public ListNameResolverImpl() {
		collectionHeapResolver = CollectionHeapResolverRegistry.getInstance();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver#resolve(org.eclipse.mat.snapshot.model.IObject)
	 */
	public String resolve(IObject object) throws SnapshotException {
		StringBuffer stringBuffer = new StringBuffer(100);
		getCollectionHeapSize(object,stringBuffer);
		getCollectionSize(object,stringBuffer);
		getSourceCodeReference(object, stringBuffer);
		return stringBuffer.toString();
	}
	
	private void getCollectionHeapSize(IObject object, StringBuffer stringBuffer) throws SnapshotException {
		long heap =collectionHeapResolver.getCollectionHeapSize(object);
		stringBuffer.append("Heap: ");
		stringBuffer.append(BytesFormat.getInstance().format(heap));
	}
	
	private void getCollectionSize(IObject object, StringBuffer stringBuffer) throws SnapshotException {
		stringBuffer.append(" Size: ");
		int resolvedSize = collectionHeapResolver.getCollectionSize(object);
		if(resolvedSize >= 0) {
			stringBuffer.append(resolvedSize);
		}else {
			stringBuffer.append("?");
		}
	}
	
	private void getSourceCodeReference(IObject object, StringBuffer stringBuffer) throws SnapshotException {
		stringBuffer.append(" Source: List<Object> ");
		String referenceName = "?";
		referenceName = collectionHeapResolver.getSourceCodeReference(object);
		stringBuffer.append(referenceName);
	}

}
