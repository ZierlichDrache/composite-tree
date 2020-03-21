package pl.solejnik.compositetree.util;

import pl.solejnik.compositetree.entity.Component;
import pl.solejnik.compositetree.entity.Composite;
import pl.solejnik.compositetree.to.ComponentTO;

import java.util.*;

public final class StreamUtil {

    public static Set<Component> flatComponent(final Component component) {
        if (component.isLeaf()) {
            return Collections.singleton(component);
        } else {
            HashSet<Component> set = new HashSet<>();
            set.add(component);
            ((Composite) component).getChildren().forEach(c -> set.addAll(flatComponent(c)));
            return set;
        }
    }

    public static List<Component> sortedFlatComponent(final Component component) {
        Set<Component> flatTos = flatComponent(component);
        List<Component> list = new ArrayList<>(flatTos);
        list.sort(Comparator.comparing(Component::getId));
        return list;
    }

    public static Set<ComponentTO> flatComponentTO(final ComponentTO componentTO) {
        if (componentTO.isLeaf()) {
            return Collections.singleton(componentTO);
        } else {
            HashSet<ComponentTO> set = new HashSet<>();
            set.add(componentTO);
            componentTO.getChildren().forEach(c -> set.addAll(flatComponentTO(c)));
            return set;
        }
    }

    public static List<ComponentTO> sortedFlatComponentTO(final ComponentTO componentTO) {
        Set<ComponentTO> flatTos = flatComponentTO(componentTO);
        List<ComponentTO> list = new ArrayList<>(flatTos);
        list.sort(Comparator.comparing(ComponentTO::getId));
        return list;
    }
}
