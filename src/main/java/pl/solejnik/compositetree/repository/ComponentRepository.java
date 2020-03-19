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
}
