package lecture.mobile.final_project.ma02_20151019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    public static final String TAG = "SearchActivity";
    Intent intent;
    EditText key;
    Button btn_research;
    RadioButton rb_cine;
    RadioButton rb_book;
    RadioGroup rb_group;
    ListView lvList;
    String apiAddress;

    int flag;
    String query;

    MyBookAdapter adapter;
    ArrayList<BookDto> resultList;
    BookXmlParser parser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        rb_cine = (RadioButton)findViewById(R.id.rb_cine2);
        rb_book = (RadioButton)findViewById(R.id.rb_book2);
        rb_group = (RadioGroup)findViewById(R.id.rb_group);
        key = (EditText)findViewById(R.id.et_key2);
        lvList = (ListView)findViewById(R.id.lv_search);

        resultList = new ArrayList();
        adapter = new MyBookAdapter(this, R.layout.search_item, resultList);
        lvList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.api_url);
        parser = new BookXmlParser();

        intent = getIntent();
        key.setText(intent.getStringExtra("key"));

        flag = intent.getIntExtra("flag", -1);
        if(flag == 0)
            rb_group.check(R.id.rb_book2);
        else
            rb_group.check(R.id.rb_cine2);

        btn_research = (Button)findViewById(R.id.btn_search2);
        btn_research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_book.isChecked())
                query = key.getText().toString();
                new NaverAsyncTask().execute();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageFileManager imgManager = new ImageFileManager(this, ImageFileManager.CACHE_IMAGE);
        imgManager.removeAllImages();
    }
/**
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_search2:
                query = key.getText().toString();
                new NaverAsyncTask().execute();
                break;
        }
    }
**/
    class NaverAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(SearchActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer response = new StringBuffer();

            String clientId = getResources().getString(R.string.client_id);
            String clientSecret = getResources().getString(R.string.client_secret);

            try {
                String apiURL = apiAddress + URLEncoder.encode(query, "UTF-8");
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("oKwCrFt7ngRbYcziwYup", clientId);
                con.setRequestProperty("SjwrgZMOuh", clientSecret);
                // response 수신
                int responseCode = con.getResponseCode();
                if (responseCode==200) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                } else {
                    Log.e(TAG, "API 호출 에러 발생 : 에러코드=" + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);

            resultList = parser.parse(result);
            adapter.setList(resultList);
            adapter.notifyDataSetChanged();

            progressDlg.dismiss();
        }
    }
}