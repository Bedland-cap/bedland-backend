package com.capgemini.bedland.flat.internal;

import com.capgemini.bedland.flat.api.FlatProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/flat")
public class FlatController {
    @Autowired
    private FlatService flatService;
    @Autowired
    private FlatProvider flatProvider;
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
