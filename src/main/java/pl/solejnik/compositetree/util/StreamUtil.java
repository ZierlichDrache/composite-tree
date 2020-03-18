package pl.solejnik.compositetree.util;

import pl.solejnik.compositetree.entity.Component;
import pl.solejnik.compositetree.entity.Composite;

import java.util.stream.Stream;

public final class StreamUtil {

    public static Stream<Component> flatten(Component component) {
        if (component.isLeaf()) {
            return Stream.of(component);
        } else {
            return ((Composite) component).getChildren().stream()
                    .map(StreamUtil::flatten)
                    .reduce(Stream.of(component), Stream::concat);
        }
    }
}
