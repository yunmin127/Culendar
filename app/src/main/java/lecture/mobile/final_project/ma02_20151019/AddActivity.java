package lecture.mobile.final_project.ma02_20151019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {
    EditText et_key;
    Button btn_search;
    Button btn_ok;
    Button btn_cancel;
    TextView tv_title;
    TextView tv_author;

    int flag = -1;

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        et_key = (EditText)findViewById(R.id.et_key);
        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(AddActivity.this, SearchActivity.class);
                intent.putExtra("flag", flag);
                intent.putExtra("key", et_key.getText().toString());
                startActivity(intent);
            }
        });

        btn_ok = (Button)findViewById(R.id.button3);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_cancel = (Button)findViewById(R.id.button2);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void onRadioClick(View v) {
        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()) {
            case R.id.rb_book:
                if (checked)
                    flag = 0;
                break;
            case R.id.rb_cine:
                if (checked)
                    flag = 1;
                break;
        }
    }
}
