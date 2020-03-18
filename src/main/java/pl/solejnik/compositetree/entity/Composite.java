package pl.solejnik.compositetree.entity;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("C")
public class Composite extends Component {

    @ManyToMany(mappedBy = "parents", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Component> children = new ArrayList<>();

    public void addChild(Component newChild) {
        getParents().forEach(newChild::addParent);
        newChild.addParent(this);
        this.children.add(newChild);
    }

    public void removeChild(Component child) {
        getParents().forEach(child::removeParent);
        child.removeParent(this);
        this.children.remove(child);
    }

    public List<Component> getChildren() {
        return children;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
