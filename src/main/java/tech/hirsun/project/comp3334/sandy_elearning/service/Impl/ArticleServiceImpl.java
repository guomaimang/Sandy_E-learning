package tech.hirsun.project.comp3334.sandy_elearning.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.project.comp3334.sandy_elearning.dao.ArticleDao;
import tech.hirsun.project.comp3334.sandy_elearning.entity.Article;
import tech.hirsun.project.comp3334.sandy_elearning.service.ArticleService;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageResult;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageUtil;

import java.util.Date;
import java.util.List;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public PageResult getArticlePage(PageUtil pageUtil) {
        List<Article> articleList = articleDao.getArticles(pageUtil);
        int totalArticles = articleDao.getTotalArticles(pageUtil);
        return new PageResult(articleList, totalArticles, pageUtil.getLimit(), pageUtil.getPage());
    }

    @Override
    public Article queryById(Integer id) {
        return articleDao.getArticleById(id);
    }

    @Override
    public int add(Article article) {
        return articleDao.insertArticle(article);
    }

    @Override
    public int update(Article article) {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        article.setUpdateTime(date);
        return articleDao.updateArticle(article);
    }

    @Override
    public int delete(Integer id) {
        return articleDao.deleteArticle(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return articleDao.deleteBatch(ids);
    }

}
