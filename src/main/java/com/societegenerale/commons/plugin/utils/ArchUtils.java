package com.societegenerale.commons.plugin.utils;

import com.societegenerale.commons.plugin.model.ConfigurableRule;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by agarg020917 on 11/17/2017.
 */
public class ArchUtils {

    private static final String JUNIT4_ASSERT_PACKAGE_NAME = "org.junit.Assert";
    private static final String JUNIT5_ASSERT_PACKAGE_NAME = "org.junit.jupiter.api.Assertions";

    public static final String NO_JUNIT_ASSERT_DESCRIPTION = "not use Junit assertions";

    public static final String TEST_CLASSES_FOLDER = "/test-classes";
    public static final String SRC_CLASSES_FOLDER = "/classes";
    private static final String PACKAGE_SEPARATOR = ".";

    public static final String NO_PREFIX_INTERFACE_VIOLATION_MESSAGE = " : Interfaces shouldn't be prefixed with \"I\" - caller doesn't need to know it's an interface + this is a .Net convention";
    public static final String POWER_MOCK_VIOLATION_MESSAGE = "Favor Mockito and proper dependency injection - ";
    public static final String NO_INJECTED_FIELD_MESSAGE = "Favor constructor injection and avoid field injection - ";
    public static final String NO_AUTOWIRED_FIELD_MESSAGE = "Favor constructor injection and avoid autowiring fields - ";
    public static final String NO_JODA_VIOLATION_MESSAGE = "Use Java8 Date API instead of Joda library";
    public static final String NO_JUNIT_IGNORE_VIOLATION_MESSAGE = "Tests shouldn't been ignored";
    public static final String NO_JUNIT_IGNORE_WITHOUT_COMMENT_VIOLATION_MESSAGE = "Tests shouldn't been ignored without providing a comment explaining why";

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");


    private ArchUtils() {
        throw new UnsupportedOperationException();
    }

    public static JavaClasses importAllClassesInPackage(String path, String classFolder) {
        Path classesPath = Paths.get(path + classFolder);
        if (classesPath.toFile().exists()) {
            return new ClassFileImporter().importPath(classesPath);
        }
        return new ClassFileImporter().importPath(Paths.get(path));
    }

    public static boolean isJunitAssert(JavaClass javaClass) {

        String packageNameToCheck = new StringBuilder().append(javaClass.getPackageName()).append(PACKAGE_SEPARATOR).append(javaClass.getSimpleName()).toString();

        return JUNIT4_ASSERT_PACKAGE_NAME.equals(packageNameToCheck) || JUNIT5_ASSERT_PACKAGE_NAME.equals(packageNameToCheck);
    }

    public static String getPackageNameOnWhichRulesToApply(ConfigurableRule rule) {

        StringBuilder packageNameBuilder = new StringBuilder(SRC_CLASSES_FOLDER);

        if (rule.getApplyOn() != null) {
            if (rule.getApplyOn().getScope() != null && "test".equals(rule.getApplyOn().getScope())) {
                packageNameBuilder = new StringBuilder(TEST_CLASSES_FOLDER);
            }
            packageNameBuilder.append("/").append(rule.getApplyOn().getPackageName());

        }
        return packageNameBuilder.toString().replace(".", "/");
    }

    public static Map<String, Method> getAllMethodsWhichReturnAnArchCondition(Method[] methods) {

        Map<String, Method> archConditionReturningMethods = new HashMap<>();
        for (Method method : methods) {
            if (method.getReturnType().equals(ArchCondition.class)) {
                archConditionReturningMethods.put(method.getName(), method);
            }
        }
        return archConditionReturningMethods;
    }

    public static Map<String, Field> getAllFieldsWhichAreArchRules(Field[] fields) {

        Map<String, Field> archRuleFields = new HashMap<>();

        for (Field field : fields) {
            if (field.getType().equals(ArchRule.class)) {
                archRuleFields.put(field.getName(), field);
            }
        }

        return archRuleFields;
    }

    public static String getPrefixContentForAllRuleFailures(List<String> ruleFailureList) {

        StringBuilder prefixBuilder = new StringBuilder("ArchUnit Maven plugin reported architecture failures listed below :");

        prefixBuilder.append(LINE_SEPARATOR)
                .append(ruleFailureList.toString()).append(LINE_SEPARATOR)
                .append("More Details on failures are below :").append(LINE_SEPARATOR);

        return prefixBuilder.toString();
    }
}