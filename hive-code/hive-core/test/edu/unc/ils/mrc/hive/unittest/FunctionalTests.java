package edu.unc.ils.mrc.hive.unittest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.unc.ils.mrc.hive.admin.TestImportSocSciVocabs;
import edu.unc.ils.mrc.hive.admin.TestImportVocabs;

@RunWith(Suite.class)
@SuiteClasses({ TestImportSocSciVocabs.class, TestImportVocabs.class })
public class FunctionalTests {

}
