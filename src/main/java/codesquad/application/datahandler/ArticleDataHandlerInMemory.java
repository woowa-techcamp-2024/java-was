package codesquad.application.datahandler;

import codesquad.application.domain.Article;
import codesquad.webserver.annotation.DataHandler;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@DataHandler("ArticleDataHandlerInMemory")
public class ArticleDataHandlerInMemory implements ArticleDataHandler {
    private Map<String, Article> store = new ConcurrentHashMap<>();

    @Override
    public void insert(Article article) {
        String key = UUID.randomUUID().toString();
        Article insertArticle = new Article(key, article.getTitle(), article.getContent(), article.getAuthor(), LocalDateTime.now());
        store.put(key, insertArticle);
    }

    @Override
    public List<Article> getAll() {
        return store.values().stream().sorted(Comparator.comparing(Article::getCreatedDt)).toList();
    }

    @Override
    public void deleteAll() {
        store.clear();
    }
}
