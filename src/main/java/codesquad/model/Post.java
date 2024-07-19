package codesquad.model;

public class Post {

        private User author;
        private String content;

        public Post(User user, String content) {
            this.author = user;
            this.content = content;
        }

        public User getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "Post[author=" + author + ", content=" + content + "]";
        }
}
