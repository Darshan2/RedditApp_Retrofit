package darshan.com.redditdemoapp.models.feed;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Darshan B.S on 05-07-2018.
 */


/**
 * If strict = true, then i must include all tags under feed tag in RSS feed
 * (here https://www.reddit.com/r/news/.rss) as parameters in model class, otherwise it won't work.
 * But here i am skipping couple of useless tags(like: category, updated). So strict = false
 */
@Root(name = "feed", strict = false)
public class Feed implements Serializable {

    @Element(name = "icon") //name should match original tag in RSSFeed web page
    private String icon;

    @Element(name = "id")
    private String id;

    @Element(name = "logo")
    private String logo;

    @Element(name = "title")
    private String title;

    @Element(name = "updated")
    private String updated;

    @Element(name = "subtitle")
    private String subtitle;

    @ElementList(inline = true ,name = "entry" )
    private List<Entry> entries;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "icon='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", logo='" + logo + '\'' +
                ", title='" + title + '\'' +
                ", updated='" + updated + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", entries=" + entries +
                '}';
    }
}
