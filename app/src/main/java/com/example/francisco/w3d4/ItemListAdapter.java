package com.example.francisco.w3d4;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.beardedhen.androidbootstrap.BootstrapWell;
import com.example.francisco.w3d4.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by FRANCISCO on 10/08/2017.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private static final String TAG = "ItemListAdapter";
    ArrayList<Item> itemList = new ArrayList<>();
    Context context;

    public ItemListAdapter(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.img)
        BootstrapCircleThumbnail img;

        @Nullable
        @BindView(R.id.tvName)
        TextView tvName;

        @Nullable
        @BindView(R.id.tvLink)
        TextView tvLink;

        @Nullable
        @BindView(R.id.tvDate)
        TextView tvDate;

        @Nullable
        @BindView(R.id.tvDescription)
        TextView tvDescription;

        @Nullable
        @BindView(R.id.scroll)
        ScrollView scroll;

        @Nullable
        @BindView(R.id.scroll_parent)
        FrameLayout scroll_parent;

        @Nullable
        @BindView(R.id.tvNameParent)
        BootstrapWell tvNameParent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(TAG, "onCreateViewHolder: ");
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    private int lastPosition = -1;
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if(position > lastPosition){
            //Animation animation = AnimationUtils
        }

        //Log.d(TAG, "onBindViewHolder: ");
        final Item items = itemList.get(position);
        Picasso.with(context).load(items.getMedia().getM()).into(holder.img);
        if(!items.getTitle().trim().equals(""))
            holder.tvName.setText(items.getTitle());
        else
            holder.tvNameParent.setVisibility(holder.tvNameParent.getRootView().GONE);

        if(!items.getLink().trim().equals(""))
            holder.tvLink.setText(items.getLink());
        else
            holder.tvLink.setVisibility(holder.tvLink.getRootView().GONE);

        if(!items.getDateTaken().trim().equals(""))
            holder.tvDate.setText(items.getDateTaken());
        else
            holder.tvDate.setVisibility(holder.tvDate.getRootView().GONE);

        if(!items.getDateTaken().trim().equals(""))
            holder.tvDescription.setText(RemoveHTML(items.getDescription()));
        else
            holder.tvDescription.setVisibility(holder.tvDescription.getRootView().GONE);

        holder.scroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                if(holder.scroll.getChildAt(0).getHeight() > holder.scroll_parent.getMeasuredHeight()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
                return true;
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                    .setTitle(context.getString(R.string.fa_image)+"Options")
                    .setMessage("Display image:")
                    .setNegativeButton("Small Image", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final Dialog dialogCustom = new Dialog(v.getContext());
                            dialogCustom.setContentView(R.layout.thumb);
                            BootstrapThumbnail ivThumb = (BootstrapThumbnail) dialogCustom.findViewById(R.id.ivThumb);
                            Picasso.with(context).load(items.getMedia().getM()).into(ivThumb);
                            dialogCustom.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialogCustom.show();
                        }
                    })
                    .setPositiveButton("Full Image", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(v.getContext(),ImageActivity.class);
                            intent.putExtra("image",items.getMedia().getM());
                            v.getContext().startActivity(intent);
                        }
                    })
                    .show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        //Log.d(TAG, "getItemCount: "+itemList.size());
        return itemList.size();
    }

    public String RemoveHTML(String html){
        html = html.replaceAll("<(.*?)\\>"," ");//Removes all items in brackets
        html = html.replaceAll("<(.*?)\\\n"," ");//Must be undeneath
        html = html.replaceFirst("(.*?)\\>", " ");//Removes any connected item to the last bracket
        html = html.replaceAll("&nbsp;"," ");
        html = html.replaceAll("&amp;"," ");
        html = html.replaceAll("\\s+", " ");
        return html.trim();
    }
}
