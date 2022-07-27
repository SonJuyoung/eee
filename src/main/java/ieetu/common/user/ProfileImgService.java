package ieetu.common.user;

import ieetu.common.entity.ProfileImgEntity;
import ieetu.common.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileImgService {

    @Autowired
    ProfileImgRepository profileImgRepository;

    public String profileImgCall(UserEntity entity) {
        try {
            return profileImgRepository.findByIuser(entity).getFileNm();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int profileImgSave(ProfileImgEntity entity) {
        try {
            profileImgRepository.save(entity);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int profileImgDel(UserEntity iuser) {
        try {
            profileImgRepository.deleteByIuser(iuser);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
