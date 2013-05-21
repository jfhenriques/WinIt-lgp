package pt.techzebra.winit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class FixedAspectRatioButton extends Button {
    
    public FixedAspectRatioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedAspectRatioButton(Context context, AttributeSet attrs, int def_style) {
        super(context, attrs, def_style);
    }
    
    @Override
    protected void onMeasure(int width_measure_spec, int height_measure_spec) {
        super.onMeasure( width_measure_spec, width_measure_spec);
    }
}
