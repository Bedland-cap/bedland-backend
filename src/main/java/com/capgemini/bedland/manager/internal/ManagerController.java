package com.capgemini.bedland.manager.internal;

import com.capgemini.bedland.manager.api.ManagerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ManagerProvider managerProvider;

    @GetMapping()
    List<ManagerDto> getAll() {
        return managerProvider.getAll();
    }

    @GetMapping("/{id}")
    ManagerDto getById(@PathVariable Long id) {
        return managerProvider.getById(id);
    }

    @PostMapping()
    ManagerDto create(@RequestBody ManagerDto request) {
        return managerService.create(request);
    }

    @PatchMapping()
    ManagerDto update(@RequestBody ManagerDto request) {
        return managerService.update(request);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        managerService.delete(id);
    }


}
