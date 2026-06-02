package org.example.lastmeterbackend.presentation.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderGroupCreationDto {
    private String name;
    private List<Long> orderRequestIds;
}
