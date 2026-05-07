package org.example.lastmeterbackend.presentation.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequestFulfillDto {
    private List<String> trackingNumbers;
}
