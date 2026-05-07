package org.example.lastmeterbackend.unitTests;

import org.example.lastmeterbackend.business.serviceImplementations.UserServiceImpl;
import org.example.lastmeterbackend.domain.models.User;
import org.example.lastmeterbackend.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void searchByName_returnsMatchingUsers() {
        // Arrange
        String query = "mi";
        List<User> expectedUsers = List.of(
                createUser("Mila", "Bos", "mila@example.com"),
                createUser("Milan", "Janssen", "milan@example.com")
        );
        when(userRepository.searchByName(query)).thenReturn(expectedUsers);

        // Act
        List<User> result = userService.searchByName(query);

        // Assert
        assertEquals(expectedUsers, result);
        verify(userRepository).searchByName(query);
    }

    @Test
    void searchByName_throwsExceptionWhenRepositoryFails() {
        // Arrange
        String query = "mi";
        when(userRepository.searchByName(query)).thenThrow(new RuntimeException("search failed"));

        // Act
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.searchByName(query)
        );

        // Assert
        assertEquals("search failed", exception.getMessage());
        verify(userRepository).searchByName(query);
    }

    private User createUser(String firstName, String lastName, String email) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }
}
