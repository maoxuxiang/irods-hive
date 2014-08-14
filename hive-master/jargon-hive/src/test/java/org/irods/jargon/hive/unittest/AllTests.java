package org.irods.jargon.hive.unittest;

import org.irods.jargon.hive.service.VocabularyServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite to run all tests
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({

// HiveContainerImplTest.class,
VocabularyServiceTest.class })
public class AllTests {

}
