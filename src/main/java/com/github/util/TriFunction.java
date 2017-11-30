package com.github.util;

@FunctionalInterface
public interface TriFunction<T,R,S,V> {
    V apply(T t,R r,S s);
}
