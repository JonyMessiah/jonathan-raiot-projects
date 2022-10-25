package com.raiot.raiotprojects.dao;

import com.raiot.raiotprojects.interfaces.DAOInterface;
import com.raiot.raiotprojects.models.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements DAOInterface<UserModel> {
    private List<UserModel> users = new ArrayList<>();

    @Override
    public Optional<UserModel> getByEmail(String email) {
        return Optional.ofNullable(users.stream().filter(p -> p.getEmail().equals(email)).findAny()
                .orElse(null));
    }

    @Override
    public void save(UserModel user) {
        users.add(user);
    }
}