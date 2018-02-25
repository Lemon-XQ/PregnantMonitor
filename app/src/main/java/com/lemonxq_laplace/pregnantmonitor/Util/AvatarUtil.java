package com.lemonxq_laplace.pregnantmonitor.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/13
 */

public class AvatarUtil {

    private static Uri imageUri;

    public static Uri getImageUri(String imageFile) {
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                imageFile));
    }

    public static Intent resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//解决部分手机拍照后点击确定图片,无法回到app
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 是否进行缩放
        intent.putExtra("scale", true);
        // 设置图片的输出大小
        intent.putExtra("outputX", 7);
        intent.putExtra("outputY", 7);
        // 设置图片输出格式
        intent.putExtra("return-data", false);
        imageUri=uri;
        return intent;
    }

    public static void showResizeImage(Context context,ImageView mImageView,Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = decodeUriAsBitmap(context,imageUri);
            mImageView.setImageBitmap(photo);
        }
    }

    public static Bitmap decodeUriAsBitmap(Context context,Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
