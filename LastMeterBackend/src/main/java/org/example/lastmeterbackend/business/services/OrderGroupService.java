package org.example.lastmeterbackend.business.services;

import org.example.lastmeterbackend.domain.models.OrderGroup;

import java.util.List;

public interface OrderGroupService {
    OrderGroup createGroup(String name, Long requestedById, List<Long> orderRequestIds);
    List<OrderGroup> getAllGroups();
    OrderGroup getGroupById(Long id);
    void deleteGroup(Long id);
}
