package gertec;

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
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);

//int id frameMantaLayout_pin 0x7f0800a2
        //   int layout teclado_pin 0x7f0b002b
/*int string btn0Tag 0x7f0e001c
int string btn1Tag 0x7f0e001d
int string btn2Tag 0x7f0e001e
int string btn3Tag 0x7f0e001f
int string btn4Tag 0x7f0e0020
int string btn5Tag 0x7f0e0021
int string btn6Tag 0x7f0e0022
int string btn7Tag 0x7f0e0023
int string btn8Tag 0x7f0e0024
int string btn9Tag 0x7f0e0025
int string btnCancelTag 0x7f0e0026
int string btnClearTag 0x7f0e0027
int string btnEnterTag 0x7f0e0028
        int string lblDigitsTag 0x7f0e0039
         */

        String frameMantaLayout_pin = "0x7f0800a2";
        String teclado_pin = "0x7f0b002b";
        String btn0Tag = "0x7f0e001c";
        String btn1Tag = "0x7f0e001d";
        String btn2Tag = "0x7f0e001e";
        String btn3Tag = "0x7f0e001f";
        String btn4Tag = "0x7f0e0020";
        String btn5Tag = "0x7f0e0021";
        String btn6Tag = "0x7f0e0022";
        String btn7Tag = "0x7f0e0023";
        String btn8Tag = "0x7f0e0024";
        String btn9Tag = "0x7f0e0025";
        String btnCancelTag = "0x7f0e0026";
        String btnClearTag = "0x7f0e0027";
        String btnEnterTag = "0x7f0e0028";
        String lblDigitsTag = "0x7f0e0039";

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
