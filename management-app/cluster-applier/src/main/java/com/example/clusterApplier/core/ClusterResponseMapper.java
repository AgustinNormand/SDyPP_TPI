package com.example.clusterApplier.core;

public interface ClusterResponseMapper<T> {

    T mapResult(String result);
}
