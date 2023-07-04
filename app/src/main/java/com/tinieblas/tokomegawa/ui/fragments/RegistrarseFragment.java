package com.tinieblas.tokomegawa.ui.fragments;

import static com.google.common.collect.ComparisonChain.start;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinieblas.tokomegawa.data.FetchRequest;
import com.tinieblas.tokomegawa.data.remote.LoginRepositoryImp;
import com.tinieblas.tokomegawa.data.remote.SignUpRepositoryImp;
import com.tinieblas.tokomegawa.databinding.FragmentRegistrarseBinding;
import com.tinieblas.tokomegawa.domain.models.RegistroDataModelo;
import com.tinieblas.tokomegawa.ui.activities.MainActivity;
import com.tinieblas.tokomegawa.utils.Alertdialog;
import com.tinieblas.tokomegawa.utils.NavigationContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegistrarseFragment extends Fragment {

    private FragmentRegistrarseBinding binding;
    private RegistroDataModelo registroData;
    SignUpRepositoryImp repository;
    LoginRepositoryImp repositoryLogin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistrarseBinding.inflate(inflater, container, false);


        setupViews();
        setupListeners();
        validateFields();
        btnRegistro();

        return binding.getRoot();
    }

    private void btnRegistro(){
        binding.buttonRegistro.setOnClickListener(view -> {
            RegistroDataModelo registroData = getRegistroData();

            repository = new SignUpRepositoryImp();
            repositoryLogin = new LoginRepositoryImp();

            String result = repository.createUser(registroData.getCorreoElectronico(), registroData.getContrasena());

            boolean isSuccess = result != null && !result.isEmpty();

            //String uid = repositoryLogin.getUIDUser();
            //registroData.setIUD(uid);


            if (isSuccess) {
                //Alertdialog alertDialog = new Alertdialog();
                //alertDialog.alertSuccess(getContext(), "Cuenta creada exitosamente");
                guardarRegistro(registroData);
                repositoryLogin.login(registroData.getCorreoElectronico(), registroData.getContrasena());
                NavigationContent.cambiarActividad(getActivity(), MainActivity.class);

                //Boolean isLogged = repositoryLogin.login(registroData.getCorreoElectronico(), registroData.getContrasena());

                //
            }

        });
    }
    /*private void guardarRegistro(RegistroDataModelo registroData) {


        boolean isSuccess = result != null && !result.isEmpty();
            repository = new SignUpRepositoryImp();
            repositoryLogin = new LoginRepositoryImp();


        Log.e("Resultado ==> ", result);
    }*/

    private void guardarRegistro(final RegistroDataModelo registroData) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> asyncTask =
                new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    FetchRequest fetchRequest = new FetchRequest();
                    String url = "http://tokomegawa.somee.com/Registrarse";
                    return fetchRequest.fetchRegistro(url, registroData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String response) {
                if (response != null) {
                    // Manejar la respuesta aquí
                    Log.e("response ==> ", response);
                } else {
                    // Manejar el caso de error
                }
            }
        };

        asyncTask.execute();
    }


    private void SubirToDB() throws IOException {
        FetchRequest fetchRequest = new FetchRequest();
        String url = "http://tokomegawa.somee.com/Registrarse";

        // Crear una instancia de RegistroDataModelo con los datos del registro
        //RegistroDataModelo registroData = new RegistroDataModelo();

        /*registroData.setApellidos("Apellidos de ejemplo");
        registroData.setTelefono("123456789");
        registroData.setTipoDocumento("DNI");
        registroData.setNumDocumento("12345678");
        registroData.setContrasena("contrasena123");
        registroData.setIUD("IUD de ejemplo");
        registroData.setCorreoElectronico("ejfewfemplo111@example.com");
        registroData.setDepartamento("Lima");
        registroData.setProvincia("Lima");
        registroData.setDistrito("SMPorres");*/

        // Realizar la solicitud de registro y obtener la respuesta
        String response = fetchRequest.fetchRegistro(url, registroData);
        Log.e("response ==> ", response);
    }


    private RegistroDataModelo getRegistroData() {
        registroData = new RegistroDataModelo();
        registroData.setNombre(binding.editTextNombre.getText().toString().trim());
        registroData.setApellidos(binding.editTextApelllidos.getText().toString().trim());
        registroData.setCorreoElectronico(binding.editTextCorreoElectronico.getText().toString().trim());
        registroData.setTelefono(binding.editTextPhone.getText().toString().trim());
        registroData.setTipoDocumento(binding.spinnerTipoDocumento.getSelectedItem().toString());
        registroData.setNumDocumento(binding.editTextNumDeDocumento.getText().toString().trim());
        registroData.setContrasena(binding.editTextContrasena.getText().toString().trim());
        registroData.setSwitchValue(binding.switch1.isChecked());
        return registroData;
    }

    private void setupViews() {
        List<String> opciones = new ArrayList<>();
        opciones.add("DNI");
        opciones.add("Carnet de Extranjeria");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_dropdown_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTipoDocumento.setAdapter(adapter);
    }

    private void setupListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateFields();
            }
        };

        binding.editTextNombre.addTextChangedListener(textWatcher);
        binding.editTextApelllidos.addTextChangedListener(textWatcher);
        binding.editTextCorreoElectronico.addTextChangedListener(textWatcher);
        binding.editTextPhone.addTextChangedListener(textWatcher);
        binding.editTextNumDeDocumento.addTextChangedListener(textWatcher);
        binding.editTextContrasena.addTextChangedListener(textWatcher);

        binding.switch1.setOnCheckedChangeListener((buttonView, isChecked) -> validateFields());

        binding.buttonRegistro.setOnClickListener(view ->
                Toast.makeText(getContext(), "Registrado", Toast.LENGTH_SHORT).show());
    }

    private void validateFields() {
        boolean isNameValid = !TextUtils.isEmpty(binding.editTextNombre.getText().toString().trim());
        boolean isApellidoValid = !TextUtils.isEmpty(binding.editTextApelllidos.getText().toString().trim());
        boolean isEmailValid = !TextUtils.isEmpty(binding.editTextCorreoElectronico.getText().toString().trim());
        boolean isPhoneValid = !TextUtils.isEmpty(binding.editTextPhone.getText().toString().trim());
        boolean isNumDocumentoValid = !TextUtils.isEmpty(binding.editTextNumDeDocumento.getText().toString().trim());
        boolean isPasswordValid = !TextUtils.isEmpty(binding.editTextContrasena.getText().toString().trim());
        boolean isSwitchValid = binding.switch1.isChecked();

        boolean isButtonEnabled = isSwitchValid && isNumDocumentoValid && isPhoneValid && isApellidoValid &&
                isNameValid && isEmailValid && isPasswordValid;

        binding.buttonRegistro.setEnabled(isButtonEnabled);
        binding.buttonRegistro.setAlpha(isButtonEnabled ? 1.0f : 0.5f);
    }




}























