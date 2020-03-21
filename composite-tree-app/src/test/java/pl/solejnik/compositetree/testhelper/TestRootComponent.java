package pl.solejnik.compositetree.testhelper;

import pl.solejnik.compositetree.entity.Component;

public class TestRootComponent extends Component {

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
