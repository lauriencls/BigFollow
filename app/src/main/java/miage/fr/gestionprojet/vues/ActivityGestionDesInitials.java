package miage.fr.gestionprojet.vues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

import miage.fr.gestionprojet.R;
import miage.fr.gestionprojet.adapter.AdapterInitiales;
import miage.fr.gestionprojet.models.LoggedUser;
import miage.fr.gestionprojet.models.Ressource;
import miage.fr.gestionprojet.models.dao.DaoRessource;

/**
 * Created by gamouzou on 01/03/2017.
 */

public class ActivityGestionDesInitials extends AppCompatActivity {

    private ListView liste = null;
    private List<Ressource> lstRessourceInitials = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);
        setContentView(R.layout.activity_gestion_des_initials);




        //on récupère la liste des ressources
        DaoRessource daoRessource = new DaoRessource();
        lstRessourceInitials = daoRessource.loadAllWithInitialNotEmpty();
        liste = (ListView) findViewById(R.id.listViewInitials);

        // si le nombre de ressource est supérieur à 1 on affiche une liste
        if(lstRessourceInitials.size()>0) {
            //on affiche cette liste
            final ArrayAdapter<Ressource> adapter2 = new AdapterInitiales(this, R.layout.list_view_initiales, lstRessourceInitials);
            liste.setAdapter(adapter2);

            //liste.setAdapter(adapter);

            liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(ActivityGestionDesInitials.this, MainActivity.class);
                    LoggedUser.getInstance().setInitials(lstRessourceInitials.get(position).getInitiales());
                    startActivity(intent);
                }
            });
        }else{
                // sinon on affiche un message indiquand qu'il n'y a aucun projet en cours
                List<String> list = new ArrayList<>(1);
                list.add("Cliquez ici !!");
                final ArrayAdapter<String> adapter_2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
                liste.setAdapter(adapter_2);

            liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(ActivityGestionDesInitials.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
        }



    }

