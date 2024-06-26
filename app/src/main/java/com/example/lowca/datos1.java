package com.example.lowca;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.InputFilter;
import android.text.Spanned;

import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class datos1 extends AppCompatActivity {
    TextInputLayout tilEstatura, tilPeso, tilFechaNa;
    TextInputEditText etPeso,etEstatura,etNacido;
    Button btnContinuar;
    String[] datos1;
    String[] datosDos;
    Bundle recibirDatos;
    private int maxLength;

    public double alturaMax,alturaMin,imc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos1);
        etPeso=(TextInputEditText) findViewById(R.id.etPeso);
        etEstatura=(TextInputEditText) findViewById(R.id.etEstatura);
        etNacido=(TextInputEditText) findViewById(R.id.etFechaNacido);
        tilEstatura = (TextInputLayout) findViewById(R.id.TILEstatura);
        tilPeso = (TextInputLayout) findViewById(R.id.TILPeso);
        tilFechaNa = (TextInputLayout) findViewById(R.id.TILFechaN);
        btnContinuar = (Button) findViewById(R.id.btnSiguiente);
        recibirDatos= getIntent().getExtras();
        //Establece que la alturo solo debe ser max de 3 cifras
        InputFilter lengthFilter = new InputFilter.LengthFilter(3);
        etEstatura.setFilters(new InputFilter[]{lengthFilter});

        alturaMax=230;
        alturaMin=100;

        // Crea un InputFilter para limitar los decimales
        InputFilter decimalFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String inputText = dest.toString();
                String newText = inputText.substring(0, dstart) + source.subSequence(start, end) + inputText.substring(dend);

                if (!newText.matches("^\\d{0,3}(\\.\\d{0,2})?$")) {
                    return "";
                }

                return null;
            }
        };



        etPeso.setFilters(new InputFilter[]{decimalFilter});

        btnContinuar.setOnClickListener(v -> {
            try {
                if(validar()){
                    datos1=recibirDatos.getStringArray("keyDatos");
                    String peso=etPeso.getText().toString();
                    String estatura=etEstatura.getText().toString();
                    String nacido=etNacido.getText().toString();
                    datosDos= new String[]{peso,estatura,nacido};
                    Bundle pasarDatos= new Bundle();
                    Bundle pasarDatos2= new Bundle();
                    pasarDatos.putStringArray("keyDatos",datos1);
                    pasarDatos2.putStringArray("keyDatos2",datosDos);

                    Intent intent = new Intent(this,datos2.class);
                    intent.putExtras(pasarDatos);
                    intent.putExtras(pasarDatos2);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"Verifique sus datos",Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
            }
        });
        etEstatura.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilEstatura.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPeso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPeso.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNacido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilFechaNa.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNacido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }
    public void atras(View view){
        this.finish();
    }
    public boolean validar(){
        boolean retorno = true;
        String peso=etPeso.getText().toString();
        String estatura=etEstatura.getText().toString();
        String fechan=etNacido.getText().toString();
        verificarIMC();

        if(imc<=14 || imc>=45){
            tilEstatura.setError("Ingresa dato coherente");
            tilPeso.setError("Ingresa dato coherente");

            retorno = false;

        }else{
            tilPeso.setErrorEnabled(false);
            tilEstatura.setErrorEnabled(false);
        }
        if(peso.isEmpty()){
            tilPeso.setError("Ingresa tu Peso Actual");
            retorno = false;
        }else {
            tilPeso.setErrorEnabled(false);
        }
        if (estatura.isEmpty() ) {
            tilEstatura.setError("Ingresa tu Estatura");
            retorno = false;
        }else{
            tilEstatura.setErrorEnabled(false);
        }
        if (fechan.isEmpty()) {
            tilFechaNa.setError("Ingresa tu Fecha de Nacimiento");
            retorno = false;
        }else{
            tilFechaNa.setErrorEnabled(false);
        }
        return retorno;
    }
    public void openDialog(){
        final Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        int dia = d;
        int year = y-15;
        int yearmin = y-50;
        int mes = m;
        DatePickerDialog fechaNacido = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                if(y<yearmin ){
                    String title = "Fecha invalida";
                    String message = "Ingrese una fecha valida";
                    dialogAlert(title,message);
                }else if(y>year || y>=year && d>dia && m>=mes){
                    String title = "Eres menor de edad";
                    String message = "Tienes que tener 15 años cumplidos o mas";
                    dialogAlert(title,message);
                }else{
                    etNacido.setText(String.valueOf(d)+"/"+String.valueOf(m+1)+"/"+String.valueOf(y));
                }
            }
        }, y-15, m, d);
        fechaNacido.show();
    }
    private void dialogAlert (String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openDialog();
                    }
                });
        AlertDialog dialog = builder.create();
        builder.show();
    }

    private void verificarIMC(){
        double peso= Double.parseDouble(etPeso.getText().toString());

        double estatura=Double.parseDouble(etEstatura.getText().toString());

        if(estatura>=alturaMin && estatura<=alturaMax){
            imc=peso/((estatura/100)*(estatura/100));
            Log.d("IMC: ",""+imc);
        }else{

        }


    }
}