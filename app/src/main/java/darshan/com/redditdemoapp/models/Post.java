package darshan.com.redditdemoapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Darshan B.S on 07-07-2018.
 */

public class Post implements Parcelable {
    private String title, author, postURL, datePosted, thumbURL, id;

    public Post() {
    }

    public Post(String title, String author, String postURL, String datePosted, String thumbURL, String id) {
        this.title = title;
        this.author = author;
        this.postURL = postURL;
        this.datePosted = datePosted;
        this.thumbURL = thumbURL;
        this.id = id;
    }

    protected Post(Parcel in) {
        title = in.readString();
        author = in.readString();
        postURL = in.readString();
        datePosted = in.readString();
        thumbURL = in.readString();
        id = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", postURL='" + postURL + '\'' +
                ", datePosted='" + datePosted + '\'' +
                ", thumbURL='" + thumbURL + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(postURL);
        dest.writeString(datePosted);
        dest.writeString(thumbURL);
        dest.writeString(id);
    }
}
