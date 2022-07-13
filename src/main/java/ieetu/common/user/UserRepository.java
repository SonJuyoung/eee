package ieetu.common.user;

import ieetu.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUidAndUpw(String uid, String upw); //

    Optional<UserEntity> findByUid(String uid);

    UserEntity findByNameAndPhoneAndMail(String name, String phone, String mail);

    UserEntity findByUidAndNameAndPhoneAndMail(String uid, String name, String phone, String mail);
}
