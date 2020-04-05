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

    public CustomRunner(Class testClass) {
        super();
        this.testClass = testClass;

        //Iterate methods and add them into hashmap
        testMethodMap = new HashMap<String, CustomTestMethod>();
        for (Method method : testClass.getMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                testMethodMap.put(method.getName(), new CustomTestMethod(method));
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
}