package org.example.lastmeterbackend.domain.repositories;

import org.example.lastmeterbackend.domain.models.User;

import java.util.List;

public interface UserRepository {
    List<User> searchByName(String query);
}
