package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.MemberDto;

import java.util.List;

public interface MemberProvider {

    List<MemberDto> getAll();

    MemberDto getById(Long id);

    byte[] getAvatarByMemberId(Long id);

}
