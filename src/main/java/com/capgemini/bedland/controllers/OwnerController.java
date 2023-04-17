package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.OwnerDto;
import com.capgemini.bedland.providers.OwnerProvider;
import com.capgemini.bedland.services.OwnerService;
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
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private final OwnerService ownerService;
    @Autowired
    private final OwnerProvider ownerProvider;

    @GetMapping()
    List<OwnerDto> getAll() {
        return ownerProvider.getAll();
    }

    @GetMapping("/{id}")
    OwnerDto getById(@PathVariable Long id) {
        return ownerProvider.getById(id);
    }

    @GetMapping("/image/{id}")
    ResponseEntity<byte[]> getAvatarByOwnerId(@PathVariable Long id) {
        return ResponseEntity.status(OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(ownerProvider.getAvatarByOwnerId(id));
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    OwnerDto create(@RequestBody OwnerDto request) {
        return ownerService.create(request);
    }

    @PatchMapping()
    OwnerDto update(@RequestBody OwnerDto request) {
        return ownerService.update(request);
    }

    @PatchMapping("/image/{id}")
    OwnerDto updateImage(@PathVariable Long id, @RequestParam(value = "image", required = false) MultipartFile file) {
        return ownerService.updateAvatar(id, file);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        ownerService.delete(id);
    }
}
