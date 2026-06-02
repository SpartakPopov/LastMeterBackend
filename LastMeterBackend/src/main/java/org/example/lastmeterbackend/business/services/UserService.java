package org.example.lastmeterbackend.business.services;

import org.example.lastmeterbackend.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> searchByName(String query);
    Optional<User> findByEmail(String email);
}
