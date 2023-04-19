package tech.hirsun.project.comp3334.sandy_elearning.service;

import tech.hirsun.project.comp3334.sandy_elearning.entity.Article;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageResult;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageUtil;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    PageResult getArticlePage(PageUtil pageUtil);

    Article queryById(Integer id);

    int add(Article article);

    int update(Article article);

    int delete(Integer id);

    int deleteBatch(Integer[] ids);
}
