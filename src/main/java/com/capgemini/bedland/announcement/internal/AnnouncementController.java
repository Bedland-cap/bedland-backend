package com.capgemini.bedland.announcement.internal;

import com.capgemini.bedland.announcement.api.AnnouncementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

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