package sk.ics.upjs.VkSystemko.Utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;

/**
 * Created by Viliam on 3.6.2014.
 */
public class Utils {

    public static int getColor(ColorDrawable drawable) {

        if (Build.VERSION.SDK_INT >= 11) {
            return drawable.getColor();
        }
        try {
            Field field = drawable.getClass().getDeclaredField("mState");
            field.setAccessible(true);
            Object object = field.get(drawable);
            field = object.getClass().getDeclaredField("mUseColor");
            field.setAccessible(true);
            return field.getInt(object);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return Color.TRANSPARENT;
    }

    public static int getRelativeLayoutColor(RelativeLayout layout){
        int color = Color.TRANSPARENT;
        Drawable background = layout.getBackground();
        if (background instanceof ColorDrawable) {
            color = getColor((ColorDrawable) background);

        }
        return color;
    }
}
