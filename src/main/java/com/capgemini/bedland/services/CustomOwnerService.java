package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.OwnerSummaryDto;

import java.util.List;

public interface CustomOwnerService {
    List<OwnerSummaryDto> findAllOwnersForGivenManager(Long managerId);

}
