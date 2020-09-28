package com.aliceapps.uielements.spinners;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.aliceapps.uielements.R;

public class ImageSpinnerView extends androidx.appcompat.widget.AppCompatSpinner {
    private static final int MODE_THEME = -1;
    private int layoutId;
    private CharSequence[] entries;
    private CharSequence[] values;
    private int imagesId;

    /**
     * Construct a new spinner with the given context's theme.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public ImageSpinnerView(Context context) {
        this(context, null);
    }

    /**
     * Construct a new spinner with the given context's theme and the supplied
     * mode of displaying choices. <code>mode</code> may be one of MODE_DIALOG or MODE_DROPDOWN
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param mode    Constant describing how the user will select choices from the spinner.
     */
    public ImageSpinnerView(Context context, int mode) {
        this(context, null, R.attr.spinnerStyle, mode);
    }

    /**
     * Construct a new spinner with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public ImageSpinnerView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.spinnerStyle);
    }

    /**
     * Construct a new spinner with the given context's theme, the supplied attribute set,
     * and default style attribute.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     */
    public ImageSpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, MODE_THEME);
    }

    /**
     * Construct a new spinner with the given context's theme, the supplied attribute set,
     * and default style. <code>mode</code> may be one of MODE_DIALOG or
     * MODE_DROPDOWN and determines how the user will select choices from the spinner.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @param mode         Constant describing how the user will select choices from the spinner.
     */
    public ImageSpinnerView(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        this(context, attrs, defStyleAttr, mode, null);
    }

    /**
     * Constructs a new spinner with the given context's theme, the supplied
     * attribute set, default styles, popup mode (one of MODE_DIALOG
     * or MODE_DROPDOWN), and the context against which the popup
     * should be inflated.
     * Constructor initializes ImageSpinnerAdapter
     *
     * @param context      The context against which the view is inflated, which
     *                     provides access to the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default
     *                     values for the view. Can be 0 to not look for
     *                     defaults.
     * @param mode         Constant describing how the user will select choices from
     *                     the spinner.
     * @param popupTheme   The theme against which the dialog or dropdown popup
     *                     should be inflated. May be {@code null} to use the
     *                     view theme. If set, this will override any value
     *                     specified by
     *                     {@link R.styleable#Spinner_popupTheme}.
     */
    @SuppressLint({"PrivateResource", "CustomViewStyleable"})
    public ImageSpinnerView(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);

        if (attrs != null) {
            TypedArray spinnerAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.Spinner, defStyleAttr, 0);
            entries = spinnerAttrs.getTextArray(R.styleable.Spinner_android_entries);
            spinnerAttrs.recycle();

            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImageSpinnerView);
            layoutId = a.getResourceId(R.styleable.ImageSpinnerView_spinner_item_layout, R.layout.image_spinner);
            values = a.getTextArray(R.styleable.ImageSpinnerView_spinner_values);
            imagesId = a.getResourceId(R.styleable.ImageSpinnerView_spinner_images, 0);
            a.recycle();
        }

        ImageSpinnerAdapter adapterEntries = new ImageSpinnerAdapter(context, entries, values, layoutId, imagesId);
        adapterEntries.setDropDownViewResource(layoutId);
        setAdapter(adapterEntries);
    }
    /**
     *
     * @return list of Adapter entries
     */
    public CharSequence[] getEntries() {
        return entries;
    }
    /**
     *
     * @return list of Adapter values
     */
    public CharSequence[] getValues() {
        return values;
    }
}
