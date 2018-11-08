package saul.orc.app.orc.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import saul.orc.app.orc.entity.MstbUserEntity;

public interface UserRepos extends JpaRepository<MstbUserEntity,Integer> {
    /**
     * 依据用户代码,找到对应的用户
     * @param usercode
     * @return
     */
    MstbUserEntity findMstbUserEntityByMuCodeEquals(String usercode);
}
