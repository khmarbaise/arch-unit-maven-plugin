package com.societegenerale.commons.plugin.rules;

import static com.societegenerale.commons.plugin.utils.ArchUtils.SRC_CLASSES_FOLDER;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS;

import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;


public class NoStandardStreamRuleTest implements ArchRuleTest {

  private static final ArchCondition<JavaClass> notUseStandardStream = ACCESS_STANDARD_STREAMS;

  @Override
  public void execute(String path) {
    noClasses().should(notUseStandardStream).check(ArchUtils.importAllClassesInPackage(path, SRC_CLASSES_FOLDER));
  }

  public static ArchCondition<JavaClass> getNotUseStandardStream(){
    return ACCESS_STANDARD_STREAMS;
  }
}
