package darshan.com.redditdemoapp.models.signin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Darshan B.S on 14-07-2018.
 */

public class SignInData {

    @SerializedName("json")
    @Expose
    Json json;

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "SignInData{" +
                "json=" + json +
                '}';
    }
}
