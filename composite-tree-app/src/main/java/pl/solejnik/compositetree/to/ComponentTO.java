package pl.solejnik.compositetree.to;

import java.io.Serializable;
import java.util.List;

public class ComponentTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long value;

    private Long childOrder;

    private List<ComponentTO> children;

    private boolean isLeaf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValue() {
        return value;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getChildOrder() {
        return childOrder;
    }

    public void setChildOrder(Long childOrder) {
        this.childOrder = childOrder;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public List<ComponentTO> getChildren() {
        return children;
    }

    public void setChildren(List<ComponentTO> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}
