package com.capgemini.bedland.member.internal;

import com.capgemini.bedland.member.api.MemberProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/member")
class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberProvider memberProvider;

    @GetMapping()
    List<MemberDto> getAll() {
        return memberProvider.getAll();
    }

    @GetMapping("/{id}")
    MemberDto getById(@PathVariable Long id) {
        return memberProvider.getById(id);
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

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        memberService.delete(id);
    }

}