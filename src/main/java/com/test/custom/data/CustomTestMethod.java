package com.test.custom.data;

import com.test.custom.annotations.EstimateTime;
import com.test.custom.annotations.PrintTime;
import com.test.custom.annotations.Repeat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CustomTestMethod {
    private int repeatCount = 1;
    private int estimateTime = 0;
    private boolean printTime = false;
    private Method method;


    public CustomTestMethod(Method method) {
        if (method == null)
            throw new IllegalArgumentException("method parameter for Custom Test Method must not be null");
        this.method = method;

        for (Annotation annotation : method.getAnnotations()) {
            if (annotation.annotationType().equals(Repeat.class))
                repeatCount = method.getDeclaredAnnotation(Repeat.class).value();
            else if (annotation.annotationType().equals(PrintTime.class))
                printTime = true;
            else if (annotation.annotationType().equals(EstimateTime.class))
                estimateTime = method.getDeclaredAnnotation(EstimateTime.class).value();
        }
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(int estimateTime) {
        this.estimateTime = estimateTime;
    }

    public boolean isPrintTime() {
        return printTime;
    }

    public void setPrintTime(boolean printTime) {
        this.printTime = printTime;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}