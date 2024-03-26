package gertec;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.gertec.gandi.Gandi;
import br.com.gertec.gandi.IGandi;
import br.com.gertec.gedi.GEDI;
import br.com.gertec.gedi.interfaces.IGEDI;
import br.com.gertec.ppcomp.PPComp;
import br.com.gertec.ppcomp.PPCompException;
import br.com.gertec.ppcomp.exceptions.PPCompNotifyException;
import br.com.gertec.ppcomp.exceptions.PPCompProcessingException;
import br.com.gertec.ppcomp.exceptions.PPCompTabExpException;

public class MainActivity extends AppCompatActivity {


    Button start;
    ImageView stGedi;
    ImageView stGand;
    ImageView stPPComp;
    ImageView stGrc;
    ImageView stGoc;
    ImageView stTransact;

    static ImageView ledAzul;
    static PPComp ppComp;
    static String input;
    static String quantidadeRegistros = "01";
    static String idRede = "01"; //Índice da rede adquirente
    static String timeStamp = "0123456789"; //Timestamp
    static String[]arrayAIDs = {"3141016107A000000003101000000000000000000001CREDITO         030084008300000769862010172648530001539918000918E0F0887000F0F00123D84000A8000010000000D84004F8000000C350R120000271000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3DC4000A8000010000000DC4004F800",
            "3141010507A000000003101000000000000000000001CREDITO         030084008300000769862010172648530001539918000918E0F0D87000F0F00122D84000A8000010000000D84004F8000000C350R120000753000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3DC4000A8000010000000DC4004F800",
            "3141010607A000000003201000000000000000000002ELECTRON        030084008300000769862010172648530001539918000918E0E0C07000F0F00122D84000A8000810000000D84004F80000000000R120000271000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3DC4000A8000010000000DC4004F800",
            "3141010707A000000003301000000000000000000002ELECTRON2       030084008300000769862010172648530001539918000918E0E0C07000F0F00122D84000A8000810000000D84004F80000000000R120000271000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3DC4000A8000010000000DC4004F800",
            "3141010807A000000004101000000000000000000001MASTERCARD CREDI030002000200000769862010172648530001539918000918E0F0E8F000F0A00122FC50ACA0000000000000FC50ACF80000000000R140000271000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3FC508C88000000000000FC508C8800",
            "3141010907A000000004306000000000000000000002MAESTRO         030002000000000769862010172648530001539918000918E0F0E8F000F0F00122FC50BCA0000000000000FC50BCF80000000000R140000271000001388000013880001100000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z3FC500C88000000800000FC500C8800"};

    static String[] arrayCAPKs = {"61120401A0000000250101103FFFF096AFAD7010F884E2824650F764D47D7951A16EED6DBB881F384DEDB6702E0FB55C0FBEF945A2017705E5286FA249A591E194BDCD74B21720B44CE986F144237A25F95789F38B47EA957F9ADB2372F6D5D41340A147EAC2AF324E8358AE1120EF3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            "61120102A0000000250201103FFFF112AF4B8D230FDFCB1538E975795A1DB40C396A5359FAA31AE095CB522A5C82E7FFFB252860EC2833EC3D4A665F133DD934EE1148D81E2B7E03F92995DDF7EB7C90A75AB98E69C92EC91A533B21E1C4918B43AFED5780DE13A32BBD37EBC384FA3DD1A453E327C56024DACAEA74AA052C4DFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            "61120103A0000000250301103FFFF128B0C2C6E2A6386933CD17C239496BF48C57E389164F2A96BFF133439AE8A77B20498BD4DC6959AB0C2D05D0723AF3668901937B674E5A2FA92DDD5E78EA9D75D79620173CC269B35F463B3D4AAFF2794F92E6C7A3FB95325D8AB95960C3066BE548087BCB6CE12688144A8B4A66228AE4659C634C99E36011584C095082A3A3E3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            "61120104A0000000250401103FFFF096D0F543F03F2517133EF2BA4A1104486758630DCFE3A883C77B4E4844E39A9BD6360D23E6644E1E071F196DDF2E4A68B4A3D93D14268D7240F6A14F0D714C17827D279D192E88931AF7300727AE9DA80A3F0E366AEBA61778171737989E1EE309FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            "61120105A0000000250E01103FFFF144AA94A8C6DAD24F9BA56A27C09B01020819568B81A026BE9FD0A3416CA9A71166ED5084ED91CED47DD457DB7E6CBCD53E560BC5DF48ABC380993B6D549F5196CFA77DFB20A0296188E969A2772E8C4141665F8BB2516BA2C7B5FC91F8DA04E8D512EB0F6411516FB86FC021CE7E969DA94D33937909A53A57F907C40C22009DA7532CB3BE509AE173B39AD6A01BA5BB85FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            "61120106A0000000256201103FFFF096BA29DE83090D8D5F4DFFCEB98918995A768F41D0183E1ACA3EF8D5ED9062853E4080E0D289A5CEDD4DD96B1FEA2C53428436CE15A2A1BFE69D46197D3F5A79BCF8F4858BFFA04EDB07FC5BE8560D9CE38F5C3CA3C742EDFDBAE3B5E6DDA45557FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000000000000000000000000000000000000000000000000000000000000000000000000000000"};


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //chamada automatica do metodo de inicialização
        initLibs(getApplicationContext());

        start.setOnClickListener(v -> {
           //chamada do GRC
            grc();
        });
    }
    
    //Inicializa todas as Lib's
    public void initLibs(Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    GEDI.init(context);
                    IGEDI iGedi = GEDI.getInstance(context);
            
                    IGandi mGandi = Gandi.getInstance(context);
             
                    ppComp = PPComp.getInstance(MainActivity.this);
                    try {
                        ppComp.PP_Open();
                        OutputCallbacks outputCallbacks = new OutputCallbacks(MainActivity.this, ledAzul, ppComp);
                        ppComp.PP_SetDspCallbacks(outputCallbacks);
                    } catch (PPCompException e) {
                        e.printStackTrace();
                    }
            
                }
            }
        }).start();
    }


    //iniciando Grc
    public void grc() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Parametros de entrada do GetCard
                String gcr_imput = "0099100000001000240226164311012345678900100";
                String output = "";
                StringBuffer msgNotify = new StringBuffer();

                try {
                    ppComp.PP_StartGetCard(gcr_imput);
                    while (true) {
                        try {
                            showNote("Apresente o Cartão");
                            output = ppComp.PP_GetCard();
                            showNote("Resultado: " +output);
                            break;
                        }catch (PPCompProcessingException e){
                        }catch (PPCompNotifyException e){
                        }catch (PPCompTabExpException e){
                            carregaRegistros();
                            ppComp.PP_ResumeGetCard();
                        }
                    }
              
                    goc();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

    //iniciando Goc
    public void goc() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Parametros de entrada do GoOnChip
                String goc_input =
                        "000000001000" +
                        "000000000000" +
                        "0" +
                        "0" +
                        "0" +
                        "301" +
                        "000000000000000000000000000000001000003E820000003E880000";

                String goc_inputTags = "0019B";
                String goc_inputTagsOpt = "0119F0B1F813A9F6B9F6C9F66";
                String output = "";
                try {
                    ppComp.PP_StartGoOnChip(goc_input, goc_inputTags, goc_inputTagsOpt);
                    while (true) {
                        try {
                            output = ppComp.PP_GoOnChip();
                            showNote("Resultado = " + output);
                            break;
                        } catch (PPCompException e) {
                            //e.printStackTrace();
                            if ( PinKBDActivity.getKBDData() != null) {
                                PinKBDActivity.getKBDData().activity.finish();
                            }
                            break;
                        }
                    }
                    //chamada do teclad
                    if ( PinKBDActivity.getKBDData() != null) {
                        PinKBDActivity.getKBDData().activity.finish();
                    }
          
                } catch (Exception e) {
                    e.printStackTrace();
                    showNote("Erro no GOC");
                    //fechamento altomatico do teclado
                    if ( PinKBDActivity.getKBDData() != null) {
                        PinKBDActivity.getKBDData().activity.finish();
                    }
                }
              

            }
        }).start();
    }

    //Carga de tabelas
    public void carregaRegistros(){
        input = idRede + timeStamp;
        showNote("Atualizando tabelas...");
        try {
    
            //Chama o PP_TableLoadInit passando o índice da rede adquirente + o timestamp
            ppComp.PP_TableLoadInit(input);
            showNote("Tabela já carregada para esse timestamp");
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
                showNote("Tabela carregada com sucesso");
            } catch (Exception f) {
                f.printStackTrace();
                showNote("Erro ao carregar tabela");
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }
    public void showNote(String t) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                @SuppressLint("ShowToast")
                Toast toast = Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT);
                toast.show();
  
            }
        });
    }

    @SuppressLint("ResourceType")
    public void setSt(ImageView t) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
         //       t.setImageResource(R.drawable.check);
            }
        });
    }
}
