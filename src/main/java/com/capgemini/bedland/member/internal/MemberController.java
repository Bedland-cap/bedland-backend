package com.capgemini.bedland.member.internal;

import com.capgemini.bedland.member.api.MemberProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    MemberDto create(@RequestBody MemberDto request) {
        return memberService.create(request);
    }

    @PatchMapping()
    MemberDto update(@RequestBody MemberDto request) {
        return memberService.update(request);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        memberService.delete(id);
    }

}