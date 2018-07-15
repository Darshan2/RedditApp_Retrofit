package darshan.com.redditdemoapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Darshan B.S on 15-07-2018.
 */

public class CommentPosted {
    @SerializedName("success")
    @Expose
    String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "CommentPosted{" +
                "success='" + success + '\'' +
                '}';
    }
}
