package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.MemberDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    MemberDto create(MemberDto request);

    void delete(Long id);

    MemberDto update(MemberDto request);

    MemberDto updateAvatar(Long id, MultipartFile file);

}
