package pl.solejnik.compositetree.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.solejnik.compositetree.entity.Component;
import pl.solejnik.compositetree.entity.Composite;
import pl.solejnik.compositetree.entity.Leaf;
import pl.solejnik.compositetree.exception.CannotRemoveRootException;
import pl.solejnik.compositetree.exception.ComponentNotFoundException;
import pl.solejnik.compositetree.exception.InconsistentRootException;
import pl.solejnik.compositetree.repository.ComponentParentRepository;
import pl.solejnik.compositetree.repository.ComponentRepository;
import pl.solejnik.compositetree.service.ComponentService;
import pl.solejnik.compositetree.to.ComponentTO;
import pl.solejnik.compositetree.to.mapper.ComponentMapper;
import pl.solejnik.compositetree.util.FlatComponentUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComponentServiceImpl implements ComponentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentServiceImpl.class);

    private ComponentRepository componentRepository;

    private ComponentParentRepository componentParentRepository;

    public ComponentServiceImpl(final ComponentRepository componentRepository,
                                final ComponentParentRepository componentParentRepository) {
        this.componentRepository = componentRepository;
        this.componentParentRepository = componentParentRepository;
    }

    @Override
    public ComponentTO getRootComponent() {
        LOGGER.info("Start getting root component");

        final Component component = componentRepository
                .findById(1L)
                .orElseThrow(() -> new ComponentNotFoundException(1L));

        LOGGER.info("End of getting root component");
        return ComponentMapper.map(component);
    }

    @Override
    public ComponentTO addNewLeafToComponent(final Long compositeId) {
        LOGGER.info("Start adding new leaf to the component with the id: {}", compositeId);

        final Component found = componentRepository
                .findById(compositeId)
                .orElseThrow(() -> new ComponentNotFoundException(compositeId));

        final Composite source = updateLeaf(found);

        final Leaf newLeaf = new Leaf();
        newLeaf.setValue(0L);
        newLeaf.setFirstParent(source);

        final long newChildOrder = source.getChildren()
                .stream()
                .map(Component::getChildOrder)
                .max(Comparator.comparing(Long::valueOf))
                .orElse(0L) + 1L;
        newLeaf.setChildOrder(newChildOrder);

        source.addChild(newLeaf);

        newLeaf.calculateValueFromParents();
        final Component savedComponent = componentRepository.save(source);

        LOGGER.info("End of adding new leaf to the component with the id: {}", compositeId);
        return ComponentMapper.map(savedComponent);
    }

    @Override
    public void updateComponentValue(final Long componentId, final Long newValue) {
        LOGGER.info("Start updating component value for the id: {} of the new value: {}", componentId, newValue);

        final Component found = componentRepository
                .findById(componentId)
                .orElseThrow(() -> new ComponentNotFoundException(componentId));

        if (found.isLeaf()) {
            updateLeafValue((Leaf) found, newValue);
        } else {
            updateCompositeValue((Composite) found, newValue);
        }

        LOGGER.info("End of updating component value for the id: {} of the new value: {}", componentId, newValue);
    }

    @Override
    public void removeComponent(final Long componentId) {
        LOGGER.info("Start remove component with the id: {}", componentId);
        final Component found = componentRepository
                .findById(componentId)
                .orElseThrow(() -> new ComponentNotFoundException(componentId));

        if (found.isRoot()) {
            throw new CannotRemoveRootException();
        }

        final Set<Long> componentsIds = FlatComponentUtils
                .flatComponent(found)
                .stream()
                .map(Component::getId).collect(Collectors.toSet());

        updateParentWithoutOtherChildren(found, componentsIds);

        componentParentRepository.deleteByComponentOrParentIds(componentsIds);
        componentRepository.removeFirstParentsByIds(componentsIds);
        componentRepository.deleteByIds(componentsIds);

        LOGGER.info("End of remove component with the id: {}", componentId);
    }

    @Override
    public ComponentTO updateRootComponent(final ComponentTO newRootTO) {
        LOGGER.info("Start updating root component");

        final Component oldRoot = componentRepository
                .findById(newRootTO.getId())
                .orElseThrow(() -> new ComponentNotFoundException(newRootTO.getId()));

        final List<Component> components = FlatComponentUtils.sortedFlatComponent(oldRoot);
        final List<ComponentTO> tos = FlatComponentUtils.sortedFlatComponentTO(newRootTO);

        if (components.size() != tos.size()) {
            throw new InconsistentRootException();
        }

        for (int i = 0; i < components.size(); i++) {
            final Component component = components.get(i);
            final ComponentTO componentTO = tos.get(i);
            if (!component.getId().equals(componentTO.getId())) {
                throw new InconsistentRootException();
            }
            component.setValue(componentTO.getValue());
        }

        final Component newRoot = componentRepository.save(oldRoot);

        LOGGER.info("End of updating root component");
        return ComponentMapper.map(newRoot);
    }

    private void adjustLeafsValues(final Component component, final long delta) {
        if (delta != 0) {
            final Set<Long> collect = FlatComponentUtils
                    .flatComponent(component)
                    .stream()
                    .filter(Component::isLeaf)
                    .map(Component::getId)
                    .collect(Collectors.toSet());
            componentRepository.updateValuesByDeltaAndIds(delta, collect);
        }
    }

    private Composite updateLeaf(final Component found) {
        if (found.isLeaf()) {
            final Composite newComposite = new Composite();
            newComposite.setChildOrder(found.getChildOrder());
            newComposite.setValue(found.getValue());
            newComposite.setFirstParent(found.getFirstParent());

            found.getParents().forEach(newComposite::addParent);
            found.removeAllParents();

            componentRepository.save(found);
            componentParentRepository.deleteByComponentOrParentIds(Collections.singleton(found.getId()));
            componentRepository.deleteByIds(Collections.singleton(found.getId()));
            return newComposite;
        } else {
            return (Composite) found;
        }
    }

    private void updateParentWithoutOtherChildren(final Component component, final Set<Long> componentsIds) {
        final boolean hasParentNoMoreChildren = hasFirstParentNoMoreChildren(component);

        if (hasParentNoMoreChildren) {
            Leaf newLeaf = new Leaf();
            newLeaf.setValue(component.getFirstParent().getValue());
            newLeaf.setChildOrder(component.getFirstParent().getChildOrder());
            newLeaf.setFirstParent(component.getFirstParent().getFirstParent());

            component.getFirstParent().getParents().forEach(newLeaf::addParent);

            component.getFirstParent().removeRelations();

            componentsIds.add(component.getFirstParent().getId());

            componentRepository.save(newLeaf);
        }
    }

    private boolean hasFirstParentNoMoreChildren(final Component component) {

        final Composite firstParent = component
                .getFirstParent();
        return !firstParent.isRoot() && firstParent
                .getChildren()
                .stream()
                .anyMatch(c -> !c.getId().equals(component.getId()));
    }

    private void updateCompositeValue(final Composite composite, Long newValue) {
        final long delta = newValue - composite.getValue();

        composite.setValue(newValue);

        componentRepository.save(composite);

        adjustLeafsValues(composite, delta);
    }

    private void updateLeafValue(final Leaf leaf, final Long newValue) {
        long remainingDelta = newValue - leaf.getValue();

        if (remainingDelta > 0) {
            updateComponentValue(leaf.getFirstParent().getId(), remainingDelta);
        } else {
            final Map<Composite, Long> parentsToUpdate = new HashMap<>();
            for (Composite parent : leaf.getParents()) {
                remainingDelta = remainingDelta + parent.getValue();
                parentsToUpdate.put(parent, Math.max(0L, remainingDelta));
                if (remainingDelta >= 0) {
                    break;
                }
            }
            parentsToUpdate.forEach((k, v) -> updateComponentValue(k.getId(), v));
        }
    }
}
