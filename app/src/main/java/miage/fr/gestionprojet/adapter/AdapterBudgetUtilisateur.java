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
import miage.fr.gestionprojet.models.Ressource;
import miage.fr.gestionprojet.models.dao.DaoAction;
import miage.fr.gestionprojet.outils.Outils;
import miage.fr.gestionprojet.vues.ActivityBudget;

/**
 * Created by Audrey on 25/04/2017.
 */

public class AdapterBudgetUtilisateur extends ArrayAdapter<Ressource> {
    private List<Ressource> lstUtilisateurs;
    private ActivityBudget activity;
    private List<Integer> lstNbActionsRealisees;
    private List<Integer> lstNbActions;


    public AdapterBudgetUtilisateur(ActivityBudget context,  int resource,  List<Ressource> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.lstUtilisateurs= objects;
        chargerNbAction();
    }


    @Override
    public int getCount() {
        return lstUtilisateurs.size();
    }

    @Override
    public Ressource getItem(int position) {
        return lstUtilisateurs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterBudgetUtilisateur.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // on récupère la vue à laquelle doit être ajouter l'image
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lst_view_budget, parent, false);
            holder = new AdapterBudgetUtilisateur.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (AdapterBudgetUtilisateur.ViewHolder) convertView.getTag();
        }

        // on définit le texte à afficher
        holder.utilisateur.setText(getItem(position).toString());
        holder.nbActionRealisees.setText(Integer.valueOf(this.lstNbActionsRealisees.get(position))+"/"+Integer.valueOf(this.lstNbActions.get(position)));
        holder.avancement.setProgress(Outils.calculerPourcentage(this.lstNbActionsRealisees.get(position),this.lstNbActions.get(position)));
        return convertView;
    }

    private void chargerNbAction(){
        this.lstNbActions = new ArrayList<>();
        this.lstNbActionsRealisees = new ArrayList<>();
        HashMap<String, Integer> results= DaoAction.getNbActionRealiseeGroupByUtilisateurOeu();
        if(results.size()>0){
            for(Ressource r : this.lstUtilisateurs){
                if(results.get(String.valueOf(r.getId()))!=null) {
                    this.lstNbActionsRealisees.add(results.get(String.valueOf(r.getId())));
                }else{
                    this.lstNbActionsRealisees.add(0);
                }
            }

        }

        results= DaoAction.getNbActionRealiseeGroupByUtilisateurOuv();
        if(results.size()>0){
            for(int i =0; i<this.lstUtilisateurs.size(); i++){
                if(results.get(String.valueOf(this.lstUtilisateurs.get(i).getId()))!=null) {
                    this.lstNbActionsRealisees.add(i,this.lstNbActionsRealisees.get(i)+results.get(String.valueOf(this.lstUtilisateurs.get(i).getId())));
                }else{
                    this.lstNbActionsRealisees.add(0);
                }
            }

        }

        results= DaoAction.getNbActionTotalGroupByUtilisateurOeu();
        if(results.size()>0){
            for(Ressource r : this.lstUtilisateurs){
                if(results.get(String.valueOf(r.getId()))!=null) {
                    this.lstNbActions.add(results.get(String.valueOf(r.getId())));
                }else{
                    this.lstNbActions.add(0);
                }
            }

        }

        results= DaoAction.getNbActionTotalGroupByUtilisateurOuv();
        if(results.size()>0){
            for(int i =0; i<this.lstUtilisateurs.size(); i++){
                if(results.get(String.valueOf(this.lstUtilisateurs.get(i).getId()))!=null) {
                    this.lstNbActions.add(i,this.lstNbActions.get(i)+results.get(String.valueOf(this.lstUtilisateurs.get(i).getId())));
                }else{
                    this.lstNbActions.add(0);
                }
            }

        }
    }
    private class ViewHolder {
        private TextView utilisateur;
        private TextView nbActionRealisees;
        private ProgressBar avancement;

        public ViewHolder(View v) {
            utilisateur = (TextView) v.findViewById(R.id.typeAffiche);
            nbActionRealisees = (TextView) v.findViewById(R.id.nbActionRealisees);
            avancement = (ProgressBar) v.findViewById(R.id.progress_bar_budget);
        }
    }
}
