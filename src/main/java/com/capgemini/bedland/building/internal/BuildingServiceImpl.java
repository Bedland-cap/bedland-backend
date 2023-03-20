package com.capgemini.bedland.building.internal;

import com.capgemini.bedland.building.api.BuildingEntity;
import com.capgemini.bedland.building.api.BuildingProvider;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.image.ImageUtil;
import com.capgemini.bedland.manager.api.ManagerEntity;
import com.capgemini.bedland.manager.internal.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService, BuildingProvider {

    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private BuildingMapper buildingMapper;
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public List<BuildingDto> getAll() {
        return buildingMapper.entities2DTOs(buildingRepository.findAll());
    }

    @Override
    public BuildingDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        return buildingMapper.entity2Dto(buildingRepository.findById(id)
                                                           .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public byte[] getPhotoByBuildingId(Long id) {
        BuildingEntity buildingEntity = buildingRepository.findById(id)
                                                       .orElseThrow(() -> new NotFoundException(id));
        return ImageUtil.decompressImage(buildingEntity.getPhoto());
    }

    @Override
    public BuildingDto create(BuildingDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Building can't be created");
        }
        BuildingEntity createBuilding = buildingRepository.save(repackDtoToEntity(request));
        return buildingMapper.entity2Dto(createBuilding);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        if (!buildingRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        buildingRepository.deleteById(id);

    }

    @Override
    public BuildingDto update(BuildingDto request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!buildingRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        BuildingEntity updateBuilding = buildingRepository.save(repackDtoToEntity(request));
        return buildingMapper.entity2Dto(updateBuilding);
    }

    @Override
    public BuildingDto updatePhoto(Long id, MultipartFile file) {
        if (id == null || file == null) {
            throw new IllegalArgumentException("Given param is null");
        }
        BuildingEntity buildingEntity =buildingRepository.findById(id)
                                                       .orElseThrow(() -> new NotFoundException(id));
        try {
            buildingEntity.setPhoto(ImageUtil.compressImage(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buildingMapper.entity2Dto(buildingRepository.save(buildingEntity));
    }

    private BuildingEntity repackDtoToEntity(BuildingDto dto) {
        BuildingEntity entity = buildingMapper.dto2Entity(dto);
        entity.setManagerEntity(managerRepository.findById(dto.getManagerId())
                                                 .orElseThrow(() -> new NotFoundException(dto.getManagerId())));
        return entity;
    }

}
