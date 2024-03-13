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

    IGEDI mGedi;
    Button btnGCR;
    Button btnGOC;
    PPComp ppComp;
    String retornoPinPad = "";

    String quantidadeRegistros = "01"; //Quantidade de registros que será enviado no PP_TableLoadRec

    //Lista de AIDs à serem carregados
    String[] arrayAIDs = {"3141016107A000000003101000000000000000000001CREDITO         030084008300000769862010172648530001539918000918E0F0887000F0F00123D84000A8000010000000D84004F8000000C350R120000271000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3DC4000A8000010000000DC4004F800",
        "3141010507A000000003101000000000000000000001CREDITO         030084008300000769862010172648530001539918000918E0F0D87000F0F00122D84000A8000010000000D84004F8000000C350R120000753000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3DC4000A8000010000000DC4004F800",
        "3141010607A000000003201000000000000000000002ELECTRON        030084008300000769862010172648530001539918000918E0E0C07000F0F00122D84000A8000810000000D84004F80000000000R120000271000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3DC4000A8000010000000DC4004F800",
        "3141010707A000000003301000000000000000000002ELECTRON2       030084008300000769862010172648530001539918000918E0E0C07000F0F00122D84000A8000810000000D84004F80000000000R120000271000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3DC4000A8000010000000DC4004F800",
        "3141010807A000000004101000000000000000000001MASTERCARD CREDI030002000200000769862010172648530001539918000918E0F0E8F000F0A00122FC50ACA0000000000000FC50ACF80000000000R140000271000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3FC508C88000000000000FC508C8800",
        "3141010907A000000004306000000000000000000002MAESTRO         030002000000000769862010172648530001539918000918E0F0E8F000F0F00122FC50BCA0000000000000FC50BCF80000000000R140000271000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3FC500C88000000800000FC500C8800"};

    //Lista de CAPKs à serem carregadas
    String[] arrayCAPKs = {"61120401A0000000250101103FFFF096AFAD7010F884E2824650F764D47D7951A16EED6DBB881F384DEDB6702E0FB55C0FBEF945A2017705E5286FA249A591E194BDCD74B21720B44CE986F144237A25F95789F38B47EA957F9ADB2372F6D5D41340A147EAC2AF324E8358AE1120EF3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000",
        "61120102A0000000250201103FFFF112AF4B8D230FDFCB1538E975795A1DB40C396A5359FAA31AE095CB522A5C82E7FFFB252860EC2833EC3D4A665F133DD934EE1148D81E2B7E03F92995DDF7EB7C90A75AB98E69C92EC91A533B21E1C4918B43AFED5780DE13A32BBD37EBC384FA3DD1A453E327C56024DACAEA74AA052C4DFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000",
        "61120103A0000000250301103FFFF128B0C2C6E2A6386933CD17C239496BF48C57E389164F2A96BFF133439AE8A77B20498BD4DC6959AB0C2D05D0723AF3668901937B674E5A2FA92DDD5E78EA9D75D79620173CC269B35F463B3D4AAFF2794F92E6C7A3FB95325D8AB95960C3066BE548087BCB6CE12688144A8B4A66228AE4659C634C99E36011584C095082A3A3E3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000",
        "61120104A0000000250401103FFFF096D0F543F03F2517133EF2BA4A1104486758630DCFE3A883C77B4E4844E39A9BD6360D23E6644E1E071F196DDF2E4A68B4A3D93D14268D7240F6A14F0D714C17827D279D192E88931AF7300727AE9DA80A3F0E366AEBA61778171737989E1EE309FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000",
        "61120105A0000000250E01103FFFF144AA94A8C6DAD24F9BA56A27C09B01020819568B81A026BE9FD0A3416CA9A71166ED5084ED91CED47DD457DB7E6CBCD53E560BC5DF48ABC380993B6D549F5196CFA77DFB20A0296188E969A2772E8C4141665F8BB2516BA2C7B5FC91F8DA04E8D512EB0F6411516FB86FC021CE7E969DA94D33937909A53A57F907C40C22009DA7532CB3BE509AE173B39AD6A01BA5BB85FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000",
        "61120106A0000000256201103FFFF096BA29DE83090D8D5F4DFFCEB98918995A768F41D0183E1ACA3EF8D5ED9062853E4080E0D289A5CEDD4DD96B1FEA2C53428436CE15A2A1BFE69D46197D3F5A79BCF8F4858BFFA04EDB07FC5BE8560D9CE38F5C3CA3C742EDFDBAE3B5E6DDA45557FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000"};

    String idRede = "00"; //Índice da rede adquirente
    //               01
    String timeStamp = "0123456789"; //Timestamp

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {

        super.initialize(cordova, webView);
        this.context = cordova.getActivity().getApplicationContext();
        this.cordovaInt = cordova;

        new Thread(new Runnable() {
            @Override
            public void run() {
                GEDI.init(context);
                mGedi = GEDI.getInstance(context);
                IPRNTR mPRNTR = mGedi.getPRNTR();

                ppComp = new PPComp(context);
                try {              
                    ppComp.PP_Open();
                    OutputCallbacks outputCallbacks = new OutputCallbacks(context, ppComp);
                    ppComp.PP_SetDspCallbacks(outputCallbacks);
                } catch (PPCompException e) {
                    
            
                }
            }
        }).start();
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
                callbackContext.error(obterLog(e) + " inicializarPinPad:" + retornoPinPad);
                return false;
            }
            callbackContext.success("Ok....");
            return true;
        }

        callbackContext.error(action + " is not a supported action");
        return false;
    }

    private void inicializar(String texto, CallbackContext callbackContext) throws PPCompException, Exception {

        LocalTime horaAtual = LocalTime.now();
        LocalDate dataAtual = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        DateTimeFormatter formatterYear = DateTimeFormatter.ofPattern("yyMMdd");

        String dataAtualFormatada = dataAtual.format(formatterYear);
        String horaAtualFormatada = horaAtual.format(formatter);

        String valorTransacaoCentavos = "000000000001";

        //  ppComp = PPComp.getInstance(context);
        //String timeStamp = ppComp.PP_GetTimeStamp("00");
        String gcr_input = "0099" + valorTransacaoCentavos + dataAtualFormatada + horaAtualFormatada + "000000000000";

        String output = "";
        StringBuffer msgNotify = new StringBuffer();

        try {
            ppComp.PP_StartGetCard(gcr_input);
            while (true) {
                try {
                    mostrarMensagem("Insira o cartão...");
                    retornoPinPad += "iniciou6";
                    output = ppComp.PP_GetCard();
                    retornoPinPad += "Resultado:" + output;
                    // PP_GOONCHIP() 
                    mostrarMensagem("Resultado = " + output);
                    break;
                } catch (PPCompProcessingException e) {
                } catch (PPCompNotifyException e) {
                } catch (PPCompTabExpException e) {
                    carregarRegistros();
                    ppComp.PP_ResumeGetCard();
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage() + " while");
        }

    }

    private void PP_GoOnChip(String texto) throws PPCompException, Exception {
        String valorTransacaoCentavos = "000000000001";
        PPComp ppComp;
        GEDI.init(context);
        mGedi = GEDI.getInstance(context);
        IPRNTR mPRNTR = mGedi.getPRNTR();
        //  ppComp = new PPComp(context);
        ppComp = PPComp.getInstance(context);
        ppComp.PP_Open();
        String goc_input = valorTransacaoCentavos + "000000000000001101000000000000000000000000000000001000003E820000003E880000";
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

    public void carregarRegistros() throws Exception {
        String input = idRede + timeStamp;
        mostrarMensagem("Atualizando tabelas...");
        try {
            //Chama o PP_TableLoadInit passando o índice da rede adquirente + o timestamp
            ppComp.PP_TableLoadInit(input);
            mostrarMensagem("Tabela já carregada para esse timestamp");
        } catch (PPCompTabExpException e) {
            //Caso ocorra a exception de Tabela expirada, chama o método para carregar os registros
            try {
                //Carrega a lista de AIDs
                for (int i = 0; i < arrayAIDs.length; i++) {
                    //Passa a quantidade de registros + o registro
                    ppComp.PP_TableLoadRec(quantidadeRegistros + arrayAIDs[i]);
                }
                //Carrega a lista de CAPKs
                for (int i = 0; i < arrayCAPKs.length; i++) {
                    //Passa a quantidade de registros + o registro
                    ppComp.PP_TableLoadRec(quantidadeRegistros + arrayCAPKs[i]);
                }
                //Chama o PP_TableLoadEnd para finalizar o processo
                ppComp.PP_TableLoadEnd();
                mostrarMensagem("Tabela carregada com sucesso");
            } catch (Exception f) {
                mostrarMensagem("Erro ao carregar tabela");
                throw new Exception(f.getMessage());

            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void imprimirComprovante(String texto) throws PPCompException {

       // GEDI.init(context);
       // mGedi = GEDI.getInstance(context);

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
