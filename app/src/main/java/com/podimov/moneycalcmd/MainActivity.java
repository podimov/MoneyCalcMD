package com.podimov.moneycalcmd;

import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.podimov.moneycalcmd.model.BankModel;
import com.podimov.moneycalcmd.model.Organizations;
import com.podimov.moneycalcmd.model.Rates;
import com.podimov.moneycalcmd.net.Api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    private EditText edit_eur;
    private EditText edit_usd;
    private EditText edit_rub;
    private EditText edit_uah;
    private EditText edit_ron;

    private String eur_buy_sum  = "";
    private String eur_sell_sum = "";
    private String usd_buy_sum  = "";
    private String usd_sell_sum = "";
    private String rub_buy_sum  = "";
    private String rub_sell_sum = "";
    private String uah_buy_sum  = "";
    private String uah_sell_sum = "";
    private String ron_buy_sum  = "";
    private String ron_sell_sum = "";

    private BankModel bankModel = null;

    private Integer selectedBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = sdf.format(new Date());
        TextView app_title = (TextView) findViewById(R.id.app_title);
        app_title.setText(getString(R.string.app_title, currentDate));

        initializeUI();

        try {
            new Api().get().getData().enqueue(new Callback<BankModel>() {
                @Override
                public void onResponse(Call<BankModel> call, Response<BankModel> response) {
                    if(response.isSuccessful()) {
                        bankModel = response.body();

                        Organizations organizations = bankModel.getOrganizations();

                        for (Integer i = 0; i <= 5; i++) {
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

        edit_eur = (EditText) findViewById(R.id.edit_eur);
        edit_usd = (EditText) findViewById(R.id.edit_usd);
        edit_rub = (EditText) findViewById(R.id.edit_rub);
        edit_uah = (EditText) findViewById(R.id.edit_uah);
        edit_ron = (EditText) findViewById(R.id.edit_ron);

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, banks);
        //adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                selectedBank = selectedItemPosition;

                Rates rates = bankModel.getOrganizations().getBank(selectedItemPosition).getRates();

                eur_buy_sum  = rates.getEUR().getBuy().toString();
                eur_sell_sum = rates.getEUR().getSell().toString();
                usd_buy_sum  = rates.getUSD().getBuy().toString();
                usd_sell_sum = rates.getUSD().getSell().toString();
                rub_buy_sum  = rates.getRUB().getBuy().toString();
                rub_sell_sum = rates.getRUB().getSell().toString();
                uah_buy_sum  = rates.getUAH().getBuy().toString();
                uah_sell_sum = rates.getUAH().getSell().toString();
                ron_buy_sum  = rates.getRON().getBuy().toString();
                ron_sell_sum = rates.getRON().getSell().toString();

                eur_buy.setText(eur_buy_sum);
                eur_sell.setText(eur_sell_sum);
                usd_buy.setText(usd_buy_sum);
                usd_sell.setText(usd_sell_sum);
                rub_buy.setText(rub_buy_sum);
                rub_sell.setText(rub_sell_sum);
                uah_buy.setText(uah_buy_sum);
                uah_sell.setText(uah_sell_sum);
                ron_buy.setText(ron_buy_sum);
                ron_sell.setText(ron_sell_sum);

                edit_eur.setText("100");
                edit_eur.requestFocus();
                edit_eur.selectAll();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edit_eur, InputMethodManager.SHOW_IMPLICIT);

            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        edit_eur.addTextChangedListener(new MyTextWatcher("EUR"));

        edit_usd.addTextChangedListener(new MyTextWatcher("USD"));

        edit_rub.addTextChangedListener(new MyTextWatcher("RUB"));

        edit_uah.addTextChangedListener(new MyTextWatcher("UAH"));

        edit_ron.addTextChangedListener(new MyTextWatcher("RON"));

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

            Double eur_calc;
            Double usd_calc;
            Double rub_calc;
            Double uah_calc;
            Double ron_calc;

            Rates rates = bankModel.getOrganizations().getBank(selectedBank).getRates();

            Double eur = rates.getEUR().getBuy();
            Double usd = rates.getUSD().getBuy();
            Double rub = rates.getRUB().getBuy();
            Double uah = rates.getUAH().getBuy();
            Double ron = rates.getRON().getBuy();

            TextView currentTextView = null;

            if (this.currentCurrency == "EUR") {
                currentTextView = edit_eur;
                currency_rate = eur;
            } else if (this.currentCurrency == "USD") {
                currentTextView = edit_usd;
                currency_rate = usd;
            } else if (this.currentCurrency == "RUB") {
                currentTextView = edit_rub;
                currency_rate = rub;
            } else if (this.currentCurrency == "UAH") {
                currentTextView = edit_uah;
                currency_rate = uah;
            } else if (this.currentCurrency == "RON") {
                currentTextView = edit_ron;
                currency_rate = ron;
            }

            if (currentTextView.isFocused()) {
                String currentField = currentTextView.getText().toString();

                if (currentField.length() > 0) {
                    Double currency_calc = Double.parseDouble(currentField);

                    if (this.currentCurrency != "EUR") {
                        eur_calc = (currency_calc * currency_rate / eur);
                        edit_eur.setText(String.format("%.02f", eur_calc));
                    }

                    if (this.currentCurrency != "USD") {
                        usd_calc = (currency_calc * currency_rate / usd);
                        edit_usd.setText(String.format("%.02f", usd_calc));
                    }

                    if (this.currentCurrency != "RUB") {
                        rub_calc = (currency_calc * currency_rate / rub);
                        edit_rub.setText(String.format("%.02f", rub_calc));
                    }

                    if (this.currentCurrency != "UAH") {
                        uah_calc = (currency_calc * currency_rate / uah);
                        edit_uah.setText(String.format("%.02f", uah_calc));
                    }

                    if (this.currentCurrency != "RON") {
                        ron_calc = (currency_calc * currency_rate / ron);
                        edit_ron.setText(String.format("%.02f", ron_calc));
                    }
                }
            }
        }
    }
}