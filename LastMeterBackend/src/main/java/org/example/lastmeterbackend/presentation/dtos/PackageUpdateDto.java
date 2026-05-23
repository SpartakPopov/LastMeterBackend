package org.example.lastmeterbackend.presentation.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PackageUpdateDto {
    private String trackingNumber;
    private String description;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
}
