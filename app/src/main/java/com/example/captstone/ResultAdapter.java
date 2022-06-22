package com.example.captstone;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.captstone.models.Result;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private List<Result> mResults;
    private Context mContext;

    // Define listener member variable
    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // View lookup cache
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvCreator;
        public TextView tvMediaType;

        public ViewHolder(final View itemView, final OnItemClickListener clickListener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivCover = itemView.findViewById(R.id.ivCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCreator = itemView.findViewById(R.id.tvCreator);
            tvMediaType = itemView.findViewById(R.id.tvMediaType);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }
    }

    public ResultAdapter(Context context, ArrayList<Result> aResults) {
        mResults = aResults;
        mContext = context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View resultView = inflater.inflate(R.layout.item_result, parent, false);

        // Return a new holder instance
        ResultAdapter.ViewHolder viewHolder = new ResultAdapter.ViewHolder(resultView, listener);
        return viewHolder;
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ResultAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Result result = mResults.get(position);

        // Populate data into the template view using the data object
        viewHolder.tvTitle.setText(result.getTitle());
        viewHolder.tvCreator.setText(result.getCreator());
        viewHolder.tvMediaType.setText(result.getMediaType());

        Glide.with(getContext())
                .load(Uri.parse(result.getCoverUrl()))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background))
                .into(viewHolder.ivCover);
        // Return the completed view to render on screen
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mResults.size();
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

}