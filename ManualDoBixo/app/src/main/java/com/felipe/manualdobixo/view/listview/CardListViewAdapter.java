package com.felipe.manualdobixo.view.listview;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
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
    public void onBindViewHolder(CardViewHolder holder, int position) {

        final Card card = items.get(position);

        Uri uri = Uri.parse("asset:///" + card.getImage());
        holder.image.setImageURI(uri);

        holder.title.setText(card.getTitle());
        holder.subtitle.setText(card.getSubtitle());

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

        public CardViewHolder(View itemView) {
            super(itemView);

            image = (SimpleDraweeView) itemView.findViewById(R.id.mb_card_image);
            title = (TextView) itemView.findViewById(R.id.mb_card_title);
            subtitle = (TextView) itemView.findViewById(R.id.mb_card_subtitle);
            layout = itemView.findViewById(R.id.mb_card_layout);
        }
    }

    public interface OnCardClickListener<T> {
        void onCardClicked(View view, Card<T> card);
    }

}
