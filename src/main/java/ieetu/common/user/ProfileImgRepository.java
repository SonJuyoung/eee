package ieetu.common.user;

import ieetu.common.entity.ProfileImgEntity;
import ieetu.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImgRepository extends JpaRepository<ProfileImgEntity, Integer> {
    ProfileImgEntity findByIuser(UserEntity iuser);
    void deleteByIuser(UserEntity iuser);
}
