package darshan.com.redditdemoapp.models.feed;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import darshan.com.redditdemoapp.models.feed.Author;

/**
 * Created by Darshan B.S on 05-07-2018.
 */

@Root(name = "entry", strict = false)
public class Entry implements Serializable {
    @Element(name = "content")
    private String content;

    @Element(name = "author")
    private Author author;

    @Element(name = "title")
    private String title;

    @Element(name = "updated")
    private String updated;

    @Element(name = "id")
    private String id;

    public Entry() {
    }

    public Entry(String content, Author author, String title, String updated, String id) {
        this.content = content;
        this.author = author;
        this.title = title;
        this.updated = updated;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "content='" + content + '\'' +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", updated='" + updated + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
