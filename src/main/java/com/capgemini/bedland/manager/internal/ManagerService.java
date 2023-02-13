package com.capgemini.bedland.manager.internal;

interface ManagerService {

    ManagerDto create(ManagerDto request);

    void delete(Long id);

    ManagerDto update(ManagerDto request);

}
