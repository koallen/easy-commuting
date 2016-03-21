package sg.edu.ntu.cz2006.seproject.viewmodel;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sg.edu.ntu.cz2006.seproject.R;

/**
 * Created by koAllen on 17/3/16.
 */
public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder> {

    public static class InformationViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView data;
        TextView suggestion;
//        ImageView icon;

        public InformationViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            data = (TextView) itemView.findViewById(R.id.data_textview);
            suggestion = (TextView) itemView.findViewById(R.id.suggestion_textview);
//            icon = (ImageView) itemView.findViewById(R.id.icon_imageview);
        }
    }

    private List<InfoData> infoDataList;

    public InformationAdapter(List<InfoData> infoDataList) {
        this.infoDataList= infoDataList;
    }

    @Override
    public InformationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_card, parent, false);
        InformationViewHolder informationViewHolder = new InformationViewHolder(v);
        return informationViewHolder;
    }

    @Override
    public void onBindViewHolder(InformationViewHolder holder, int position) {
        holder.data.setText(infoDataList.get(position).getData());
        holder.suggestion.setText(infoDataList.get(position).getSuggestion());
//        holder.icon.setImageDrawable(R.drawable.ic_my_location_24dp);
    }

    @Override
    public int getItemCount() {
        return infoDataList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
