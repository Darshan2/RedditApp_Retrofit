package darshan.com.redditdemoapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import darshan.com.redditdemoapp.R;
import darshan.com.redditdemoapp.models.Post;

/**
 * Created by Darshan B.S on 07-07-2018.
 */

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Post> mPostsList;
    private OnPostClickListener mOnPostClickListener;

    public PostsListAdapter(Context mContext, ArrayList<Post> mPostsList, OnPostClickListener onPostClickListener) {
        this.mContext = mContext;
        this.mPostsList = mPostsList;
        mOnPostClickListener = onPostClickListener;

    }

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.card_layout, null);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mPostsList.size();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Post post = mPostsList.get(position);

        holder.cardTitle.setText(post.getTitle());
        holder.cardAuthor.setText(mContext.getString(R.string.posted_by).concat(post.getAuthor()));
        holder.cardDatePosted.setText(post.getDatePosted());

        UniversalImageLoader.setImage(
                post.getThumbURL(),
                holder.thumbImage,
                holder.progressBar
        );

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPostClickListener.onPostClick(post);
            }
        });



    }


    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rootLayout;
        ImageView thumbImage;
        TextView cardAuthor, cardTitle, cardDatePosted;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbImage = itemView.findViewById(R.id.cardImage);
            cardAuthor = itemView.findViewById(R.id.cardAuthor);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDatePosted = itemView.findViewById(R.id.cardUpdated);
            progressBar = itemView.findViewById(R.id.cardProgressBar);
            rootLayout = itemView.findViewById(R.id.rootView);
        }
    }


    private void setupImageLoader(final ViewHolder holder, Post post) {



    }
}
