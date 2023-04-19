package tech.hirsun.project.comp3334.sandy_elearning.service.Impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.RawTransactionManager;
import tech.hirsun.project.comp3334.sandy_elearning.dao.ArticleDao;
import tech.hirsun.project.comp3334.sandy_elearning.entity.Article;
import tech.hirsun.project.comp3334.sandy_elearning.service.ArticleService;
import tech.hirsun.project.comp3334.sandy_elearning.utils.CloudNoteContract;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageResult;
import tech.hirsun.project.comp3334.sandy_elearning.utils.PageUtil;
import tech.hirsun.project.comp3334.sandy_elearning.utils.Web3jClient;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    private Web3j web3j;
    private Credentials credentials;
    private final int CHAIN_ID = 137; // Polygon chainId

    private BigInteger GAS_PRICE = BigInteger.valueOf(0); // 0 Gwei
    private final BigInteger GAS_LIMIT = BigInteger.valueOf(100_0000); // 500,000

    private final String contractAddress = "xxx";
    private final String privateKey = "xxx";
    private CloudNoteContract cloudNoteContract;

    @PostConstruct
    private void load() {
        credentials = Credentials.create(privateKey);
        web3j = Web3jClient.getClient();
        RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials, CHAIN_ID);
        try {
            GAS_PRICE = web3j.ethGasPrice().send().getGasPrice().multiply(BigInteger.valueOf(3)).divide(BigInteger.valueOf(2));
            System.out.println("Gas price suggest with 1.5x: " + GAS_PRICE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cloudNoteContract = CloudNoteContract.load(contractAddress, web3j, transactionManager, GAS_PRICE, GAS_LIMIT);
    }


    @Override
    public PageResult getArticlePage(PageUtil pageUtil) {
        List<Article> articleList = articleDao.getArticles(pageUtil);
        int totalArticles = articleDao.getTotalArticles(pageUtil);
        return new PageResult(articleList, totalArticles, pageUtil.getLimit(), pageUtil.getPage());
    }

    @Override
    public Article queryById(Integer id) {
        Article article = articleDao.getArticleById(id);
        String title = article.getTitle();
        String author = article.getAuthor();

        try {
            article.setContent(cloudNoteContract.get(title, author).send());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return article;
    }

    @Override
    public Article queryByTitle(String title) {
        return articleDao.getArticleByTitle(title);
    }


    // Asynchronous
    @Async
    @Override
    public void add(Article article) {
        String title = article.getTitle();
        String author = article.getAuthor();
        String content = article.getContent();

        try {
            // for blockchain
            System.out.println(cloudNoteContract.add(title, author,content).send().toString());
            // for database
            articleDao.insertArticle(article);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Asynchronous
    @Override
    @Async
    public void update(Article article) {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        article.setUpdateTime(date);

        String title = article.getTitle();
        String author = article.getAuthor();
        String content = article.getContent();

        try {
            // for blockchain
            System.out.println(cloudNoteContract.update(title, author,content).send().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Asynchronous
    @Override
    @Async
    public void delete(Integer id) {
        Article article = articleDao.getArticleById(id);
        String title = article.getTitle();
        String author = article.getAuthor();

        try {
            // for blockchain
            System.out.println(cloudNoteContract.remove(title, author).send().toString());
            // for database
            articleDao.deleteArticle(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Asynchronous
    @Override
    @Async
    public void deleteBatch(Integer[] ids) {
        for (Integer id : ids) {
            Article article = articleDao.getArticleById(id);
            String title = article.getTitle();
            String author = article.getAuthor();

            try {
                // for blockchain
                System.out.println(cloudNoteContract.remove(title, author).send().toString());
                // for database
                articleDao.deleteArticle(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



}
