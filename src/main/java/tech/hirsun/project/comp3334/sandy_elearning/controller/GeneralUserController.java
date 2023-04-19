package tech.hirsun.project.comp3334.sandy_elearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.hirsun.project.comp3334.sandy_elearning.common.Constants;
import tech.hirsun.project.comp3334.sandy_elearning.common.Result;
import tech.hirsun.project.comp3334.sandy_elearning.common.ResultGenerator;
import tech.hirsun.project.comp3334.sandy_elearning.config.annotation.TokenToUser;
import tech.hirsun.project.comp3334.sandy_elearning.entity.GeneralUser;
import tech.hirsun.project.comp3334.sandy_elearning.service.GeneralUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageUtil;
import java.util.Map;



@RestController
@RequestMapping("/users")
public class GeneralUserController {

    @Value("${azure.ad.email-suffix}")
    private String emailSuffix;

    @Autowired
    private GeneralUserService generalUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody GeneralUser generalUser) {
        Result result = ResultGenerator.genFailResult("Login failed!");
        if (generalUser == null || generalUser.getUserName() == null || generalUser.getPassword() == null){
            result.setMessage("Please fill in the login information!");
            return result;
        }

        GeneralUser loginUser = generalUserService.login(generalUser.getUserName(), generalUser.getPassword());
        if (loginUser != null) {
            result = ResultGenerator.genSuccessResult(loginUser);
            return result;
        }

        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam Map<String, Object> params, @TokenToUser GeneralUser loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "Not logged in！");
        }

        // forbid student to access
        if (loginUser.getIsStudent() == 1){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "You have no permission to access!");
        }

        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "Parameter error!");
        }

        PageUtil pageUtil = new PageUtil(params);
        return ResultGenerator.genSuccessResult(generalUserService.getGeneralUserPage(pageUtil));
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result save(@RequestBody GeneralUser generalUser, @TokenToUser GeneralUser loginUser) {

        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "Not logged in！");
        }

        // forbid student to access
        if (loginUser.getIsStudent() == 1){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "You have no permission to access!");
        }

        //Validate parameters
        if (generalUser == null || generalUser.getUserName() == null || generalUser.getPassword() == null){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "Parameter error!");
        }

        if (!generalUser.getUserName().endsWith(emailSuffix)){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "Please use the email address of " + emailSuffix +"!");
        }

        //Query database to exclude the possibility of the same name
        GeneralUser tempUser = generalUserService.queryByName(generalUser.getUserName());
        if (tempUser != null){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "User already exists, please do not add repeatedly!");
        }

        // Add user to database
        if (generalUserService.add(generalUser) > 0){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("Save failed!");
        }


    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public Result update(@RequestBody GeneralUser generalUser, @TokenToUser GeneralUser loginUser) {

        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "Not logged in！");
        }
        // forbid student to access
        if (loginUser.getIsStudent() == 1){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "You have no permission to access!");
        }
        if (StringUtils.isEmpty(generalUser.getPassword())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "Please enter your password！");
        }
        GeneralUser tempUser = generalUserService.queryById(generalUser.getId());
        if (tempUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "No such user！");
        }
        tempUser.setPassword(generalUser.getPassword());
        if (generalUserService.updatePassword(tempUser) > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("Modification failure!");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result delete(@RequestBody Integer[] ids, @TokenToUser GeneralUser loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "Not logged in！");
        }

        // forbid student to access
        if (loginUser.getIsStudent() == 1){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "You have no permission to access!");
        }

        if (ids.length < 1) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "Parameter Error！");
        }

        if (loginUser.getId().intValue() == ids[0]){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "You cannot delete yourself！");
        }

        if (generalUserService.deleteBatch(ids) > 0) {
            return ResultGenerator.genSuccessResult();
        }

        return ResultGenerator.genFailResult("Failed to delete!");
    }



}
