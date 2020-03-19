package pl.solejnik.compositetree.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.solejnik.compositetree.entity.Component;
import pl.solejnik.compositetree.entity.Composite;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class StreamUtilTest {

    @Test
    public void should_flatternChilrParentRelation() {

        // given
        final Composite c1 = new Composite();
        final Composite c2 = new Composite();
        c1.addChild(c2);

        // when
        Stream<Component> flatten = StreamUtil.flatten(c1);

        // then
        assertEquals(2, flatten.count());
    }
}
