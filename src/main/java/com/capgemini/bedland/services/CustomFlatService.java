package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.FlatDetailsDto;
import com.capgemini.bedland.dtos.FlatShortenDetailsDto;

import java.util.List;

public interface CustomFlatService {

    List<FlatDetailsDto> getFlatDetailsForGivenManagerInGivenBuilding(Long managerId, Long buildingId);

    List<FlatShortenDetailsDto> getFlatsForGivenOwner(Long ownerId);
}
