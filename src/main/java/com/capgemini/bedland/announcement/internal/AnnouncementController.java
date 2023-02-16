package com.capgemini.bedland.announcement.internal;

import com.capgemini.bedland.announcement.api.AnnouncementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcement")
class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private AnnouncementProvider announcementProvider;

    @GetMapping()
    List<AnnouncementDto> getAll() {
        return announcementProvider.getAll();
    }

    @GetMapping("/{id}")
    AnnouncementDto getById(@PathVariable Long id) {
        return announcementProvider.getById(id);
    }

    @PostMapping()
    AnnouncementDto create(@RequestBody AnnouncementDto request) {
        return announcementService.create(request);
    }

    @PatchMapping()
    AnnouncementDto update(@RequestBody AnnouncementDto request) {
        return announcementService.update(request);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        announcementService.delete(id);
    }

}