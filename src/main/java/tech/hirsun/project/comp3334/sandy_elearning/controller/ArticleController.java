package tech.hirsun.project.comp3334.sandy_elearning.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.project.comp3334.sandy_elearning.common.Constants;
import tech.hirsun.project.comp3334.sandy_elearning.common.Result;
import tech.hirsun.project.comp3334.sandy_elearning.common.ResultGenerator;
import tech.hirsun.project.comp3334.sandy_elearning.config.annotation.TokenToUser;
import tech.hirsun.project.comp3334.sandy_elearning.entity.Article;
import tech.hirsun.project.comp3334.sandy_elearning.entity.GeneralUser;
import tech.hirsun.project.comp3334.sandy_elearning.service.ArticleService;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageResult;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam Map<String, Object> params, @TokenToUser GeneralUser loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "Not logged in！");
        }

        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "Parameter error！");
        }
        PageUtil pageUtil = new PageUtil(params);
        //查询列表数据
        PageResult articlePage = articleService.getArticlePage(pageUtil);
        return ResultGenerator.genSuccessResult(articlePage);
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public Result info(@PathVariable("id") Integer id, @TokenToUser GeneralUser loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "Not logged in！");
        }
        Article article = articleService.queryById(id);
        return ResultGenerator.genSuccessResult(article);
    }

    // Asynchronous
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Article article, @TokenToUser GeneralUser loginUser) {
        if (article.getAuthor()==null){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "Author cannot be empty!");
        }
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "Not logged in!");
        }

        //Query database to exclude the possibility of the same name
        Article tempArticle = articleService.queryByTitle(article.getTitle());
        if (tempArticle != null){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "User already exists, please use another title");
        }

        articleService.add(article);
        return ResultGenerator.genSuccessResult();
    }

    // Asynchronous
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result update(@RequestBody Article article, @TokenToUser GeneralUser loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "Not logged in");
        }
        if (!articleService.queryById(article.getId()).getAuthor().equals(loginUser.getUserName()) && loginUser.getIsStudent() == 1) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "You have no right to edit!");
        }
        article.setAuthor(articleService.queryById(article.getId()).getAuthor());
        articleService.update(article);
        return ResultGenerator.genSuccessResult();
    }

    // Asynchronous
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result delete(@RequestBody Integer[] ids, @TokenToUser GeneralUser loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "Not logged in");
        }

        for (Integer id : ids) {
            if (!articleService.queryById(id).getAuthor().equals(loginUser.getUserName()) && loginUser.getIsStudent() == 1) {
                return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "You have no right to edit!");
            }
        }

        if (ids.length < 1) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "Parameter error!");
        }

        articleService.deleteBatch(ids);
        return ResultGenerator.genSuccessResult();
    }
}

