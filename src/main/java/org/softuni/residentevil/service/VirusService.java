package org.softuni.residentevil.service;

import org.softuni.residentevil.domain.models.service.VirusServiceModel;

import java.util.List;

public interface VirusService {

    VirusServiceModel spreadVirus(VirusServiceModel virusServiceModel);
    List<VirusServiceModel> findAllViruses();
    boolean deleteVirusById(String id);
    VirusServiceModel findById(String id);
    void editVirus(VirusServiceModel virusServiceModel);
}
