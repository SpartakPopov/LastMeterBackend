package org.example.lastmeterbackend.domain.repositories;

import org.example.lastmeterbackend.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> searchByName(String query);
    Optional<User> findByEmail(String email);
}
