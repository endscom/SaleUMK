package com.guma.desarrollo.salesumk.Fragments;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.guma.desarrollo.salesumk.Activity.BandejaPedido;
import com.guma.desarrollo.salesumk.Activity.ObservacionActivity;
import com.guma.desarrollo.salesumk.Activity.bandejaCobroActivity;
import com.guma.desarrollo.salesumk.Adapters.MyExpandableListAdapter;
import com.guma.desarrollo.salesumk.DataBase.DataBaseHelper;
import com.guma.desarrollo.salesumk.Activity.MainActivity;
import com.guma.desarrollo.salesumk.Lib.ClssURL;
import com.guma.desarrollo.salesumk.Lib.Funciones;
import com.guma.desarrollo.salesumk.Lib.Variables;
import com.guma.desarrollo.salesumk.R;
import com.guma.desarrollo.salesumk.Lib.ChildRow;
import com.guma.desarrollo.salesumk.Lib.ParentRow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.apache.http.Header;
import org.apache.http.util.LangUtils;

import java.util.ArrayList;
import java.util.Calendar;

// TODO: 08/08/2016 QUEDA PENDIENTE EL REFRESH DE LA LISTA DE AGENDADO
// TODO: FALTA LA VALIDACION DE LOS RANGO DE FECHA DE EL PLAN DE TRABAJO

public class AgendadosCls extends Fragment  implements SearchView.OnQueryTextListener,SearchView.OnCloseListener
{
    DataBaseHelper myDB;
    Variables vrb;
    Funciones vrf;
    ProgressDialog pdialog;
    private MyExpandableListAdapter listAdapter;
    ArrayList<ChildRow> childRows;
    private ExpandableListView myList;
    private ArrayList<ParentRow> parentList = new ArrayList<ParentRow>();
    private ArrayList<ParentRow> showTheseParentList = new ArrayList<ParentRow>();
    private View view;
    TextView WeekStar,WeekEnd,RUTA,NombreVendedor,ZONA,txtIdPlan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_cls_agendados, container, false);

        myDB = new DataBaseHelper(getActivity());
        ImageView imgOpciones = (ImageView) view.findViewById(R.id.tres);

        vrb.setIdPlan("Error");

        NombreVendedor = (TextView) view.findViewById(R.id.FrmEjecutivo);
        RUTA = (TextView) view.findViewById(R.id.txtRuta);
        ZONA = (TextView) view.findViewById(R.id.txtZona);
        WeekStar = (TextView) view.findViewById(R.id.txtWDEL);
        WeekEnd = (TextView) view.findViewById(R.id.txtWAL);
        txtIdPlan = (TextView) view.findViewById(R.id.idplan);
        NombreVendedor.setText(vrb.getNameVendedor());
        RUTA.setText(vrb.getIdVendedor());

        imgOpciones.setOnClickListener
        (new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final CharSequence[]items = { "GUARDAR", "SINCRONIZAR"};
                    builder.setItems
                    (items, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int item) {
                                if (items[item].equals(items[0])){
                                    SaveIdPlan();
                                }else{
                                    if (txtIdPlan.getText().toString().equals("Error")|| myList.getCount() == 5 ){
                                        Error("Crear un plan de Trabajo");
                                    }else {
                                        //Toast.makeText(getActivity(), "Mandarlo", Toast.LENGTH_SHORT).show();
                                        Push(BuilderSqlAgenda(),BuilderSqlVCliente());
                                    }
                                }
                            }
                        }
                    ).create().show();
                }
            }
        );
        parentList = new ArrayList<ParentRow>();
        showTheseParentList = new ArrayList<ParentRow>();
        displayList();
        expandAll();
        return view;
    }
    private String BuilderSqlAgenda()
    {
        String Strsql="";
        Cursor ResAgenda = myDB.PushAgenda(txtIdPlan.getText().toString());
        if (ResAgenda.getCount()!=0)
        {
            Strsql = "INSERT INTO Agenda (IdPlan, Vendedor, Ruta, Zona, Revisado) VALUES";
            if (ResAgenda.moveToFirst())
            {
                do
                {
                    Strsql +=
                            "("+
                                    "'"+ResAgenda.getString(0)+"',"+
                                    "'"+ResAgenda.getString(1)+"',"+
                                    "'"+ResAgenda.getString(2)+"',"+
                                    "'"+ResAgenda.getString(3)+"',"+
                                    "'"+ResAgenda.getString(4)+"'"
                                    +"),";
                } while(ResAgenda.moveToNext());
                Strsql = Strsql.substring(0,Strsql.length()-1);
            }
        }
        return Strsql;
    }
    private String BuilderSqlVCliente()
    {
        String Strsql="";
        Cursor ResDetalle = myDB.PushVCliente(txtIdPlan.getText().toString());
        if (ResDetalle.getCount()!=0){
            Strsql = "INSERT INTO VClientes (IdPlan, Lunes, Martes, Miercoles, Jueves,Viernes,Sabado,Observaciones) VALUES";
            if (ResDetalle.moveToFirst())
            {
                do
                {
                    Strsql +=
                            "("+
                                    "'"+ResDetalle.getString(0)+"',"+
                                    "'"+ResDetalle.getString(1)+"',"+
                                    "'"+ResDetalle.getString(2)+"',"+
                                    "'"+ResDetalle.getString(3)+"',"+
                                    "'"+ResDetalle.getString(4)+"',"+
                                    "'"+ResDetalle.getString(5)+"',"+
                                    "'"+ResDetalle.getString(6)+"',"+
                                    "'"+ResDetalle.getString(7)+"'"
                                    +"),";
                } while(ResDetalle.moveToNext());
                Strsql = Strsql.substring(0,Strsql.length()-1);
            }
        }
        return Strsql;
    }
    private void Push(String SqlPush,String SqlPushDetalle)
    {
        AsyncHttpClient Cnx = new AsyncHttpClient();
        RequestParams PushDataRecibo = new RequestParams();
        RequestParams PushDataRDetalle = new RequestParams();

        PushDataRecibo.put("D",SqlPush);
        PushDataRDetalle.put("D",SqlPushDetalle);

        pdialog = ProgressDialog.show(getActivity(), "","Procesando. Porfavor Espere...", true);
        Cnx.post
        (ClssURL.getURL_doom(), PushDataRecibo, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    if (statusCode==200){ }
                    else{ Error("Problemas de Conexion al Servidor de Recibos"); }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    Error("Problemas de Conexion al Servidor Recibos");
                }
            }
        );

        Cnx.post(ClssURL.getURL_doom(), PushDataRDetalle, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200){

                    //AQUI POMER LA ACTUALIZACION DE LA LISTA DE AGENDADOS
                    myDB.UpdateEstado(vrb.getIdPlan(),1);
                    pdialog.dismiss();
                    Error("La Informacion Fue Ingresada Al Servidor");
                }else{
                    Error("Problemas de Conexion al Servidor de detalle");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Error("Problemas de Conexion al Servidor de detalle");
            }
        });
    }
    private void expandAll()
    {
        int count = listAdapter.getGroupCount();
        for (int i=0;i<count;i++){ myList.expandGroup(i); }
    }
    private boolean FrmValida()
    {
        boolean OK = false;
        WeekStar.setError(null);
        WeekEnd.setError(null);
        RUTA.setError(null);
        ZONA.setError(null);

        if (TextUtils.isEmpty(NombreVendedor.getText())){ NombreVendedor.setError("Requerido"); }
        else
        {
            if (TextUtils.isEmpty(RUTA.getText())){ RUTA.setError("Requerido"); }
            else
            {
                if (TextUtils.isEmpty(ZONA.getText())){ ZONA.setError("Requerido"); }
                else
                {
                    if (TextUtils.isEmpty(WeekStar.getText())||TextUtils.isEmpty(WeekEnd.getText()))
                    {
                        WeekStar.setError("Requerido");
                        WeekEnd.setError("Requerido");
                    }
                    else
                    {
                        OK = true;
                    }
                }
            }
        }
        return OK;
    }
    private boolean Decodificar(String str){
        boolean ok = false;

        int annoDB  = Integer.parseInt(str.substring(3,5));
        int annoNow = Integer.parseInt(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2,4));
        int weekNow = 10;
        //int weekNow = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        int WstarDB = Integer.parseInt(str.substring(6,8));
        int WendsDB = Integer.parseInt(str.substring(8,10));
        if (annoDB == annoNow){
            if (weekNow >= WstarDB && weekNow <=WendsDB){
                ok = true;
            }
        }


        return ok;
    }
    private String OncreteIdPlan(){
        String IdPlan = "";

        if (FrmValida()){
            int Year = Calendar.getInstance().get(Calendar.YEAR);
            IdPlan = RUTA.getText().toString()+ String.valueOf(Year).substring(2,4) + "-" + vrf.prefixZero(WeekStar.getText().toString()) + vrf.prefixZero(WeekEnd.getText().toString());
        }
        return IdPlan;
    }
    private  Boolean SaveIdPlan(){
        boolean OK = false;


        String ID       =       OncreteIdPlan();
        String Vendedor =       NombreVendedor.getText().toString();
        String Ruta     =       RUTA.getText().toString();
        String Zona     =       ZONA.getText().toString();
        String Wstar    =       WeekStar.getText().toString();
        String Wend     =       WeekEnd.getText().toString();

        if (ID!=""){
            if (myDB.GetCountPlanTrabajo(ID)==0){
                if (myDB.insertPlanTRabajo(ID,Vendedor,Ruta,Zona,"Revisado")==true){
                    vrb.setIdPlan(ID);
                    txtIdPlan.setText(ID);
                    Error("Fue Creado Correctamente el Plan de Trabajo");
                }else{
                    Toast.makeText(getActivity(), "Ocurrio un problema al crear el Plan", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getActivity(), "ACTUALIZAR PLAN NUEVO", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "ERROR AL CREAR EL PLAN", Toast.LENGTH_SHORT).show();
        }

        return OK;

    };
    private void displayList(){
        loadData();
        myList = (ExpandableListView) view.findViewById(R.id.expandableListView_search);
        listAdapter = new MyExpandableListAdapter(getActivity(),parentList);

        myList.setAdapter(listAdapter);
    }

    private void loadData(){
        String Estado="";
        Cursor res =  myDB.GetPlanTrabajo(vrb.getIdVendedor());
        String[] empry = new String[res.getCount()];
        int i=0;
        if (res.getCount()!=0){
            if (res.moveToFirst()) {
                do {
                    if (Decodificar(res.getString(0))){
                        vrb.setIdPlan(res.getString(0));
                        txtIdPlan.setText(res.getString(0));
                        ZONA.setText(res.getString(3));
                        WeekStar.setText(res.getString(0).substring(6,8).replace("0",""));
                        WeekEnd.setText(res.getString(0).substring(8,10).replace("0",""));
                        if (res.getString(5).equals("0")){

                        }
                        switch (res.getString(5)){
                            case "0":
                                Estado = " - [Pendiente de Enviar]";
                            break;
                            case "1":
                                Estado = " - [Enviado]";
                                break;

                        }
                    }else{
                        Toast.makeText(getActivity(), "No pasar", Toast.LENGTH_SHORT).show();
                    }
                } while(res.moveToNext());
            }else{
                empry[i] = "";
            }
        }
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("PLAN DE TRABAJO " + Estado);
        String[] Lu = new String[0],Ma = new String[0],Mi  = new String[0],Ju  = new String[0],Vi  = new String[0];
        if (vrb.getIdPlan() != "Error"){
            Lu = myDB.GetDiaPlanTrabajo(txtIdPlan.getText().toString(),"Lunes");
            Ma = myDB.GetDiaPlanTrabajo(txtIdPlan.getText().toString(),"Martes");
            Mi = myDB.GetDiaPlanTrabajo(txtIdPlan.getText().toString(),"Miercoles");
            Ju = myDB.GetDiaPlanTrabajo(txtIdPlan.getText().toString(),"Jueves");
            Vi = myDB.GetDiaPlanTrabajo(txtIdPlan.getText().toString(),"Viernes");
        }
        CrearList("LUNES", Lu);
        CrearList("MARTES", Ma);
        CrearList("MIERCOLES", Mi);
        CrearList("JUEVES", Ju);
        CrearList("VIERNES", Vi);

    }
    private void CrearList(String Dia, String[] Cliente){
        childRows = new ArrayList<ChildRow>();

        ParentRow parentRow = null;
        childRows.clear();
        parentRow = new ParentRow(Dia,childRows);
        for (int i=0;i<Cliente.length;i++){
            String[] items = Cliente[i].toString().split("_");
            childRows.add(new ChildRow(R.drawable.ic_remove_white_24dp,items[1],items[0]));
        }
        parentList.add(parentRow);
    }



    public void Error(String TipoError){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setMessage(TipoError)
                .setNegativeButton("OK",null)
                .create()
                .show();
    }
    @Override
    public boolean onClose() {
        listAdapter.filterData("");
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listAdapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listAdapter.filterData(newText);
        expandAll();
        return false;
    }
}
