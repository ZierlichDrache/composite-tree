package pl.solejnik.compositetree.to.mapper;

import pl.solejnik.compositetree.entity.Component;
import pl.solejnik.compositetree.entity.Composite;
import pl.solejnik.compositetree.to.ComponentTO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class ComponentMapper {
    private ComponentMapper() {
    }

    public static ComponentTO map(final Component component) {
        final ComponentTO to = new ComponentTO();
        to.setId(component.getId());
        to.setValue(component.getValue());
        to.setChildOrder(component.getChildOrder());
        if (!component.isLeaf()) {
            final List<ComponentTO> toChildren = ((Composite) component).getChildren()
                    .stream()
                    .filter(c -> c.getFirstParent().getId().equals(component.getId()))
                    .map(ComponentMapper::map)
                    .sorted(Comparator.comparing(ComponentTO::getChildOrder))
                    .collect(Collectors.toList());
            to.setChildren(toChildren);
        }
        return to;
    }
}
