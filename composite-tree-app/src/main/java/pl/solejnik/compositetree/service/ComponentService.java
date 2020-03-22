package pl.solejnik.compositetree.service;

import pl.solejnik.compositetree.entity.Component;
import pl.solejnik.compositetree.to.ComponentTO;

/**
 * Service interface to entity {@link Component} and transfer object {@link ComponentTO}
 */
public interface ComponentService {

    /**
     * Get the root component
     *
     * @return root component
     */
    ComponentTO getRootComponent();

    /**
     * Add a new leaf to the component
     *
     * @param compositeId component id
     * @return updated component
     */
    ComponentTO addNewLeafToComponent(Long compositeId);

    /**
     * Update component with the new value
     *
     * @param componentId component id
     * @param newValue    new value
     */
    void updateComponentValue(Long componentId, Long newValue);

    /**
     * Remove component
     *
     * @param id id of the given component
     */
    void removeComponent(Long id);

    /**
     * Update the root component
     *
     * @param newTO new state of the root component
     * @return updated component
     */
    ComponentTO updateRootComponent(ComponentTO newTO);
}
