package gertec;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import br.com.gertec.exemplo_init.R;

public class PinKBDActivity extends Activity {
    private static final String TAG = PinKBDActivity.class.getName();
    public static KBDData mKBDData;
    public static boolean active = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
   
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);

        setContentView(R.layout.teclado_pin);
        final View rootView = getWindow().getDecorView().getRootView();

       /* mKBDData = new KBDData();
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
        mKBDData.textView = rootView.findViewWithTag(getString(R.string.lblDigitsTag));*/
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

