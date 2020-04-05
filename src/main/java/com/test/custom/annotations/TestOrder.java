package com.test.custom.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestOrder {
    enum OrderType {DEFAULT, ASCEND, DESCEND}

    OrderType value() default OrderType.DEFAULT;
}
