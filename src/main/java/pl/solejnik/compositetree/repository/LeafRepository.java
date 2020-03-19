package pl.solejnik.compositetree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.solejnik.compositetree.entity.Leaf;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface LeafRepository extends JpaRepository<Leaf, Long> {

    @Transactional
    @Modifying
    @Query("Delete from Leaf l where l.id in :ids")
    void removeByIds(Set<Long> ids);

    @Transactional
    @Modifying
    @Query("Update Leaf l set l.pathLength = l.pathLength + :delta where l.id in :ids")
    void updatePathsLengthsByIds(final Long delta, final Set<Long> ids);
}
