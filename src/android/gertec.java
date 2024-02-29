package gertec;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.content.Context;
import android.app.Activity;

import br.com.gertec.gedi.enums.GEDI_PRNTR_e_PrintDensity;
import br.com.gertec.gedi.exceptions.GediException;
import br.com.gertec.gedi.interfaces.IGEDI;
import br.com.gertec.gedi.interfaces.IPRNTR;
import br.com.gertec.gedi.structs.GEDI_PRNTR_st_StringConfig;
import br.com.gertec.gedi.GEDI;
import java.lang.Exception;

import br.com.gertec.ppcomp.PPComp;
import br.com.gertec.ppcomp.PPCompException;
import br.com.gertec.ppcomp.exceptions.PPCompCancelException;
import br.com.gertec.ppcomp.exceptions.PPCompNotifyException;
import br.com.gertec.ppcomp.exceptions.PPCompProcessingException;
import br.com.gertec.ppcomp.exceptions.PPCompTabExpException;

/**
 * This class echoes a string called from JavaScript.
 */
public class gertec extends CordovaPlugin {

    private Context context;
    private CordovaInterface cordovaInt;

    IGEDI mGedi;
    Button btnGCR;
    Button btnGOC;
    PPComp ppComp;
    String retornoPinPad = "";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {

        super.initialize(cordova, webView);
        this.context = cordova.getActivity().getApplicationContext();
        this.cordovaInt = cordova;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("imprimir".equals(action)) {
            String texto = args.toString();

            try {
                imprimirComprovante(texto);
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
                return false;
            }
            callbackContext.success("Ok...");
            return true;
        } else if ("inicializarPinPad".equals(action)) {

            String texto = args.toString();

            try {
                inicializarPinPad();
            } catch (Throwable e) {
                callbackContext.error(retornoPinPad + "::" + e.getMessage());
                return false;
            }
            callbackContext.success("Ok.." + retornoPinPad);
            return true;
        } else if ("mostrarMensagem".equals(action)) {

            String texto = args.toString();

            try {
                mostrarMensagem(texto);
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
                return false;
            }
            callbackContext.success("Ok....");
            return true;
        }

        callbackContext.error(action + " is not a supported action");
        return false;
    }

    private void inicializarPinPad() throws PPCompException {
        try {
            cordovaInt.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MainActivity iniciar = (MainActivity) cordovaInt.getActivity();
                    } catch (Exception e) {
                        retornoPinPad += 2;
                        throw e;
                    }

                }
            });
        } catch (Exception e) {
            retornoPinPad += 1;
            throw e;
        }
    }

    private void imprimirComprovante(String texto) throws PPCompException {

        GEDI.init(context);
        mGedi = GEDI.getInstance(context);

        new Thread(() -> {
            IPRNTR mPRNTR = mGedi.getPRNTR();
            GEDI_PRNTR_st_StringConfig strconfig = new GEDI_PRNTR_st_StringConfig(new Paint());

            try {
                mPRNTR.Init();
                strconfig.paint.setTypeface(Typeface.MONOSPACE);
                strconfig.paint.setTextSize(16);
                mPRNTR.SetPrintDensity(GEDI_PRNTR_e_PrintDensity.HIGH);
                mPRNTR.DrawStringExt(strconfig, texto);
                mPRNTR.Output();
                mostrarMensagem("Comprovante impresso");
            } catch (GediException e) {

                mostrarMensagem("Erro ao imprimir comprovante: " + e);
            }
        }).start();
    }

    private void mostrarMensagem(String texto) {
        cordovaInt.getActivity().runOnUiThread(
                new Runnable() {
            public void run() {
                Toast.makeText(context, texto, Toast.LENGTH_LONG).show();
            }
        });
    }

}
