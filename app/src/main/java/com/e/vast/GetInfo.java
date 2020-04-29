package com.e.vast;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//This class gets the information about a user from the database
public class GetInfo extends StringRequest {
    private static final String GETINFO_URL = "https://vastproject.000webhostapp.com/GetInfo.php"; //php file link uploaded on web host
    private Map<String, String> params;

    public GetInfo(String Email, Response.Listener<String> listener){
        super(Method.POST, GETINFO_URL, listener, null);
        params = new HashMap<>();
        params.put("Email", Email); //passes Email to php file
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

