package lecture.mobile.final_project.ma02_20151019;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UnknownFormatFlagsException;

public class ImageFileManager {
    public final static String TAG = "ImageFileManager";
    public final static String SAVE_IMAGE = "SAVE";
    public final static String CACHE_IMAGE = "TEMP";

    private Context context;
    private String imageFileSavePath;
    private String saveType;

    public ImageFileManager(Context context, String type) throws UnknownFormatFlagsException {
        this.context = context;
        this.saveType = type;
        if (saveType.equals(SAVE_IMAGE)) {
            imageFileSavePath = context.getExternalFilesDir(null) + context.getResources().getString(R.string.my_image_path);
        } else if (saveType.equals(CACHE_IMAGE)) {
            imageFileSavePath = context.getCacheDir().getAbsolutePath() + context.getResources().getString(R.string.tmp_image_path);
        } else {
            throw new UnknownFormatFlagsException(type);
        }
        createSaveDir();
        Log.i(TAG, "imageFileSavePath: " + imageFileSavePath);
    }

    //    이미지 저장 폴더 생성
    private void createSaveDir() {
        boolean mounted = true;
        if (saveType.equals(SAVE_IMAGE)) {
            String sdState = Environment.getExternalStorageState();

            if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                mounted = false;
            }
        }

        if (mounted) {
            File saveDir = new File(imageFileSavePath);

            if (!saveDir.exists()) {
                if (saveDir.mkdirs()) Log.i(TAG, "directory is created");
                else Log.i(TAG, "directory is not created");
            }
        }
    }

    public boolean checkFileExist(String fileName) {
        File checkFile = new File(imageFileSavePath, imageFileSavePath + fileName);
        if (checkFile.exists()) return true;
        return false;
    }

    public String getImageFileNameFromUrl(String url) {
        int beginIndex = url.lastIndexOf("/") + 1;
        int endIndex = url.lastIndexOf("?");
        String extractedFileName = url.substring(beginIndex, endIndex);
        Log.i(TAG, extractedFileName);
        return extractedFileName;
    }

    public void saveImage(Bitmap bitmap, String fileName) {
        File saveFile = new File(imageFileSavePath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getSavedImage(String fileName) {
        String path = imageFileSavePath + "/" + fileName;
        Log.i(TAG, path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    public void removeAllImages() {
        String path = imageFileSavePath;
        Log.i(TAG, path);
        File files = new File (path);
        File[] images = files.listFiles();
        for (File image : images) {
            image.delete();
        }
    }
}