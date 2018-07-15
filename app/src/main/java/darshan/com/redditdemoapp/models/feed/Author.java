package darshan.com.redditdemoapp.models.feed;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Darshan B.S on 05-07-2018.
 */

@Root(name = "author", strict = false)
public class Author implements Serializable {

    @Element(name = "name")
    private String name;

    @Element(name = "uri")
    private String uri;

    public Author() {
    }

    public Author(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
