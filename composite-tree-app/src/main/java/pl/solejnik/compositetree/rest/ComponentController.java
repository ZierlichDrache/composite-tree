package pl.solejnik.compositetree.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.solejnik.compositetree.service.ComponentService;
import pl.solejnik.compositetree.to.ComponentTO;

@RestController
@RequestMapping("/component")
@CrossOrigin(origins = "http://localhost:4200")
public class ComponentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentController.class);

    private ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping(value = "/root", produces = "application/json")
    public ResponseEntity<ComponentTO> getRootComponent() {
        LOGGER.info("Start calling rest for getting root component");
        final ComponentTO rootComponent = componentService.getRootComponent();

        LOGGER.info("End of calling rest for getting root component");
        return new ResponseEntity<>(rootComponent, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/create-leaf")
    public ResponseEntity<ComponentTO> createLeafForComponentWithId(@PathVariable final Long id) {
        LOGGER.info("Start calling rest for creating new leaf to the component with id: {}", id);
        final ComponentTO updatedComponent = componentService.addNewLeafToComponent(id);

        LOGGER.info("End of calling rest for creating new leaf to the component with id: {}", id);
        return new ResponseEntity<>(updatedComponent, HttpStatus.CREATED);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<ComponentTO> deleteComponent(@PathVariable final Long id) {
        LOGGER.info("Start calling rest for removing component with id: {}", id);
        componentService.removeComponent(id);

        LOGGER.info("End of calling rest for removing component with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/root")
    public ResponseEntity<ComponentTO> updateRootComponent(@RequestBody final ComponentTO newTO) {
        LOGGER.info("Start calling rest for update root component");
        final ComponentTO updatedTO = componentService.updateRootComponent(newTO);

        LOGGER.info("End of calling rest for update root component");
        return new ResponseEntity<>(updatedTO, HttpStatus.OK);
    }
}
