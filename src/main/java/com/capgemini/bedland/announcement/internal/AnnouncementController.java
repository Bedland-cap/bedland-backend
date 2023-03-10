package com.capgemini.bedland.announcement.internal;

import com.capgemini.bedland.announcement.api.AnnouncementProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RequiredArgsConstructor
@RestController
@RequestMapping("/announcement")
class AnnouncementController {


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