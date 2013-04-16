package pt.techzebra.promgamemobile.platform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = null;
        BitmapShader shader;
        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        
        
    }

}
