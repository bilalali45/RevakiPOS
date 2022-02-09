package com.revaki.revakipos.connectivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revaki.revakipos.Configuration;
import com.revaki.revakipos.beans.User;
import com.revaki.revakipos.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ServiceManager {

    static String token = "";

    //static String serviceURL ="http://192.168.80.111:1281";
    static String serviceURL = "http://revaki.posapi.com.asp1-101.phx1-1.websitetestlink.com";

    static String routePrefix = "/api/RevakiPOSAPI/";


    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        ServiceManager.token = token;
    }

    private String ExecuteRequest(String request, String route) {
        String resp = "";
        HttpWeb webRequest = new HttpWeb();
        resp = webRequest.HttResponse(serviceURL + routePrefix + route, "POST", request, token);
        if (!validateResponse(resp)) {
            resp = webRequest.HttResponse(serviceURL + routePrefix + route, "POST", request, token);
        }
        return resp;
    }

    private String ExecuteLocalRequest(String request, String route) {
        String resp = "";
        HttpWeb webRequest = new HttpWeb();
        resp = webRequest.HttResponse("http://demo.movais.com/revakidemo/api/revaki/" + route, "POST", request, token);

        return resp;
    }

    private boolean validateResponse(String response) {
        boolean status = true;
        JSONObject objres = null;
        try {
            if (response != "") {
                objres = new JSONObject(response);
                if (objres.has("StatusCode") && objres.getInt("StatusCode") == 301) {
                    User user = Configuration.getUser();
                    Login(user.getUsername(), user.getPassword());
                }
            }
        } catch (JSONException e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }

    public JSONObject Login(String Username, String Password) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();

            objreq.put("Email", Username);
            objreq.put("Password", Password);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "login");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject VerifyPin(String PosKey) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();

            objreq.put("PosKey", PosKey);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "verifypin");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject UserList(String PlaceId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "userlist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject FloorList(String PlaceId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "floorlist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject TableList(String PlaceId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "tablelist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject FoodCategoriesList(String PlaceId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "foodcategorieslist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject DishList(String PlaceId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "dishlist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject PromotionList(String PlaceId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "promotionlist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject WaiterList(String PlaceId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "waiterlist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject MasterList(String PlaceId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "masterlist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject CustomerList(String PlaceId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "customerlist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject PostOrderMaster(String json) {
        String resjson = "";
        JSONObject objres = null;
        try {
            resjson = ExecuteRequest(json, "postordermaster");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.writeError(e);
        }
        return objres;
    }

    public JSONObject PostShiftRecord(String json) {
        String resjson = "";
        JSONObject objres = null;
        try {
            resjson = ExecuteRequest(json, "postshiftrecord");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.writeError(e);
        }
        return objres;
    }

    public JSONObject HeartBeat(String PlaceId, String UserId,String Now) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("UserId", UserId);
            objreq.put("Now", Now);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "pingservice");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

}