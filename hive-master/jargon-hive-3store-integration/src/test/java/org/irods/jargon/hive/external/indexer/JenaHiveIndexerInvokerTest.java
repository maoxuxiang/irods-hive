package org.irods.jargon.hive.external.indexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.CollectionAO;
import org.irods.jargon.core.pub.DataObjectAO;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.query.AVUQueryElement;
import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker.VisitorDesiredAction;
import org.irods.jargon.hive.irods.IRODSHiveServiceImpl;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class JenaHiveIndexerInvokerTest {
	private static Properties testingProperties = new Properties();
	private static org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesLoader = new TestingPropertiesHelper();
		testingProperties = testingPropertiesLoader.getTestProperties();
	}

	@Test
	public void testOnePageCollsOnePageDataObjects() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito
				.mock(IRODSAccessObjectFactory.class);
		JenaHiveIndexerVisitor visitor = Mockito
				.mock(JenaHiveIndexerVisitor.class);
		Mockito.when(
				visitor.invoke(Matchers.any(MetaDataAndDomainData.class),
						Matchers.any(JenaHiveIndexerInvoker.class)))
				.thenReturn(VisitorDesiredAction.CONTINUE);

		List<MetaDataAndDomainData> collectionMetadata = new ArrayList<MetaDataAndDomainData>();
		MetaDataAndDomainData collectionMetadataValue = MetaDataAndDomainData
				.instance(MetadataDomain.COLLECTION, "1", "/a/collection", 0,
						"http://www.fao.org/aos/agrovoc#c_49830", "blah",
						"blah");
		collectionMetadataValue.setCount(1);
		collectionMetadataValue.setLastResult(true);
		collectionMetadata.add(collectionMetadataValue);
		CollectionAO collectionAO = Mockito.mock(CollectionAO.class);
		List<AVUQueryElement> query = IRODSHiveServiceImpl
				.buildQueryToFindHiveMetadata();
		Mockito.when(collectionAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(collectionMetadata);

		List<MetaDataAndDomainData> dataObjectMetadata = new ArrayList<MetaDataAndDomainData>();
		MetaDataAndDomainData dataObjectMetadataValue = MetaDataAndDomainData
				.instance(MetadataDomain.DATA, "1", "/a/collection", 0,
						"http://www.fao.org/aos/agrovoc#c_49830", "blah",
						"blah");
		dataObjectMetadataValue.setCount(1);
		dataObjectMetadataValue.setLastResult(true);
		dataObjectMetadata.add(dataObjectMetadataValue);
		DataObjectAO dataObjectAO = Mockito.mock(DataObjectAO.class);

		Mockito.when(dataObjectAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(dataObjectMetadata);

		Mockito.when(irodsAccessObjectFactory.getDataObjectAO(irodsAccount))
				.thenReturn(dataObjectAO);
		Mockito.when(irodsAccessObjectFactory.getCollectionAO(irodsAccount))
				.thenReturn(collectionAO);

		JenaHiveIndexerInvoker invoker = new JenaHiveIndexerInvoker(
				irodsAccessObjectFactory, irodsAccount, visitor);
		invoker.execute();

		Assert.assertEquals(1, invoker.getCollectionsProcessed());
		Assert.assertEquals(1, invoker.getDataObjectsProcessed());

	}

	@Test
	public void testTwoPageCollsOnePageDataObjects() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito
				.mock(IRODSAccessObjectFactory.class);
		JenaHiveIndexerVisitor visitor = Mockito
				.mock(JenaHiveIndexerVisitor.class);
		Mockito.when(
				visitor.invoke(Matchers.any(MetaDataAndDomainData.class),
						Matchers.any(JenaHiveIndexerInvoker.class)))
				.thenReturn(VisitorDesiredAction.CONTINUE);

		List<MetaDataAndDomainData> collectionMetadata = new ArrayList<MetaDataAndDomainData>();
		MetaDataAndDomainData collectionMetadataValue = MetaDataAndDomainData
				.instance(MetadataDomain.COLLECTION, "1", "/a/collection", 0,
						"http://www.fao.org/aos/agrovoc#c_49830", "blah",
						"blah");
		collectionMetadataValue.setCount(1);
		collectionMetadataValue.setLastResult(false);
		collectionMetadata.add(collectionMetadataValue);
		CollectionAO collectionAO = Mockito.mock(CollectionAO.class);
		List<AVUQueryElement> query = IRODSHiveServiceImpl
				.buildQueryToFindHiveMetadata();
		Mockito.when(collectionAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(collectionMetadata);

		collectionMetadata = new ArrayList<MetaDataAndDomainData>();
		collectionMetadataValue = MetaDataAndDomainData.instance(
				MetadataDomain.COLLECTION, "1", "/a/collection", 0,
				"http://www.fao.org/aos/agrovoc#c_49830", "blah", "blah");
		collectionMetadataValue.setCount(2);
		collectionMetadataValue.setLastResult(true);
		collectionMetadata.add(collectionMetadataValue);
		Mockito.when(collectionAO.findMetadataValuesByMetadataQuery(query, 1))
				.thenReturn(collectionMetadata);

		List<MetaDataAndDomainData> dataObjectMetadata = new ArrayList<MetaDataAndDomainData>();
		MetaDataAndDomainData dataObjectMetadataValue = MetaDataAndDomainData
				.instance(MetadataDomain.DATA, "1", "/a/collection", 0,
						"http://www.fao.org/aos/agrovoc#c_49830", "blah",
						"blah");
		dataObjectMetadataValue.setCount(1);
		dataObjectMetadataValue.setLastResult(true);
		dataObjectMetadata.add(dataObjectMetadataValue);
		DataObjectAO dataObjectAO = Mockito.mock(DataObjectAO.class);

		Mockito.when(dataObjectAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(dataObjectMetadata);

		Mockito.when(irodsAccessObjectFactory.getDataObjectAO(irodsAccount))
				.thenReturn(dataObjectAO);
		Mockito.when(irodsAccessObjectFactory.getCollectionAO(irodsAccount))
				.thenReturn(collectionAO);

		JenaHiveIndexerInvoker invoker = new JenaHiveIndexerInvoker(
				irodsAccessObjectFactory, irodsAccount, visitor);
		invoker.execute();

		Assert.assertEquals(2, invoker.getCollectionsProcessed());
		Assert.assertEquals(1, invoker.getDataObjectsProcessed());

	}

	@Test
	public void testTwoPageCollsThreePageDataObjects() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito
				.mock(IRODSAccessObjectFactory.class);
		JenaHiveIndexerVisitor visitor = Mockito
				.mock(JenaHiveIndexerVisitor.class);
		Mockito.when(
				visitor.invoke(Matchers.any(MetaDataAndDomainData.class),
						Matchers.any(JenaHiveIndexerInvoker.class)))
				.thenReturn(VisitorDesiredAction.CONTINUE);

		List<MetaDataAndDomainData> collectionMetadata = new ArrayList<MetaDataAndDomainData>();
		MetaDataAndDomainData collectionMetadataValue = MetaDataAndDomainData
				.instance(MetadataDomain.COLLECTION, "1", "/a/collection", 0,
						"http://www.fao.org/aos/agrovoc#c_49830", "blah",
						"blah");
		collectionMetadataValue.setCount(1);
		collectionMetadataValue.setLastResult(false);
		collectionMetadata.add(collectionMetadataValue);
		CollectionAO collectionAO = Mockito.mock(CollectionAO.class);
		List<AVUQueryElement> query = IRODSHiveServiceImpl
				.buildQueryToFindHiveMetadata();
		Mockito.when(collectionAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(collectionMetadata);

		collectionMetadata = new ArrayList<MetaDataAndDomainData>();
		collectionMetadataValue = MetaDataAndDomainData.instance(
				MetadataDomain.COLLECTION, "1", "/a/collection", 0,
				"http://www.fao.org/aos/agrovoc#c_49830", "blah", "blah");
		collectionMetadataValue.setCount(2);
		collectionMetadataValue.setLastResult(true);
		collectionMetadata.add(collectionMetadataValue);
		Mockito.when(collectionAO.findMetadataValuesByMetadataQuery(query, 1))
				.thenReturn(collectionMetadata);

		List<MetaDataAndDomainData> dataObjectMetadata = new ArrayList<MetaDataAndDomainData>();
		MetaDataAndDomainData dataObjectMetadataValue = MetaDataAndDomainData
				.instance(MetadataDomain.DATA, "1", "/a/collection", 0,
						"http://www.fao.org/aos/agrovoc#c_49830", "blah",
						"blah");
		dataObjectMetadataValue.setCount(1);
		dataObjectMetadataValue.setLastResult(false);
		dataObjectMetadata.add(dataObjectMetadataValue);
		DataObjectAO dataObjectAO = Mockito.mock(DataObjectAO.class);

		Mockito.when(dataObjectAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(dataObjectMetadata);

		dataObjectMetadata = new ArrayList<MetaDataAndDomainData>();
		dataObjectMetadataValue = MetaDataAndDomainData.instance(
				MetadataDomain.DATA, "1", "/a/collection", 0,
				"http://www.fao.org/aos/agrovoc#c_49830", "blah", "blah");
		dataObjectMetadataValue.setCount(2);
		dataObjectMetadataValue.setLastResult(false);
		dataObjectMetadata.add(dataObjectMetadataValue);

		Mockito.when(dataObjectAO.findMetadataValuesByMetadataQuery(query, 1))
				.thenReturn(dataObjectMetadata);

		dataObjectMetadata = new ArrayList<MetaDataAndDomainData>();
		dataObjectMetadataValue = MetaDataAndDomainData.instance(
				MetadataDomain.DATA, "1", "/a/collection", 0,
				"http://www.fao.org/aos/agrovoc#c_49830", "blah", "blah");
		dataObjectMetadataValue.setCount(3);
		dataObjectMetadataValue.setLastResult(true);
		dataObjectMetadata.add(dataObjectMetadataValue);

		Mockito.when(dataObjectAO.findMetadataValuesByMetadataQuery(query, 2))
				.thenReturn(dataObjectMetadata);

		Mockito.when(irodsAccessObjectFactory.getDataObjectAO(irodsAccount))
				.thenReturn(dataObjectAO);
		Mockito.when(irodsAccessObjectFactory.getCollectionAO(irodsAccount))
				.thenReturn(collectionAO);

		JenaHiveIndexerInvoker invoker = new JenaHiveIndexerInvoker(
				irodsAccessObjectFactory, irodsAccount, visitor);
		invoker.execute();

		Assert.assertEquals(2, invoker.getCollectionsProcessed());
		Assert.assertEquals(3, invoker.getDataObjectsProcessed());

	}

	@Test
	public void testOnePageCollsNoDataObjects() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito
				.mock(IRODSAccessObjectFactory.class);
		JenaHiveIndexerVisitor visitor = Mockito
				.mock(JenaHiveIndexerVisitor.class);
		Mockito.when(
				visitor.invoke(Matchers.any(MetaDataAndDomainData.class),
						Matchers.any(JenaHiveIndexerInvoker.class)))
				.thenReturn(VisitorDesiredAction.CONTINUE);

		List<MetaDataAndDomainData> collectionMetadata = new ArrayList<MetaDataAndDomainData>();
		MetaDataAndDomainData collectionMetadataValue = MetaDataAndDomainData
				.instance(MetadataDomain.COLLECTION, "1", "/a/collection", 0,
						"http://www.fao.org/aos/agrovoc#c_49830", "blah",
						"blah");
		collectionMetadataValue.setCount(1);
		collectionMetadataValue.setLastResult(true);
		collectionMetadata.add(collectionMetadataValue);
		CollectionAO collectionAO = Mockito.mock(CollectionAO.class);
		List<AVUQueryElement> query = IRODSHiveServiceImpl
				.buildQueryToFindHiveMetadata();
		Mockito.when(collectionAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(collectionMetadata);

		List<MetaDataAndDomainData> dataObjectMetadata = new ArrayList<MetaDataAndDomainData>();
		DataObjectAO dataObjectAO = Mockito.mock(DataObjectAO.class);

		Mockito.when(dataObjectAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(dataObjectMetadata);

		Mockito.when(irodsAccessObjectFactory.getDataObjectAO(irodsAccount))
				.thenReturn(dataObjectAO);
		Mockito.when(irodsAccessObjectFactory.getCollectionAO(irodsAccount))
				.thenReturn(collectionAO);

		JenaHiveIndexerInvoker invoker = new JenaHiveIndexerInvoker(
				irodsAccessObjectFactory, irodsAccount, visitor);
		invoker.execute();

		Assert.assertEquals(1, invoker.getCollectionsProcessed());
		Assert.assertEquals(0, invoker.getDataObjectsProcessed());

	}

	@Test
	public void testNoCollsOnePageDataObjects() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito
				.mock(IRODSAccessObjectFactory.class);
		JenaHiveIndexerVisitor visitor = Mockito
				.mock(JenaHiveIndexerVisitor.class);
		Mockito.when(
				visitor.invoke(Matchers.any(MetaDataAndDomainData.class),
						Matchers.any(JenaHiveIndexerInvoker.class)))
				.thenReturn(VisitorDesiredAction.CONTINUE);

		List<MetaDataAndDomainData> collectionMetadata = new ArrayList<MetaDataAndDomainData>();

		CollectionAO collectionAO = Mockito.mock(CollectionAO.class);
		List<AVUQueryElement> query = IRODSHiveServiceImpl
				.buildQueryToFindHiveMetadata();
		Mockito.when(collectionAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(collectionMetadata);

		List<MetaDataAndDomainData> dataObjectMetadata = new ArrayList<MetaDataAndDomainData>();
		MetaDataAndDomainData dataObjectMetadataValue = MetaDataAndDomainData
				.instance(MetadataDomain.DATA, "1", "/a/collection", 0,
						"http://www.fao.org/aos/agrovoc#c_49830", "blah",
						"blah");
		dataObjectMetadataValue.setCount(1);
		dataObjectMetadataValue.setLastResult(true);
		dataObjectMetadata.add(dataObjectMetadataValue);
		DataObjectAO dataObjectAO = Mockito.mock(DataObjectAO.class);

		Mockito.when(dataObjectAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(dataObjectMetadata);

		Mockito.when(irodsAccessObjectFactory.getDataObjectAO(irodsAccount))
				.thenReturn(dataObjectAO);
		Mockito.when(irodsAccessObjectFactory.getCollectionAO(irodsAccount))
				.thenReturn(collectionAO);

		JenaHiveIndexerInvoker invoker = new JenaHiveIndexerInvoker(
				irodsAccessObjectFactory, irodsAccount, visitor);
		invoker.execute();

		Assert.assertEquals(0, invoker.getCollectionsProcessed());
		Assert.assertEquals(1, invoker.getDataObjectsProcessed());

	}

	@Test
	public void testNoCollsNoDataObjects() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito
				.mock(IRODSAccessObjectFactory.class);
		JenaHiveIndexerVisitor visitor = Mockito
				.mock(JenaHiveIndexerVisitor.class);
		Mockito.when(
				visitor.invoke(Matchers.any(MetaDataAndDomainData.class),
						Matchers.any(JenaHiveIndexerInvoker.class)))
				.thenReturn(VisitorDesiredAction.CONTINUE);

		List<MetaDataAndDomainData> collectionMetadata = new ArrayList<MetaDataAndDomainData>();

		CollectionAO collectionAO = Mockito.mock(CollectionAO.class);
		List<AVUQueryElement> query = IRODSHiveServiceImpl
				.buildQueryToFindHiveMetadata();
		Mockito.when(collectionAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(collectionMetadata);

		List<MetaDataAndDomainData> dataObjectMetadata = new ArrayList<MetaDataAndDomainData>();

		DataObjectAO dataObjectAO = Mockito.mock(DataObjectAO.class);

		Mockito.when(dataObjectAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(dataObjectMetadata);

		Mockito.when(irodsAccessObjectFactory.getDataObjectAO(irodsAccount))
				.thenReturn(dataObjectAO);
		Mockito.when(irodsAccessObjectFactory.getCollectionAO(irodsAccount))
				.thenReturn(collectionAO);

		JenaHiveIndexerInvoker invoker = new JenaHiveIndexerInvoker(
				irodsAccessObjectFactory, irodsAccount, visitor);
		invoker.execute();

		Assert.assertEquals(0, invoker.getCollectionsProcessed());
		Assert.assertEquals(0, invoker.getDataObjectsProcessed());

	}
}
