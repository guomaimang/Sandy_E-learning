package tech.hirsun.project.comp3334.sandy_elearning.service;

import tech.hirsun.project.comp3334.sandy_elearning.entity.GeneralUser;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageResult;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageUtil;

public interface GeneralUserService {

    // User login
    GeneralUser login(String userName, String password);

    // Get user by username
    GeneralUser queryByName(String userName);

    // Add user record
    PageResult getGeneralUserPage(PageUtil pageUtil);

    // Add user
    int add(GeneralUser user);

    // Get user by id
    GeneralUser queryById(Long id);

    // Update password
    int updatePassword(GeneralUser user);

    GeneralUser queryByToken(String userToken);

    int deleteBatch(Integer[] ids);

    GeneralUser oauth2Login(String userName);


}
