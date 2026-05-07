package org.example.lastmeterbackend.DAL.repositories;

import org.example.lastmeterbackend.DAL.mappers.UserPersistenceMapper;
import org.example.lastmeterbackend.domain.models.User;
import org.example.lastmeterbackend.domain.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository,
                              UserPersistenceMapper userPersistenceMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    @Override
    public List<User> searchByName(String query) {
        return userJpaRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
                .stream()
                .map(userPersistenceMapper::toDomain)
                .toList();
    }
}
