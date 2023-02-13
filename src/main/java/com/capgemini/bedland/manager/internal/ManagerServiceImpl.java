package com.capgemini.bedland.manager.internal;

import com.capgemini.bedland.manager.api.ManagerEntity;
import com.capgemini.bedland.manager.api.ManagerProvider;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
class ManagerServiceImpl implements ManagerService, ManagerProvider {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public List<ManagerDto> getAll() {
        return managerMapper.entities2Dtos(managerRepository.findAll());
    }

    @Override
    public ManagerDto getById(final Long id) {
        return managerMapper.entity2Dto(managerRepository.findById(id).get());
    }

    @Override
    public ManagerDto create(final ManagerDto request) {
        if(request.getId() != null) throw new IllegalArgumentException("Given request contains an ID. Manager can't be created");
        ManagerEntity newManager = managerMapper.dto2Entity(request);
        ManagerEntity createdManager = managerRepository.save(newManager);
        return managerMapper.entity2Dto(createdManager);
    }

    @Override
    public ManagerDto update(final ManagerDto request) {
        if(request.getId() == null) throw new IllegalArgumentException("Given request has no ID");
        if(!managerRepository.existsById(request.getId())) {
            throw new NoSuchElementException("Manager with ID = " + request.getId() + " does not exist");
            // TODO: add error codes (404)
        }
        ManagerEntity updatedManager = managerRepository.findById(request.getId()).orElseThrow();
        updatedManager.setLogin(request.getLogin());
        updatedManager.setName(request.getName());
        updatedManager.setLastName(request.getLastName());
        updatedManager.setEmail(request.getEmail());
        updatedManager.setPassword(request.getPassword());
        updatedManager.setPhoneNumber(request.getPhoneNumber());
        return managerMapper.entity2Dto(managerRepository.save(updatedManager));
    }

    @Override
    public void delete(final Long id) {
        managerRepository.deleteById(id);
    }

}
