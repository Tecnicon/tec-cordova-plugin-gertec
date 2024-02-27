package gertec;

import android.content.Context;
import android.content.Intent;
import android.media.ToneGenerator;
import android.util.Log;
import android.view.ViewTreeObserver;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import br.com.gertec.ppcomp.IPPCompDSPCallbacks;
import br.com.gertec.ppcomp.PPComp;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

class OutputCallbacks implements IPPCompDSPCallbacks {
    String mMenuTitle = "";
    int mSelectedItem;
    int sTxt2Pin = -1;
    boolean isClear = false;
    boolean wasCancelBefore = false;
    String TAG = "ExemploGCR_GOC_OutputCallbacks";
    Context context;
    PPComp ppComp;
    String txtPinDisplay = "";

    public OutputCallbacks(Context context, PPComp ppComp) {
        this.context = context;
        this.ppComp = ppComp;
    }


    @Override
    public void Text(long lFlags, final String sTxt1, final String sTxt2) {
        Log.d(TAG, "lFlags = " + lFlags + " txt1 = " + sTxt1 + " txt2 = " + sTxt2);
        if (lFlags == 135170) {
           //INSERT_SWIPE_CARD
        } else if (lFlags == 327940) {
           //INVALID_APP
        } else if (lFlags == 4099) {
            //SECOND_TAP
        } else if (lFlags == 524292) {
           //PIN_BLOCKED
        } else if (lFlags == 589824) {
           //PIN_LAST_TRY
        } else if (lFlags == 69632) {
           //PROCESSING
        } else if (lFlags == 724993) {
            //REMOVE_CARD
        } else if (lFlags == 851968) {
            String[]selected = sTxt1.split("\r");
            //SELECTED_S
        } else if (lFlags == 200707) {
           //TAP_INSERT_SWIPE_CARD
        } else if (lFlags == 256 || lFlags == 0 || lFlags == 721153) {
            //TEXT_S
        } else if (lFlags == 790657 ) {
            //UPDATING_TABLES
        } else if (lFlags == 790658 ) {
            //UPDATING_RECORD
        } else if (lFlags == 393220 ) {
            //WRONG_PIN_S
        } else if (lFlags == 917504 ) {
            if (sTxt2.length() == 0) {
               //PIN_STARTING
            } else {
                //PIN_STARTING_S
            }
            txtPinDisplay = "";
            Log.d(TAG, "SHOW KBD");
            showKBD();
        }
        if (lFlags == 512 && sTxt2.length() == 0 && sTxt2Pin == -1) {
            isClear = false;
            sTxt2Pin = sTxt2.length();
        } else if (sTxt2Pin == sTxt2.length()) {
            mMenuTitle = "";
        } else if (lFlags == 512 && sTxt2.length() >= 0 && sTxt2Pin != -1 ) {
            if (sTxt2Pin <= sTxt2.length()) {
                isClear = false;
                wasCancelBefore = false;
               //PIN Entry
               beep();
                txtPinDisplay = txtPinDisplay + "*";

                PinKBDActivity.mKBDData.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        PinKBDActivity.mKBDData.textView.setText(txtPinDisplay);
                    }
                });
            } else if (sTxt2Pin > sTxt2.length() && !isClear) {
                wasCancelBefore = false;
                beep();
                isClear = true;
                txtPinDisplay = "";
                PinKBDActivity.mKBDData.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        PinKBDActivity.mKBDData.textView.setText(txtPinDisplay);
                    }
                });
            }
            sTxt2Pin = sTxt2.length();
        }
    }

    @Override
    public void Clear() {
        if (!wasCancelBefore && sTxt2Pin > 0) {
            wasCancelBefore = true;
        }
    }

    @Override
    public int MenuStart(String sTitle, AtomicLong lFlags) {
        mMenuTitle = sTitle;
        lFlags.set(DSP_F_BLOCK);
        //SELECT
        return 100;
    }

    @Override
    public int MenuShow(long lFlags, final List<String> lsOpts, final int iOptSel) {
        Log.d(TAG, "MenuShow() callback invoked.");
        Log.d(TAG, "lFlags=" + lFlags);
        Log.d(TAG, "saOpts=");
        for (String opt : lsOpts) {
            Log.d(TAG, "\t" + opt);
        }

        Log.d(TAG, "iOptSel=" + iOptSel);

        return mSelectedItem;
    }

    public void beep() {
        try {
            ToneGenerator toneGen = new ToneGenerator(4, 100);
            toneGen.startTone(93, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Mostra o teclado e PIN
    public void showKBD() {
        //Inicia a tela do teclado
        openPinKBD();
        //Aguarda até que ela tenha sido iniciada
        waitActivityOpen();
        KBDData kbdData = PinKBDActivity.getKBDData();
        //Seta o teclado na PPComp
        ppComp.PP_SetKbd(kbdData.btn1, kbdData.btn2, kbdData.btn3, kbdData.btn4,
                kbdData.btn5, kbdData.btn6, kbdData.btn7, kbdData.btn8, kbdData.btn9, kbdData.btn0, kbdData.btnCancel, kbdData.btnConfirm, kbdData.btnClear,
                kbdData.activity);
    }

    public void openPinKBD() {
        Intent intent = new Intent(context, PinKBDActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        try{
            context.startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int waitActivityOpen() {
        Log.d(TAG, "wait activity PIN start");
        try {
            while (!PinKBDActivity.active) {
                Log.d(TAG, "waiting activity PIN");
            }
            Log.d(TAG, "wait activity PIN end");
        } catch (Exception e) {
            Log.d(TAG, "wait activity PIN error");
            e.printStackTrace();
        }
        return 0;
    }

}
