package darshan.com.redditdemoapp.models.signin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Darshan B.S on 14-07-2018.
 */

public class Json {
    @SerializedName("data")
    @Expose
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Json{" +
                "data=" + data +
                '}';
    }
}
