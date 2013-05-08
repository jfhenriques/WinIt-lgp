package pt.techzebra.promgamemobile.platform;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.widget.ImageView.ScaleType;

public class RoundedDrawable extends Drawable {
    public static final String TAG = "RoundedDrawable";

    private final RectF mBounds = new RectF();
    
    private final RectF mDrawableRect = new RectF();
    private float mCornerRadius;
    
    private final RectF mBitmapRect = new RectF();
    private final BitmapShader mBitmapShader;
    private final Paint mBitmapPaint;
    private final int mBitmapWidth;
    private final int mBitmapHeight;
    
    private final RectF mBorderRect = new RectF();
    private final Paint mBorderPaint;
    private int mBorderWidth;
    private int mBorderColor;
    
    private ScaleType mScaleType = ScaleType.FIT_XY;
    
    private final Matrix mShaderMatrix = new Matrix();
    
    RoundedDrawable(Bitmap bitmap, float cornerRadius, int border, int borderColor) {
        
        mBorderWidth = border;
        mBorderColor = borderColor; 
        
        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);
        
        mCornerRadius = cornerRadius;
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapShader.setLocalMatrix(mShaderMatrix);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);
        
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(border);
    }
    
    protected void setScaleType(ScaleType scaleType) {
        if (scaleType == null) { scaleType = ScaleType.FIT_XY; }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            setMatrix();
        }
    }
    
    protected ScaleType getScaleType() {
        return mScaleType;
    }
    
    private void setMatrix() {
        mBorderRect.set(mBounds);
        mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
        
        float scale;
        float dx;
        float dy;
        
        switch(mScaleType) {
        case CENTER:
//          Log.d(TAG, "CENTER");
            mBorderRect.set(mBounds);
            mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
            
            mShaderMatrix.set(null);
            mShaderMatrix.setTranslate((int) ((mDrawableRect.width() - mBitmapWidth) * 0.5f + 0.5f), (int) ((mDrawableRect.height() - mBitmapHeight) * 0.5f + 0.5f));
            break;
        case CENTER_CROP:
//          Log.d(TAG, "CENTER_CROP");
            mBorderRect.set(mBounds);
            mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
            
            mShaderMatrix.set(null);

            dx = 0;
            dy = 0;

            if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
                scale = (float) mDrawableRect.height() / (float) mBitmapHeight; 
                dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
            } else {
                scale = (float) mDrawableRect.width() / (float) mBitmapWidth;
                dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
            }

            mShaderMatrix.setScale(scale, scale);
            mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);
            break;
        case CENTER_INSIDE:
//          Log.d(TAG, "CENTER_INSIDE");
            mShaderMatrix.set(null);
            
            if (mBitmapWidth <= mBounds.width() && mBitmapHeight <= mBounds.height()) {
                scale = 1.0f;
            } else {
                scale = Math.min((float) mBounds.width() / (float) mBitmapWidth,
                        (float) mBounds.height() / (float) mBitmapHeight);
            }
            
            dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
            dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f);

            mShaderMatrix.setScale(scale, scale);
            mShaderMatrix.postTranslate(dx, dy);
            
            mBorderRect.set(mBitmapRect);
            mShaderMatrix.mapRect(mBorderRect);
            mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
            mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
            break;
        case FIT_CENTER:
            mBorderRect.set(mBitmapRect);
            mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER);
            mShaderMatrix.mapRect(mBorderRect);
            mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
            mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
            break;
        case FIT_END:
            mBorderRect.set(mBitmapRect);
            mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END);
            mShaderMatrix.mapRect(mBorderRect);
            mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
            mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
            break;
        case FIT_START:
            mBorderRect.set(mBitmapRect);
            mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START);
            mShaderMatrix.mapRect(mBorderRect);
            mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
            mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
            break;
        case FIT_XY:
        default:
//          Log.d(TAG, "DEFAULT TO FILL");
            mBorderRect.set(mBounds);
            mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
            mShaderMatrix.set(null);
            mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
            break;
        }
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
//      Log.i(TAG, "onboundschange: w: " + bounds.width() + "h:" + bounds.height());
        super.onBoundsChange(bounds);
        
        mBounds.set(bounds);

//      if (USE_VIGNETTE) {
//          RadialGradient vignette = new RadialGradient(
//                  mDrawableRect.centerX(), mDrawableRect.centerY() * 1.0f / 0.7f, mDrawableRect.centerX() * 1.3f,
//                  new int[] { 0, 0, 0x7f000000 }, new float[] { 0.0f, 0.7f, 1.0f },
//                  Shader.TileMode.CLAMP);
//
//          Matrix oval = new Matrix();
//          oval.setScale(1.0f, 0.7f);
//          vignette.setLocalMatrix(oval);
//
//          mBitmapPaint.setShader(
//                  new ComposeShader(mBitmapShader, vignette, PorterDuff.Mode.SRC_OVER));
//      }
        
        setMatrix();
    }

    @Override
    public void draw(Canvas canvas) {
//      Log.w(TAG, "Draw: " + mScaleType.toString());
        if (mBorderWidth > 0) {
            canvas.drawRoundRect(mBorderRect, mCornerRadius, mCornerRadius, mBorderPaint);
            canvas.drawRoundRect(mDrawableRect, Math.max(mCornerRadius - mBorderWidth, 0), Math.max(mCornerRadius - mBorderWidth, 0), mBitmapPaint);
        } else {
            canvas.drawRoundRect(mDrawableRect, mCornerRadius, mCornerRadius, mBitmapPaint);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mBitmapPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mBitmapPaint.setColorFilter(cf);
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }
    
    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }
    
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        
        Bitmap bitmap;
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width > 0 && height > 0) {
            bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap); 
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } else {
            bitmap = null;
        }

        return bitmap;
    }
    
    public static Drawable fromDrawable(Drawable drawable, float radius) {
        return fromDrawable(drawable, radius, 0, 0);
    }
    
    public static Drawable fromDrawable(Drawable drawable, float radius, int border, int borderColor) {
        if (drawable != null) {
            if (drawable instanceof TransitionDrawable) {
                TransitionDrawable td = (TransitionDrawable) drawable;
                int num = td.getNumberOfLayers();
                
                Drawable[] drawableList = new Drawable[num];
                for (int i = 0; i < num; i++) {
                    Drawable d = td.getDrawable(i);
                    if (d instanceof ColorDrawable) {
                        // TODO skip colordrawables for now
                        drawableList[i] = d;
                    } else {
                        drawableList[i] = new RoundedDrawable(drawableToBitmap(td.getDrawable(i)), radius, border, borderColor);
                    }
                }
                return new TransitionDrawable(drawableList);
            } 
            
            Bitmap bm = drawableToBitmap(drawable);
            if (bm != null) {
                return new RoundedDrawable(bm, radius, border, borderColor);
            } else {
                Log.w(TAG, "Failed to create bitmap from drawable!");
            }
        }
        return drawable;
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setCornerRadius(float radius) {
        this.mCornerRadius = radius;
    }

    public void setBorderWidth(int width) {
        this.mBorderWidth = width;
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    public void setBorderColor(int color) {
        this.mBorderColor = color;
        mBorderPaint.setColor(color);
    }
}

//public class RoundedDrawable extends Drawable {
//    public static final String TAG = "RoundedDrawable";
//    
//    private final RectF bounds_ = new RectF();
//    
//    private final RectF drawable_rect_ = new RectF();
//    private float corner_radius_;
//    
//    private final RectF bitmap_rect_ = new RectF();
//    private final BitmapShader bitmap_shader_;
//    private final Paint bitmap_paint_;
//    private final int bitmap_width_;
//    private final int bitmap_height_;
//
//    private final RectF border_rect_ = new RectF();
//    private final Paint border_paint_;
//    private int border_width_;
//    private int border_color_;
//    
//    private ScaleType scale_type_ = ScaleType.FIT_XY;
//    
//    private final Matrix shader_matrix_ = new Matrix();
//    
//    public RoundedDrawable(Bitmap bitmap, float corner_radius, int border, int border_color) {
//        border_width_ = border;
//        border_color_ = border_color;
//        
//        bitmap_width_ = bitmap.getWidth();
//        bitmap_height_ = bitmap.getHeight();
//        bitmap_rect_.set(0, 0, bitmap_width_, bitmap_height_);
//        
//        corner_radius_ = corner_radius;
//        bitmap_shader_ = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        bitmap_shader_.setLocalMatrix(shader_matrix_);
//        
//        bitmap_paint_ = new Paint();
//        bitmap_paint_.setAntiAlias(true);
//        bitmap_paint_.setShader(bitmap_shader_);
//        
//        border_paint_ = new Paint();
//        border_paint_.setAntiAlias(true);
//        border_paint_.setColor(border_color_);
//        border_paint_.setStrokeWidth(border);
//    }
//    
//    protected void setScaleType(ScaleType scale_type) {
//        if (scale_type == null) {
//            scale_type = ScaleType.FIT_XY;
//        }
//        
//        if (scale_type_ != scale_type) {
//            scale_type_ = scale_type;
//            setMatrix();
//        }
//    }
//    
//    protected ScaleType getScaleType() {
//        return scale_type_;
//    }
//    
//    private void setMatrix() {
//        border_rect_.set(bounds_);
//        drawable_rect_.set(0 + border_width_, 0 + border_width_, border_rect_.width() - border_width_, border_rect_.height() - border_width_);
//        
//        float scale;
//        float dx;
//        float dy;
//        
//        switch (scale_type_) {
//            case CENTER:
//                border_rect_.set(bounds_);
//                drawable_rect_.set(0 + border_width_, 0 + border_width_, border_rect_.width() - border_width_, border_rect_.height() - border_width_);
//                
//                shader_matrix_.set(null);
//                shader_matrix_.setTranslate((int) ((drawable_rect_.width() - bitmap_width_) * 0.5f + 0.5f), (int) ((drawable_rect_.height() - bitmap_height_) * 0.5f + 0.5f));
//                break;
//            case CENTER_CROP:
//                border_rect_.set(bounds_);
//                drawable_rect_.set(0 + border_width_, 0 + border_width_, border_rect_.width() - border_width_, border_rect_.height() - border_width_);
//                
//                shader_matrix_.set(null);
//                
//                dx = 0;
//                dy = 0;
//                
//                if (bitmap_width_ * drawable_rect_.height() > drawable_rect_.width() * bitmap_height_) {
//                    scale = (float) drawable_rect_.height() / (float) bitmap_height_;
//                    dx = (drawable_rect_.width() - bitmap_width_ * scale) * 0.5f;
//                } else {
//                    scale = (float) drawable_rect_.width() / (float) bitmap_width_;
//                    dy = (drawable_rect_.height() - bitmap_height_ * scale) * 0.5f;
//                }
//                
//                shader_matrix_.setScale(scale, scale);
//                shader_matrix_.postTranslate((int) (dx + 0.5f) + border_width_, (int) (dy + 0.5f) + border_width_);
//                break;
//            case CENTER_INSIDE:
//                shader_matrix_.set(null);
//                
//                if (bitmap_width_ <= bounds_.width() && bitmap_height_ <= bounds_.height()) {
//                    scale = 1.0f;
//                } else {
//                    scale = Math.min((float) bounds_.width() / (float) bitmap_width_,
//                            (float) bounds_.height() / (float) bitmap_height_);
//                }
//
//                dx = (int) ((bounds_.width() - bitmap_width_ * scale) * 0.5f + 0.5f);
//                dy = (int) ((bounds_.height() - bitmap_height_ * scale) * 0.5f + 0.5f);
//
//                shader_matrix_.setScale(scale, scale);
//                shader_matrix_.postTranslate(dx, dy);
//
//                border_rect_.set(bitmap_rect_);
//                shader_matrix_.mapRect(border_rect_);
//                drawable_rect_.set(border_rect_.left + border_width_, border_rect_.top + border_width_, border_rect_.right - border_width_, border_rect_.bottom - border_width_);
//                shader_matrix_.setRectToRect(bitmap_rect_, drawable_rect_, Matrix.ScaleToFit.FILL);
//                break;
//            case FIT_CENTER:
//                border_rect_.set(bitmap_rect_);
//                shader_matrix_.setRectToRect(bitmap_rect_, bounds_, Matrix.ScaleToFit.CENTER);
//                shader_matrix_.mapRect(border_rect_);
//                drawable_rect_.set(border_rect_.left + border_width_, border_rect_.top + border_width_, border_rect_.right - border_width_, border_rect_.bottom - border_width_);
//                shader_matrix_.setRectToRect(bitmap_rect_, drawable_rect_, Matrix.ScaleToFit.FILL);
//                break;
//            case FIT_END:
//                border_rect_.set(bitmap_rect_);
//                shader_matrix_.setRectToRect(bitmap_rect_, bounds_, Matrix.ScaleToFit.END);
//                shader_matrix_.mapRect(border_rect_);
//                drawable_rect_.set(border_rect_.left + border_width_, border_rect_.top + border_width_, border_rect_.right - border_width_, border_rect_.bottom - border_width_);
//                shader_matrix_.setRectToRect(bitmap_rect_, drawable_rect_, Matrix.ScaleToFit.FILL);
//                break;
//            case FIT_START:
//                border_rect_.set(bitmap_rect_);
//                shader_matrix_.setRectToRect(bitmap_rect_, bounds_, Matrix.ScaleToFit.START);
//                shader_matrix_.mapRect(border_rect_);
//                drawable_rect_.set(border_rect_.left + border_width_, border_rect_.top + border_width_, border_rect_.right - border_width_, border_rect_.bottom - border_width_);
//                shader_matrix_.setRectToRect(bitmap_rect_, drawable_rect_, Matrix.ScaleToFit.FILL);
//                break;
//            case FIT_XY:
//            default:
//                border_rect_.set(bounds_);
//                drawable_rect_.set(0 + border_width_, 0 + border_width_, border_rect_.width() - border_width_, border_rect_.height() - border_width_);
//                shader_matrix_.set(null);
//                shader_matrix_.setRectToRect(bitmap_rect_, drawable_rect_, Matrix.ScaleToFit.FILL);
//        }
//        
//        bitmap_shader_.setLocalMatrix(shader_matrix_);
//    }
//    
//    @Override
//    protected void onBoundsChange(Rect bounds) {
//        super.onBoundsChange(bounds);
//        
//        bounds_.set(bounds);
//        
//        setMatrix();
//    }
//    
//    @Override
//    public void draw(Canvas canvas) {
//        if (border_width_ > 0) {
//            canvas.drawRoundRect(border_rect_, corner_radius_, corner_radius_, border_paint_);
//            canvas.drawRoundRect(drawable_rect_, Math.max(corner_radius_ - border_width_, 0), Math.max(corner_radius_ - border_width_, 0), bitmap_paint_);
//        } else {
//            canvas.drawRoundRect(drawable_rect_, corner_radius_, corner_radius_, bitmap_paint_);
//        }
//    }
//
//    @Override
//    public int getOpacity() {
//        return PixelFormat.TRANSLUCENT;
//    }
//
//    @Override
//    public void setAlpha(int alpha) {
//        bitmap_paint_.setAlpha(alpha);
//    }
//
//    @Override
//    public void setColorFilter(ColorFilter cf) {
//        bitmap_paint_.setColorFilter(cf);
//    }
//    
//    @Override
//    public int getIntrinsicWidth() {
//        return bitmap_width_;
//    }
//    
//    @Override
//    public int getIntrinsicHeight() {
//        return bitmap_height_;
//    }
//    
//    public static Bitmap drawableToBitmap(Drawable drawable) {
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        }
//        
//        Bitmap bitmap;
//        int width = drawable.getIntrinsicWidth();
//        int height = drawable.getIntrinsicHeight();
//            
//        if (width > 0 && height > 0) {
//            bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);
//            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//            drawable.draw(canvas);
//        } else {
//            bitmap = null;
//        }
//        
//        return bitmap;
//    }
//    
//    public static Drawable fromDrawable(Drawable drawable, float radius) {
//        return fromDrawable(drawable, radius, 0, 0);
//    }
//    
//    public static Drawable fromDrawable(Drawable drawable, float radius, int border, int border_color) {
//        if (drawable != null) {
//            if (drawable instanceof TransitionDrawable) {
//                TransitionDrawable td = (TransitionDrawable) drawable;
//                int num = td.getNumberOfLayers();
//                
//                Drawable[] drawable_list = new Drawable[num];
//                for (int i = 0; i < num; ++i) {
//                    Drawable d = td.getDrawable(i);
//                    if (d instanceof ColorDrawable) {
//                        // TODO: skip colordrawables for now
//                        drawable_list[i] = d;
//                    } else {
//                        drawable_list[i] = new RoundedDrawable(drawableToBitmap(td.getDrawable(i)), radius, border, border_color);
//                    }
//                }
//                
//                return new TransitionDrawable(drawable_list);
//            }
//            
//            Bitmap bm = drawableToBitmap(drawable);
//            if (bm != null) {
//                return new RoundedDrawable(bm, radius, border, border_color);
//            }
//        }
//        
//        return drawable;
//    }
//    
//    public float getCornerRadius() {
//        return corner_radius_;
//    }
//    
//    public int getBorderWidth() {
//        return border_width_;
//    }
//    
//    public int getBorderColor() {
//        return border_color_;
//    }
//    
//    public void setCornerRadius(float radius) {
//        corner_radius_ = radius;
//    }
//
//    public void setBorderWidth(int width) {
//        border_width_ = width;
//        border_paint_.setStrokeWidth(width);
//    }
//    
//    public void setBorderColor(int color) {
//        border_color_ = color;
//        border_paint_.setColor(color);
//    }
//}
