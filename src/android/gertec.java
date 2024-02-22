package gertec;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Paint;
import android.graphics.Typeface;
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
            callbackContext.success();
            return true;
        } else if ("inserir".equals(action)) {

            String texto = args.toString();

            try {
                inserir();
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
                return false;
            }
            callbackContext.success();
            return true;
        } else if ("aproximar".equals(action)) {

            String texto = args.toString();

            try {
                aproximar();
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
                return false;
            }
            callbackContext.success();
            return true;
        }

        callbackContext.error(action + " is not a supported action");
        return false;
    }

    private void imprimirComprovante(String texto) {
        IGEDI mGedi;
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

    private void inserir() {

        PPComp ppComp = new PPComp(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Parametros de entrada do GetCard
                String gcr_input = "0001000000001000691231210457012345678900";
                String output = "";
                StringBuffer msgNotify = new StringBuffer();

                try {
                    ppComp.PP_StartGetCard(gcr_input);
                    while (true) {
                        try {
                            mostrarMensagem("Insira o cartão...");
                            output = ppComp.PP_GetCard();
                            mostrarMensagem("Resultado = " + output);
                            break;
                        } catch (PPCompProcessingException e) {
                        } catch (PPCompNotifyException e) {
                        } catch (PPCompTabExpException e) {
                            ppComp.PP_ResumeGetCard();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void aproximar() {
        PPComp ppComp = new PPComp(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Parametros de entrada do GoOnChip
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
                            cordovaInt.getActivity().finish();
                         
                            //  PinKBDActivity.getKBDData().activity.finish();
                            //     imprimirComprovante(output);
                            break;
                        } catch (PPCompProcessingException e) {
                        } catch (PPCompNotifyException e) {
                        }
                    }
                } catch (Exception e) {

                    mostrarMensagem("Erro no GOC" + e);
                    cordovaInt.getActivity().finish();
                 
                    //  PinKBDActivity.getKBDData().activity.finish();
                }
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
