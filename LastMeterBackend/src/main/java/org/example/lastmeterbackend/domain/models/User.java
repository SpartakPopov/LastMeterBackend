package org.example.lastmeterbackend.domain.models;

import lombok.*;
import org.example.lastmeterbackend.domain.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Setter(AccessLevel.NONE)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;

    @Builder.Default
    private List<Package> packages = new ArrayList<>();
}
