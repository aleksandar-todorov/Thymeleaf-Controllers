package org.softuni.residentevil.service;

import org.softuni.residentevil.domain.models.service.CapitalServiceModel;

import java.util.List;

public interface CapitalService {
    List<CapitalServiceModel> findAllCapitals();
}
