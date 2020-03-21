package pl.solejnik.compositetree.service;

import pl.solejnik.compositetree.to.ComponentTO;

public interface ComponentService {

    ComponentTO getRootComponent();

    ComponentTO addNewLeafToComponent(Long compositeId);

    void updateComponentValue(Long componentId, Long newValue);

    void removeComponent(Long id);

    ComponentTO updateRootComponent(ComponentTO newTO);
}
