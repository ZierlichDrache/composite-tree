package pl.solejnik.compositetree.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.solejnik.compositetree.service.ComponentService;

@ExtendWith(MockitoExtension.class)
public class ComponentControllerTest {

    @Mock
    private ComponentService componentService;

    private ComponentController componentController;

    @BeforeEach
    public void setup() {
        componentController = new ComponentController(componentService);
    }

    @Test
    public void should_callService_whenGetRootComponent() {

        // when
        componentController.getRootComponent();

        // then
        Mockito.verify(componentService, Mockito.times(1)).getRootComponent();
    }

    @Test
    public void should_callService_whenCreateLeafForComponentWithId() {

        // when
        componentController.createLeafForComponentWithId(1L);

        // then
        Mockito
                .verify(componentService, Mockito.times(1))
                .addNewLeafToComponent(Mockito.anyLong());
    }

    @Test
    public void should_callService_whenUpdateComponentValue() {

        // when
        componentController.updateComponentValue(1L, 1L);

        // then
        Mockito
                .verify(componentService, Mockito.times(1))
                .updateComponentValue(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void should_callService_whenDeleteComponent() {

        // when
        componentController.deleteComponent(1L);

        // then
        Mockito.verify(componentService, Mockito.times(1)).removeComponent(Mockito.anyLong());
    }
}
