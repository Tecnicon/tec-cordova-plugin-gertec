package gertec;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

public class KBDData {
    public Button btn0;
    public Button btn1;
    public Button btn2;
    public Button btn3;
    public Button btn4;
    public Button btn5;
    public Button btn6;
    public Button btn7;
    public Button btn8;
    public Button btn9;
    public Button btnCancel;
    public Button btnClear;
    public Button btnConfirm;
    public TextView textView;
    public Activity activity;

    public KBDData() {
    }

    public KBDData(Button btn0, Button btn1, Button btn2, Button btn3, Button btn4, Button btn5,
                   Button btn6, Button btn7, Button btn8, Button btn9, Button btnCancel,
                   Button btnClear, Button btnConfirm, TextView textView, Activity activity) {
        this.btn0 = btn0;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
        this.btn4 = btn4;
        this.btn5 = btn5;
        this.btn6 = btn6;
        this.btn7 = btn7;
        this.btn8 = btn8;
        this.btn9 = btn9;
        this.btnCancel = btnCancel;
        this.btnClear = btnClear;
        this.btnConfirm = btnConfirm;
        this.textView = textView;
        this.activity = activity;
    }

}

