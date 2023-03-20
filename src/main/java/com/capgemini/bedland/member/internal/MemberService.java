package com.capgemini.bedland.member.internal;

import org.springframework.web.multipart.MultipartFile;

interface MemberService {

    MemberDto create(MemberDto request);

    void delete(Long id);

    MemberDto update(MemberDto request);

    MemberDto updateAvatar(Long id, MultipartFile file);

}
