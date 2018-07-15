package darshan.com.redditdemoapp.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import darshan.com.redditdemoapp.R;
import darshan.com.redditdemoapp.models.Comment;
import darshan.com.redditdemoapp.models.Post;

/**
 * Created by Darshan B.S on 10-07-2018.
 */

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Comment> mCommentsList;
    private OnCommentClickListener onCommentClickListener;


    public CommentsListAdapter(Context mContext, ArrayList<Comment> mCommentsList,
                               OnCommentClickListener onCommentClickListener) {
        this.mContext = mContext;
        this.mCommentsList = mCommentsList;
        this.onCommentClickListener = onCommentClickListener;
    }


    public interface OnCommentClickListener {
        void onCommentClick(Comment comment);
    }

    @NonNull
    @Override
    public CommentsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.comments_item_view, null);

        return new CommentsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Comment comment = mCommentsList.get(position);

        holder.comment.setText(comment.getComment());
        holder.commentAuthor.setText(comment.getAuthor());
        holder.commentUpdated.setText(comment.getDatePosted());

        holder.commentItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommentClickListener.onCommentClick(comment);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView comment, commentAuthor, commentUpdated;
        ProgressBar commentProgressBar;
        RelativeLayout commentItemLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            comment = itemView.findViewById(R.id.comment);
            commentAuthor = itemView.findViewById(R.id.commentAuthor);
            commentUpdated = itemView.findViewById(R.id.commentUpdated);
//            commentProgressBar = itemView.findViewById(R.id.commentProgressBar);
            commentItemLayout = itemView.findViewById(R.id.commentRelLayout);

        }
    }
}
