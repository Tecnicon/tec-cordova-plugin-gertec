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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import br.com.gertec.ppcomp.IPPCompDSPCallbacks;

/**
 * This class echoes a string called from JavaScript.
 */
public class gertec extends CordovaPlugin {

    private Context context;
    private CordovaInterface cordovaInt;
    private String retornoInit;

    IGEDI mGedi;
    PPComp ppComp;
    String retornoPinPad = "";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {

        super.initialize(cordova, webView);
        this.context = cordova.getActivity().getApplicationContext();
        this.cordovaInt = cordova;

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                GEDI.init(context);
                mGedi = GEDI.getInstance(context);
                IPRNTR mPRNTR = mGedi.getPRNTR();

                ppComp = PPComp.getInstance(context);
                try {              
                    ppComp.PP_Open();
                    OutputCallbacks outputCallbacks = new OutputCallbacks(context, ppComp);
                    ppComp.PP_SetDspCallbacks(outputCallbacks);
                } catch (Exception e) {
                   retornoInit = obterLog(e);
                }
            }
        }).start();*/
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
                inicializar(texto, callbackContext);
            } catch (Exception e) {
                callbackContext.error("inicializacao ppcomp" + retornoInit + "fim" + obterLog(e));
                return false;
            }
            callbackContext.success("Ok....");
            return true;
        }

        callbackContext.error(action + " is not a supported action");
        return false;
    }

    private void inicializar(String texto, CallbackContext callbackContext) throws PPCompException, Exception {

     /*   LocalTime horaAtual = LocalTime.now();
        LocalDate dataAtual = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        DateTimeFormatter formatterYear = DateTimeFormatter.ofPattern("yyMMdd");

        String dataAtualFormatada = dataAtual.format(formatterYear);
        String horaAtualFormatada = horaAtual.format(formatter);

        String valorTransacaoCentavos = "000000000001";

 
        //String timeStamp = ppComp.PP_GetTimeStamp("00");
        String gcr_input = "0099" + valorTransacaoCentavos + dataAtualFormatada + horaAtualFormatada + "000000000000";*/
     
     /*  cordovaInt.getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(cordova.getActivity(), MainActivity.class);
            cordovaInt.getActivity().startActivity(intent);
        }
    });*/


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

    private String obterLog(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);

        StringBuilder strRet = new StringBuilder();
        strRet.append("Mensagem: ").append(ex.getMessage()).append("\n");
        strRet.append("Linha: ").append(ex.getStackTrace()[0].getLineNumber()).append("\n");
        strRet.append("StackTrace: ").append(sw.toString());
        return strRet.toString();
    }

}
