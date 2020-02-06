package lecture.mobile.final_project.ma02_20151019;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyBookAdapter extends BaseAdapter {
    public static final String TAG = "MyBookAdapter";
    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<BookDto> list;
    private ViewHolder viewHodler = null;
    private ImageFileManager imgManager = null;
    private String imageSavedPath;

    public MyBookAdapter(Context context, int resource, ArrayList<BookDto> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        this.imgManager = new ImageFileManager(context, ImageFileManager.CACHE_IMAGE);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageSavedPath = context.getResources().getString(R.string.tmp_image_path);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BookDto getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView with position : " + position);
        View view = convertView;

        if (view == null) {
            viewHodler = new ViewHolder();
            view = inflater.inflate(layout, parent, false);
            viewHodler.tvTitle = (TextView)view.findViewById(R.id.tvTitle);
            viewHodler.tvAuthor = (TextView)view.findViewById(R.id.tvAuthor);
            viewHodler.ivImage = (ImageView)view.findViewById(R.id.ivImage);
            view.setTag(viewHodler);
        } else {
            viewHodler = (ViewHolder)view.getTag();
        }

        BookDto dto = list.get(position);
        viewHodler.tvTitle.setText(dto.getTitle());
        viewHodler.tvAuthor.setText(dto.getAuthor());

        String imageFileName = imgManager.getImageFileNameFromUrl(dto.getImage());

        if (imgManager.checkFileExist(imageFileName)) {
            viewHodler.ivImage.setImageBitmap(imgManager.getSavedImage(imageFileName));
        } else {
            GetImageAsyncTask task = new GetImageAsyncTask();
            try {
                Bitmap bitmap = task.execute(dto.getImage(), imageFileName).get();
                viewHodler.ivImage.setImageBitmap(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public void setList(ArrayList<BookDto> list) {
        this.list = list;
    }

    public void clear() {
        this.list = new ArrayList<BookDto>();
    }

    static class ViewHolder {
        public TextView tvTitle = null;
        public TextView tvAuthor = null;
        public ImageView ivImage = null;
    }

    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        String imageFileName;

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            String imageAddress = params[0];
            imageFileName = params[1];
            Log.i(TAG, imageAddress);

            try {
                URL Url = new URL(imageAddress);
                URLConnection imageConn = Url.openConnection();
                imageConn.connect();

                int imageLength = imageConn.getContentLength();
                BufferedInputStream bis = new BufferedInputStream(imageConn.getInputStream(), imageLength);
                bitmap = BitmapFactory.decodeStream(bis);

                bis.close();
            } catch (FileNotFoundException e) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgManager.saveImage(bitmap, imageFileName);
        }
    }
}