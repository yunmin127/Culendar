package lecture.mobile.final_project.ma02_20151019;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    GridView mGridView;
    DateAdapter adapter;
    ArrayList<CalData> arrData;
    Calendar mCalToday;
    Calendar mCal;

    Intent intent;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Calendar 객체 생성
        mCalToday = Calendar.getInstance();
        mCal = Calendar.getInstance();

        // 달력 세팅
        setCalendarDate(mCal.get(Calendar.MONTH)+1);

        btn = (Button)findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setCalendarDate(int month){
        arrData = new ArrayList<CalData>();

        // 1일에 맞는 요일을 세팅하기 위한 설정
        mCalToday.set(mCal.get(Calendar.YEAR), month-1, 1);

        int startday = mCalToday.get(Calendar.DAY_OF_WEEK);
        if(startday != 1)
        {
            for(int i=0; i<startday-1; i++)
            {
                arrData.add(null);
            }
        }

        // 요일은 +1해야 되기때문에 달력에 요일을 세팅할때에는 -1 해준다.
        mCal.set(Calendar.MONTH, month-1);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            mCalToday.set(mCal.get(Calendar.YEAR), month-1, (i+1));
            arrData.add(new CalData((i+1), mCalToday.get(Calendar.DAY_OF_WEEK)));
        }

        adapter = new DateAdapter(this, arrData);

        mGridView = (GridView)findViewById(R.id.calGrid);
        mGridView.setAdapter(adapter);
    }
}

// GridView와 연결해주기위한 어댑터 구성
class DateAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CalData> arrData;
    private LayoutInflater inflater;

    public DateAdapter(Context c, ArrayList<CalData> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return arrData.size();
    }

    public Object getItem(int position) {
        return arrData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cal_item, parent, false);
        }

        TextView ViewText = (TextView)convertView.findViewById(R.id.ViewText);
        if(arrData.get(position) == null)
            ViewText.setText("");
        else
        {
            ViewText.setText(arrData.get(position).getDay()+"");
            if(arrData.get(position).getDayofweek() == 1)
            {
                ViewText.setTextColor(Color.RED);
            }
            else if(arrData.get(position).getDayofweek() == 7)
            {
                ViewText.setTextColor(Color.BLUE);

            }
            else
            {
                ViewText.setTextColor(Color.BLACK);
            }
        }

        return convertView;

    }

}