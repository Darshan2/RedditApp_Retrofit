package darshan.com.redditdemoapp.models.signin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Darshan B.S on 14-07-2018.
 */

public class Data {
    @SerializedName("modhash")
    @Expose
    String modhash;

    @SerializedName("cookie")
    @Expose
    String cookie;

    public String getModhash() {
        return modhash;
    }

    public void setModhash(String modhash) {
        this.modhash = modhash;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public String toString() {
        return "Data{" +
                "modhash='" + modhash + '\'' +
                ", cookie='" + cookie + '\'' +
                '}';
    }
}
