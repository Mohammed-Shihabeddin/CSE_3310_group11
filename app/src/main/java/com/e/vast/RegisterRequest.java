package com.e.vast;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//This class registers a user
public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://vastproject.000webhostapp.com/Register.php"; //php file link for registering uploaded on web host
    private Map<String, String> params;

    public RegisterRequest(String FName, String MName, String LName, String Email, String Password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        //Pass parameters to php file
        params = new HashMap<>();
        params.put("FName", FName);
        params.put("MName", MName);
        params.put("LName", LName);
        params.put("Email", Email);
        params.put("Password", Password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
