package pt.techzebra.winit.platform;

import pt.techzebra.winit.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class RoundedImageView extends ImageView {
    public static final String TAG = "RoundedImageView";
    
    public static final int DEFAULT_RADIUS = 0;
    public static final int DEFAULT_BORDER = 0;
    public static final int DEFAULT_BORDER_COLOR = Color.WHITE;
    
    private int corner_radius_;
    private int border_width_;
    private int border_color_;
    
    private boolean round_background_;
    
    private Drawable drawable_;
    private Drawable background_drawable_;
    
    private ScaleType scale_type_;
    
    private static final ScaleType[] scale_types_ = {
        ScaleType.MATRIX,
        ScaleType.FIT_XY,
        ScaleType.FIT_START,
        ScaleType.FIT_CENTER,
        ScaleType.FIT_END,
        ScaleType.CENTER,
        ScaleType.CENTER_CROP,
        ScaleType.CENTER_INSIDE
    };
    
    public RoundedImageView(Context context) {
        super(context);
        
        corner_radius_ = DEFAULT_RADIUS;
        border_width_ = DEFAULT_BORDER;
        border_color_ = DEFAULT_BORDER_COLOR;
    }
    
    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public RoundedImageView(Context context, AttributeSet attrs, int def_style) {
        super(context, attrs, def_style);
        
        TypedArray styled_attributes = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, def_style, 0);

        int index = styled_attributes.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
        
        if (index >= 0) {
            setScaleType(scale_types_[index]);
        }
        
        corner_radius_ = styled_attributes.getDimensionPixelSize(R.styleable.RoundedImageView_cornerRadius, -1);
        border_width_ = styled_attributes.getDimensionPixelSize(R.styleable.RoundedImageView_borderWidth, -1);
        
        if (corner_radius_ < 0) {
            corner_radius_ = DEFAULT_RADIUS;
        }
        
        if (border_width_ < 0) {
            border_width_ = DEFAULT_BORDER;
        }
        
        border_color_ = styled_attributes.getColor(R.styleable.RoundedImageView_borderColor, DEFAULT_BORDER_COLOR);
        
        round_background_ = styled_attributes.getBoolean(R.styleable.RoundedImageView_roundBackground, false);
        
        styled_attributes.recycle();
    }
    
    @Override
    public void setScaleType(ScaleType scale_type) {
        if (scale_type == null) {
            throw new NullPointerException();
        }
        
        if (scale_type_ != scale_type) {
            scale_type_ = scale_type;
        
            switch(scale_type) {
                case CENTER:
                case CENTER_CROP:
                case CENTER_INSIDE:
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                case FIT_XY:
                    super.setScaleType(ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scale_type);
                    break;
            }
            
            if (drawable_ instanceof RoundedDrawable
                    && ((RoundedDrawable) drawable_).getScaleType() != scale_type) {
                ((RoundedDrawable) drawable_).setScaleType(scale_type);
            }
            
            if (background_drawable_ instanceof RoundedDrawable
                    && ((RoundedDrawable) background_drawable_).getScaleType() != scale_type) {
                ((RoundedDrawable) background_drawable_).setScaleType(scale_type);
            }
            
            setWillNotCacheDrawing(true);
            requestLayout();
            invalidate();
        }
    }
    
    @Override
    public ScaleType getScaleType() {
        return scale_type_;
    }
    
    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable != null) {
            drawable_ = RoundedDrawable.fromDrawable(drawable, corner_radius_, border_width_, border_color_);
            ((RoundedDrawable) drawable_).setScaleType(scale_type_);
            ((RoundedDrawable) drawable_).setCornerRadius(corner_radius_);
            ((RoundedDrawable) drawable_).setBorderWidth(border_width_);
            ((RoundedDrawable) drawable_).setBorderColor(border_color_);
        } else {
            drawable_ = null;
        }
        
        super.setImageDrawable(drawable_);
    }
    
    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm != null) {
            drawable_ = new RoundedDrawable(bm, corner_radius_, border_width_, border_color_);
            ((RoundedDrawable) drawable_).setScaleType(scale_type_);
            ((RoundedDrawable) drawable_).setCornerRadius(corner_radius_);
            ((RoundedDrawable) drawable_).setBorderWidth(border_width_);
            ((RoundedDrawable) drawable_).setBorderColor(border_color_);
        } else {
            drawable_ = null;
        }
        
        super.setImageDrawable(drawable_);
    }
    
    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }
    
    @Override
    @Deprecated
    public void setBackgroundDrawable(Drawable background) {
        if (round_background_ && background != null) {
            background_drawable_ = RoundedDrawable.fromDrawable(background, corner_radius_, border_width_, border_color_);
            ((RoundedDrawable) background_drawable_).setScaleType(scale_type_);
            ((RoundedDrawable) background_drawable_).setCornerRadius(corner_radius_);
            ((RoundedDrawable) background_drawable_).setBorderWidth(border_width_);
            ((RoundedDrawable) background_drawable_).setBorderColor(border_color_);
        } else {
            background_drawable_ = background;
        }
        super.setBackgroundDrawable(background_drawable_);
    }
    
    public int getCornerRadius() {
        return corner_radius_;
    }
    
    public int getBorder() {
        return border_width_;
    }
    
    public int getBorderColor() {
        return border_color_;
    }
    
    public void setCornerRadius(int radius) {
        if (corner_radius_ == radius) {
            return;
        }
        
        corner_radius_ = radius;
        
        if (drawable_ instanceof RoundedDrawable) {
            ((RoundedDrawable) drawable_).setCornerRadius(radius);
        }
        
        if (round_background_ && background_drawable_ instanceof RoundedDrawable) {
            ((RoundedDrawable) background_drawable_).setCornerRadius(radius);
        }
    }
    
    public void setBorderWidth(int width) {
        if (border_width_ == width) {
            return;
        }

        border_width_ = width;
        if (drawable_ instanceof RoundedDrawable) {
            ((RoundedDrawable) drawable_).setBorderWidth(width);
        }

        if (round_background_ && background_drawable_ instanceof RoundedDrawable) {
            ((RoundedDrawable) background_drawable_).setBorderWidth(width);
        }

        invalidate();
    }
    
    public void setBorderColor(int color) {
        if (border_color_ == color) {
            return;
        }

        border_color_ = color;
        if (drawable_ instanceof RoundedDrawable) {
            ((RoundedDrawable) drawable_).setBorderColor(color);
        }

        if (round_background_ && background_drawable_ instanceof RoundedDrawable) {
            ((RoundedDrawable) background_drawable_).setBorderColor(color);
        }

        if (border_width_ > 0) {
            invalidate();
        }
    }
    
    public boolean isRoundBackground() {
        return round_background_;
    }
    
    public void setRoundBackground(boolean round_background) {
        if (round_background_ == round_background) {
            return;
        }

        round_background_ = round_background;
        
        if (round_background) {
            if (background_drawable_ instanceof RoundedDrawable) {
                ((RoundedDrawable) background_drawable_).setScaleType(scale_type_);
                ((RoundedDrawable) background_drawable_).setCornerRadius(corner_radius_);
                ((RoundedDrawable) background_drawable_).setBorderWidth(border_width_);
                ((RoundedDrawable) background_drawable_).setBorderColor(border_color_);
            } else {
                setBackgroundDrawable(background_drawable_);
            }
        } else if (background_drawable_ instanceof RoundedDrawable) {
            ((RoundedDrawable) background_drawable_).setBorderWidth(0);
            ((RoundedDrawable) background_drawable_).setCornerRadius(0);
        }

        invalidate();
    }
}
