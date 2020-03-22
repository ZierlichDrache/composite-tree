package pl.solejnik.compositetree.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Component entity
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CHILD_TYPE", length = 1)
@Table(name = "component")
public abstract class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private Long value;

    @Column(name = "child_order")
    private Long childOrder;

    @OneToOne
    @JoinColumn(name = "first_parent")
    private Composite firstParent;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "component_parent",
            joinColumns = @JoinColumn(name = "component_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id", referencedColumnName = "id"))
    private Set<Composite> parents = new HashSet<>();

    public void addParent(final Composite parent) {
        this.parents.add(parent);
    }

    public void removeParent(final Composite parent) {
        this.parents.remove(parent);
    }

    public void removeAllParents() {
        this.parents.clear();
    }

    public Long getId() {
        return id;
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

    public void setChildOrder(final Long childOrder) {
        this.childOrder = childOrder;
    }

    public Composite getFirstParent() {
        return firstParent;
    }

    public void setFirstParent(final Composite firstParent) {
        this.firstParent = firstParent;
    }

    public Set<Composite> getParents() {
        return parents;
    }

    public boolean isRoot() {
        return this.id != null && this.id == 1L;
    }

    public abstract boolean isLeaf();
}

