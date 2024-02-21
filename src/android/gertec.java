package gertec;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.Toast;
import android.content.Context;

import br.com.gertec.gedi.enums.GEDI_PRNTR_e_PrintDensity;
import br.com.gertec.gedi.exceptions.GediException;
import br.com.gertec.gedi.interfaces.IGEDI;
import br.com.gertec.gedi.interfaces.IPRNTR;
import br.com.gertec.gedi.structs.GEDI_PRNTR_st_StringConfig;
import br.com.gertec.gedi.GEDI;
//import br.com.gertec.ppcomp.PPComp;

/**
 * This class echoes a string called from JavaScript.
 */
public class gertec extends CordovaPlugin {
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("imprimir".equals(action)) {
            
            String texto = args.toString();

            imprimirComprovante(texto);
            callbackContext.success();
            return true;
        }

        callbackContext.error(action + " is not a supported action");
        return false;
    }

    private void imprimirComprovante(String texto) {
       // PPComp ppComp;
           IGEDI mGedi;
           GEDI.init(gertec.this);
           mGedi = GEDI.getInstance(gertec.this);

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
            //    mostrarMensagem("Comprovante impresso");
            } catch (GediException e) {

             //   mostrarMensagem("Erro ao imprimir comprovante: " + e);
            }
        }).start();
    }
}
