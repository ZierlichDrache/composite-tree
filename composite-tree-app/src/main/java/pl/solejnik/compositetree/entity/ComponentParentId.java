package pl.solejnik.compositetree.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class ComponentParentId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "component_id")
    private Long componentId;

    @Column(name = "parent_id")
    private Long parentId;

    public ComponentParentId() {
    }

    public ComponentParentId(final Long componentId, final Long parentId) {
        this.componentId = componentId;
        this.parentId = parentId;
    }

    public Long getComponentId() {
        return componentId;
    }

    public Long getParentId() {
        return parentId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentParentId that = (ComponentParentId) o;
        return componentId.equals(that.componentId) &&
                parentId.equals(that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentId, parentId);
    }
}
