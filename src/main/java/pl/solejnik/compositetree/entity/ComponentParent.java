package pl.solejnik.compositetree.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "component_parent")
public class ComponentParent {

    @EmbeddedId
    private ComponentParentId id;

    public ComponentParentId getId() {
        return id;
    }

    public void setId(ComponentParentId id) {
        this.id = id;
    }
}
