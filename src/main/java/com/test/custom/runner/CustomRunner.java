package com.test.custom.runner;

import com.test.custom.data.CustomTestMethod;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;

public class CustomRunner extends Runner {
    private Class testClass;
    private HashMap<String, CustomTestMethod> testMethodMap;
    private static final String runArgsPropertyName = "sun.java.command";

    public CustomRunner(Class testClass) {
        super();
        String targetMethod;
        this.testClass = testClass;
        testMethodMap = new HashMap<String, CustomTestMethod>();

        //get target method from System.properties
        targetMethod = getMethodNameFromArgs();

        //if target method is present, add single method to map
        if (targetMethod.length() > 0) {
            try {
                Method method;
                method = testClass.getMethod(targetMethod);
                testMethodMap.put(method.getName(), new CustomTestMethod(method));
            } catch (NoSuchMethodException e) {
                System.out.println("No such test method : []" + targetMethod);
            }
        } else {
            //Iterate methods and add them into hashmap
            for (Method method : testClass.getMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    testMethodMap.put(method.getName(), new CustomTestMethod(method));
                }
            }
        }
    }

    @Override
    public Description getDescription() {
        return Description.createTestDescription(testClass, testClass.getName());
    }

    @Override
    public void run(RunNotifier runNotifier) {
        Iterator<String> mapKeys = testMethodMap.keySet().iterator();
        CustomTestMethod testMethod;
        Method method;
        Object testObject = null;

        //Get class instance
        try {
            testObject = testClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("Error instanciating testClass object");
            e.printStackTrace();
        }

        //iterate methods
        for (String methodName : testMethodMap.keySet()) {
            testMethod = testMethodMap.get(methodName);
            method = testMethod.getMethod();
            System.out.println("Testing testcase[" + method.getName() + "]");

            for (int i = 0; i < testMethod.getRepeatCount(); i++) {
                //fire started & invoke
                runNotifier.fireTestStarted(Description.createTestDescription(testClass, methodName));
                try {
                    method.invoke(testObject);
                } catch (InvocationTargetException ite) {
                    if (ite.getTargetException() instanceof AssertionError) {
                        System.out.println("Assertion failed");
                    } else
                        ite.printStackTrace();
                    runNotifier.fireTestFailure(new Failure(Description.createTestDescription(testClass, methodName), ite));
                    continue;
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                    runNotifier.fireTestFailure(new Failure(Description.createTestDescription(testClass, methodName), e));
                    continue;
                }
                //If invoke was successful, fire finished
                runNotifier.fireTestFinished(Description.createTestDescription(testClass, methodName));
            }
        }
    }

    /**
     * Retrieves target method from run argument which is located in "sun.java.command" system property
     * CAUTION: may be valid for only intellij JUnit runner.
     *
     * @return test method name by string if there is one. if not, returns empty string.
     */
    private String getMethodNameFromArgs() {
        String property;
        String[] args, targets;

        property = System.getProperty(runArgsPropertyName);
        args = property.split("\\s+");
        if (args.length >= 4) {
            targets = args[3].split(",");
            if (targets.length >= 2)
                return targets[1];
        }
        return "";
    }
}