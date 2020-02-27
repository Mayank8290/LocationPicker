package com.place.pickaddress


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.place.pickaddress.Cloudware.Cloudware
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.rtchagas.pingplacepicker.PingPlacePicker
import org.json.JSONObject


class MainActivity: AppCompatActivity() {


    var REQUEST_PLACE_PICKER = 1

    private var cust_id = ""
    private var login_id = ""
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = intent
        try {
            cust_id = intent.getStringExtra("cust")
            login_id = intent.getStringExtra("loginid")
            token = intent.getStringExtra("token")
            ShowPlacePicker()
        }
        catch (e:java.lang.Exception) {
            // handler
            Toast.makeText(applicationContext,"Intent Data is not Correct",Toast.LENGTH_LONG).show()
            finish()
        }

    }


    private fun setDialog(show: Boolean) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        //View view = getLayoutInflater().inflate(R.layout.progress);
        builder.setView(R.layout.progress)
        val dialog: Dialog = builder.create()
        if (show) dialog.show() else dialog.dismiss()
    }

    private fun ShowPlacePicker()
    {
        val builder = PingPlacePicker.IntentBuilder()
        builder.setAndroidApiKey("AIzaSyDEkFqerganKdP74jx5u0a4UT1PODmhd5M")
                .setMapsApiKey("AIzaSyDEkFqerganKdP74jx5u0a4UT1PODmhd5M")
        try {
            val placeIntent = builder.build(this@MainActivity)
            startActivityForResult(placeIntent, REQUEST_PLACE_PICKER)
        }
        catch (ex: Exception) {
            Toast.makeText(applicationContext,"Google Play Service Not Available.",Toast.LENGTH_LONG).show()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == REQUEST_PLACE_PICKER) && (resultCode == Activity.RESULT_OK)) {
            val place: Place? = PingPlacePicker.getPlace(data!!)
            Toast.makeText(applicationContext,"You selected: ${place?.name}",Toast.LENGTH_LONG).show()
            var latLng: LatLng? = PingPlacePicker.getPlace(data!!)?.latLng
            var address = place?.address
            sendthedata(latLng?.latitude.toString(),latLng?.longitude.toString(),address.toString())
        }
    }


  fun sendthedata(lat:String,lng:String,address:String)
  {
      setDialog(true)
      val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap
      val network = BasicNetwork(HurlStack())
      val requestQueue = RequestQueue(cache, network).apply {
          start()
      }
      val url = "https://mobileapps.heromotocorp.com/cloudware_uat/GatewayAnalyserJson?ORG_ID=HMC"
      val params = HashMap<String,String>()
      params["0"] = login_id
      params["1"] = cust_id
      params["2"] = lat
      params["3"] = lng
      params["4"] = address
      val jsonObject = JSONObject(params as Map<*, *>)

      var datatosend = Cloudware("MB_Submit_LatLong", jsonObject,login_id).datatosend


      val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST,
              url,
              JSONObject(datatosend),
              Response.Listener { response ->
                  Log.d("Response", response.toString())
                  Toast.makeText(applicationContext,"Success",Toast.LENGTH_LONG).show()
                  val data = Intent()
                  data.putExtra("cust_id", cust_id)
                  data.putExtra("login_id", login_id)
                  data.putExtra("lat", lat)
                  data.putExtra("lng", lng)
                  data.putExtra("address", address)
                  setResult(Activity.RESULT_OK, data)
                  finish()
              },
              Response.ErrorListener { error -> Log.d("Error", error.toString()) }) {
          @Throws(AuthFailureError::class)
          override fun getHeaders(): Map<String, String> {
              val headers: HashMap<String, String> = HashMap()
              headers["Content-Type"] = "application/json; charset=utf-8"
              headers["Authorization"] = token
              return headers
          }

          @Throws(AuthFailureError::class)
          override fun getParams(): Map<String, String> {
              val params: MutableMap<String, String> = HashMap()
              params[""] = ""
              return params
          }
      }

      val socketTimeout = 60000 //30 seconds - change to what you want
      val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
      jsonObjectRequest.retryPolicy = policy
      requestQueue.add(jsonObjectRequest)

  }







}