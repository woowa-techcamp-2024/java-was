package codesquad.servlet.fixture;

import codesquad.domain.model.Article;

public class ArticleFixture {

    public static Article createArticle1(Long authorId) {
        return new Article(authorId, "게시글제목1", "게시글내용1", "");
    }

    public static Article createArticle2(Long authorId) {
        return new Article(authorId, "게시글제목2", "게시글내용2", "");
    }
}
