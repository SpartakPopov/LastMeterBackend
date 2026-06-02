package org.example.lastmeterbackend.business.services;

import org.example.lastmeterbackend.domain.models.OrderGroup;
import org.example.lastmeterbackend.presentation.dtos.FulfillPackageDto;

import java.util.List;

public interface OrderGroupService {
    OrderGroup createGroup(String name, List<Long> orderRequestIds);
    List<OrderGroup> getAllGroups();
    OrderGroup getGroupById(Long id);
    void deleteGroup(Long id);
    void fulfillGroup(Long groupId, List<FulfillPackageDto> packages);
}
