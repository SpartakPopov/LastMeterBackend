package org.example.lastmeterbackend.domain.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderGroup {

    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;
    private User requestedBy;
    private List<OrderRequest> orderRequests;
    private LocalDateTime createdAt;
}
