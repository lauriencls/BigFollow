package miage.fr.gestionprojet.adapter;


import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import miage.fr.gestionprojet.R;
import miage.fr.gestionprojet.models.Action;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActionViewHolder> implements View.OnClickListener{


    private List<Action> actions;
    private ActionClicked mListener;

    public ActionsAdapter(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action, parent, false);
        ActionViewHolder holder = new ActionViewHolder(view);
        holder.mActionContainer.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ActionViewHolder holder, int position) {
        Action action = actions.get(position);
        holder.mPhasetextView.setText(action.getPhase());
        holder.mDomaineTextView.setText(action.getTypeTravail());
        holder.mDateTextView.setText(getDateFormat(action.getDtDeb().getTime()));
        holder.mNameTextView.setText(action.getCode());
        holder.mActionContainer.setTag(position);
    }

    public void setmListener(ActionClicked mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        Action action = actions.get(position);
        if(mListener != null)
            mListener.SelectedAction(action);
    }

    public static String getDateFormat(Long time){
        return DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS).toString();
    }

    public static class ActionViewHolder extends RecyclerView.ViewHolder{

        TextView mPhasetextView;
        TextView mNameTextView;
        TextView mDateTextView;
        TextView mDomaineTextView;
        LinearLayout mActionContainer;

        public ActionViewHolder(View itemView) {
            super(itemView);
            mPhasetextView = (TextView) itemView.findViewById(R.id.phase);
            mNameTextView = (TextView) itemView.findViewById(R.id.name);
            mDateTextView = (TextView) itemView.findViewById(R.id.date);
            mDomaineTextView = (TextView) itemView.findViewById(R.id.domain);
            mActionContainer = (LinearLayout) itemView.findViewById(R.id.actionContainer);
        }
    }

    public interface ActionClicked{
        void SelectedAction(Action action);
    }
}
