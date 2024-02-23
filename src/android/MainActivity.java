package br.com.gertec.exemplogcr_goc;

import androidx.appcompat.app.AppCompatActivity;

import br.com.gertec.gedi.GEDI;
import br.com.gertec.gedi.enums.GEDI_PRNTR_e_PrintDensity;
import br.com.gertec.gedi.exceptions.GediException;
import br.com.gertec.gedi.interfaces.IGEDI;
import br.com.gertec.gedi.interfaces.IPRNTR;
import br.com.gertec.gedi.structs.GEDI_PRNTR_st_StringConfig;
import br.com.gertec.ppcomp.PPComp;
import br.com.gertec.ppcomp.PPCompException;
import br.com.gertec.ppcomp.exceptions.PPCompCancelException;
import br.com.gertec.ppcomp.exceptions.PPCompNotifyException;
import br.com.gertec.ppcomp.exceptions.PPCompProcessingException;
import br.com.gertec.ppcomp.exceptions.PPCompTabExpException;


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

public class MainActivity extends AppCompatActivity {
    IGEDI mGedi;
    Button btnGCR;
    Button btnGOC;
    String TAG = "ExemploGCR_GOC";
    PPComp ppComp;
    String input;
    String idRede = "01"; //Índice da rede adquirente
    String timeStamp = "0123456789"; //Timestamp
    String quantidadeRegistros = "01"; //Quantidade de registros que será enviado no PP_TableLoadRec

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnGCR = (Button) findViewById(R.id.buttonGCR);
        btnGOC = (Button) findViewById(R.id.buttonGOC);

        
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Pega a instância da PPComp
                GEDI.init(MainActivity.this);
                mGedi = GEDI.getInstance(MainActivity.this);
                IPRNTR mPRNTR = mGedi.getPRNTR();

                ppComp = new PPComp(MainActivity.this);
                try {
                    ppComp.PP_Open();
                    OutputCallbacks outputCallbacks = new OutputCallbacks(MainActivity.this, ppComp);
                    ppComp.PP_SetDspCallbacks(outputCallbacks);
                } catch (PPCompException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        btnGCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                    mostraMensagem("Insira o cartão...");
                                    output = ppComp.PP_GetCard();
                                    mostraMensagem("Resultado = " + output);
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
        });


        //aproximar
        btnGOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                    mostraMensagem("Resultado = " + output);
                                    PinKBDActivity.getKBDData().activity.finish();
                                  //  imprimirComprovante();
                                    break;
                                } catch (PPCompProcessingException e) {
                                } catch (PPCompNotifyException e) {
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mostraMensagem("Erro no GOC");
                            PinKBDActivity.getKBDData().activity.finish();
                        }
                    }
                }).start();
            }
        });
    }

    public void mostraMensagem(String texto) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, texto, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void imprimirComprovante(){
 String comprovante = "teste";
        new Thread(new Runnable() {
            @Override
            public void run() {
                IPRNTR mPRNTR = mGedi.getPRNTR();
                GEDI_PRNTR_st_StringConfig strconfig = new GEDI_PRNTR_st_StringConfig(new Paint());

      
                    try {
                        mPRNTR.Init();
                        strconfig.paint.setTypeface(Typeface.MONOSPACE);
                        strconfig.paint.setTextSize(16);
                        mPRNTR.SetPrintDensity(GEDI_PRNTR_e_PrintDensity.HIGH);
                        mPRNTR.DrawStringExt(strconfig, comprovante);
                        mPRNTR.Output();
                        mostraMensagem("Comprovante impresso");
                    } catch (GediException e) {
                        e.printStackTrace();
                        mostraMensagem("Erro ao imprimir comprovante: " +e);
                    }
                 

            }
        }).start();
    }

    public String getProductName(){
        String productName = null;
        try {
            productName = mGedi.getINFO().ProductNameGet();
        } catch (GediException e) {
            e.printStackTrace();
        }
        return productName;
    }
}
