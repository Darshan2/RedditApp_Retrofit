package darshan.com.redditdemoapp.models;

/**
 * Created by Darshan B.S on 08-07-2018.
 */

public class Comment {
    private String id, comment, author, datePosted;

    public Comment(String id) {
        this.id = id;
    }

    public Comment(String id, String comment, String author, String datePosted) {
        this.id = id;
        this.comment = comment;
        this.author = author;
        this.datePosted = datePosted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", comment='" + comment + '\'' +
                ", author='" + author + '\'' +
                ", datePosted='" + datePosted + '\'' +
                '}';
    }
}
