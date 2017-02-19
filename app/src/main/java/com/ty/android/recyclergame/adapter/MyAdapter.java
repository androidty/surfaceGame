package com.ty.android.recyclergame.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ty.android.recyclergame.R;

import java.util.List;

/**
 * Created by Android on 2016/11/21.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String[] words;
    private List<String> infos;
    private Context context;
    private int flag;
    private Callback mCallback;

    private LayoutInflater layoutInflater;

    private int headerCount = 1;
    private int footerCount = 1;

    private final static int HEADER_ITEM = 0;
    private final static int CONTENT_ITEM = 1;
    private final static int FOOTER_ITEM = 2;


    public void setFooterCount(int footerCount) {
        this.footerCount = footerCount;
    }

    public int getFooterCount() {
        return footerCount;
    }

    public List<String> getInfos() {
        return infos;
    }

    public void setInfos(List<String> infos) {
        this.infos = infos;
    }

    public int getInfosCount() {
        return infos.size();
    }

    private MyViewHolder myHolder;

    public interface Callback {
        void click(int position);

        void footClick(int position);
    }

    public void setmCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public MyAdapter(Context context, List<String> infos, int flag) {
        this.infos = infos;
        this.context = context;
        this.flag = flag;

        layoutInflater = LayoutInflater.from(context);
    }

    public boolean isHeaderItem(int position) {
        return headerCount != 0 && position < headerCount;
    }

    public boolean isFooterItem(int position) {
        return footerCount != 0 && position >= getInfosCount() + footerCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_ITEM) {
            View view = layoutInflater.inflate(R.layout.header_item, null);
            HeaderViewHolder viewHolder = new HeaderViewHolder(view);
            return viewHolder;
        } else if (viewType == FOOTER_ITEM) {

            View view = layoutInflater.inflate(R.layout.footer_item, null);
            FooterViewHolder viewHolder = new FooterViewHolder(view);
            return viewHolder;
        } else {
            View view = layoutInflater.inflate(R.layout.word_item, null);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).footerCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.footClick(position);
                    Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();

                    notifyDataSetChanged();
                }
            });
        }

        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).tvContent.setText(infos.get(position - 1));
//            ((MyViewHolder)holder).tvContent.setTag(infos.get(position));


            ((MyViewHolder) holder).myCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.click(position);
                    notifyDataSetChanged();
                }

            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderItem(position)) {
            return HEADER_ITEM;
        } else if (isFooterItem(position)) {
            return FOOTER_ITEM;
        } else {
            return CONTENT_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return getInfosCount() + headerCount +getFooterCount();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView myCardView;
        private TextView tvContent;
        private RelativeLayout rv;

        public MyViewHolder(View itemView) {
            super(itemView);
            rv = (RelativeLayout) itemView.findViewById(R.id.rv);
            myCardView = (CardView) itemView.findViewById(R.id.my_cardview);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private CardView headerCard;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerCard = (CardView) itemView.findViewById(R.id.header_card);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        private CardView footerCard;

        public FooterViewHolder(View itemView) {
            super(itemView);
            footerCard = (CardView) itemView.findViewById(R.id.footer_card);
        }
    }
}
