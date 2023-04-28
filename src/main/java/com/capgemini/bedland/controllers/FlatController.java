package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.FlatDto;
import com.capgemini.bedland.providers.FlatProvider;
import com.capgemini.bedland.services.FlatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flat")
public class FlatController {
    @Autowired
    private final FlatService flatService;
    @Autowired
    private final FlatProvider flatProvider;
    @GetMapping()
    List<FlatDto> getAll(){
        return flatProvider.getAll();
    }
    @GetMapping("/{id}")
    FlatDto getById(@PathVariable Long id){
        return flatProvider.getById(id);
    }
    @PostMapping
    @ResponseStatus(CREATED)
    FlatDto create(@RequestBody FlatDto request){
        return flatService.create(request);
    }
    @PatchMapping()
    FlatDto update(@RequestBody FlatDto request){
        return flatService.update(request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id){
        flatService.delete(id);
    }

}
