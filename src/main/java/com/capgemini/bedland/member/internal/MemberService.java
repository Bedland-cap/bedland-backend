package com.capgemini.bedland.member.internal;

interface MemberService {

    MemberDto create(MemberDto request);

    void delete(Long id);

    MemberDto update(MemberDto request);

}
