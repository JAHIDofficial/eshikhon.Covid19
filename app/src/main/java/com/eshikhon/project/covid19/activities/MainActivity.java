package com.eshikhon.project.covid19.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eshikhon.project.covid19.R;
import com.eshikhon.project.covid19.model.CountryResponse;
import com.eshikhon.project.covid19.model.LatestStatByCountry;
import com.eshikhon.project.covid19.model.StateByCountryResponse;
import com.eshikhon.project.covid19.model.WorldStateResponse;
import com.eshikhon.project.covid19.session.SharedHelper;
import com.eshikhon.project.covid19.utils.AppConstant;
import com.eshikhon.project.covid19.view_model.WorldStatViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.tv_status_last_update)
    TextView tvStatusLastUpdate;

    @BindView(R.id.tv_total_infected)
    TextView tvTotalInfected;

    @BindView(R.id.tv_new_infected)
    TextView tvNewInfected;

    @BindView(R.id.tv_total_death)
    TextView tvTotalDeath;

    @BindView(R.id.tv_new_death)
    TextView tvNewDeath;

    @BindView(R.id.tv_total_recovered)
    TextView tvTotalRecovered;

    @BindView(R.id.tv_serious_critical)
    TextView tvSeriousCritical;

    @BindView(R.id.tv_death_per_1m)
    TextView tvDeathPer1m;

    @BindView(R.id.tv_infected_per_1m)
    TextView tvInfectedPer1m;

    @BindView(R.id.swipeRefreshHome)
    SwipeRefreshLayout swipeRefreshHome;

    @BindView(R.id.root)
    RelativeLayout root;

    @BindView(R.id.tv_country_name)
    TextView tvCountryName;

    @BindView(R.id.tv_total_infected_by_country)
    TextView tvTotalInfectedByCountry;

    @BindView(R.id.tv_new_infected_by_country)
    TextView tvNewInfectedByCountry;

    @BindView(R.id.tv_total_death_by_country)
    TextView tvTotalDeathByCountry;

    @BindView(R.id.tv_new_death_by_country)
    TextView tvNewDeathByCountry;

    @BindView(R.id.tv_total_recovered_by_country)
    TextView tvTotalRecoveredByCountry;

    @BindView(R.id.tv_serious_critical_by_country)
    TextView tvSeriousCriticalByCountry;

    @BindView(R.id.tv_death_per_1m_by_country)
    TextView tvDeathPer1mByCountry;

    @BindView(R.id.tv_infected_per_1m_by_country)
    TextView tvInfectedPer1mByCountry;

    @BindView(R.id.tv_view_all_country)
    TextView tvViewAllCountry;

    @BindView(R.id.tv_about_me)
    TextView tvAboutMe;

    private WorldStatViewModel worldStatViewModel;
    private SharedHelper sharedHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        sharedHelper = new SharedHelper(this);

        worldStatViewModel = ViewModelProviders.of(this).get(WorldStatViewModel.class);

        SharedHelper.writeKey(AppConstant.KEY_COUNTRY_NAME, "Bangladesh");

        String country = SharedHelper.readKey(AppConstant.KEY_COUNTRY_NAME);

        worldStatViewModel.getWorldStateLiveData().observe(this, worldStateResponse -> {
            setGlobalData(worldStateResponse);
            swipeRefreshHome.setRefreshing(false);
        });

        worldStatViewModel.getStateByCountryLiveData(country).observe(this, stateByCountryResponse -> {
            setStateByCountryData(stateByCountryResponse);
            swipeRefreshHome.setRefreshing(false);
        });

        swipeRefreshHome.setOnRefreshListener(() -> {

            worldStatViewModel.getWorldStateLiveData().observe(this, this::setGlobalData);

            worldStatViewModel.getStateByCountryLiveData("Bangladesh").observe(this, this::setStateByCountryData);
        });

        tvAboutMe.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Toast.makeText ( MainActivity.this, "About the developer..." , LENGTH_SHORT ).show ();
                startActivity ( new Intent ( MainActivity.this,AboutActivity.class ) );
            }
        } );


    }

    public void selectCountry() {

    }

    @OnClick(R.id.tv_view_all_country)
    public void goToViewAllCountry() {
        startActivity(new Intent(this,AllCountriesActivity.class));
    }

    private void getCountryData(List<CountryResponse> countryResponse) {
        Timber.e(countryResponse.get(0).getName());
    }

    private void setStateByCountryData(StateByCountryResponse stateByCountryResponse) {
        List<LatestStatByCountry> latestStatByCountryList = stateByCountryResponse.getLatestStatByCountry();

        String countryName = stateByCountryResponse.getCountry();
        String newDeaths = latestStatByCountryList.get(0).getNewDeaths();
        String newInfected = latestStatByCountryList.get(0).getNewCases();
        String totalInfected = latestStatByCountryList.get(0).getTotalCases();
        String totalDeaths = latestStatByCountryList.get(0).getTotalDeaths();
        String totalRecovered = latestStatByCountryList.get(0).getTotalRecovered();
        String lastUpdate = latestStatByCountryList.get(0).getRecordDate();
        String seriousCritical = latestStatByCountryList.get(0).getSeriousCritical();
        String deathsPer1mPopulation = latestStatByCountryList.get(0).getDeathsPer1m();
        String totalCasesPer1mPopulation = latestStatByCountryList.get(0).getTotalCasesPer1m();

        tvCountryName.setText(countryName);
        tvTotalDeathByCountry.setText(totalDeaths);
        tvNewDeathByCountry.setText(newDeaths);
        tvTotalInfectedByCountry.setText(totalInfected);
        tvNewInfectedByCountry.setText(newInfected);
        tvTotalRecoveredByCountry.setText(totalRecovered);
        tvSeriousCriticalByCountry.setText(seriousCritical);
        tvDeathPer1mByCountry.setText(deathsPer1mPopulation);
        tvInfectedPer1mByCountry.setText(totalCasesPer1mPopulation);
    }

    private void setGlobalData(WorldStateResponse worldStateResponse) {
        String newDeaths = worldStateResponse.getNewDeaths();
        String newInfected = worldStateResponse.getNewCases();
        String totalInfected = worldStateResponse.getTotalCases();
        String totalDeaths = worldStateResponse.getTotalDeaths();
        String totalRecovered = worldStateResponse.getTotalRecovered();
        String lastUpdate = worldStateResponse.getStatisticTakenAt();
        String seriousCritical = worldStateResponse.getSeriousCritical();
        String deathsPer1mPopulation = worldStateResponse.getDeathsPer1mPopulation();
        String totalCasesPer1mPopulation = worldStateResponse.getTotalCasesPer1mPopulation();

        tvTotalDeath.setText(totalDeaths);
        tvNewDeath.setText(newDeaths);
        tvTotalInfected.setText(totalInfected);
        tvNewInfected.setText(newInfected);
        tvStatusLastUpdate.setText(lastUpdate);
        tvTotalRecovered.setText(totalRecovered);
        tvSeriousCritical.setText(seriousCritical);
        tvDeathPer1m.setText(deathsPer1mPopulation);
        tvInfectedPer1m.setText(totalCasesPer1mPopulation);
    }


    ////onBackPressed AlertDialog
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon( R.drawable.ic_covid_19 )
                .setTitle( R.string.app_name )
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                        MainActivity.this.onSuperBackPressed();
                        //super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void onSuperBackPressed(){
        super.onBackPressed();
    }

}
