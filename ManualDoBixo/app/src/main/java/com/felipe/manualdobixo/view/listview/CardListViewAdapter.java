package com.felipe.manualdobixo.view.listview;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.felipe.manualdobixo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe on 01/03/16.
 */
public class CardListViewAdapter extends RecyclerView.Adapter<CardListViewAdapter.CardViewHolder> {

    Context context;
    List<Card> items;
    private OnCardClickListener onCardClickListener = null;

    public CardListViewAdapter(Context context) {

        this.context = context;
        items = new ArrayList<>();

    }

    public void setOnCardClickListener(OnCardClickListener onCardClickListener) {

        this.onCardClickListener = onCardClickListener;

    }

    public void addCard(Card card) {

        items.add(card);
        notifyItemInserted(items.size() - 1);

    }

    public void addCard(Card card, int index) {

        items.add(index, card);
        notifyItemInserted(index);

    }

    public void removeItem(int index) {

        items.remove(index);
        notifyItemRemoved(index);

    }

    public void removeAllItems() {

        items.clear();
        notifyDataSetChanged();

    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_large_image, null);

        return new CardViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {

        final Card card = items.get(position);

        if(!card.getImage().equals("none")) {

            Uri uri = Uri.parse(card.getImage());
            holder.imageLayout.setVisibility(View.VISIBLE);

            ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                    Log.i("DEBUG", "Image received: " + card.getTitle());
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                    Log.i("DEBUG", "Intermediate image received: " + card.getTitle());
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                    Log.i("DEBUG", "Final mage received: " + card.getTitle());
                    holder.progressBar.setVisibility(View.GONE);
                }
            };

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            holder.image.setController(controller);


        } else {

            holder.imageLayout.setVisibility(View.GONE);

        }

        holder.title.setText(card.getTitle());

        if(card.getSubtitle().isEmpty()) {

            holder.subtitle.setVisibility(View.GONE);

        } else {

            holder.subtitle.setVisibility(View.VISIBLE);
            holder.subtitle.setText(card.getSubtitle());

        }

        holder.layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(onCardClickListener != null)

                    onCardClickListener.onCardClicked(v, card);

            }

        });

    }

    @Override
    public int getItemCount() {

        return items.size();

    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView image;
        public TextView title;
        public TextView subtitle;
        public View layout;
        public View imageLayout;
        public ProgressBar progressBar;

        public CardViewHolder(View itemView) {

            super(itemView);

            image = (SimpleDraweeView) itemView.findViewById(R.id.mb_card_image);
            title = (TextView) itemView.findViewById(R.id.mb_card_title);
            subtitle = (TextView) itemView.findViewById(R.id.mb_card_subtitle);
            layout = itemView.findViewById(R.id.mb_card_layout);
            imageLayout = itemView.findViewById(R.id.mb_card_image_layout);
            progressBar = (ProgressBar) itemView.findViewById(R.id.mb_card_progressbar);

        }

    }

    public interface OnCardClickListener<T> {

        void onCardClicked(View view, Card<T> card);

    }

}
