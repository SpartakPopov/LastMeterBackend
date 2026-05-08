package org.example.lastmeterbackend.presentation.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestFulfillDto {
    private List<FulfillPackageDto> packages;
}
