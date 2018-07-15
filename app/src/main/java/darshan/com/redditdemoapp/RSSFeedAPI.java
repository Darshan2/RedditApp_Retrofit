package darshan.com.redditdemoapp;

import java.util.Map;

import darshan.com.redditdemoapp.models.CommentPosted;
import darshan.com.redditdemoapp.models.feed.Feed;
import darshan.com.redditdemoapp.models.signin.SignInData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Darshan B.S on 05-07-2018.
 */

public interface RSSFeedAPI {
    //static url
    //        @GET("earthporn/.rss")
    //        Call<Feed> getFeed();


    //Non-static feed name
    @GET("{feedName}/.rss")
    Call<Feed> getFeed(@Path("feedName") String feed_name);


    /* Url i want
       https://www.reddit.com/api/login/YourUserName?user=YourUserName&passwd=your password&api_type=json
       (why this url? This is how Reddit login api designed)

       LOG_IN_URL = https://www.reddit.com/api/login/
        @Path("user") to set UserName.
         can' t use query. Because if i use query instead of path here i will get
          https://www.reddit.com/api/login/?user=userName

       If we don't know what kind of data we are receiving from the targeted url.
        Use ResponseBody(Call<ResponseBody> get/post___), instead of modelled class(here Feed).
     */
    @POST("{user}")
    Call<SignInData> postLoginInfo(
            @HeaderMap Map<String, String> header,// use this if you have more than one header
            @Path("user") String userName, // LOG_IN_URL/YourUserName
            @Query("user") String user_name,// ?user=YourUserName
            @Query("passwd") String passWord,// &passwd=your password
            @Query("api_type") String type// &api_type=json
    );


    /*
        Wanted url ex: https://www.reddit.com/api/comment?parent=t3_8w9ho5&amp;text=newComment
     */
    @POST("{comment}")
    Call<CommentPosted> postComment(
            @HeaderMap Map<String, String> header,
            @Path("comment") String comment,
            @Query("parent") String parentItemId,
            @Query("amp;text") String commentString
    );


    @POST("?parent=t1_e2erok6&amp;text=hello")
    Call<CommentPosted> postCommentStatic();

}
