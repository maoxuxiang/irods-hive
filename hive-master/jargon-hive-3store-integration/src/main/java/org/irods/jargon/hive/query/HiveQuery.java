package org.irods.jargon.hive.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains a query for IRODS data using HIVE vocabulary terms.  This can be turned into a SPARQL query against the triple store.
 * 
 * @author Le
 *
 */
public class HiveQuery implements Serializable  {

	/**
	 * The absolute path (blank if starting at root) that will be used to constrain the query
	 */
	private String irodsParentCollectionAbsolutePath = "";
	
	/**
	 * Constrain results to return data objects, collections, or both
	 * @author Mike
	 *
	 */
	public enum ObjectTypeEnum {DATA_OBJECT_ONLY, COLLECTION_ONLY, BOTH}
	
	private ObjectTypeEnum objectTypeConstraint;
	
	private List<HiveQueryVocabularyItem> hiveQueryVocabularyItems;
	
	public HiveQuery(){
		hiveQueryVocabularyItems = new ArrayList<HiveQueryVocabularyItem>();
		objectTypeConstraint = ObjectTypeEnum.BOTH;
	}

	public String getIrodsParentCollectionAbsolutePath() {
		return irodsParentCollectionAbsolutePath;
	}

	public void setIrodsParentCollectionAbsolutePath(
			String irodsParentCollectionAbsolutePath) {
		this.irodsParentCollectionAbsolutePath = irodsParentCollectionAbsolutePath;
	}

	public ObjectTypeEnum getObjectTypeConstraint() {
		return objectTypeConstraint;
	}

	public void setObjectTypeConstraint(ObjectTypeEnum objectTypeConstraint) {
		this.objectTypeConstraint = objectTypeConstraint;
	}

	public List<HiveQueryVocabularyItem> getHiveQueryVocabularyItems() {
		return hiveQueryVocabularyItems;
	}

	public void setHiveQueryVocabularyItems(
			List<HiveQueryVocabularyItem> hiveQueryVocabularyItems) {
		this.hiveQueryVocabularyItems = hiveQueryVocabularyItems;
	}
	
	public void addVocabularyItems(HiveQueryVocabularyItem vi) {
		if(vi != null) {
			hiveQueryVocabularyItems.add(vi);
		}
	}
	
	
	
	
}
