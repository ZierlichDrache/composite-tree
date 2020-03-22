package pl.solejnik.compositetree.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.solejnik.compositetree.service.ComponentService;
import pl.solejnik.compositetree.to.ComponentTO;

/**
 * Rest endpoints for {@link ComponentTO} dtos
 */
@RestController
@RequestMapping("/component")
@CrossOrigin(origins = "http://localhost:4200")
public class ComponentController {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentController.class);

    private ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    /**
     * Call for the root component
     *
     * @return the root component
     */
    @GetMapping(value = "/root", produces = "application/json")
    public ResponseEntity<ComponentTO> getRootComponent() {
        LOGGER.info("Start calling rest for getting root component");
        final ComponentTO rootComponent = componentService.getRootComponent();

        LOGGER.info("End of calling rest for getting root component");
        return new ResponseEntity<>(rootComponent, HttpStatus.OK);
    }

    /**
     * Call for create a new leaf to the given component id
     *
     * @param id id of given component
     * @return updated component
     */
    @PostMapping(value = "/{id}/create-leaf")
    public ResponseEntity<ComponentTO> createLeafForComponentWithId(@PathVariable final Long id) {
        LOGGER.info("Start calling rest for creating new leaf to the component with id: {}", id);
        final ComponentTO updatedComponent = componentService.addNewLeafToComponent(id);

        LOGGER.info("End of calling rest for creating new leaf to the component with id: {}", id);
        return new ResponseEntity<>(updatedComponent, HttpStatus.CREATED);
    }

    /**
     * Call for update the given component value
     *
     * @param id       id of component to be updated
     * @param newValue new value
     * @return status 202 wrapper
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComponentTO> updateComponentValue(@PathVariable final Long id,
                                                            @RequestParam final Long newValue) {
        LOGGER.info("Start calling rest for updating component value for the id: {} of the new value: {}",
                id, newValue);
        componentService.updateComponentValue(id, newValue);

        LOGGER.info("End of calling rest for updating component value for the id: {} of the new value: {}",
                id, newValue);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * Call for delete component with the given id
     *
     * @param id id of component to be deleted
     * @return status 204 wrapper
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ComponentTO> deleteComponent(@PathVariable final Long id) {
        LOGGER.info("Start calling rest for removing component with id: {}", id);
        componentService.removeComponent(id);

        LOGGER.info("End of calling rest for removing component with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Call for update the root component
     *
     * @param newTO transfer object with new state of the root component
     * @return updated root component
     */
    @PutMapping("/root")
    public ResponseEntity<ComponentTO> updateRootComponent(@RequestBody final ComponentTO newTO) {
        LOGGER.info("Start calling rest for update root component");
        final ComponentTO updatedTO = componentService.updateRootComponent(newTO);

        LOGGER.info("End of calling rest for update root component");
        return new ResponseEntity<>(updatedTO, HttpStatus.OK);
    }
}
