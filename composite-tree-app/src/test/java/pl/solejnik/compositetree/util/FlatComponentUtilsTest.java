package pl.solejnik.compositetree.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.solejnik.compositetree.entity.Composite;
import pl.solejnik.compositetree.to.ComponentTO;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FlatComponentUtilsTest {

    @Test
    public void should_throwNullpointerSortingFlagComponentWithoutId() {

        // given
        final Composite c1 = new Composite();
        final Composite c2 = new Composite();
        c1.addChild(c2);

        // when
        assertThrows(NullPointerException.class, () -> {
            FlatComponentUtils.sortedFlatComponent(c1);
        });

        // then
        // nothing
    }

    @Test
    public void should_sortFlatComponentTO() {

        // given
        final ComponentTO c1 = new ComponentTO();
        final ComponentTO c2 = new ComponentTO();
        c1.setId(5L);
        c2.setId(1L);
        c1.setChildren(Collections.singletonList(c2));

        // when
        List<ComponentTO> sorted = FlatComponentUtils.sortedFlatComponentTO(c1);

        // then
        assertEquals(c2, sorted.get(0));
        assertEquals(c1, sorted.get(1));
    }
}
