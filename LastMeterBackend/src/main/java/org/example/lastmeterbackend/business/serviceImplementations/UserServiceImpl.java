package org.example.lastmeterbackend.business.serviceImplementations;

import org.example.lastmeterbackend.business.services.UserService;
import org.example.lastmeterbackend.domain.models.User;
import org.example.lastmeterbackend.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> searchByName(String query) {
        return userRepository.searchByName(query);
    }
}
