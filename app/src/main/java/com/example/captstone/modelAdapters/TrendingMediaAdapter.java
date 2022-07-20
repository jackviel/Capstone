package com.example.captstone.modelAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.captstone.MainActivity;
import com.example.captstone.R;
import com.example.captstone.SelectedMediaActivity;
import com.example.captstone.fragments.SelectedMediaFragment;
import com.example.captstone.models.Result;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TrendingMediaAdapter extends RecyclerView.Adapter<TrendingMediaAdapter.ViewHolder> {
    private static final String TAG = "TrendingMediaAdapter";

    private List<Result> mResults;
    private Context mContext;

    // Define listener member variable
    private TrendingMediaAdapter.OnItemClickListener listener;

    // Clean all elements of the recycler
    public void clear() {
        mResults.clear();
        notifyDataSetChanged();
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(TrendingMediaAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    // View lookup cache
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvCreator;
        public TextView tvMediaType;

        public ViewHolder(final View itemView, final TrendingMediaAdapter.OnItemClickListener clickListener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivCover = itemView.findViewById(R.id.ivMediaPic);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCreator = itemView.findViewById(R.id.tvCreator);
            tvMediaType = itemView.findViewById(R.id.tvMediaType);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get the position of the item clicked
                    int position = getAdapterPosition();
                    // print out the media title and creator
                    Log.i(TAG, "Position: " + position + " Title: " + mResults.get(position).getTitle() + " Creator: " + mResults.get(position).getCreator());

                    // make sure the position is valid
                    if (position != RecyclerView.NO_POSITION) {
                        // get the movie at the position clicked
                        Result result = mResults.get(position);
                        // launch selected media fragment
                        SelectedMediaFragment selectedMediaFragment = new SelectedMediaFragment();
                        // pass the movie to the fragment
                        Bundle args = new Bundle();
                        args.putParcelable("result", Parcels.wrap(result));
                        selectedMediaFragment.setArguments(args);
                        // launch the SelectedMediaActivity
                        Intent intent = new Intent(mContext, SelectedMediaActivity.class);
                        intent.putExtra("result", Parcels.wrap(result));
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

    public TrendingMediaAdapter(Context context, ArrayList<Result> aResults) {
        mResults = aResults;
        mContext = context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public TrendingMediaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View trendingMediaView = inflater.inflate(R.layout.item_trending_media, parent, false);

        // Return a new holder instance
        TrendingMediaAdapter.ViewHolder viewHolder = new TrendingMediaAdapter.ViewHolder(trendingMediaView, listener);
        return viewHolder;
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TrendingMediaAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Result result = mResults.get(position);

        // Populate data into the template view using the data object
        viewHolder.tvTitle.setText(result.getTitle());
        viewHolder.tvCreator.setText(result.getCreator());
        viewHolder.tvMediaType.setText(result.getMediaType());

        // set the image to the imageview (commented out for now, images aren't implemented yet)
//        Glide.with(getContext())
//                .load(Uri.parse(result.getBookCoverUrl()))
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.ic_launcher_background))
//                .into(viewHolder.ivCover);
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

