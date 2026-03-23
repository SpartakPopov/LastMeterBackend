package org.example.lastmeterbackend.domain.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Building {

    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;
    private String address;
    private String description;

    @Builder.Default
    private List<Locker> lockers = new ArrayList<>();
}
