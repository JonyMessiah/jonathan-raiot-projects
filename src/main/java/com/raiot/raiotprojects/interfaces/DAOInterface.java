package com.raiot.raiotprojects.interfaces;

import java.util.Optional;

public interface DAOInterface<T> {
    Optional<T> getByEmail(String email);
    void save(T t);
}