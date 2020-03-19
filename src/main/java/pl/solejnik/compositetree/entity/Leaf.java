package pl.solejnik.compositetree.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.SecondaryTable;

@Entity
@DiscriminatorValue("L")
@SecondaryTable(name = "leaf")
public class Leaf extends Component {

    @Column(table = "leaf")
    private Long pathLength;

    public Long getPathLength() {
        return pathLength;
    }

    public void setPathLength(final Long pathLength) {
        this.pathLength = pathLength;
    }

    public void calculatePathLength() {
        this.setPathLength(getParents().stream().map(Composite::getValue).reduce(0L, Long::sum));
        this.setPathLength(getPathLength() + getValue());
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
