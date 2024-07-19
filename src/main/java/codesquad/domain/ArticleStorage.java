package codesquad.domain;

import codesquad.domain.model.Article;
import java.util.List;
import java.util.Optional;

public interface ArticleStorage {

    Long insert(Article article);

    Optional<Article> selectById(Long id);

    List<Article> selectAll();

    void deleteById(Long id);

}
