package pl.solejnik.compositetree.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import pl.solejnik.compositetree.to.ComponentTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ComponentControllerIntegrationTest {

    private final static String LOCALHOST = "http://localhost:";
    /**
     * Random port for our application
     */
    @LocalServerPort
    private int port;

    /**
     * Rest client for our application
     */
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void should_findRootComponent() {

        // when
        ResponseEntity<ComponentTO> response = restTemplate
                .getForEntity(LOCALHOST + port + "/component/root", ComponentTO.class);

        // then
        assertNotNull(response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void should_addLeafToNotExistingComponent() {

        // when
        ResponseEntity<ComponentTO> response = restTemplate
                .postForEntity(LOCALHOST + port + "/component/1/create-leaf", null, ComponentTO.class);

        // then
        assertNotNull(response.getBody().getId());
        assertEquals(1, response.getBody().getChildren().size());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void should_updateComponentValueForExistingComponent() {

        // given
        final Long newValue = 5L;

        // when
        restTemplate.put(LOCALHOST + port + "/component/1?newValue=" + newValue, ComponentTO.class);
        ResponseEntity<ComponentTO> response = restTemplate
                .getForEntity(LOCALHOST + port + "/component/root", ComponentTO.class);

        // then
        assertEquals(newValue, response.getBody().getValue());
    }

    @Test
    public void should_deleteComponentForExistingComponentIsCalled() {

        // given
        final int expectedChildrenAmount = 0;
        final Long childId = restTemplate
                .postForEntity(LOCALHOST + port + "/component/1/create-leaf", null, ComponentTO.class)
                .getBody().getChildren().get(0).getId();

        // when
        restTemplate.delete(LOCALHOST + port + "/component/" + childId, ComponentTO.class);
        ResponseEntity<ComponentTO> response = restTemplate
                .getForEntity(LOCALHOST + port + "/component/root", ComponentTO.class);

        // then
        assertEquals(expectedChildrenAmount, response.getBody().getChildren().size());
    }

    @Test
    public void should_updateRootComponent() {

        // given
        final Long newValue = 2L;
        final ComponentTO to = new ComponentTO();
        to.setId(1L);
        to.setValue(newValue);

        // when
        restTemplate.put(LOCALHOST + port + "/component/root", to, ComponentTO.class);
        ResponseEntity<ComponentTO> response = restTemplate
                .getForEntity(LOCALHOST + port + "/component/root", ComponentTO.class);

        // then
        assertEquals(newValue, response.getBody().getValue());
    }
}
