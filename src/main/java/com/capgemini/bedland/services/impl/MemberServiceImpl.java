package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.MemberDto;
import com.capgemini.bedland.entities.MemberEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.MemberMapper;
import com.capgemini.bedland.providers.MemberProvider;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.repositories.MemberRepository;
import com.capgemini.bedland.services.MemberService;
import com.capgemini.bedland.utilities.ImageUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Transactional
@Service
public class MemberServiceImpl implements MemberService, MemberProvider {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private FlatRepository flatRepository;

    @Override
    public List<MemberDto> getAll() {
        return memberMapper.entities2DTOs(memberRepository.findAll());
    }

    @Override
    public MemberDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        return memberMapper.entity2Dto(memberRepository.findById(id)
                                                       .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public byte[] getAvatarByMemberId(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id)
                                                    .orElseThrow(() -> new NotFoundException(id));
        return ImageUtil.decompressImage(memberEntity.getAvatar());
    }

    @Override
    public MemberDto create(MemberDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Member can't be created");
        }
        MemberEntity createdMember = memberRepository.save(repackDtoToEntity(request));
        return memberMapper.entity2Dto(createdMember);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        if (!memberRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        memberRepository.deleteById(id);
    }

    @Override
    public MemberDto update(MemberDto request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!memberRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        MemberDto memberDto = memberMapper.entity2Dto(memberRepository.findById(request.getId())
                                                                      .orElseThrow(() -> new NotFoundException(
                                                                              request.getId())));
        if (request.getAvatar() == null) {
            request.setAvatar(memberDto.getAvatar());
        }
        MemberEntity updateMember = memberRepository.save(repackDtoToEntity(request));
        return memberMapper.entity2Dto(updateMember);
    }

    @Override
    public MemberDto updateAvatar(Long id, MultipartFile file) {
        if (id == null || file == null) {
            throw new IllegalArgumentException("Given param is null");
        }
        MemberDto member = memberMapper.entity2Dto(memberRepository.findById(id)
                                                                   .orElseThrow(() -> new NotFoundException(id)));
        try {
            member.setAvatar(ImageUtil.compressImage(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MemberEntity updateMember = memberRepository.save(repackDtoToEntity(member));
        return memberMapper.entity2Dto(updateMember);
    }

    private MemberEntity repackDtoToEntity(MemberDto dto) {
        MemberEntity entity = memberMapper.dto2Entity(dto);
        entity.setFlatEntity(flatRepository.findById(dto.getFlatId())
                                           .orElseThrow(() -> new NotFoundException(dto.getFlatId())));
        return entity;
    }

}