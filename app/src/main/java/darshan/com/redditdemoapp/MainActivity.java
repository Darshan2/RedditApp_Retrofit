package darshan.com.redditdemoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import darshan.com.redditdemoapp.models.feed.Feed;
import darshan.com.redditdemoapp.models.Post;
import darshan.com.redditdemoapp.models.feed.Entry;
import darshan.com.redditdemoapp.util.ExtractXML_CData;
import darshan.com.redditdemoapp.util.PostsListAdapter;
import darshan.com.redditdemoapp.util.UniversalImageLoader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity implements PostsListAdapter.OnPostClickListener {

    private static final String TAG = "MainActivity";
    private final String BASE_URL = "https://www.reddit.com/r/";

    private String mCurrentFeed;
    private ExtractXML_CData mExtractXML_CData;
    private String mSessionCookie, mSessionModhash, mSessionUserName;

    private RecyclerView mPostsRecyclerList;
    private EditText mFeedName;
    private Button mLoadFeedBtn;
    private ProgressBar mProgressBar;


    @Override
    public void onPostClick(Post post) {
        //start CommentActivity
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(getString(R.string.selected_post), post);

        Log.d(TAG, "onPostClick: " + post);

        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        mPostsRecyclerList = findViewById(R.id.recyclerList);
        mFeedName = findViewById(R.id.feedName);
        mLoadFeedBtn = findViewById(R.id.loadFeedBtn);
        mProgressBar = findViewById(R.id.feedProgressBar);

        mProgressBar.setVisibility(View.GONE);

        mExtractXML_CData = new ExtractXML_CData();

        setupToolBar();

        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getImageLoaderConfig());

        mLoadFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedName = mFeedName.getText().toString();
                if(!feedName.equals("")) {
                   mCurrentFeed = feedName;
                   getWebData();
                }
            }
        });

    }


    private void getWebData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        RSSFeedAPI rssFeedAPI = retrofit.create(RSSFeedAPI.class);

        //loads page BASE_URL + mCurrentFeed/.rss
        Call<Feed> call = rssFeedAPI.getFeed(mCurrentFeed);

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                List<Entry> entries = response.body().getEntries();
                ArrayList<Post> postsList = new ArrayList<>();

                for(int i = 0; i < entries.size(); i++) {
                    Entry entry = entries.get(i);

                    String content = entry.getContent();

                    ArrayList<String> allContentLinks =
                            mExtractXML_CData.extractXMLFor(content, "a href=", null);
                    ArrayList<String> thumbNail =
                            mExtractXML_CData.extractXMLFor(content, "img src=", null);

                    String thumbNailLink = null;
                    try {
                        for(String str: thumbNail) {
                            if(str.contains("thumbs")) {
                                thumbNailLink = str;
                            }
                        }
                    } catch (NullPointerException | IndexOutOfBoundsException e) {
                        thumbNailLink = null;
                    }


                    String postURL = "";
                    for(String str: allContentLinks) {
                        if(str.contains("comments")) {
                            postURL = str;
                            break;
                        }
                    }


                    Post post = new Post(
                            entry.getTitle(),
                            entry.getAuthor().getName(),
                            postURL,
                            entry.getUpdated(),
                            thumbNailLink,
                            entry.getId()
                    );

                    postsList.add(post);
                }

                Log.d(TAG, "onResponse: postsList: " + postsList);
                setupRecyclerList(postsList);

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: Unable to retrieve rss feed" + t.getMessage());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void setupRecyclerList(ArrayList<Post> postsList) {
        mProgressBar.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mPostsRecyclerList.setLayoutManager(linearLayoutManager);

        //to add divider between recycler items
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_item_divider));
        mPostsRecyclerList.addItemDecoration(itemDecorator);

        PostsListAdapter postsListAdapter = new PostsListAdapter(this, postsList, this);
        mPostsRecyclerList.setAdapter(postsListAdapter);
    }


    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logIn: {
                        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                return true;
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
