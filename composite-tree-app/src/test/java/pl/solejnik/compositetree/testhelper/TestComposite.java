package pl.solejnik.compositetree.testhelper;

import pl.solejnik.compositetree.entity.Composite;

public class TestComposite extends Composite {

    @Override
    public Long getId() {
        return 1L;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
