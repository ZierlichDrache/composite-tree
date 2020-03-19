package pl.solejnik.compositetree.service;

import pl.solejnik.compositetree.entity.Component;
import pl.solejnik.compositetree.to.ComponentTO;

public interface ComponentService {

    ComponentTO getRootComponent();

    void addNewLeafToComponent(Long compositeId);

    void updateComponentValue(Long componentId, Long newValue);

    void removeComponent(Long id);
}
