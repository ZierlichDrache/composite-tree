package pl.solejnik.compositetree.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.solejnik.compositetree.exception.CannotRemoveRootException;
import pl.solejnik.compositetree.exception.ComponentNotFoundException;
import pl.solejnik.compositetree.exception.InconsistentRootException;
import pl.solejnik.compositetree.repository.ComponentParentRepository;
import pl.solejnik.compositetree.repository.ComponentRepository;
import pl.solejnik.compositetree.testhelper.TestComposite;
import pl.solejnik.compositetree.testhelper.TestRootComponent;
import pl.solejnik.compositetree.to.ComponentTO;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ComponentServiceImplTest {

    @Mock
    private ComponentRepository componentRepository;

    @Mock
    private ComponentParentRepository componentParentRepository;

    private ComponentServiceImpl service;

    @BeforeEach
    public void setup() {
        service = new ComponentServiceImpl(componentRepository, componentParentRepository);
    }

    @Test
    public void should_throwException_when_componentNotFoundCallingGetRootComponent() {

        // when
        assertThrows(ComponentNotFoundException.class, () -> {
            service.getRootComponent();
        });

        // then
        // nothing
    }

    @Test
    public void should_getRootComponent() {

        // given
        Mockito.when(componentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new TestComposite()));

        // when
        ComponentTO rootComponent = service.getRootComponent();

        // then
        assertNotNull(rootComponent);
    }

    @Test
    public void should_throwException_when_componentNotFoundCallingAddNewLeafToComponent() {

        // when
        assertThrows(ComponentNotFoundException.class, () -> {
            service.addNewLeafToComponent(1L);
        });

        // then
        // nothing
    }

    @Test
    public void should_addNewLeafToComponent() {

        // given
        final TestComposite component = new TestComposite();
        component.setValue(0L);
        Mockito.when(componentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(component));
        Mockito.when(componentRepository.save(Mockito.any()))
                .thenReturn((new TestComposite()));

        // when
        ComponentTO rootComponent = service.addNewLeafToComponent(1L);

        // then
        assertNotNull(rootComponent);
        Mockito.verify(componentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void should_throwException_when_componentNotFoundCallingUpdateComponentValue() {

        // when
        assertThrows(ComponentNotFoundException.class, () -> {
            service.updateComponentValue(1L, 1L);
        });

        // then
        // nothing
    }

    @Test
    public void should_updateComponentValue() {

        // given
        final TestComposite component = new TestComposite();
        component.setValue(0L);
        Mockito.when(componentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(component));

        // when
        service.updateComponentValue(1L, 1L);

        // then
        Mockito.verify(componentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void should_throwException_when_componentNotFoundCallingRemoveComponent() {

        // when
        assertThrows(ComponentNotFoundException.class, () -> {
            service.removeComponent(1L);
        });

        // then
        // nothing
    }

    @Test
    public void should_throwException_when_componentIsRootCallingRemoveComponent() {

        // given
        Mockito.when(componentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new TestRootComponent()));

        // when
        assertThrows(CannotRemoveRootException.class, () -> {
            service.removeComponent(1L);
        });

        // then
        // nothing
    }

    @Test
    public void should_removeComponent() {

        // given
        final TestComposite component = new TestComposite();
        component.setValue(0L);
        component.setFirstParent(new TestComposite());
        Mockito.when(componentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(component));

        // when
        service.removeComponent(1L);

        // then
        Mockito.verify(componentParentRepository, Mockito.times(1))
                .deleteByComponentOrParentIds(Mockito.anySet());
        Mockito.verify(componentRepository, Mockito.times(1))
                .removeFirstParentsByIds(Mockito.anySet());
        Mockito.verify(componentRepository, Mockito.times(1))
                .deleteByIds(Mockito.anySet());
    }

    @Test
    public void should_throwException_when_componentNotFoundCallingUpdateRootComponent() {

        // given
        final ComponentTO newRoot = new ComponentTO();
        newRoot.setId(1L);

        // when
        assertThrows(ComponentNotFoundException.class, () -> {
            service.updateRootComponent(newRoot);
        });

        // then
        // nothing
    }

    @Test
    public void should_throwException_when_invalidRootChildrenLengthCallingUpdateRootComponent() {

        // given
        Mockito.when(componentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new TestComposite()));

        final ComponentTO child = new ComponentTO();
        child.setId(2L);

        final ComponentTO newRootTO = new ComponentTO();
        newRootTO.setId(1L);
        newRootTO.setChildren(Collections.singletonList(child));

        // when
        assertThrows(InconsistentRootException.class, () -> {
            service.updateRootComponent(newRootTO);
        });

        // then
        // nothing
    }

    @Test
    public void should_throwException_when_invalidRootChildIdLengthCallingUpdateRootComponent() {

        // given
        Mockito.when(componentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new TestComposite()));
        final ComponentTO newRootTO = new ComponentTO();
        newRootTO.setId(2L);

        // when
        assertThrows(InconsistentRootException.class, () -> {
            service.updateRootComponent(newRootTO);
        });

        // then
        // nothing
    }

    @Test
    public void should_updateRootComponent() {

        // given
        Mockito.when(componentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new TestComposite()));
        Mockito.when(componentRepository.save(Mockito.any()))
                .thenReturn(new TestComposite());

        final ComponentTO newRootTO = new ComponentTO();
        newRootTO.setId(1L);

        ComponentTO updatedRoot = service.updateRootComponent(newRootTO);

        // then
        assertNotNull(updatedRoot);
        Mockito.verify(componentRepository, Mockito.times(1)).save(Mockito.any());

    }
}
