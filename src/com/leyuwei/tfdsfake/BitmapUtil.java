package com.leyuwei.tfdsfake;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

/**
 * Created by Administrator  .
 */
public class BitmapUtil {
    private static final String DEAFAULT_FILE_PATH = getSdCardPath() + File.separator;

    private BitmapUtil() {
    }

    /**
     * ����ʡ�ڴ�ķ�ʽ��ȡ������Դ��ͼƬ
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // ��ȡ��ԴͼƬ
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

        /*Bitmap bmp = BitmapUtil.getDiskBitmap(path);
        if(bmp != null) {
            mHeaderIv.setImageBitmap(bmp);
        }*/
    /**
     * ��ȡ����ͼƬ
     * @param path ͼƬ·��
     * */
    public static Bitmap getDiskBitmap(String path) {
        Bitmap bitmap = null;
        if(TextUtils.isEmpty(path)) {
            return bitmap;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                bitmap = BitmapFactory.decodeFile(path, opt);
             }
        } catch (Exception e) {
        }

        return bitmap;
    }

   
    /**
     * ����ͼƬ������ ��һ��������ͼƬ���� �ڶ�������Ϊ��Ҫ�����bitmap
     * */
    public static void saveImgToDisk(String imgName, Bitmap bitmap) {
        File file = new File(DEAFAULT_FILE_PATH, imgName);
        if(file == null) {
            return;
        }

        if(isFileExists(file.getPath())) {
            return;
        }


        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out); 
            String path = file.getPath(); 
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * �ж��ļ�·���Ƿ��Ѿ�����
     * @param filePath �ļ�·��
     * */
    private static boolean isFileExists(String filePath) {
        try {
            File file = new File( filePath );
            return file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * ����SD����Ŀ¼ <br>
     *
     * @return SD����Ŀ¼
     */
    public static String getSdCardPath() {
        File sdDir ;
        boolean sdCardExist = isSdCardExist(); // �ж�sd���Ƿ����
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
            return sdDir.toString();
        } else {
            return null;
        }
    }

    /**
     * �ж�SD���Ƿ���� <br>
     *
     * @return SD�����ڷ���true�����򷵻�false
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}