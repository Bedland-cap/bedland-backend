package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.BuildingDto;
import com.capgemini.bedland.providers.BuildingProvider;
import com.capgemini.bedland.services.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    private final BuildingService buildingService;
    @Autowired
    private final BuildingProvider buildingProvider;

    @GetMapping()
    List<BuildingDto> getAll() {
        return buildingProvider.getAll();
    }

    @GetMapping("/{id}")
    BuildingDto getById(@PathVariable Long id) {
        return buildingProvider.getById(id);
    }

    @GetMapping("/image/{id}")
    ResponseEntity<byte[]> getPhotoByBuildingId(@PathVariable Long id) {
        return ResponseEntity.status(OK)
                             .contentType(MediaType.valueOf("image/png"))
                             .body(buildingProvider.getPhotoByBuildingId(id));
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

    @PatchMapping("/image/{id}")
    BuildingDto updatePhoto(@PathVariable Long id, @RequestParam(value = "image", required = false) MultipartFile file) {
        return buildingService.updatePhoto(id, file);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        buildingService.delete(id);
    }

}
