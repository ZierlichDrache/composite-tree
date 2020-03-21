package pl.solejnik.compositetree.util;

import pl.solejnik.compositetree.entity.Component;
import pl.solejnik.compositetree.entity.Composite;

import java.util.stream.Stream;

public final class StreamUtil {

    public static Stream<Component> flatten(Component component) {
        if (component.isLeaf()) {
            return Stream.of(component);
        } else {

            return Stream.concat(
                    Stream.of(component),
                    ((Composite) component).getChildren().stream().flatMap(StreamUtil::flatten)
            );
        }
    }
}
