package pl.solejnik.compositetree.to;

import java.io.Serializable;
import java.util.List;

public class ComponentTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long value;
    private Long childOrder;
    private List<ComponentTO> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getChildOrder() {
        return childOrder;
    }

    public void setChildOrder(Long childOrder) {
        this.childOrder = childOrder;
    }

    public List<ComponentTO> getChildren() {
        return children;
    }

    public void setChildren(List<ComponentTO> children) {
        this.children = children;
    }
}
