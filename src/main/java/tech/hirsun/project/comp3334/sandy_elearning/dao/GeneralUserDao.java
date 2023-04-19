package tech.hirsun.project.comp3334.sandy_elearning.dao;

import tech.hirsun.project.comp3334.sandy_elearning.entity.GeneralUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GeneralUserDao {

    GeneralUser getGeneralUserByUserNameAndPassword(@Param("userName") String username, @Param("passwordMD5") String passwordMD5);

    int updateUserToken(@Param("userId") Long userId, @Param("newToken") String newToken);

    List<GeneralUser> getGeneralUsers(Map params);

    int getTotalGeneralUsers(Map params);

    GeneralUser getGeneralUserByUserName(String userName);

    int insertUser(GeneralUser user);

    GeneralUser getGeneralUserById(Long id);

    int updateGeneralUserPassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    GeneralUser getGeneralUserByToken(String token);

    int deleteBatch(Object[] ids);

}
