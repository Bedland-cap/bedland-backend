package com.capgemini.bedland.building.internal;

import com.capgemini.bedland.building.api.BuildingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/building")
class BuildingController {

    @Autowired
    private BuildingService buildingService;
    @Autowired
    private BuildingProvider buildingProvider;

    @GetMapping()
    List<BuildingDto> getAll() {
        return buildingProvider.getAll();
    }

    @GetMapping("/{id}")
    BuildingDto getById(@PathVariable Long id) {
        return buildingProvider.getById(id);
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    BuildingDto create(@RequestBody BuildingDto request) {
        return buildingService.create(request);
    }

    @PatchMapping()
    BuildingDto update(@RequestBody BuildingDto request) {
        return buildingService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        buildingService.delete(id);
    }

}
