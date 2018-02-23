package miage.fr.gestionprojet.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.List;

import miage.fr.gestionprojet.R;
import miage.fr.gestionprojet.models.dao.DaoAction;
import miage.fr.gestionprojet.outils.Outils;
import miage.fr.gestionprojet.vues.ActivityBudget;

/**
 * Created by Audrey on 25/04/2017.
 */

public class AdapterBudgetType extends ArrayAdapter<String> {
    private List<String> lstTypeTravail;
    private ActivityBudget activity;
    private List<Integer> lstNbActionsRealisees;
    private List<Integer> lstNbActions;


    public AdapterBudgetType(ActivityBudget context,  int resource,  List<String> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.lstTypeTravail= objects;
        chargerNbAction();
    }


    @Override
    public int getCount() {
        return lstTypeTravail.size();
    }

    @Override
    public String getItem(int position) {
        return lstTypeTravail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterBudgetType.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // on récupère la vue à laquelle doit être ajouter l'image
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lst_view_budget, parent, false);
            holder = new AdapterBudgetType.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (AdapterBudgetType.ViewHolder) convertView.getTag();
        }

        // on définit le texte à afficher
        holder.type.setText(getItem(position).toString());
        holder.nbActionRealisees.setText(Integer.valueOf(this.lstNbActionsRealisees.get(position))+"/"+Integer.valueOf(this.lstNbActions.get(position)));
        holder.avancement.setProgress(Outils.calculerPourcentage(this.lstNbActionsRealisees.get(position),this.lstNbActions.get(position)));
        return convertView;
    }

    private void chargerNbAction(){
        this.lstNbActions = new ArrayList<>();
        this.lstNbActionsRealisees = new ArrayList<>();
        HashMap<String, Integer> results= DaoAction.getNbActionRealiseeGroupByTypeTravail();
        if(results.size()>0){
            for(String t : this.lstTypeTravail){
                if(results.get(t)!=null) {
                    this.lstNbActionsRealisees.add(results.get(t));
                }else{
                    this.lstNbActionsRealisees.add(0);
                }
            }

        }

        results= DaoAction.getNbActionTotalGroupByTypeTravail();
        if(results.size()>0){
            for(String t : this.lstTypeTravail){
                if(results.get(t)!=null) {
                    this.lstNbActions.add(results.get(t));
                }else{
                    this.lstNbActions.add(0);
                }
            }

        }
    }
    private class ViewHolder {
        private TextView type;
        private TextView nbActionRealisees;
        private ProgressBar avancement;

        public ViewHolder(View v) {
            type = (TextView) v.findViewById(R.id.typeAffiche);
            nbActionRealisees = (TextView) v.findViewById(R.id.nbActionRealisees);
            avancement = (ProgressBar) v.findViewById(R.id.progress_bar_budget);
        }
    }
}
