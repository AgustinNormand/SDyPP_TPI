package com.TPI.Server.Servidor.core;

public interface ClusterResponseMapper<T> {

    T mapResult(String result);
}
