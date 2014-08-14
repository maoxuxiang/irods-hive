package org.irods.jargon.hive.external.indexer.unittest;

import org.irods.jargon.hive.external.indexer.JenaHiveIndexerInvokerTest;
import org.irods.jargon.hive.external.indexer.JenaHiveIndexerServiceImplTest;
import org.irods.jargon.hive.external.indexer.JenaHiveIndexerVisitorTest;
import org.irods.jargon.hive.external.query.JargonHiveQueryServiceImplTest;
import org.irods.jargon.hive.external.sparql.JenaHiveSPARQLServiceImplTest;
import org.irods.jargon.hive.external.utils.template.SPARQLTemplateUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite to run all tests
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ JenaHiveIndexerVisitorTest.class,
		JenaHiveIndexerInvokerTest.class, JenaHiveIndexerServiceImplTest.class,
		JenaHiveSPARQLServiceImplTest.class, SPARQLTemplateUtilsTest.class,
		JargonHiveQueryServiceImplTest.class })
public class AllTests {

}
