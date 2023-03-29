package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.AnnouncementDto;
import com.capgemini.bedland.providers.AnnouncementProvider;
import com.capgemini.bedland.services.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RequiredArgsConstructor
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {


    private final AnnouncementService announcementService;

    private final AnnouncementProvider announcementProvider;

    @GetMapping()
    List<AnnouncementDto> getAll() {
        return announcementProvider.getAll();
    }

    @GetMapping("/{id}")
    AnnouncementDto getById(@PathVariable Long id) {
        return announcementProvider.getById(id);
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    AnnouncementDto create(@RequestBody AnnouncementDto request) {
        return announcementService.create(request);
    }

    @PatchMapping()
    AnnouncementDto update(@RequestBody AnnouncementDto request) {
        return announcementService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        announcementService.delete(id);
    }

}