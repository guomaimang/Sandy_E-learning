package tech.hirsun.project.comp3334.sandy_elearning.service;

import tech.hirsun.project.comp3334.sandy_elearning.entity.Article;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageResult;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageUtil;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    PageResult getArticlePage(PageUtil pageUtil);

    Article queryById(Integer id);

    void add(Article article);

    void update(Article article);

    void delete(Integer id);

    void deleteBatch(Integer[] ids);

    Article queryByTitle(String title);


}
