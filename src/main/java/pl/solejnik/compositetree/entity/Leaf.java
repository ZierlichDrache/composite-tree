package pl.solejnik.compositetree.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("L")
public class Leaf extends Component {

    public void calculateValueFromParents() {
        final Long calculatedValue = getParents()
                .stream()
                .map(Composite::getValue)
                .reduce(0L, Long::sum);
        this.setValue(calculatedValue);
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
