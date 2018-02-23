package miage.fr.gestionprojet.vues;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.PopupMenu;

import com.activeandroid.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

import miage.fr.gestionprojet.R;
import miage.fr.gestionprojet.adapter.AdapterSaisieCharge;
import miage.fr.gestionprojet.models.Action;
import miage.fr.gestionprojet.models.Domaine;
import miage.fr.gestionprojet.models.Projet;
import miage.fr.gestionprojet.models.Ressource;
import miage.fr.gestionprojet.models.SaisieCharge;
import miage.fr.gestionprojet.models.dao.DaoSaisieCharge;

public class ActivityIndicateursSaisieCharge extends AppCompatActivity {

    private Projet proj;
    private List<SaisieCharge> lstSaisieCharge;
    private ListView liste;
    public static final String SAISIECHARGE = "saisie charge";
    public final static String EXTRA_INITIAL = "initial";
    public String initialUtilisateur =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicateurs_saisie_charge);
        Intent intent = getIntent();
        //on récupère le projet sélectionné
        long id =  intent.getLongExtra(ActivityDetailsProjet.PROJET,0);
        liste = (ListView) findViewById(R.id.listViewSaisieCharge);
        initialUtilisateur = intent.getStringExtra(ActivityDetailsProjet.EXTRA_INITIAL);

        if (id > 0 ) {
            // on récupère les données associées à ce projet
            proj = Model.load(Projet.class, id);
            // on récupère la liste des travaux à afficher
            lstSaisieCharge= new ArrayList<SaisieCharge>();
            List<Domaine> lstDomaines = proj.getLstDomaines();
            for(Domaine d : lstDomaines){
                for(Action a: d.getLstActions()){
                    if(a.getTypeTravail().equals("Saisie")||a.getTypeTravail().equals("Test")){
                        SaisieCharge s = DaoSaisieCharge.loadSaisieChargeByAction(a.getId());
                        if(s!=null){
                            lstSaisieCharge.add(s);
                        }
                    }
                }

            }

            //on affiche cette liste
            final ArrayAdapter<SaisieCharge> adapter = new AdapterSaisieCharge(this, R.layout.list_view_layout_saisie_charge, lstSaisieCharge);
            liste.setAdapter(adapter);
            liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ActivityIndicateursSaisieCharge.this, ActivityDetailsIndicateursSaisieCharge.class);
                    intent.putExtra(SAISIECHARGE, lstSaisieCharge.get(position).getId());
                    intent.putExtra(EXTRA_INITIAL,initialUtilisateur);
                    startActivity(intent);
                }
            });
        }else{
            // si pas de saisiecharge en cours
            List<String> list = new ArrayList<>(1);
            list.add("Aucune saisie en cours");
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
            liste.setAdapter(adapter);
        }
    }

    //ajout du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.initial_utilisateur, menu);
        menu.findItem(R.id.initial_utilisateur).setTitle(initialUtilisateur);
        getMenuInflater().inflate(R.menu.activity_indicateurs_saisie_charge, menu);

        return true;
    }

    // action à réaliser pour chaque item du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.initial_utilisateur:
                return true;
            case R.id.charger_donnees:
                Intent intent = new Intent(ActivityIndicateursSaisieCharge.this, ChargementDonnees.class);
                intent.putExtra(EXTRA_INITIAL, (initialUtilisateur));
                startActivity(intent);
                return true;
            case R.id.menu_trie_utilisateur:
                showPopup("utilisateurs");
                return true;
            case R.id.menu_trie_domaine:
                showPopup("domaine");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Domaine> getDomainesAffiches(){
        List<Domaine> doms = new ArrayList<>();
        for(SaisieCharge s : lstSaisieCharge){
            if(doms.indexOf(s.getAction().getDomaine())<0){
                doms.add(s.getAction().getDomaine());
            }
        }
        return doms;
    }

    private List<Ressource> getRessourcesAffiches(){
        List<Ressource> res = new ArrayList<>();
        for(SaisieCharge s : lstSaisieCharge){
            if(res.indexOf(s.getAction().getRespOeu())<0){
                res.add(s.getAction().getRespOeu());
            }
            if(res.indexOf(s.getAction().getRespOuv())<0){
                res.add(s.getAction().getRespOuv());
            }
        }
        return res;
    }

    private void showPopup(String type){
        ContextThemeWrapper wrapper = new ContextThemeWrapper(this, R.style.MyPopupMenu);
        PopupMenu pMenu = new PopupMenu(wrapper,liste);
        Menu menu = pMenu.getMenu();
        if(type.equalsIgnoreCase("domaine")){
            pMenu.getMenuInflater().inflate(R.menu.popup_menu_domaine,menu);
            pMenu.setGravity(Gravity.CENTER);
            List<Domaine> doms = getDomainesAffiches();
            for(Domaine d : doms){
                menu.add(0, (int)(long)d.getId(), 0, d.getNom());
            }
            pMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.all) {
                        refreshAdapter(lstSaisieCharge);
                    }else{
                        refreshAdapter(DaoSaisieCharge.loadSaisieChargesByDomaine(item.getItemId()));
                    }
                    return true;
                }
            });

        }

        if(type.equalsIgnoreCase("utilisateurs")){
            pMenu.getMenuInflater().inflate(R.menu.popup_menu_utilisateur,menu);
            pMenu.setGravity(Gravity.CENTER);
            List<Ressource> res = getRessourcesAffiches();
            for(Ressource r : res){
                menu.add(0, (int)(long)r.getId(), 0, r.getInitiales());
            }
            pMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId()==R.id.all){
                        refreshAdapter(lstSaisieCharge);
                    }else {
                        refreshAdapter(DaoSaisieCharge.loadSaisieChargeByUtilisateur(item.getItemId()));
                    }
                    return true;
                }
            });

        }
        pMenu.show();
    }

    private void refreshAdapter(List<SaisieCharge> actions){
        if(actions != null && actions.size() > 0) {
            AdapterSaisieCharge adapter = new AdapterSaisieCharge(this, R.layout.list_view_layout_saisie_charge,actions);
            liste.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}

