package pl.solejnik.compositetree.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    @JoinTable(name = "component_parent",
            joinColumns = @JoinColumn(name = "component_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id", referencedColumnName = "id"))
    private List<Composite> parents = new ArrayList<>();

    public void addParent(Composite parent) {
        this.parents.add(parent);
    }

    public void removeParent(Composite parent) {
        this.parents.remove(parent);
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

    public List<Composite> getParents() {
        return parents;
    }

    public abstract boolean isLeaf();
}

