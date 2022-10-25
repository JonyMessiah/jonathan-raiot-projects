package com.raiot.raiotprojects.interfaces;

import java.util.Optional;

public interface DAOInterface<T> {
    void save(T t);
}