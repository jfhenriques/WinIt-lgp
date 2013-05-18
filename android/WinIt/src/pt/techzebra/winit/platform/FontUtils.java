package pt.techzebra.winit.platform;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontUtils {
    public static interface FontTypes {
        public static String LIGHT = "Light";
        public static String BOLD = "Bold";
    }
    
    private static Map<String, String> font_map_ = new HashMap<String, String>();
    static {
        font_map_.put(FontTypes.LIGHT, "fonts/Roboto-Light.ttf");
        font_map_.put(FontTypes.BOLD, "fonts/Roboto-Bold.ttf");
    }
    
    private static Map<String, Typeface> typeface_cache_ = new HashMap<String, Typeface>();
    
    private static Typeface getRobotoTypeface(Context context, String font_type) {
        String font_path = font_map_.get(font_type);
        if (!typeface_cache_.containsKey(font_path)) {
            typeface_cache_.put(font_type, Typeface.createFromAsset(context.getAssets(), font_path));
        }
        
        return typeface_cache_.get(font_type);
    }
    
    private static Typeface getRobotoTypeface(Context context, Typeface original_typeface) {
        String roboto_font_type = FontTypes.LIGHT;
        if (original_typeface != null) {
            int style = original_typeface.getStyle();
            switch (style) {
                case Typeface.BOLD:
                    roboto_font_type = FontTypes.BOLD;
                    break;
            }
        }
        return getRobotoTypeface(context, roboto_font_type);
    }
    
    public static void setRobotoFont(Context context, View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
                setRobotoFont(context, ((ViewGroup) view).getChildAt(i));
            }
        } else if (view instanceof TextView) {
            Typeface current_typeface = ((TextView) view).getTypeface();
            ((TextView) view).setTypeface(getRobotoTypeface(context, current_typeface));
        }
    }
}
