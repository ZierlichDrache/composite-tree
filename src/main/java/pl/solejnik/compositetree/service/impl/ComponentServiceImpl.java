package pl.solejnik.compositetree.service.impl;

import org.springframework.stereotype.Service;
import pl.solejnik.compositetree.entity.Component;
import pl.solejnik.compositetree.entity.Composite;
import pl.solejnik.compositetree.entity.Leaf;
import pl.solejnik.compositetree.exception.CannotRemoveRootException;
import pl.solejnik.compositetree.exception.ComponentNotFoundException;
import pl.solejnik.compositetree.repository.ComponentParentRepository;
import pl.solejnik.compositetree.repository.ComponentRepository;
import pl.solejnik.compositetree.service.ComponentService;
import pl.solejnik.compositetree.util.StreamUtil;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ComponentServiceImpl implements ComponentService {

    private ComponentRepository componentRepository;

    private ComponentParentRepository componentParentRepository;

    public ComponentServiceImpl(final ComponentRepository componentRepository,
                                final ComponentParentRepository componentParentRepository) {
        this.componentRepository = componentRepository;
        this.componentParentRepository = componentParentRepository;
    }

    @Override
    public Component addNewLeafToComponent(final Long compositeId) {
        final Component found = componentRepository
                .findById(compositeId)
                .orElseThrow(() -> new ComponentNotFoundException(compositeId));

        final Composite source = updateLeaf(found);

        final Leaf newLeaf = new Leaf();
        newLeaf.setValue(0L);

        final long newChildOrder = source.getChildren()
                .stream()
                .map(Component::getChildOrder)
                .max(Comparator.comparing(Long::valueOf))
                .orElse(0L) + 1L;
        newLeaf.setChildOrder(newChildOrder);

        source.addChild(newLeaf);

        newLeaf.calculateValueFromParents();

        return componentRepository.save(source);
    }

    @Override
    public void updateComponentValue(final Long componentId, final Long newValue) {
        final Component found = componentRepository
                .findById(componentId)
                .orElseThrow(() -> new ComponentNotFoundException(componentId));

        if (found.isRoot()) {
            throw new CannotRemoveRootException();
        }

        if (found.isLeaf()) {
            updateLeafValue((Leaf) found, newValue);
        } else {
            updateCompositeValue((Composite) found, newValue);
        }
    }

    @Override
    public void removeComponent(final Long componentId) {
        final Component found = componentRepository
                .findById(componentId)
                .orElseThrow(() -> new ComponentNotFoundException(componentId));
        final Set<Long> componentsIds = StreamUtil
                .flatten(found)
                .map(Component::getId).collect(Collectors.toSet());

        updateParentWithoutOtherChildren(found, componentsIds);

        componentParentRepository.deleteByComponentOrParentIds(componentsIds);
        componentRepository.deleteByIds(componentsIds);
    }


    private void adjustLeafsValues(final Component component, final long delta) {
        if (delta != 0) {
            final Set<Long> collect = StreamUtil
                    .flatten(component)
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

            found.getParents().forEach(newComposite::addParent);
            found.removeAllParents();

            componentRepository.save(found);
            componentRepository.deleteById(found.getId());
            return newComposite;
        } else {
            return (Composite) found;
        }
    }

    private void updateParentWithoutOtherChildren(final Component component, final Set<Long> componentsIds) {
        final Composite parentWithoutOtherChildren = findParentWithoutOtherChildren(component);

        if (parentWithoutOtherChildren != null) {
            Leaf newLeaf = new Leaf();
            newLeaf.setValue(parentWithoutOtherChildren.getValue());
            newLeaf.setChildOrder(parentWithoutOtherChildren.getChildOrder());

            parentWithoutOtherChildren.getParents().forEach(newLeaf::addParent);

            parentWithoutOtherChildren.removeRelations();

            componentsIds.add(parentWithoutOtherChildren.getId());

            componentRepository.save(newLeaf);
        }
    }

    private Composite findParentWithoutOtherChildren(final Component component) {
        for (final Composite parent : component.getParents()) {
            if (!parent.isRoot()) {
                long otherChildrenAmount = parent.getChildren()
                        .stream()
                        .filter(c -> !c.getId().equals(component.getId()))
                        .count();
                if (otherChildrenAmount == 0) {
                    return parent;
                }
            }
        }
        return null;
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
            updateComponentValue(leaf.getParents().get(0).getId(), remainingDelta);
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
