package pl.solejnik.compositetree.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.solejnik.compositetree.service.ComponentService;
import pl.solejnik.compositetree.to.ComponentTO;

@RestController
public class ComponentController {

    private ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping("/component/root")
    public ResponseEntity<ComponentTO> getRootComponent() {
        return new ResponseEntity<ComponentTO>(componentService.getRootComponent(), HttpStatus.OK);
    }

    @PostMapping("/component/create-leaf")
    public ResponseEntity<ComponentTO> createLeafForComponentWithId(@RequestParam Long id) {
        componentService.addNewLeafToComponent(id);
        return new ResponseEntity<ComponentTO>(HttpStatus.CREATED);
    }

    @PutMapping("/component/update")
    public ResponseEntity<ComponentTO> updateComponentValue(@RequestParam final Long id, @RequestParam final Long newValue) {
        componentService.updateComponentValue(id, newValue);
        return new ResponseEntity<ComponentTO>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/component/delete")
    public ResponseEntity<ComponentTO> deleteComponent(@RequestParam final Long id) {
        componentService.removeComponent(id);
        return new ResponseEntity<ComponentTO>(HttpStatus.NO_CONTENT);
    }
}
