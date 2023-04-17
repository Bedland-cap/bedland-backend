package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.MemberDto;
import com.capgemini.bedland.entities.MemberEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.MemberMapper;
import com.capgemini.bedland.providers.MemberProvider;
import com.capgemini.bedland.repositories.MemberRepository;
import com.capgemini.bedland.utilities.ImageUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureTestDatabase
@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberProvider memberProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @AfterEach
    void cleanUp() {
        Mockito.clearAllCaches();
    }

    @Test
    void shouldReturnAllMembersWhenGettingAllMembers() {
        //given
        List<MemberDto> memberDtos = memberMapper.entities2DTOs(memberRepository.findAll());
        //when
        List<MemberDto> memberDtos1 = memberProvider.getAll();
        //then
        assertNotEquals(0, memberDtos.size());
        assertEquals(memberDtos.size(), memberDtos1.size());
        assertEquals(memberDtos, memberDtos1);
    }

    @Test
    void shouldFindMemberByIdWhenGettingMemberById() {
        //given
        MemberDto memberDto = memberMapper.entity2Dto(memberRepository.findAll()
                .get(0));
        //when
        MemberDto foundMember = memberProvider.getById(memberDto.getId());
        //then
        assertNotEquals(null, foundMember.getId());
        assertNotEquals(null, foundMember);
        assertEquals(foundMember, memberDto);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindingMemberByIdAndMemberIsNotPresentInDB() {
        //given
        Long sampleID = 999999L;
        //when
        //then
        assertThrows(NotFoundException.class, () -> memberProvider.getById(sampleID));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFindingMemberByIdGivenIdIsNull() {
        //given
        Long sampleID = null;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> memberProvider.getById(sampleID));
    }

    @Test
    void shouldFindMemberAvatarByMemberIdWhenGettingMemberById() throws IOException {
        //given
        MemberEntity member = memberRepository.findAll()
                .get(0);
        byte[] data = {1, 2};
        MultipartFile file = new MockMultipartFile("name.xD", data);
        //when
        mockStatic(ImageUtil.class);
        when(ImageUtil.decompressImage(data)).thenReturn(data);
        member.setAvatar(file.getBytes());
        memberRepository.save(member);
        //then
        assertEquals(memberProvider.getAvatarByMemberId(member.getId()), member.getAvatar());
    }

    @Test
    void shouldCreateMemberWhenCreatingMember() {
        //given
        MemberDto newMember = MemberDto.builder()
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .flatId(1L)
                .build();
        List<MemberEntity> membersBeforeSavingNewOne = memberRepository.findAll();
        //when
        MemberDto createdMember = memberService.create(newMember);
        List<MemberEntity> membersAfterSavingOne = memberRepository.findAll();
        //then
        assertNotEquals(membersBeforeSavingNewOne.size(), membersAfterSavingOne.size());
        assertEquals(membersBeforeSavingNewOne.size() + 1, membersAfterSavingOne.size());
        assertTrue(memberRepository.existsById(createdMember.getId()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingMemberWhoHasID() {
        //given
        MemberDto newMember = MemberDto.builder()
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .flatId(1L)
                .build();
        newMember.setId(9999L);
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> memberService.create(newMember));
    }

    @Test
    void shouldDeleteMemberWhenDeletingMember() {
        //given
        List<MemberDto> membersBeforeDeletingOne = memberProvider.getAll();
        MemberDto memberToDeleteDto = memberProvider.getAll()
                .get(0);
        Long id = memberToDeleteDto.getId();
        //when
        memberService.delete(id);
        List<MemberEntity> memberAfterDeletingOne = memberRepository.findAll();
        //then
        assertFalse(memberRepository.existsById(id));
        assertNotEquals(membersBeforeDeletingOne.size(), memberAfterDeletingOne.size());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingMemberWithIDNotPresentInDB() {
        //given
        //when + then
        assertThrows(NotFoundException.class, () -> memberService.delete(99999L));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDeletingMemberWithNullID() {
        //given
        //when + then
        assertThrows(IllegalArgumentException.class, () -> memberService.delete(null));
    }

    @Test
    void shouldUpdateMemberWhenUpdatingMember() {
        //given
        MemberDto memberToUpdate = memberProvider.getAll()
                .get(0);
        String oldName = memberToUpdate.getName();
        String newName = "John";
        memberToUpdate.setName(newName);
        //when
        memberService.update(memberToUpdate);
        MemberDto updatedMember = memberProvider.getById(memberToUpdate.getId());
        //then
        assertNotEquals(oldName, newName);
        assertEquals(memberToUpdate.getId(), updatedMember.getId());
        assertEquals(newName, updatedMember.getName());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingMemberWithNullID() {
        //given
        MemberDto memberToUpdate = memberProvider.getAll()
                .get(0);
        memberToUpdate.setId(null);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> memberService.update(memberToUpdate));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingMemberWithIDNotPresentInDB() {
        //given
        MemberDto memberToUpdate = memberProvider.getAll()
                .get(0);
        memberToUpdate.setId(99999L);
        //when + then
        assertThrows(NotFoundException.class, () -> memberService.update(memberToUpdate));
    }

    @Test
    void shouldReturnUpdateMemberWhenUpdateAvatar() throws IOException {
        //given
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("file.xD", data);
        MultipartFile newFile = new MockMultipartFile("updatedFile.xD", data);
        MemberEntity memberToUpdate = memberRepository.findAll()
                .get(0);
        memberToUpdate.setAvatar(file.getBytes());
        memberRepository.save(memberToUpdate);
        // when
        MemberDto updatedMember = memberService.updateAvatar(memberToUpdate.getId(), newFile);
        // then
        assertEquals(memberToUpdate.getId(), updatedMember.getId());
        assertNotEquals(file.getBytes(), updatedMember.getAvatar());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingAvatarWithNullID() {
        //given
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("file.xd", data);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> memberService.updateAvatar(null, file));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingAvatarWithNullFile() {
        //given
        Long id = memberRepository.findAll()
                .get(0)
                .getId();
        //when + then
        assertThrows(IllegalArgumentException.class, () -> memberService.updateAvatar(id, null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingAvatarWithNullIDAndNullFile() {
        //given + when + then
        assertThrows(IllegalArgumentException.class, () -> memberService.updateAvatar(null, null));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingAvatarForManagerThatNotExist() {
        //given
        Long id = Long.MAX_VALUE;
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("file.xD", data);
        // when + then
        assertThrows(NotFoundException.class, () -> memberService.updateAvatar(id, file));
    }

}
