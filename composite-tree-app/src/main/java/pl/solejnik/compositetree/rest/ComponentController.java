package pl.solejnik.compositetree.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.solejnik.compositetree.service.ComponentService;
import pl.solejnik.compositetree.to.ComponentTO;

@RestController
@RequestMapping("/component")
@CrossOrigin(origins = "http://localhost:4200")
public class ComponentController {

    private ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping(value = "/root", produces = "application/json")
    public ResponseEntity<ComponentTO> getRootComponent() {
        return new ResponseEntity<>(componentService.getRootComponent(), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/create-leaf", produces = "application/json")
    public ResponseEntity<ComponentTO> createLeafForComponentWithId(@PathVariable final Long id) {
        final ComponentTO updatedComponent = componentService.addNewLeafToComponent(id);
        return new ResponseEntity<>(updatedComponent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponentTO> updateComponentValue(@PathVariable final Long id,
                                                            @RequestParam final Long newValue) {
        componentService.updateComponentValue(id, newValue);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ComponentTO> deleteComponent(@PathVariable final Long id) {
        componentService.removeComponent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
