package saul.orc.app.orc.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import saul.orc.app.orc.entity.MstbUserEntity;

public interface UserRepos extends JpaRepository<MstbUserEntity,Integer> {
}
