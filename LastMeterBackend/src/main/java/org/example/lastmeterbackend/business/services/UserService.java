package org.example.lastmeterbackend.business.services;

import org.example.lastmeterbackend.domain.models.User;

import java.util.List;

public interface UserService {
    List<User> searchByName(String query);
}
