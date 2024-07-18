package repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleRepositoryMemory extends ArticleRepository {

    private static final ArticleRepositoryMemory ARTICLE_REPOSITORY_MEMORY = new ArticleRepositoryMemory();
    private static final Logger log = LoggerFactory.getLogger(ArticleRepositoryMemory.class);
    private final Map<Long, Article> articles = new ConcurrentHashMap<>();
    private Long sequence = 0L;

    private ArticleRepositoryMemory() {
    }

    public static ArticleRepositoryMemory getInstance() {
        return ARTICLE_REPOSITORY_MEMORY;
    }

    public synchronized void save(Article article){
        // 글 저장 로직
        article.setArticleId(sequence);
        articles.put(sequence++, article);

        for (Map.Entry<Long, Article> entry : articles.entrySet()) {
            log.info("articleId: {}, title: {}, content: {}, userId: {}",
                entry.getKey(), entry.getValue().getTitle(), entry.getValue().getContent(),
                entry.getValue().getUserId()
            );
        }
    }

    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(articles.get(id));
    }

    public Map<Long, Article> getAllArticles(){
        return articles;
    }
}
