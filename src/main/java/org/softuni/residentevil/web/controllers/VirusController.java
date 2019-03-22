package org.softuni.residentevil.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.residentevil.domain.models.binding.VirusAddBindingModel;
import org.softuni.residentevil.domain.models.service.VirusServiceModel;
import org.softuni.residentevil.domain.models.view.CapitalListViewModel;
import org.softuni.residentevil.domain.models.view.VirusEditViewModel;
import org.softuni.residentevil.domain.models.view.VirusListViewModel;
import org.softuni.residentevil.service.CapitalService;
import org.softuni.residentevil.service.VirusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/viruses")
public class VirusController extends BaseController {

    private final CapitalService capitalService;
    private final VirusService virusService;
    private final ModelMapper modelMapper;

    @Autowired
    public VirusController(CapitalService capitalService, VirusService virusService, ModelMapper modelMapper) {
        this.capitalService = capitalService;
        this.virusService = virusService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public ModelAndView add(ModelAndView modelAndView, @ModelAttribute(name = "bindingModel") VirusAddBindingModel bindingModel) {
        modelAndView.addObject("bindingModel", bindingModel);
        modelAndView.addObject("capitals",
                this.capitalService.findAllCapitals().stream()
                        .map(c -> this.modelMapper.map(c, CapitalListViewModel.class))
                        .collect(Collectors.toList()));
        return super.view("add-virus", modelAndView);

    }

    @PostMapping("/add")
    public ModelAndView addConfirm(@Valid @ModelAttribute(name = "bindingModel") VirusAddBindingModel bindingModel,
                                   BindingResult bindingResult, ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("bindingModel", bindingModel);
            return super.view("add-virus", modelAndView);

        }
        VirusServiceModel virusServiceModel = this.virusService
                .spreadVirus(this.modelMapper.map(bindingModel, VirusServiceModel.class));
        if (virusServiceModel == null) {
            throw new IllegalArgumentException("Spread virus went wrong!");
        }
        return super.redirect("/viruses/show");
    }

    @GetMapping("/show")
    public ModelAndView show(ModelAndView modelAndView) {
        List<VirusListViewModel> viruses = this.virusService.findAllViruses()
                .stream()
                .map(v -> this.modelMapper.map(v, VirusListViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("viruses", viruses);
        return super.view("show-virus", modelAndView);
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable(name = "id") String id, ModelAndView modelAndView) {
        this.virusService.deleteVirusById(id);
        return super.redirect("/viruses/show");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") String id, ModelAndView modelAndView,
                             @ModelAttribute(name = "virusEditBindingModel") VirusEditViewModel virusEditBindingModel) {
        List<CapitalListViewModel> capitals = this.capitalService.findAllCapitals().stream()
                .map(c -> this.modelMapper.map(c, CapitalListViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("capitals", capitals);

        VirusEditViewModel editViewModel = this.modelMapper.map(this.virusService.findById(id), VirusEditViewModel.class);
        modelAndView.addObject("editVirus", editViewModel);
        return super.view("edit-virus", modelAndView);
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editConfirm(@PathVariable("id") String id,
            @Valid @ModelAttribute(name = "virusEditBindingModel") VirusEditViewModel virusEditBindingModel,
            BindingResult bindingResult, ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("virusEditBindingModel", virusEditBindingModel);
            return super.view("edit-virus", modelAndView);
        }

        VirusServiceModel serviceModel = this.modelMapper.map(virusEditBindingModel, VirusServiceModel.class);
        this.virusService.editVirus(serviceModel);
        return super.redirect("/");
    }

}
