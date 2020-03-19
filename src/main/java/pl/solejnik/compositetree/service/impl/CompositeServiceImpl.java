package pl.solejnik.compositetree.service.impl;

import org.springframework.stereotype.Service;
import pl.solejnik.compositetree.entity.Component;
import pl.solejnik.compositetree.entity.Composite;
import pl.solejnik.compositetree.entity.Leaf;
import pl.solejnik.compositetree.repository.ComponentParentRepository;
import pl.solejnik.compositetree.repository.ComponentRepository;
import pl.solejnik.compositetree.repository.LeafRepository;
import pl.solejnik.compositetree.service.CompositeService;
import pl.solejnik.compositetree.util.StreamUtil;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompositeServiceImpl implements CompositeService {

    private ComponentRepository componentRepository;

    private LeafRepository leafRepository;

    private ComponentParentRepository componentParentRepository;

    public CompositeServiceImpl(final ComponentRepository componentRepository,
                                final LeafRepository leafRepository,
                                final ComponentParentRepository componentParentRepository) {
        this.componentRepository = componentRepository;
        this.leafRepository = leafRepository;
        this.componentParentRepository = componentParentRepository;
    }

    @Override
    public Component addNewLeafToComponent(final Long compositeId) {
        final Component found = componentRepository.findById(compositeId).get();

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

        newLeaf.calculatePathLength();

        return componentRepository.save(source);
    }

    @Override
    public void updateComponentValue(final Long componentId, final Long newValue) {
        final Component found = componentRepository.findById(componentId).get();

        final long delta = newValue - found.getValue();

        found.setValue(newValue);

        componentRepository.save(found);

        adjustLeafsPathLength(found, delta);
    }

    @Override
    public void removeComponent(final Long id) {
        final Component found = componentRepository.findById(id).get();
        final Set<Component> children = StreamUtil
                .flatten(found)
                .collect(Collectors.toSet());

        final Set<Long> componentsIds = new HashSet<>();
        final Set<Long> leafsIds = new HashSet<>();

        for (Component child : children) {
            componentsIds.add(child.getId());
            if (child.isLeaf()) {
                leafsIds.add(child.getId());
            }
        }


        updateParentWithoutOtherChildren(found, componentsIds);

        componentParentRepository.deleteByComponentOrParentIds(componentsIds);
        leafRepository.removeByIds(leafsIds);
        componentRepository.deleteByIds(componentsIds);
    }


    private void adjustLeafsPathLength(final Component component, final long delta) {
        if (delta > 0) {
            final Set<Long> collect = StreamUtil
                    .flatten(component)
                    .filter(Component::isLeaf)
                    .map(Component::getId)
                    .collect(Collectors.toSet());
            leafRepository.updatePathsLengthsByIds(delta, collect);
        }
    }

    private Composite updateLeaf(final Component found) {
        if (found.isLeaf()) {
            final Composite newComposite = new Composite();
            newComposite.setChildOrder(found.getChildOrder());
            newComposite.setValue(found.getValue());

            found.getParents().forEach(newComposite::addParent);
            found.removeAllParents();

            componentRepository.delete(found);
            leafRepository.deleteById(found.getId());

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

            newLeaf.calculatePathLength();

            componentsIds.add(parentWithoutOtherChildren.getId());

            componentRepository.save(newLeaf);
        }
    }

    private Composite findParentWithoutOtherChildren(final Component component) {
        for (final Composite parent : component.getParents()) {
            long otherChildrenAmount = parent.getChildren()
                    .stream()
                    .filter(c -> !c.getId().equals(component.getId()))
                    .count();
            if (otherChildrenAmount == 0) {
                return parent;
            }
        }
        return null;
    }
}
