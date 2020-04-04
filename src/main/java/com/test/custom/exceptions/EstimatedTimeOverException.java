package com.test.custom.exceptions;

public class EstimatedTimeOverException extends Exception {
    private long expectedTime;
    private long elapsedTime;

    public EstimatedTimeOverException(long expectedTime, long elapsedTime) {
        super("Estimated time elapsed expected : [" + expectedTime + "ms] elapsed : [" + elapsedTime + "]");
        this.expectedTime = expectedTime;
        this.elapsedTime = elapsedTime;
    }
}
