package com.example.lowca.Perfil.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lowca.Perfil.Controller.PerfilController;
import com.example.lowca.Perfil.Model.PerfilModel;
import com.example.lowca.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Perfil extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private PerfilController perfilController;
    View vista;
    TextView tvNombre, tvCorreo, tvPesoO;
    EditText etGenero, etPesoA, etEstatura, etActividadF, etFechan;
    Button cerrarSesion, editarDatos, guardarDatos,btnExportarInfo;
    Spinner spinnerGen, spinnerAct;
    public FirebaseAuth mAuth;
    public  FirebaseFirestore db;
    String userID;
    private ProgressDialog progressDialog;
    Activity main;
    public static Perfil newInstance(String param1, String param2) {
        Perfil fragment = new Perfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Perfil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        main = getActivity();
        vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        tvNombre = (TextView) vista.findViewById(R.id.tvNombre);
        tvCorreo = (TextView) vista.findViewById(R.id.tvCorreo);
        etEstatura = (EditText) vista.findViewById(R.id.etEstaturaD);
        etFechan = (EditText) vista.findViewById(R.id.etFechaN);
        etGenero = (EditText) vista.findViewById(R.id.etGenero);
        etPesoA = (EditText) vista.findViewById(R.id.etPesoA);
        etActividadF = (EditText) vista.findViewById(R.id.etActf);
        tvPesoO = (TextView) vista.findViewById(R.id.tvPesoO);
        cerrarSesion = (Button) vista.findViewById(R.id.cerrar_sesion);
        editarDatos = (Button) vista.findViewById(R.id.editar);
        guardarDatos = (Button) vista.findViewById(R.id.guardar);
        spinnerGen = (Spinner) vista.findViewById(R.id.spinnerGenero);
        spinnerAct = (Spinner) vista.findViewById(R.id.spinnerActividad);
        guardarDatos.setVisibility(View.INVISIBLE);
        spinnerGen.setVisibility(View.INVISIBLE);
        spinnerAct.setVisibility(View.INVISIBLE);
        btnExportarInfo=vista.findViewById(R.id.btnExportarInfo);

        etGenero.setEnabled(false);
        etEstatura.setEnabled(false);
        etPesoA.setEnabled(false);
        etFechan.setEnabled(false);
        etActividadF.setEnabled(false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Obteniendo Datos");
        progressDialog.setMessage("Cargando el perfil");
        progressDialog.show();

        perfilController = new PerfilController(this);
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        perfilController.loadPerfilData(userUID);

        return vista;
    }
    public void viewData(PerfilModel perfilModel){
        tvCorreo.setText(perfilModel.getCorre());
        tvNombre.setText(perfilModel.getName());
        etGenero.setText(perfilModel.getGender());
        etEstatura.setText(perfilModel.getHeight());
        etPesoA.setText(perfilModel.getWeight());
        etFechan.setText(perfilModel.getBirthDate());
        etActividadF.setText(perfilModel.getPhysical_activity());
        tvPesoO.setText(perfilModel.getTargetWeight()+"kl");
        if(progressDialog.isShowing()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            },500);
        }
    }
}