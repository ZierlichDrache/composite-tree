package pl.solejnik.compositetree.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("C")
public class Composite extends Component {

    @ManyToMany(mappedBy = "parents", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Set<Component> children = new HashSet<>();

    public void addChild(final Component newChild) {
        getParents().forEach(newChild::addParent);
        newChild.addParent(this);
        this.children.add(newChild);
    }

    public void removeChild(final Component child) {
        getParents().forEach(child::removeParent);
        child.removeParent(this);
        this.children.remove(child);
    }

    public void removeAllChildren() {
        this.children.clear();
    }

    public void removeRelations() {
        removeAllChildren();
        removeAllParents();
    }

    public Set<Component> getChildren() {
        return children;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
