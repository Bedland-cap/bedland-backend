package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.MemberDto;
import com.capgemini.bedland.providers.MemberProvider;
import com.capgemini.bedland.services.MemberService;
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
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private final MemberService memberService;
    @Autowired
    private final MemberProvider memberProvider;

    @GetMapping()
    List<MemberDto> getAll() {
        return memberProvider.getAll();
    }

    @GetMapping("/{id}")
    MemberDto getById(@PathVariable Long id) {
        return memberProvider.getById(id);
    }

    @GetMapping("/image/{id}")
    ResponseEntity<byte[]> getAvatarByMemberId(@PathVariable Long id) {
        return ResponseEntity.status(OK)
                             .contentType(MediaType.valueOf("image/png"))
                             .body(memberProvider.getAvatarByMemberId(id));
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    MemberDto create(@RequestBody MemberDto request) {
        return memberService.create(request);
    }

    @PatchMapping()
    MemberDto update(@RequestBody MemberDto request) {
        return memberService.update(request);
    }

    @PatchMapping("/image/{id}")
    MemberDto updateImage(@PathVariable Long id, @RequestParam(value = "image", required = false) MultipartFile file) {
        return memberService.updateAvatar(id, file);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        memberService.delete(id);
    }

}