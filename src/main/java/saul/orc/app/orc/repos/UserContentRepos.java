package saul.orc.app.orc.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import saul.orc.app.orc.entity.TstbUserContentEntity;

public interface UserContentRepos extends JpaRepository<TstbUserContentEntity,Integer> {
}
