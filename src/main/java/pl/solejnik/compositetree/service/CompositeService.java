package pl.solejnik.compositetree.service;

import pl.solejnik.compositetree.entity.Component;

public interface CompositeService {

    Component addNewLeafToComponent(Long compositeId);

    void updateComponentValue(Long componentId, Long newValue);

    void removeComponent(Long id);
}
