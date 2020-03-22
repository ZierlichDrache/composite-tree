package pl.solejnik.compositetree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.solejnik.compositetree.entity.Component;

import javax.transaction.Transactional;
import java.util.Set;

/**
 * Repository for {@link Component} entity
 */
@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {

    /**
     * Delete {@link Component} entities from the db based on ids
     *
     * @param ids ids of components to remove
     */
    @Transactional
    @Modifying
    @Query("Delete from Component c where c.id in :ids")
    void deleteByIds(final Set<Long> ids);

    /**
     * Update {@link Component} entities values based on the given delta for the given ids
     *
     * @param delta amount which will be added to the current values
     * @param ids   ids of components or its parents to update
     */
    @Transactional
    @Modifying
    @Query("Update Component c set c.value = c.value + (:delta) where c.id in :ids")
    void updateValuesByDeltaAndIds(final Long delta, final Set<Long> ids);

    /**
     * Remove {@link Component} entities first parents for the given ids
     *
     * @param ids ids of components to update
     */
    @Transactional
    @Modifying
    @Query("Update Component c set c.firstParent = null where c.id in :ids")
    void removeFirstParentsByIds(final Set<Long> ids);
}
