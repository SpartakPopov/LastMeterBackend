package org.example.lastmeterbackend.integrationTests;

import org.example.lastmeterbackend.business.services.UserService;
import org.example.lastmeterbackend.domain.models.User;
import org.example.lastmeterbackend.presentation.controllers.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIntegrationTest {

    private MockMvc mockMvc;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void searchUsers_returnsMatchingUsers() throws Exception {
        // Arrange
        String query = "mi";
        List<User> users = List.of(
                createUser(1L, "Mila", "Bos", "mila@example.com"),
                createUser(2L, "Milan", "Janssen", "milan@example.com")
        );
        when(userService.searchByName(query)).thenReturn(users);

        // Act + Assert
        mockMvc.perform(get("/users/search").param("q", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Mila"))
                .andExpect(jsonPath("$[0].email").value("mila@example.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Milan"))
                .andExpect(jsonPath("$[1].email").value("milan@example.com"));

        verify(userService).searchByName(query);
    }

    private User createUser(Long id, String firstName, String lastName, String email) {
        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }
}
