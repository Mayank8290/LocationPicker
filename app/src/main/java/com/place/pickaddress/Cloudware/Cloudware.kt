package com.place.pickaddress.Cloudware

import android.util.Log
import org.json.JSONObject

class Cloudware
{
    var datatosend:String = ""
    constructor(procedurename:String,map:JSONObject,loginid:String)
    {

        val stringifyjson = map.toString()
        Log.wtf("stringfy",stringifyjson)

        datatosend = "{" +
                "  \"PWSESSIONRS\": {" +
                "    \"PWPROCESSRS\": {" +
                "      \"PWHEADER\": {" +
                "        \"IN_PROCESS_ID\": \"1\"," +
                "        \"APP_ID\": \"HMC\"," +
                "        \"DEVICE_LATITUDE\": 0," +
                "        \"DEVICE_LONGITUDE\": 0," +
                "        \"DEVICE_MAKE\": \"Samsung SM-T295\"," +
                "        \"DEVICE_MODEL\": \"SM-T295\"," +
                "        \"DEVICE_TIMESTAMP\": \"2019-09-19\"," +
                "        \"IMEI_NO\": \"356136101877449\"," +
                "        \"INSTALLATION_ID\": \"8211974846~HMC~238577\"," +
                "        \"LOCATION\": \"\"," +
                "        \"ORG_ID\": \"HMC\"," +
                "        \"OS_VERSION\": \"9\"," +
                "        \"OUT_PROCESS_ID\": "+procedurename+"," +
                "        \"LOGIN_ID\": "+loginid+"," +
                "        \"PASSWORD\": \"\"," +
                "        \"PROCESS_ID\": \"\"," +
                "        \"PW_CLIENT_VERSION\": \"03_01_00\"," +
                "        \"PW_SESSION_ID\": \"8211974846~HMC~2385771981198922\"," +
                "        \"PW_VERSION\": \"\"," +
                "        \"SERVER_TIMESTAMP\": \"\"," +
                "        \"SESSION_EXPIRE_TIME\": \"\"," +
                "        \"SIM_ID\": \"\"," +
                "        \"USER_ID\": \"30250G04\"," +
                "        \"USER_SESSION_ID\": \"\"," +
                "        \"VERSION_ID\": \"4.1\"," +
                "        \"IS_AUTH\": \"\"" +
                "      }," +
                "      \"PWDATA\": {" +
                        procedurename+": {" +
                "          \"Row\": [" +
                stringifyjson+
                "          ]" +
                "        }" +
                "      }," +
                "      \"PWERROR\": \"\"" +
                "    }" +
                "  }" +
                "}"


    }

    fun ProvideMeRequest():String
    {
        return datatosend
    }
}