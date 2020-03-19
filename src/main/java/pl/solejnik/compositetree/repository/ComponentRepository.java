package pl.solejnik.compositetree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.solejnik.compositetree.entity.Component;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {

    @Transactional
    @Modifying
    @Query("Delete from Component c where c.id in :ids")
    void deleteByIds(final Set<Long> ids);

    @Transactional
    @Modifying
    @Query("Update Component c set c.value = c.value + (:delta) where c.id in :ids")
    void updateValuesByDeltaAndIds(final Long delta, final Set<Long> ids);

    @Transactional
    @Modifying
    @Query("Update Component c set c.firstParent = null where c.id in :ids")
    void removeFirstParentsByIds(final Set<Long> ids);
}
