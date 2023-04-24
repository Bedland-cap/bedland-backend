package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.OwnerSummaryDto;
import com.capgemini.bedland.services.CustomOwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CustomOwnerController {
    private final CustomOwnerService customOwnerService;

    @GetMapping(path = "/owners_list", params = {"manager_id"})
    List<OwnerSummaryDto> findOwnerForGivenManager(@RequestParam Long manager_id){
        return customOwnerService.findAllOwnersForGivenManager(manager_id);
    }

}
