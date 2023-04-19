package tech.hirsun.project.comp3334.sandy_elearning.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.project.comp3334.sandy_elearning.dao.GeneralUserDao;
import tech.hirsun.project.comp3334.sandy_elearning.entity.GeneralUser;
import tech.hirsun.project.comp3334.sandy_elearning.service.GeneralUserService;
import tech.hirsun.project.comp3334.sandy_elearning.utils.*;

import java.util.List;

@Service("generalUserService")
public class GeneralUserServiceImpl implements GeneralUserService {

    @Autowired
    private GeneralUserDao generalUserDao;

    @Override
    public GeneralUser login(String userName, String password) {

        GeneralUser generalUser = generalUserDao.getGeneralUserByUserNameAndPassword(userName, MD5Util.MD5Encode(password, "UTF-8"));
        if (generalUser != null) {
            // Generate token
            String token = SystemUtil.genToken(System.currentTimeMillis() + String.valueOf(generalUser.getId()) + NumberUtil.genRandomNum(4));
            // Update user table
            if (generalUserDao.updateUserToken(generalUser.getId(), token) > 0) {
                generalUser.setUserToken(token);
                generalUser.setId(null);
                return generalUser;
            }
        }
        return null;
    }

    public GeneralUser oauth2Login(String userName) {
        GeneralUser generalUser = generalUserDao.getGeneralUserByUserName(userName);
        if (generalUser != null) {
            // Generate token
            String token = SystemUtil.genToken(System.currentTimeMillis() + String.valueOf(generalUser.getId()) + NumberUtil.genRandomNum(4));
            // Update user table
            if (generalUserDao.updateUserToken(generalUser.getId(), token) > 0) {
                generalUser.setUserToken(token);
                generalUser.setId(null);
                return generalUser;
            }
        }
        return null;
    }

    @Override
    public GeneralUser queryByName(String userName) {
        return generalUserDao.getGeneralUserByUserName(userName);
    }

    @Override
    public PageResult getGeneralUserPage(PageUtil pageUtil) {
        List<GeneralUser> userList = generalUserDao.getGeneralUsers(pageUtil);
        int totalGeneralUser = generalUserDao.getTotalGeneralUsers(pageUtil);
        return new PageResult(userList, totalGeneralUser, pageUtil.getLimit(), pageUtil.getPage());
    }


    @Override
    public int add(GeneralUser user) {
        user.setPassword(MD5Util.MD5Encode(user.getPassword(), "UTF-8"));
        return generalUserDao.insertUser(user);
    }

    @Override
    public GeneralUser queryById(Long id) {
        return generalUserDao.getGeneralUserById(id);
    }

    @Override
    public int updatePassword(GeneralUser user) {
        System.out.println("User ID: " + user.getId());
        System.out.println("New Password: " + MD5Util.MD5Encode(user.getPassword(), "UTF-8"));
        return generalUserDao.updateGeneralUserPassword(user.getId(), MD5Util.MD5Encode(user.getPassword(), "UTF-8"));
    }

    @Override
    public GeneralUser queryByToken(String userToken) {
        return generalUserDao.getGeneralUserByToken(userToken);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return generalUserDao.deleteBatch(ids);
    }

}
