package com.capgemini.bedland.manager.internal;

import com.capgemini.bedland.manager.api.ManagerProvider;
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
@RequestMapping("/manager")
class ManagerController {

    @Autowired
    private final ManagerService managerService;

    @Autowired
    private final ManagerProvider managerProvider;

    @GetMapping()
    List<ManagerDto> getAll() {
        return managerProvider.getAll();
    }

    @GetMapping("/{id}")
    ManagerDto getById(@PathVariable Long id) {
        return managerProvider.getById(id);
    }

    @GetMapping("/image/{id}")
    ResponseEntity<byte[]> getAvatarByManagerId(@PathVariable Long id) {
        return ResponseEntity.status(OK)
                             .contentType(MediaType.valueOf("image/png"))
                             .body(managerProvider.getAvatarByManagerId(id));
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    ManagerDto create(@RequestBody ManagerDto request) {
        return managerService.create(request);
    }

    @PatchMapping()
    ManagerDto update(@RequestBody ManagerDto request) {
        return managerService.update(request);
    }

    @PatchMapping("/image/{id}")
    ManagerDto updateAvatar(@PathVariable Long id, @RequestParam(value = "image", required = false) MultipartFile file) {
        return managerService.updateAvatar(id, file);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        managerService.delete(id);
    }

}
