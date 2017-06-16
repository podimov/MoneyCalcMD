package com.podimov.moneycalcmd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.podimov.moneycalcmd.model.BankModel;
import com.podimov.moneycalcmd.model.EUR;
import com.podimov.moneycalcmd.model.GBP;
import com.podimov.moneycalcmd.model.Organizations;
import com.podimov.moneycalcmd.model.RON;
import com.podimov.moneycalcmd.model.RUB;
import com.podimov.moneycalcmd.model.Rates;
import com.podimov.moneycalcmd.model.UAH;
import com.podimov.moneycalcmd.model.USD;
import com.podimov.moneycalcmd.net.Api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;

    private ArrayList<String> banks = new ArrayList<String>();

    private ArrayAdapter<String> adapter;

    private TextView eur_buy;
    private TextView eur_sell;

    private TextView usd_buy;
    private TextView usd_sell;

    private TextView rub_buy;
    private TextView rub_sell;

    private TextView uah_buy;
    private TextView uah_sell;

    private TextView ron_buy;
    private TextView ron_sell;

    private TextView gbp_buy;
    private TextView gbp_sell;

    private EditText edit_mdl;
    private EditText edit_eur;
    private EditText edit_usd;
    private EditText edit_rub;
    private EditText edit_uah;
    private EditText edit_ron;
    private EditText edit_gbp;

    private TextView app_title;

    private LinearLayout linearLayoutEur;
    private LinearLayout linearLayoutUsd;
    private LinearLayout linearLayoutRub;
    private LinearLayout linearLayoutUah;
    private LinearLayout linearLayoutRon;
    private LinearLayout linearLayoutGbp;

    private BankModel bankModel = null;

    private Integer selectedBank;

    SharedPreferences mPrefs;
    Gson gson = new Gson();

    String ratesDate = null;
    String currentDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        currentDate = sdf.format(new Date());

        linearLayoutEur = (LinearLayout) findViewById(R.id.linearLayoutEur);
        linearLayoutUsd = (LinearLayout) findViewById(R.id.linearLayoutUsd);
        linearLayoutRub = (LinearLayout) findViewById(R.id.linearLayoutRub);
        linearLayoutUah = (LinearLayout) findViewById(R.id.linearLayoutUah);
        linearLayoutRon = (LinearLayout) findViewById(R.id.linearLayoutRon);
        linearLayoutGbp = (LinearLayout) findViewById(R.id.linearLayoutGbp);

        initializeUI();

        mPrefs = getPreferences(MODE_PRIVATE);
        String json = mPrefs.getString("bankModel", "");
        bankModel = gson.fromJson(json, BankModel.class);

        if (bankModel != null) {
            ratesDate = sdf.format(new Date(bankModel.getDate() * 1000L));
        }

        app_title = (TextView) findViewById(R.id.app_title);
        app_title.setText(getString(R.string.app_title, (ratesDate != null ? ratesDate : currentDate)));

        try {
            if (bankModel == null || !Objects.equals(ratesDate, currentDate)) {
                Log.e("Rates date", "from "+ratesDate);
                Log.e("Current date", "from "+currentDate);
                Log.e("SOURCE", "Rates from network");
                new Api().get().getData().enqueue(new Callback<BankModel>() {
                    @Override
                    public void onResponse(Call<BankModel> call, Response<BankModel> response) {
                        if (response.isSuccessful()) {
                            bankModel = response.body();

                            SharedPreferences.Editor prefsEditor = mPrefs.edit();

                            prefsEditor.putString("bankModel", gson.toJson(bankModel));
                            prefsEditor.apply();

                            Organizations organizations = bankModel.getOrganizations();

                            for (Integer i = 0; i <= 9; i++) {
                                banks.add(organizations.getBank(i).getName());
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<BankModel> call, Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
            } else {
                Log.e("SOURCE", "Local rates");
                Organizations organizations = bankModel.getOrganizations();

                for (Integer i = 0; i <= 9; i++) {
                    banks.add(organizations.getBank(i).getName());
                }

                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }

    private void initializeUI() {
        spinner = (Spinner) findViewById(R.id.spinner);

        eur_buy = (TextView) findViewById(R.id.eur_buy);
        eur_sell = (TextView) findViewById(R.id.eur_sell);

        usd_buy = (TextView) findViewById(R.id.usd_buy);
        usd_sell = (TextView) findViewById(R.id.usd_sell);

        rub_buy = (TextView) findViewById(R.id.rub_buy);
        rub_sell = (TextView) findViewById(R.id.rub_sell);

        uah_buy = (TextView) findViewById(R.id.uah_buy);
        uah_sell = (TextView) findViewById(R.id.uah_sell);

        ron_buy = (TextView) findViewById(R.id.ron_buy);
        ron_sell = (TextView) findViewById(R.id.ron_sell);

        gbp_buy = (TextView) findViewById(R.id.gbp_buy);
        gbp_sell = (TextView) findViewById(R.id.gbp_sell);

        edit_mdl = (EditText) findViewById(R.id.edit_mdl);
        edit_eur = (EditText) findViewById(R.id.edit_eur);
        edit_usd = (EditText) findViewById(R.id.edit_usd);
        edit_rub = (EditText) findViewById(R.id.edit_rub);
        edit_uah = (EditText) findViewById(R.id.edit_uah);
        edit_ron = (EditText) findViewById(R.id.edit_ron);
        edit_gbp = (EditText) findViewById(R.id.edit_gbp);

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, banks);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                selectedBank = selectedItemPosition;

                Rates rates = bankModel.getOrganizations().getBank(selectedItemPosition).getRates();

                if (rates.getEUR() == null) {
                    linearLayoutEur.setVisibility(LinearLayout.GONE);
                } else {
                    linearLayoutEur.setVisibility(LinearLayout.VISIBLE);
                }

                if (rates.getUSD() == null) {
                    linearLayoutUsd.setVisibility(LinearLayout.GONE);
                } else {
                    linearLayoutUsd.setVisibility(LinearLayout.VISIBLE);
                }

                if (rates.getRUB() == null) {
                    linearLayoutRub.setVisibility(LinearLayout.GONE);
                } else {
                    linearLayoutRub.setVisibility(LinearLayout.VISIBLE);
                }

                if (rates.getUAH() == null) {
                    linearLayoutUah.setVisibility(LinearLayout.GONE);
                } else {
                    linearLayoutUah.setVisibility(LinearLayout.VISIBLE);
                }

                if (rates.getRON() == null) {
                    linearLayoutRon.setVisibility(LinearLayout.GONE);
                } else {
                    linearLayoutRon.setVisibility(LinearLayout.VISIBLE);
                }

                if (rates.getGBP() == null) {
                    linearLayoutGbp.setVisibility(LinearLayout.GONE);
                } else {
                    linearLayoutGbp.setVisibility(LinearLayout.VISIBLE);
                }

                try {
                    eur_buy.setText(String.format("%.02f", rates.getEUR().getBuy()));
                    eur_sell.setText(String.format("%.02f", rates.getEUR().getSell()));
                    usd_buy.setText(String.format("%.02f", rates.getUSD().getBuy()));
                    usd_sell.setText(String.format("%.02f", rates.getUSD().getSell()));
                    rub_buy.setText(String.format("%.02f", rates.getRUB().getBuy()));
                    rub_sell.setText(String.format("%.02f", rates.getRUB().getSell()));
                    uah_buy.setText(String.format("%.02f", rates.getUAH().getBuy()));
                    uah_sell.setText(String.format("%.02f", rates.getUAH().getSell()));
                    ron_buy.setText(String.format("%.02f", rates.getRON().getBuy()));
                    ron_sell.setText(String.format("%.02f", rates.getRON().getSell()));
                    gbp_buy.setText(String.format("%.02f", rates.getGBP().getBuy()));
                    gbp_sell.setText(String.format("%.02f", rates.getGBP().getSell()));
                } catch (Exception e) {}

                edit_mdl.setText("1000");
                edit_mdl.requestFocus();
                edit_mdl.selectAll();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edit_mdl, InputMethodManager.SHOW_IMPLICIT);

            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        edit_mdl.addTextChangedListener(new MyTextWatcher("MDL"));

        edit_eur.addTextChangedListener(new MyTextWatcher("EUR"));

        edit_usd.addTextChangedListener(new MyTextWatcher("USD"));

        edit_rub.addTextChangedListener(new MyTextWatcher("RUB"));

        edit_uah.addTextChangedListener(new MyTextWatcher("UAH"));

        edit_ron.addTextChangedListener(new MyTextWatcher("RON"));

        edit_gbp.addTextChangedListener(new MyTextWatcher("GBP"));

    }

    private class MyTextWatcher implements TextWatcher {

        private final String currentCurrency;

        public MyTextWatcher(String currentCurrency) {
            this.currentCurrency = currentCurrency;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            Double currency_rate = null;

            Double mdl_calc;
            Double eur_calc;
            Double usd_calc;
            Double rub_calc;
            Double uah_calc;
            Double ron_calc;
            Double gbp_calc;

            Rates rates = bankModel.getOrganizations().getBank(selectedBank).getRates();

            try {
                EUR mEur = rates.getEUR();
                USD mUsd = rates.getUSD();
                RUB mRub = rates.getRUB();
                UAH mUah = rates.getUAH();
                RON mRon = rates.getRON();
                GBP mGbp = rates.getGBP();

                Double mdl = 1.0;
                Double eur = (mEur == null ? 0 : mEur.getBuy());
                Double usd = (mUsd == null ? 0 : mUsd.getBuy());
                Double rub = (mRub == null ? 0 : mRub.getBuy());
                Double uah = (mUah == null ? 0 : mUah.getBuy());
                Double ron = (mRon == null ? 0 : mRon.getBuy());
                Double gbp = (mGbp == null ? 0 : mGbp.getBuy());

                TextView currentTextView = null;

                if (Objects.equals(this.currentCurrency, "MDL")) {
                    currentTextView = edit_mdl;
                    currency_rate = mdl;
                } else if (Objects.equals(this.currentCurrency, "EUR")) {
                    currentTextView = edit_eur;
                    currency_rate = eur;
                } else if (Objects.equals(this.currentCurrency, "USD")) {
                    currentTextView = edit_usd;
                    currency_rate = usd;
                } else if (Objects.equals(this.currentCurrency, "RUB")) {
                    currentTextView = edit_rub;
                    currency_rate = rub;
                } else if (Objects.equals(this.currentCurrency, "UAH")) {
                    currentTextView = edit_uah;
                    currency_rate = uah;
                } else if (Objects.equals(this.currentCurrency, "RON")) {
                    currentTextView = edit_ron;
                    currency_rate = ron;
                } else if (Objects.equals(this.currentCurrency, "GBP")) {
                    currentTextView = edit_gbp;
                    currency_rate = gbp;
                }

                if (currentTextView.isFocused()) {
                    String currentField = currentTextView.getText().toString();

                    if (currentField.length() > 0) {
                        Double currency_calc = Double.parseDouble(currentField);

                        if (!Objects.equals(this.currentCurrency, "MDL")) {
                            mdl_calc = (currency_calc * currency_rate / mdl);
                            edit_mdl.setText(String.format("%.02f", mdl_calc));
                        }

                        if (!Objects.equals(this.currentCurrency, "EUR") && eur != 0) {
                            eur_calc = (currency_calc * currency_rate / eur);
                            edit_eur.setText(String.format("%.02f", eur_calc));
                        }

                        if (!Objects.equals(this.currentCurrency, "USD") && usd != 0) {
                            usd_calc = (currency_calc * currency_rate / usd);
                            edit_usd.setText(String.format("%.02f", usd_calc));
                        }

                        if (!Objects.equals(this.currentCurrency, "RUB") && rub != 0) {
                            rub_calc = (currency_calc * currency_rate / rub);
                            edit_rub.setText(String.format("%.02f", rub_calc));
                        }

                        if (!Objects.equals(this.currentCurrency, "UAH") && uah != 0) {
                            uah_calc = (currency_calc * currency_rate / uah);
                            edit_uah.setText(String.format("%.02f", uah_calc));
                        }

                        if (!Objects.equals(this.currentCurrency, "RON") && ron != 0) {
                            ron_calc = (currency_calc * currency_rate / ron);
                            edit_ron.setText(String.format("%.02f", ron_calc));
                        }

                        if (!Objects.equals(this.currentCurrency, "GBP") && gbp != 0) {
                            gbp_calc = (currency_calc * currency_rate / gbp);
                            edit_gbp.setText(String.format("%.02f", gbp_calc));
                        }
                    }
                }
            } catch (NumberFormatException e) {
                Log.e("NumberFormatException", e.getMessage());
            } catch (NullPointerException e) {
                Log.e("NullPointerException", e.getMessage());
            }
        }
    }
}