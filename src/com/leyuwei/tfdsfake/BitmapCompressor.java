package com.leyuwei.tfdsfake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * ͼƬѹ��������
 * @author touch_ping
 * 2015-1-5 ����1:29:59
 */
public class BitmapCompressor {
    /**
     * ����ѹ��
     * @author ping 2015-1-5 ����1:29:58
     * @param image
     * @param maxkb
     * @return
     */
    public static Bitmap compressBitmap(Bitmap image,int maxkb) {
        //L.showlog(ѹ��ͼƬ);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
        int options = 100;
//      Log.i(test,ԭʼ��С + baos.toByteArray().length);
        while (baos.toByteArray().length / 1024 > maxkb) { // ѭ���ж����ѹ����ͼƬ�Ƿ����(maxkb)50kb,���ڼ���ѹ��
//          Log.i(test,ѹ��һ��!);
            baos.reset();// ����baos�����baos
            options -= 10;// ÿ�ζ�����10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
        }
//      Log.i(test,ѹ�����С + baos.toByteArray().length);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
        return bitmap;
    }
     
    /**
     * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
     * ��������ȡѹ�����ͼƬ
     * 
     * @param res
     * @param resId
     * @param reqWidth
     *            ����ͼƬѹ���ߴ���С���
     * @param reqHeight
     *            ����ͼƬѹ���ߴ���С�߶�
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
            int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
         
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
 
    /**
     * ��������ȡѹ�����ͼƬ
     * 
     * @param res
     * @param resId
     * @param reqWidth
     *            ����ͼƬѹ���ߴ���С���
     * @param reqHeight
     *            ����ͼƬѹ���ߴ���С�߶�
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String filepath,
            int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
 
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }
 
    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
            int reqWidth, int reqHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] data = baos.toByteArray();
         
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }
 
    /**
     * ����ѹ������ֵ(�Ľ��� by touch_ping)
     * 
     * ԭ��2>4>8...��ѹ��
     * ��ǰ2>3>4...��ѹ��
     * 
     * @param options
     *            ����ͼƬ��������Ϣ
     * @param reqWidth
     *            ����ͼƬѹ���ߴ���С���O
     * @param reqHeight
     *            ����ͼƬѹ���ߴ���С�߶�
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
         
        final int picheight = options.outHeight;
        final int picwidth = options.outWidth;
         
        int targetheight = picheight;
        int targetwidth = picwidth;
        int inSampleSize = 1;
         
        if (targetheight > reqHeight || targetwidth > reqWidth) {
            while (targetheight  >= reqHeight
                    && targetwidth>= reqWidth) {
                inSampleSize += 1;
                targetheight = picheight/inSampleSize;
                targetwidth = picwidth/inSampleSize;
            }
        }
         
        return inSampleSize;
    }
}