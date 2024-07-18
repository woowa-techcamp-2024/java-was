package repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleRepository {

    private static final ArticleRepository articleRepository = new ArticleRepository();
    private static final Logger log = LoggerFactory.getLogger(ArticleRepository.class);
    private final Map<Long, Article> articles = new ConcurrentHashMap<>();
    private Long sequence = 0L;

    private ArticleRepository() {
    }

    public static ArticleRepository getInstance() {
        return articleRepository;
    }

    public synchronized void save(Article article) {
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

    public Map<Long, Article> getAllArticles() {
        return articles;
    }
}
