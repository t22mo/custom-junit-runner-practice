package com.test.custom.data;

import com.test.custom.annotations.Repeat;

import java.lang.reflect.Method;

public class CustomTestMethod {
    private int repeatCount;
    private Method method;

    public CustomTestMethod(Method method) {
        if (method == null)
            throw new IllegalArgumentException("method parameter for Custom Test Method must not be null");
        this.method = method;
        if (method.isAnnotationPresent(Repeat.class)) {
            repeatCount = method.getDeclaredAnnotation(Repeat.class).value();
        } else
            repeatCount = 1;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}