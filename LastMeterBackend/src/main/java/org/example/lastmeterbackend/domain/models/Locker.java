package org.example.lastmeterbackend.domain.models;

import lombok.*;
import org.example.lastmeterbackend.domain.enums.LockerSize;
import org.example.lastmeterbackend.domain.enums.LockerStatus;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Locker {

    @Setter(AccessLevel.NONE)
    private Long id;

    private String lockerNumber;
    private LockerSize size;
    private LockerStatus status;
    private Building building;
}
