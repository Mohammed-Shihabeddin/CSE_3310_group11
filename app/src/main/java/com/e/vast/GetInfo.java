package com.e.vast;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetInfo extends StringRequest {
    private static final String GETINFO_URL = "https://vastproject.000webhostapp.com/GetInfo.php";
    private Map<String, String> params;

    public GetInfo(String Email, Response.Listener<String> listener){
        super(Method.POST, GETINFO_URL, listener, null);
        params = new HashMap<>();
        params.put("Email", Email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

