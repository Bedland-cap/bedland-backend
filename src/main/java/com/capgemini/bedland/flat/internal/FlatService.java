package com.capgemini.bedland.flat.internal;

interface FlatService {

    FlatDto create(FlatDto request);

    void delete(Long id);

    FlatDto update(FlatDto request);

}
