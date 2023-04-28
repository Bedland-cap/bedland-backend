package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.OwnerDto;
import com.capgemini.bedland.entities.OwnerEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.OwnerMapper;
import com.capgemini.bedland.providers.OwnerProvider;
import com.capgemini.bedland.repositories.OwnerRepository;
import com.capgemini.bedland.services.OwnerService;
import com.capgemini.bedland.utilities.ImageUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Transactional
@Service
public class OwnerServiceImpl implements OwnerService, OwnerProvider {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private OwnerMapper ownerMapper;


    @Override
    public List<OwnerDto> getAll() {
        return ownerMapper.entities2DTOs(ownerRepository.findAll());
    }

    @Override
    public OwnerDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        return ownerMapper.entity2Dto(ownerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public byte[] getAvatarByOwnerId(Long id) {
        OwnerEntity ownerEntity = ownerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        return ImageUtil.decompressImage(ownerEntity.getAvatar());
    }

    @Override
    public OwnerDto create(OwnerDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Owner can't be null");
        }
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Member can't be created");
        }
        return ownerMapper.entity2Dto(ownerRepository.save(ownerMapper.dto2Entity(request)));
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        if (!ownerRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        ownerRepository.deleteById(id);
    }

    @Override
    public OwnerDto update(OwnerDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Owner can't be null");
        }
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        OwnerDto dto = ownerMapper.entity2Dto(ownerRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(
                        request.getId())));
        if (request.getAvatar() == null) {
            request.setAvatar(dto.getAvatar());
        }
        return ownerMapper.entity2Dto(ownerRepository.save(ownerMapper.dto2Entity(request)));
    }

    @Override
    public OwnerDto updateAvatar(Long id, MultipartFile file) {
        if (id == null || file == null || !ownerRepository.existsById(id)) {
            throw new IllegalArgumentException("incorrect params to update owner's avatar");
        }
        OwnerEntity ownerEntity = ownerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        try {
            ownerEntity.setAvatar(ImageUtil.compressImage(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ownerMapper.entity2Dto(ownerRepository.save(ownerEntity));
    }

}
