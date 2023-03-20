package com.capgemini.bedland.manager.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.image.ImageUtil;
import com.capgemini.bedland.manager.api.ManagerEntity;
import com.capgemini.bedland.manager.api.ManagerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
class ManagerServiceImpl implements ManagerService, ManagerProvider {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public List<ManagerDto> getAll() {
        return managerMapper.entities2DTOs(managerRepository.findAll());
    }

    @Override
    public ManagerDto getById(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        return managerMapper.entity2Dto(managerRepository.findById(id)
                                                         .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public byte[] getAvatarByManagerId(Long id) {
        ManagerEntity managerEntity = managerRepository.findById(id)
                                                       .orElseThrow(() -> new NotFoundException(id));
        return ImageUtil.decompressImage(managerEntity.getAvatar());
    }

    @Override
    public ManagerDto create(final ManagerDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Manager can't be created");
        }
        ManagerEntity newManager = managerMapper.dto2Entity(request);
        ManagerEntity createdManager = managerRepository.save(newManager);
        return managerMapper.entity2Dto(createdManager);
    }

    @Override
    public ManagerDto update(final ManagerDto request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        ManagerDto managerDto = managerMapper.entity2Dto(managerRepository.findById(request.getId())
                                                                          .orElseThrow(() -> new NotFoundException(
                                                                                  request.getId())));
        if (request.getAvatar() == null) {
            request.setAvatar(managerDto.getAvatar());
        }
        ManagerEntity updatedManager = managerMapper.dto2Entity(request);
        return managerMapper.entity2Dto(managerRepository.save(updatedManager));
    }

    @Override
    public ManagerDto updateAvatar(Long id, MultipartFile file) {
        if (id == null || file == null) {
            throw new IllegalArgumentException("Given param is null");
        }
        ManagerEntity managerEntity = managerRepository.findById(id)
                                                       .orElseThrow(() -> new NotFoundException(id));
        try {
            managerEntity.setAvatar(ImageUtil.compressImage(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return managerMapper.entity2Dto(managerRepository.save(managerEntity));
    }

    @Override
    public void delete(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        if (!managerRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        managerRepository.deleteById(id);
    }

}
