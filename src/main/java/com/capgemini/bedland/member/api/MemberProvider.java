package com.capgemini.bedland.member.api;

import com.capgemini.bedland.member.internal.MemberDto;

import java.util.List;

public interface MemberProvider {

    List<MemberDto> getAll();

    MemberDto getById(Long id);

    byte[] getAvatarByMemberId(Long id);

}
