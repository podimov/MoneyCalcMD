package com.podimov.moneycalcmd;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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

    String eur_buy_sum  = "";
    String eur_sell_sum = "";
    String usd_buy_sum  = "";
    String usd_sell_sum = "";
    String rub_buy_sum  = "";
    String rub_sell_sum = "";
    String uah_buy_sum  = "";
    String uah_sell_sum = "";
    String ron_buy_sum  = "";
    String ron_sell_sum = "";

    Map<String, List<String>> HashMap = new HashMap<String, List<String>>();

    ArrayList<Bank> banks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = sdf.format(new Date());
        TextView app_title = (TextView) findViewById(R.id.app_title);
        app_title.setText(getString(R.string.app_title, currentDate));

        new ParseTask().execute();
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String json_url = "https://point.md/finansy/rates/";
                URL url = new URL(json_url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            try {
                JSONObject dataJsonObj = new JSONObject(strJson);
                JSONObject organizations = dataJsonObj.getJSONObject("organizations");

                Integer i = 0;
                Iterator<String> iterator = organizations.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    JSONObject ratesArr = new JSONObject(organizations.optString(key));
                    JSONObject rates = ratesArr.getJSONObject("rates");
                    String bankName = ratesArr.getString("name");
                    banks.add(new Bank(bankName));

                    Iterator<String> iteratorRates = rates.keys();
                    while (iteratorRates.hasNext()) {
                        String currency = iteratorRates.next();
                        JSONObject rateArr = new JSONObject(rates.optString(currency));
                        String sell = rateArr.getString("sell");
                        String buy = rateArr.getString("buy");
                        HashMap.put("Bank_" + i + "_" + currency, new ArrayList<String>(Arrays.asList(sell, buy)));
                    }
                    i++;
                }
                initializeUI();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeUI() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

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

        ArrayAdapter<Bank> adapter =
                new ArrayAdapter<Bank>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, banks);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                try {
                    eur_buy_sum  = HashMap.get("Bank_" + selectedItemPosition + "_EUR").get(1);
                    eur_sell_sum = HashMap.get("Bank_" + selectedItemPosition + "_EUR").get(0);
                    usd_buy_sum  = HashMap.get("Bank_" + selectedItemPosition + "_USD").get(1);
                    usd_sell_sum = HashMap.get("Bank_" + selectedItemPosition + "_USD").get(0);
                    rub_buy_sum  = HashMap.get("Bank_" + selectedItemPosition + "_RUB").get(1);
                    rub_sell_sum = HashMap.get("Bank_" + selectedItemPosition + "_RUB").get(0);
                    uah_buy_sum  = HashMap.get("Bank_" + selectedItemPosition + "_UAH").get(1);
                    uah_sell_sum = HashMap.get("Bank_" + selectedItemPosition + "_UAH").get(0);
                    ron_buy_sum  = HashMap.get("Bank_" + selectedItemPosition + "_RON").get(1);
                    ron_sell_sum = HashMap.get("Bank_" + selectedItemPosition + "_RON").get(0);

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
                } catch (Exception e) {

                }
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        edit_eur.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float eur_calc = Float.parseFloat(edit_eur.getText().toString());
                    Float eur = Float.parseFloat(eur_buy_sum);
                    Float usd = Float.parseFloat(usd_buy_sum);
                    Float rub = Float.parseFloat(rub_buy_sum);
                    Float uah = Float.parseFloat(uah_buy_sum);
                    Float ron = Float.parseFloat(ron_buy_sum);

                    Float usd_calc = (eur_calc*eur/usd);
                    Float rub_calc = (eur_calc*eur/rub);
                    Float uah_calc = (eur_calc*eur/uah);
                    Float ron_calc = (eur_calc*eur/ron);

                    edit_usd.setText(String.format("%.02f", usd_calc));
                    edit_rub.setText(String.format("%.02f", rub_calc));
                    edit_uah.setText(String.format("%.02f", uah_calc));
                    edit_ron.setText(String.format("%.02f", ron_calc));
                } catch (NumberFormatException e) {}
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        edit_usd.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float usd_calc = Float.parseFloat(edit_usd.getText().toString());
                    Float eur = Float.parseFloat(eur_buy_sum);
                    Float usd = Float.parseFloat(usd_buy_sum);
                    Float rub = Float.parseFloat(rub_buy_sum);
                    Float uah = Float.parseFloat(uah_buy_sum);
                    Float ron = Float.parseFloat(ron_buy_sum);

                    Float eur_calc = (usd_calc*usd/eur);
                    Float rub_calc = (usd_calc*usd/rub);
                    Float uah_calc = (usd_calc*usd/uah);
                    Float ron_calc = (usd_calc*usd/ron);

                    edit_eur.setText(String.format("%.02f", eur_calc));
                    edit_rub.setText(String.format("%.02f", rub_calc));
                    edit_uah.setText(String.format("%.02f", uah_calc));
                    edit_ron.setText(String.format("%.02f", ron_calc));
                } catch (NumberFormatException e) {}
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        edit_rub.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float rub_calc = Float.parseFloat(edit_rub.getText().toString());
                    Float eur = Float.parseFloat(eur_buy_sum);
                    Float usd = Float.parseFloat(usd_buy_sum);
                    Float rub = Float.parseFloat(rub_buy_sum);
                    Float uah = Float.parseFloat(uah_buy_sum);
                    Float ron = Float.parseFloat(ron_buy_sum);

                    Float eur_calc = (rub_calc*rub/eur);
                    Float usd_calc = (rub_calc*rub/usd);
                    Float uah_calc = (rub_calc*rub/uah);
                    Float ron_calc = (rub_calc*rub/ron);

                    edit_eur.setText(String.format("%.02f", eur_calc));
                    edit_usd.setText(String.format("%.02f", usd_calc));
                    edit_uah.setText(String.format("%.02f", uah_calc));
                    edit_ron.setText(String.format("%.02f", ron_calc));
                } catch (NumberFormatException e) {}
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        edit_uah.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float uah_calc = Float.parseFloat(edit_uah.getText().toString());
                    Float eur = Float.parseFloat(eur_buy_sum);
                    Float usd = Float.parseFloat(usd_buy_sum);
                    Float rub = Float.parseFloat(rub_buy_sum);
                    Float uah = Float.parseFloat(uah_buy_sum);
                    Float ron = Float.parseFloat(ron_buy_sum);

                    Float eur_calc = (uah_calc*uah/eur);
                    Float usd_calc = (uah_calc*uah/usd);
                    Float rub_calc = (uah_calc*uah/rub);
                    Float ron_calc = (uah_calc*uah/ron);

                    edit_eur.setText(String.format("%.02f", eur_calc));
                    edit_usd.setText(String.format("%.02f", usd_calc));
                    edit_rub.setText(String.format("%.02f", rub_calc));
                    edit_ron.setText(String.format("%.02f", ron_calc));
                } catch (NumberFormatException e) {}
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        edit_ron.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float ron_calc = Float.parseFloat(edit_ron.getText().toString());
                    Float eur = Float.parseFloat(eur_buy_sum);
                    Float usd = Float.parseFloat(usd_buy_sum);
                    Float rub = Float.parseFloat(rub_buy_sum);
                    Float uah = Float.parseFloat(uah_buy_sum);
                    Float ron = Float.parseFloat(ron_buy_sum);

                    Float eur_calc = (ron_calc*ron/eur);
                    Float usd_calc = (ron_calc*ron/usd);
                    Float rub_calc = (ron_calc*ron/rub);
                    Float uah_calc = (ron_calc*ron/uah);

                    edit_eur.setText(String.format("%.02f", eur_calc));
                    edit_usd.setText(String.format("%.02f", usd_calc));
                    edit_rub.setText(String.format("%.02f", rub_calc));
                    edit_uah.setText(String.format("%.02f", uah_calc));
                } catch (NumberFormatException e) {}
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }

    private class Bank {
        private String bank_name;

        public Bank() {
        }

        public Bank(String bank_name) {
            this.bank_name = bank_name;
        }

        @Override
        public String toString() {
            return bank_name;
        }
    }
}