package org.example.lastmeterbackend.config;

import org.example.lastmeterbackend.DAL.entities.UserEntity;
import org.example.lastmeterbackend.DAL.repositories.UserJpaRepository;
import org.example.lastmeterbackend.domain.enums.UserRole;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserJpaRepository userJpaRepository;

    public DataSeeder(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedUser("spartakkpopov@gmail.com", "Spartak", "Popov", UserRole.ADMIN);
        seedUser("spartak@gmail.com", "Spartak", "Employee", UserRole.EMPLOYEE);
    }

    private void seedUser(String email, String firstName, String lastName, UserRole role) {
        if (userJpaRepository.findByEmail(email).isEmpty()) {
            userJpaRepository.save(UserEntity.builder()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .role(role)
                    .build());
        }
    }
}
