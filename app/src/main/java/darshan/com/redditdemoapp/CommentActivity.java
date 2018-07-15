package darshan.com.redditdemoapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import darshan.com.redditdemoapp.models.Comment;
import darshan.com.redditdemoapp.models.CommentPosted;
import darshan.com.redditdemoapp.models.feed.Feed;
import darshan.com.redditdemoapp.models.Post;
import darshan.com.redditdemoapp.models.feed.Entry;
import darshan.com.redditdemoapp.util.CommentsListAdapter;
import darshan.com.redditdemoapp.util.ExtractXML_CData;
import darshan.com.redditdemoapp.util.URLS;
import darshan.com.redditdemoapp.util.UniversalImageLoader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class CommentActivity extends AppCompatActivity implements CommentsListAdapter.OnCommentClickListener {
    private static final String TAG = "CommentActivity";

    //widgets
    private ProgressBar mProgressBar;
    private RecyclerView mCommentRecyclerView;

    //vars
    private Post mSelectedPost;
    private ExtractXML_CData mExtractXML_CData;
    private String mSessionCookie, mSessionModhash, mSessionUserName;


    @Override
    public void onCommentClick(Comment comment) {
        Log.d(TAG, "onCommentClick: " + comment);
        getUserComment(comment.getId());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Log.d(TAG, "onCreate: ");

        mProgressBar = findViewById(R.id.commentsProgressbar);
        mCommentRecyclerView = findViewById(R.id.commentsRecyclerList);

        mExtractXML_CData = new ExtractXML_CData();

        setupToolBar();

        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.selected_post))) {
            mSelectedPost = intent.getParcelableExtra(getString(R.string.selected_post));
            Log.d(TAG, "onCreate: intentExtraa " + mSelectedPost);
        }

        getCommentsData();

        setupWidgets();

//        getSessionParams();
    }


    private void setupWidgets() {
        TextView postAuthor, postTitle, postDateCreated;
        ImageView postThumbImage;
        ProgressBar postProgressBar;
        Button replayBtn;

        postTitle = findViewById(R.id.postTitle);
        postAuthor = findViewById(R.id.postAuthor);
        postDateCreated = findViewById(R.id.postUpdated);
        postThumbImage = findViewById(R.id.postImage);
        postProgressBar = findViewById(R.id.postProgressbar);
        replayBtn = findViewById(R.id.replayBtn);

        if(mSelectedPost != null) {
            postTitle.setText(mSelectedPost.getTitle());
            postAuthor.setText(mSelectedPost.getAuthor());
            postDateCreated.setText(mSelectedPost.getDatePosted());

            UniversalImageLoader.setImage(mSelectedPost.getThumbURL(), postThumbImage, postProgressBar);
        }

        postThumbImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: thumb");
                //start WebViewActivity
                Intent intent = new Intent(CommentActivity.this, WebViewActivity.class);
                intent.putExtra(getString(R.string.web_url), mSelectedPost.getPostURL());
                startActivity(intent);
            }
        });

        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: replayBtn");
                Log.d(TAG, "onClick: replayBtn, " + mSelectedPost);
                getUserComment(mSelectedPost.getId());
            }
        });
    }


    private void getCommentsData() {
        String postURL = mSelectedPost.getPostURL();
        String[] splitPostURL = postURL.split(URLS.BASE_URL);
        Log.d(TAG, "splitPostURL: " + splitPostURL);

        String commentsURL;
        for(String str: splitPostURL) {
            if(str.contains("comments")) {
                commentsURL = str;
                getWebData(commentsURL);
                break;
            }
        }

    }

    private void getWebData(String subURL) {
        Log.d(TAG, "getWebData: subURL " + subURL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLS.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        RSSFeedAPI rssFeedAPI = retrofit.create(RSSFeedAPI.class);

        Call<Feed> call = rssFeedAPI.getFeed(subURL);

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                try {
                    List<Entry> entries = response.body().getEntries();
                    ArrayList<Comment> commentsList = new ArrayList<>();

//                Log.d(TAG, "onResponse: feed: " + response.body().toString());
                    Log.d(TAG, "onResponse: Server Response: " + response.toString());

                    for (Entry entry : entries) {
                        ArrayList<String> commentDetails = mExtractXML_CData.extractXMLFor(entry.getContent(),
                                "<div class=\"md\"><p>", "</p>");

                        try {
                            commentsList.add(new Comment(
                                    entry.getId(),
                                    commentDetails.get(0),
                                    entry.getAuthor().getName(),
                                    entry.getUpdated()

                            ));

                        } catch (IndexOutOfBoundsException e) {
                            //In case of Comment formatted in different way than what i am using.

//                        commentsList.add(new Comment(
//                                "Error reading comment",
//                                "None",
//                                "None",
//                                "None"
//                        ));
                            Log.e(TAG, "onResponse: IndexOutOfBoundsException: " + e.getMessage());
                        } catch (NullPointerException e) {
                            //In case of comment having no author name.
                            commentsList.add(new Comment(
                                    entry.getId(),
                                    commentDetails.get(0),
                                    "None",
                                    entry.getUpdated()

                            ));
                            Log.e(TAG, "onResponse: NullPointerException: " + e.getMessage());
                        }
                    }

//                Log.d(TAG, "onResponse: CommentsList " + commentsList);
                    setupRecyclerList(commentsList);

                } catch (NullPointerException e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: Unable to retrieve rss feed" + t.getMessage());
                Toast.makeText(CommentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                
            }
        });
    }


    private void setupRecyclerList(ArrayList<Comment> commentsList) {
        mProgressBar.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCommentRecyclerView.setLayoutManager(linearLayoutManager);

        //to add divider between recycler items
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_item_divider));
        mCommentRecyclerView.addItemDecoration(itemDecorator);

        CommentsListAdapter commentsListAdapter = new CommentsListAdapter(this, commentsList, this);
        mCommentRecyclerView.setAdapter(commentsListAdapter);
    }


    private void getUserComment(final String itemId) {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Comment");
        dialog.setContentView(R.layout.dialog_input_comment);

        int width = (int)((getResources().getDisplayMetrics().widthPixels) * 0.95);
        int height = (int)((getResources().getDisplayMetrics().heightPixels) * 0.60);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        Button postCommentBtn = dialog.findViewById(R.id.btnPostComment);
        final EditText commentET = dialog.findViewById(R.id.dialogComment);

        postCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Attempting to post comment");

                String commentStr = commentET.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URLS.COMMENT_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RSSFeedAPI rssFeedAPI = retrofit.create(RSSFeedAPI.class);

                HashMap<String, String> headerMap = new HashMap<>();
                headerMap.put("User-Agent", mSessionUserName);
                headerMap.put("X-Modhash", mSessionModhash);
                headerMap.put("cookie", "reddit_session=" + mSessionCookie);
//                "reddit_session="

                Log.d(TAG, "postCommentBtn:  \n" +
                        "username: " + mSessionUserName + "\n" +
                        "modhash: " + mSessionModhash + "\n" +
                        "cookie: " + mSessionCookie + "\n" +
                        "itemId:" + itemId
                );

//                String urlFormattedCommentString = "amp;text=" + commentStr;

//                Call<CommentPosted> call = rssFeedAPI
//                        .postComment(headerMap, "comment", itemId, commentStr);

                Call<CommentPosted> call = rssFeedAPI.postCommentStatic();

                call.enqueue(new Callback<CommentPosted>() {
                    @Override
                    public void onResponse(Call<CommentPosted> call, Response<CommentPosted> response) {
                        try{
                            Log.d(TAG, "onResponse: feed: " + response.body().toString());
                            Log.d(TAG, "onResponse: Server Response: " + response.toString());

                            String postSuccess = response.body().getSuccess();
                            if (postSuccess.equals("true")){
                                dialog.dismiss();
                                Toast.makeText(CommentActivity.this, "Post Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(CommentActivity.this, "An Error Occured. Did you sign in?", Toast.LENGTH_SHORT).show();
                            }

                        }catch (NullPointerException e){
                            Log.e(TAG, "onResponse: NullPointerException: " +e.getMessage() );
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentPosted> call, Throwable t) {
                        Log.e(TAG, "onFailure: Unable to retrieve RSS: " + t.getMessage() );
                        Toast.makeText(CommentActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    /**
     * Get Session params stored in memory, which have login information
     */
    private void getSessionParams() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mSessionCookie = sharedPreferences.getString(getString(R.string.sessionCookie), "");
        mSessionModhash = sharedPreferences.getString(getString(R.string.sessionModHash), "");
        mSessionUserName = sharedPreferences.getString(getString(R.string.sessionUserName), "");

        Log.d(TAG, "getSessionParams: cookie=" + mSessionCookie +
                "\n modHash='" + mSessionModhash +
                "\n userName='" + mSessionUserName +
                "\n }");
    }


    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logIn: {
                        Intent intent = new Intent(CommentActivity.this, LogInActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    /*
       Triggers when control returns from another activity. When that activity call finish() on itself.
     */
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        getSessionParams();
    }
}

