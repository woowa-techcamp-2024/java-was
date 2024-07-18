package dto;

import model.Article;
import model.User;

public record WriteRequestDto(
    String title,
    String content,
    String filePath
) {
    public WriteRequestDto(String title, String content, String filePath) {
        this.title = title;
        this.content = content;
        this.filePath = filePath;
    }

    public Article toEntity(User user) {
        return new Article(title, content, user.getUserId(), filePath);
    }
}
