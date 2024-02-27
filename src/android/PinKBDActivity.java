package android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class PinKBDActivity extends Activity {

    private static final String TAG = PinKBDActivity.class.getName();
    static KBDData mKBDData;
    static boolean active = false;

    public static final int frameMantaLayout_pin = 2131230882;
    public static final int teclado_pin = 2131427371;
    public static final int btn0Tag = 2131623964;
    public static final int btn1Tag = 2131623965;
    public static final int btn2Tag = 2131623966;
    public static final int btn3Tag = 2131623967;
    public static final int btn4Tag = 2131623968;
    public static final int btn5Tag = 2131623969;
    public static final int btn6Tag = 2131623970;
    public static final int btn7Tag = 2131623971;
    public static final int btn8Tag = 2131623972;
    public static final int btn9Tag = 2131623973;
    public static final int btnCancelTag = 2131623974;
    public static final int btnClearTag = 2131623975;
    public static final int btnEnterTag = 2131623976;
    public static final int lblDigitsTag = 2131623993;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);

        setContentView(R.layout.activity_manta);
        final View rootView = getWindow().getDecorView().getRootView();

        FrameLayout frameLayout_pin = findViewById(frameMantaLayout_pin);
        View child = null;

        child = getLayoutInflater().inflate(teclado_pin, null);

        frameLayout_pin.addView(child);
        mKBDData = new KBDData();
        mKBDData.btn0 = rootView.findViewWithTag(getString(btn0Tag));
        mKBDData.btn1 = rootView.findViewWithTag(getString(btn1Tag));
        mKBDData.btn2 = rootView.findViewWithTag(getString(btn2Tag));
        mKBDData.btn3 = rootView.findViewWithTag(getString(btn3Tag));
        mKBDData.btn4 = rootView.findViewWithTag(getString(btn4Tag));
        mKBDData.btn5 = rootView.findViewWithTag(getString(btn5Tag));
        mKBDData.btn6 = rootView.findViewWithTag(getString(btn6Tag));
        mKBDData.btn7 = rootView.findViewWithTag(getString(btn7Tag));
        mKBDData.btn8 = rootView.findViewWithTag(getString(btn8Tag));
        mKBDData.btn9 = rootView.findViewWithTag(getString(btn9Tag));
        mKBDData.btnCancel = rootView.findViewWithTag(getString(btnCancelTag));
        mKBDData.btnClear = rootView.findViewWithTag(getString(btnClearTag));
        mKBDData.btnConfirm = rootView.findViewWithTag(getString(btnEnterTag));
        mKBDData.textView = rootView.findViewWithTag(getString(lblDigitsTag));
        mKBDData.activity = PinKBDActivity.this;

        Log.i(TAG, "onCreate: end");

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    public static KBDData getKBDData() {
        return mKBDData;
    }

}
