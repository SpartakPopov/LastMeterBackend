package org.example.lastmeterbackend.integrationTests;

import org.example.lastmeterbackend.business.services.OrderRequestService;
import org.example.lastmeterbackend.domain.enums.OrderRequestStatus;
import org.example.lastmeterbackend.domain.models.OrderRequest;
import org.example.lastmeterbackend.domain.models.User;
import org.example.lastmeterbackend.presentation.controllers.OrderRequestController;
import org.example.lastmeterbackend.presentation.mappers.OrderRequestDtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(OrderRequestDtoMapper.class)
class OrderRequestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderRequestService orderRequestService;

    @Test
    void createOrderRequest_returnsCreatedOrderRequest() throws Exception {
        // Arrange
        OrderRequest createdOrderRequest = createOrderRequest(
                1L, "Laptop", 2L, 3L, "Tom", "Visser", OrderRequestStatus.PENDING
        );
        when(orderRequestService.createOrderRequest(any(OrderRequest.class))).thenReturn(createdOrderRequest);

        // Act + Assert
        mockMvc.perform(post("/order-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "description": "Laptop",
                                  "productLinks": "https://example.com/laptop",
                                  "quantity": 2,
                                  "requestedById": 2,
                                  "requestedForId": 3
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Laptop"))
                .andExpect(jsonPath("$.requestedById").value(2))
                .andExpect(jsonPath("$.requestedForId").value(3))
                .andExpect(jsonPath("$.status").value("PENDING"));

        ArgumentCaptor<OrderRequest> orderRequestCaptor =
                org.mockito.ArgumentCaptor.forClass(OrderRequest.class);
        verify(orderRequestService).createOrderRequest(orderRequestCaptor.capture());
        OrderRequest capturedOrderRequest = orderRequestCaptor.getValue();
        assertEquals("Laptop", capturedOrderRequest.getDescription());
        assertEquals("https://example.com/laptop", capturedOrderRequest.getProductLinks());
        assertEquals(2, capturedOrderRequest.getQuantity());
        assertEquals(2L, capturedOrderRequest.getRequestedBy().getId());
        assertEquals(3L, capturedOrderRequest.getRequestedFor().getId());
        assertEquals(OrderRequestStatus.PENDING, capturedOrderRequest.getStatus());
    }

    @Test
    void getAllOrderRequests_returnsAllOrderRequests() throws Exception {
        // Arrange
        List<OrderRequest> orderRequests = List.of(
                createOrderRequest(1L, "Laptop", 2L, 3L, "Tom", "Visser", OrderRequestStatus.PENDING),
                createOrderRequest(2L, "Keyboard", 2L, 2L, "Mila", "Bos", OrderRequestStatus.APPROVED)
        );
        when(orderRequestService.getAllOrderRequests()).thenReturn(orderRequests);

        // Act + Assert
        mockMvc.perform(get("/order-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("Laptop"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].description").value("Keyboard"))
                .andExpect(jsonPath("$[1].status").value("APPROVED"));

        verify(orderRequestService).getAllOrderRequests();
    }

    @Test
    void getMyOrderRequests_returnsUserOrderRequests() throws Exception {
        // Arrange
        Long userId = 2L;
        List<OrderRequest> orderRequests = List.of(
                createOrderRequest(1L, "Laptop", userId, 3L, "Tom", "Visser", OrderRequestStatus.PENDING),
                createOrderRequest(2L, "Mouse", userId, userId, "Mila", "Bos", OrderRequestStatus.REJECTED)
        );
        when(orderRequestService.getOrderRequestsByUser(userId)).thenReturn(orderRequests);

        // Act + Assert
        mockMvc.perform(get("/order-requests/my/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].requestedById").value(2))
                .andExpect(jsonPath("$[1].requestedById").value(2));

        verify(orderRequestService).getOrderRequestsByUser(userId);
    }

    @Test
    void getOrderRequestById_returnsOrderRequest() throws Exception {
        // Arrange
        Long id = 1L;
        OrderRequest orderRequest = createOrderRequest(
                id, "Laptop", 2L, 3L, "Tom", "Visser", OrderRequestStatus.PENDING
        );
        when(orderRequestService.getOrderRequestById(id)).thenReturn(orderRequest);

        // Act + Assert
        mockMvc.perform(get("/order-requests/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Laptop"))
                .andExpect(jsonPath("$.requestedByFirstName").value("Mila"))
                .andExpect(jsonPath("$.requestedForFirstName").value("Tom"));

        verify(orderRequestService).getOrderRequestById(id);
    }

    @Test
    void approveOrderRequest_returnsApprovedOrderRequest() throws Exception {
        // Arrange
        Long id = 1L;
        String managerNotes = "Approved for purchase";
        OrderRequest approvedOrderRequest = createOrderRequest(
                id, "Laptop", 2L, 3L, "Tom", "Visser", OrderRequestStatus.APPROVED
        );
        approvedOrderRequest.setManagerNotes(managerNotes);
        when(orderRequestService.approveOrderRequest(id, managerNotes)).thenReturn(approvedOrderRequest);

        // Act + Assert
        mockMvc.perform(patch("/order-requests/{id}/approve", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "managerNotes": "Approved for purchase"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.managerNotes").value("Approved for purchase"));

        verify(orderRequestService).approveOrderRequest(id, managerNotes);
    }

    @Test
    void rejectOrderRequest_returnsRejectedOrderRequest() throws Exception {
        // Arrange
        Long id = 1L;
        String managerNotes = "Budget not available";
        OrderRequest rejectedOrderRequest = createOrderRequest(
                id, "Laptop", 2L, 3L, "Tom", "Visser", OrderRequestStatus.REJECTED
        );
        rejectedOrderRequest.setManagerNotes(managerNotes);
        when(orderRequestService.rejectOrderRequest(id, managerNotes)).thenReturn(rejectedOrderRequest);

        // Act + Assert
        mockMvc.perform(patch("/order-requests/{id}/reject", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "managerNotes": "Budget not available"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"))
                .andExpect(jsonPath("$.managerNotes").value("Budget not available"));

        verify(orderRequestService).rejectOrderRequest(id, managerNotes);
    }

    @Test
    void fulfillOrderRequest_returnsFulfilledOrderRequest() throws Exception {
        // Arrange
        Long id = 1L;
        List<String> trackingNumbers = List.of("TRACK-800", "TRACK-801");
        OrderRequest fulfilledOrderRequest = createOrderRequest(
                id, "Laptop", 2L, 3L, "Tom", "Visser", OrderRequestStatus.ORDERED
        );
        when(orderRequestService.fulfillOrderRequest(id, trackingNumbers)).thenReturn(fulfilledOrderRequest);

        // Act + Assert
        mockMvc.perform(patch("/order-requests/{id}/fulfill", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "trackingNumbers": ["TRACK-800", "TRACK-801"]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ORDERED"))
                .andExpect(jsonPath("$.description").value("Laptop"));

        verify(orderRequestService).fulfillOrderRequest(id, trackingNumbers);
    }

    private OrderRequest createOrderRequest(
            Long id,
            String description,
            Long requestedById,
            Long requestedForId,
            String requestedForFirstName,
            String requestedForLastName,
            OrderRequestStatus status
    ) {
        User requestedBy = createUser(requestedById, "Mila", "Bos");
        User requestedFor = createUser(requestedForId, requestedForFirstName, requestedForLastName);

        return OrderRequest.builder()
                .id(id)
                .description(description)
                .productLinks("https://example.com/" + description.toLowerCase())
                .quantity(2)
                .requestedBy(requestedBy)
                .requestedFor(requestedFor)
                .status(status)
                .createdAt(LocalDateTime.of(2026, 5, 7, 10, 15))
                .updatedAt(LocalDateTime.of(2026, 5, 7, 10, 30))
                .build();
    }

    private User createUser(Long id, String firstName, String lastName) {
        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(firstName.toLowerCase() + "@example.com")
                .build();
    }
}
