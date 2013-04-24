package com.mcliang.seismograph;

import java.util.Stack;

public class SizedStack<T> extends Stack<T> {
    private static int maxSize;
    
    public SizedStack(int size) {
        super();
        setMaxSize(size);
    }
    
    @Override
    public Object push(Object object) {
        while (this.size() > maxSize) {
            this.remove(0);
        }
        return super.push((T) object);
    }
    
    public static int getMaxSize() {
        return maxSize;
    }
    public static void setMaxSize(int maxSize) {
        SizedStack.maxSize = maxSize;
    }
}
