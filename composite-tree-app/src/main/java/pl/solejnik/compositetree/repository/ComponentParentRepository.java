package pl.solejnik.compositetree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.solejnik.compositetree.entity.ComponentParent;
import pl.solejnik.compositetree.entity.ComponentParentId;

import javax.transaction.Transactional;
import java.util.Set;

/**
 * Repository for {@link ComponentParent} entity
 */
@Repository
public interface ComponentParentRepository extends JpaRepository<ComponentParent, ComponentParentId> {

    /**
     * Removes the mappings based on ids
     *
     * @param parentIds components or parents ids
     */
    @Transactional
    @Modifying
    @Query("Delete from ComponentParent cp where cp.id.componentId in :parentIds or cp.id.parentId  in :parentIds")
    void deleteByComponentOrParentIds(final Set<Long> parentIds);
}
