package pt.techzebra.winit.widget;

import pt.techzebra.winit.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FixedAspectRatioImageView extends ImageView {
    private int aspect_ratio_width_;
    private int aspect_ratio_height_;
    
    public FixedAspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initialize(context, attrs);
    }

    public FixedAspectRatioImageView(Context context, AttributeSet attrs, int def_style) {
        super(context, attrs, def_style);
        
        initialize(context, attrs);
    }
    
    private void initialize(Context context, AttributeSet attrs) {
        TypedArray styled_attributes = context.obtainStyledAttributes(attrs, R.styleable.FixedAspectRatioImageView);
        
        aspect_ratio_width_ = styled_attributes.getInt(R.styleable.FixedAspectRatioImageView_aspectRatioWidth, 1);
        aspect_ratio_height_ = styled_attributes.getInt(R.styleable.FixedAspectRatioImageView_aspectRatioHeight, 1);
        
        styled_attributes.recycle();
    }
    
    @Override
    protected void onMeasure(int width_measure_spec, int height_measure_spec) {
        int original_width = MeasureSpec.getSize(width_measure_spec);
        int original_height = MeasureSpec.getSize(height_measure_spec);
        int calculated_height = original_width * aspect_ratio_height_ / aspect_ratio_width_;
        int final_width, final_height;
        
        if (calculated_height > original_height) {
            final_width = original_height * aspect_ratio_width_ / aspect_ratio_height_;
            final_height = original_height;
        } else {
            final_width = original_width;
            final_height = calculated_height;
        }
        
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(final_width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(final_height, MeasureSpec.EXACTLY));
    }
}
