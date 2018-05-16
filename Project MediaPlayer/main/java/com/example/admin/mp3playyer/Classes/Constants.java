package com.example.admin.mp3playyer.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.admin.mp3playyer.R;

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "action.main";
        public static String INIT_ACTION = "action.init";
        public static String PREV_ACTION = "action.prev";
        public static String PLAY_ACTION = "action.play";
        public static String NEXT_ACTION = "action.next";
        public static String STARTFOREGROUND_ACTION = "action.startforeground";
        public static String STOPFOREGROUND_ACTION = "action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.allmusic, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }
}
