package tech.hirsun.project.comp3334.sandy_elearning.dao;

import tech.hirsun.project.comp3334.sandy_elearning.entity.Article;
import tech.hirsun.project.comp3334.sandy_elearning.entity.GeneralUser;

import java.util.List;
import java.util.Map;

public interface ArticleDao {
    List<Article> getArticles(Map<String, Object> map);
    int getTotalArticles(Map<String, Object> map);

    int insertArticle(Article article);

    int deleteArticle(Integer id);

    int deleteBatch(Object[] id);

    Article getArticleById(Integer id);

    Article getArticleByTitle(String title);
}
