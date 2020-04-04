package com.test.custom.runner;

import com.test.custom.data.CustomTestMethod;
import com.test.custom.exceptions.EstimatedTimeOverException;
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
        long startTime = 0, endTime = 0;

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

            for (int i = 0; i < testMethod.getRepeatCount(); i++) {
                //fire started & invoke
                runNotifier.fireTestStarted(Description.createTestDescription(testClass, methodName));
                try {
                    System.out.println("Testing testcase[" + method.getName() + "]");
                    startTime = System.currentTimeMillis();
                    method.invoke(testObject);
                } catch (InvocationTargetException ite) {
                    if (ite.getTargetException() instanceof AssertionError) {
                        System.out.println("Assertion failed");
                        runNotifier.fireTestFailure(new Failure(Description.createTestDescription(testClass, methodName), ite.getCause()));
                    } else {
                        runNotifier.fireTestFailure(new Failure(Description.createTestDescription(testClass, methodName), ite));
                        ite.printStackTrace();
                    }

                    continue;
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                    runNotifier.fireTestFailure(new Failure(Description.createTestDescription(testClass, methodName), e));
                    continue;
                } finally {
                    endTime = System.currentTimeMillis();
                    if (testMethod.isPrintTime())
                        System.out.println("Test time for method [" + methodName + "] : " + (endTime - startTime));
                }

                //Check estimated time declared by @EstimateTime. If it elapsed, throw test failure.
                if (testMethod.getEstimateTime() > 0 && (endTime - startTime) > testMethod.getEstimateTime()) {
                    System.out.println("Test time exceeded estimated time. [Estimated time : " + testMethod.getEstimateTime() +
                            "] [Elapsed time : " + (endTime - startTime) + "]");
                    runNotifier.fireTestFailure(new Failure(Description.createTestDescription(testClass, methodName),
                            new EstimatedTimeOverException(testMethod.getEstimateTime(), (endTime - startTime))));
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