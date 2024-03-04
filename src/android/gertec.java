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
import java.util.concurrent.CountDownLatch;

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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

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
            String texto = args.getString(0);

            try {
                imprimirComprovante(texto);
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
                return false;
            }
            callbackContext.success("Ok...");
            return true;
        } else if ("inicializar".equals(action)) {

            String texto = args.toString();

            try {
                inicializar(texto);
            } catch (Exception e) {
                callbackContext.error(e.getMessage() + " inicializarPinPad:" + retornoPinPad);
                return false;
            }
            callbackContext.success("Ok....");
            return true;
        } else if ("aproximar".equals(action)) {

            String texto = args.toString();

            try {
                aproximar(texto);
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

    private void inicializar(String texto) throws PPCompException, Exception {

        LocalTime horaAtual = LocalTime.now();
         LocalDate dataAtual = LocalDate.now();
         
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        DateTimeFormatter formatterYear = DateTimeFormatter.ofPattern("yyMMdd");
        
        String dataAtualFormatada = dataAtual.format(formatterYear);
        String horaAtualFormatada = horaAtual.format(formatter);

        retornoPinPad = "iniciou1";
        PPComp ppComp;
        ppComp = new PPComp(context);
        retornoPinPad += "iniciou2";
       // ppComp.PP_InitLib();
        retornoPinPad += "iniciou3";
        ppComp.PP_Open();
        retornoPinPad += "iniciou4";
        String gcr_input = "000100000000001" + dataAtualFormatada + horaAtualFormatada + "7012345678900";

        String output = "";
        StringBuffer msgNotify = new StringBuffer();

        try {
            retornoPinPad += "iniciou5";
            ppComp.PP_StartGetCard(gcr_input);
            while (true) {
                try {
                    mostrarMensagem("Insira o cartão...");
                    retornoPinPad += "iniciou6";
                    output = ppComp.PP_GetCard();
                     retornoPinPad += "Resultado:" + output;
                    mostrarMensagem("Resultado = " + output);
                    break;
                } catch (PPCompProcessingException e) {
                } catch (PPCompNotifyException e) {
                } catch (PPCompTabExpException e) {
                    ppComp.PP_ResumeGetCard();
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage() + " while");
        }

    }

    private void aproximar(String texto) throws PPCompException, Exception {
        PPComp ppComp;
        ppComp = new PPComp(context);
      //  ppComp.PP_InitLib();
        ppComp.PP_Open();
        String goc_input = "000000001000000000000000001101000000000000000000000000000000001000003E820000003E880000";
        String goc_inputTags = "0019B";
        String goc_inputTagsOpt = "0119F0B1F813A9F6B9F6C9F66";
        String output = "";
        try {
            ppComp.PP_StartGoOnChip(goc_input, goc_inputTags, goc_inputTagsOpt);
            while (true) {
                try {
                    output = ppComp.PP_GoOnChip();
                    mostrarMensagem("Resultado = " + output);
                    //  imprimirComprovante();
                    break;
                } catch (PPCompProcessingException e) {
                } catch (PPCompNotifyException e) {
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        cordovaInt.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, texto, Toast.LENGTH_LONG).show();
            }
        });
    }

}
