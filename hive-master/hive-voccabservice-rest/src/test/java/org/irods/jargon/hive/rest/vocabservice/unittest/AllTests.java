package org.irods.jargon.hive.rest.vocabservice.unittest;

import org.irods.jargon.hive.rest.vocabservice.RestConceptServiceTest;
import org.irods.jargon.hive.rest.vocabservice.RestVocabularySearchServiceTest;
import org.irods.jargon.hive.rest.vocabservice.RestVocabularyServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RestVocabularyServiceTest.class, RestConceptServiceTest.class,
		RestVocabularySearchServiceTest.class })
public class AllTests {

}
