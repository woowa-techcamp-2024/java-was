package repository;

import java.util.Map;
import java.util.Optional;
import model.Article;

public abstract class ArticleRepository {

    abstract void save(Article article);

    abstract Optional<Article> findById(Long id);

    abstract Map<Long, Article> getAllArticles();
}
