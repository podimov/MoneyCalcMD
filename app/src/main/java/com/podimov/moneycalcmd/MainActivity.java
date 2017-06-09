package com.podimov.moneycalcmd;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private String json_url = "https://point.md/finansy/rates/";

    private Spinner spinner;

    private TextView app_title;

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

    Map<String, List<String>> HashMap = new HashMap<String, List<String>>();

    ArrayList<Bank> banks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = sdf.format(new Date());
        app_title = (TextView) findViewById(R.id.app_title);
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

        ArrayAdapter<Bank> adapter =
                new ArrayAdapter<Bank>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, banks);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                try {
                    eur_buy.setText(HashMap.get("Bank_" + selectedItemPosition + "_EUR").get(1));
                    eur_sell.setText(HashMap.get("Bank_" + selectedItemPosition + "_EUR").get(0));

                    usd_buy.setText(HashMap.get("Bank_" + selectedItemPosition + "_USD").get(1));
                    usd_sell.setText(HashMap.get("Bank_" + selectedItemPosition + "_USD").get(0));

                    rub_buy.setText(HashMap.get("Bank_" + selectedItemPosition + "_RUB").get(1));
                    rub_sell.setText(HashMap.get("Bank_" + selectedItemPosition + "_RUB").get(0));

                    uah_buy.setText(HashMap.get("Bank_" + selectedItemPosition + "_UAH").get(1));
                    uah_sell.setText(HashMap.get("Bank_" + selectedItemPosition + "_UAH").get(0));

                    ron_buy.setText(HashMap.get("Bank_" + selectedItemPosition + "_RON").get(1));
                    ron_sell.setText(HashMap.get("Bank_" + selectedItemPosition + "_RON").get(0));
                } catch (Exception e) {

                }
            }
            public void onNothingSelected(AdapterView<?> parent) {}
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