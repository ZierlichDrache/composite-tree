package pl.solejnik.compositetree.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import pl.solejnik.compositetree.to.ComponentTO;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
public class ComponentServiceImplIntegrationTest {

    @Autowired
    private ComponentServiceImpl service;

    @Test
    @Rollback
    public void should_addTwoLeafsToComponent() {

        // given
        final Long rootId = 1L;
        final int expectedRootLeafsAmount = 2;
        service.addNewLeafToComponent(rootId);

        // when
        ComponentTO to = service.addNewLeafToComponent(rootId);

        // then
        assertEquals(expectedRootLeafsAmount, to.getChildren().size());
    }

    @Test
    @Rollback
    public void should_updateRootsValueAfterUpdateLeafsValue() {

        // given
        final Long rootId = 1L;
        final long expectedRootValue = 2L;
        final Long newChildId = service.addNewLeafToComponent(rootId).getChildren().get(0).getId();

        // when
        service.updateComponentValue(newChildId, 2L);
        Long newRootValue = service.getRootComponent().getValue();

        // then
        assertEquals(expectedRootValue, newRootValue);
    }
}
