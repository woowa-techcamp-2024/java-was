package codesquad.application.datahandler;

import codesquad.application.domain.Article;

import java.util.List;

public interface ArticleDataHandler {
    void insert(Article article);
    List<Article> getAll();
    void deleteAll();
}
