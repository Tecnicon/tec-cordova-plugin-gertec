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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);

        setContentView(R.layout.activity_manta);
        final View rootView = getWindow().getDecorView().getRootView();

        FrameLayout frameLayout_pin = findViewById(R.id.frameMantaLayout_pin);
        View child = null;

        child = getLayoutInflater().inflate(R.layout.teclado_pin, null);

        frameLayout_pin.addView(child);
        mKBDData = new KBDData();
        mKBDData.btn0 = rootView.findViewWithTag(getString(R.string.btn0Tag));
        mKBDData.btn1 = rootView.findViewWithTag(getString(R.string.btn1Tag));
        mKBDData.btn2 = rootView.findViewWithTag(getString(R.string.btn2Tag));
        mKBDData.btn3 = rootView.findViewWithTag(getString(R.string.btn3Tag));
        mKBDData.btn4 = rootView.findViewWithTag(getString(R.string.btn4Tag));
        mKBDData.btn5 = rootView.findViewWithTag(getString(R.string.btn5Tag));
        mKBDData.btn6 = rootView.findViewWithTag(getString(R.string.btn6Tag));
        mKBDData.btn7 = rootView.findViewWithTag(getString(R.string.btn7Tag));
        mKBDData.btn8 = rootView.findViewWithTag(getString(R.string.btn8Tag));
        mKBDData.btn9 = rootView.findViewWithTag(getString(R.string.btn9Tag));
        mKBDData.btnCancel = rootView.findViewWithTag(getString(R.string.btnCancelTag));
        mKBDData.btnClear = rootView.findViewWithTag(getString(R.string.btnClearTag));
        mKBDData.btnConfirm = rootView.findViewWithTag(getString(R.string.btnEnterTag));
        mKBDData.textView = rootView.findViewWithTag(getString(R.string.lblDigitsTag));
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
