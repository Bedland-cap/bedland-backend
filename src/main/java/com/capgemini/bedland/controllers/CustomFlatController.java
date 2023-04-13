package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.FlatDetailsDto;
import com.capgemini.bedland.services.CustomFlatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flats")
public class CustomFlatController {

    private final CustomFlatService customFlatService;

    @GetMapping(params = {"manager_id", "building_id"})
    List<FlatDetailsDto> findFlatDetailsForGivenManagerAndBuilding(@RequestParam Long manager_id, @RequestParam Long building_id) {
        return customFlatService.getFlatDetailsForGivenManagerInGivenBuilding(manager_id, building_id);
    }

}
