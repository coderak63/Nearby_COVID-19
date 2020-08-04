package info.coderak63.nearby_covid_19;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import info.coderak63.nearby_covid_19.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class Menu1 extends Fragment implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    int flag=0;

    private static final int PERMISSION_REQUEST_CODE = 200;

    HashMap<String,ArrayList<String>> dist_latlong = new HashMap<String,ArrayList<String>>();
    HashMap<String,String> dist_cases = new HashMap<String,String>();



    private RecyclerView mRecyclerView;
    ArrayList<Model> list= new ArrayList<>();
    MultiViewTypeAdapter adapter;

    View rootview;
    ProgressDialog pDialog;


    private FusedLocationProviderApi locationProvider = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Double currentLatitude;
    private Double currentLongitude;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_COARSE_LOCATION };



    TextView lati;
    TextView longi;
    TextView nearby,total,cured,deceased,status;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview= inflater.inflate(R.layout.fragment_menu_1, container, false);

        //MobileAds.initialize(getActivity(), "ca-app-pub-4176612920557834~7093853081");



        if (!checkLocationPermission()) {
            flag=1;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }




        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        list.add(new Model(Model.HEADER_TYPE,"a","a","a",0));
        list.add(new Model(Model.RISK_TYPE,"Loading...","a","a",R.drawable.s1));
        //list.add(new Model(Model.NATIVE_AD_TYPE,"a","a","a",0));
        list.add(new Model(Model.GPS_TYPE,"0","0","a",0));
        list.add(new Model(Model.NEAREST_TYPE,"Fetching...","0","a",0));
       // list.add(new Model(Model.NATIVE_AD_TYPE,"a","a","a",0));
        list.add(new Model(Model.NEARBY_TYPE,"Fetching...","0","a",0));
        list.add(new Model(Model.CASES_TYPE,"0","0","0",0));
        //list.add(new Model(Model.NATIVE_AD_TYPE,"a","a","a",0));
        list.add(new Model(Model.IMAGE_TYPE,"Be Aware! Be Safe!","a","a",0));






        adapter = new MultiViewTypeAdapter(list,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        // mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView = (RecyclerView)rootview.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);


        return rootview;


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");



        pDialog = new ProgressDialog(getContext());






        if(internet_connection()) {
            new GetCases(list, adapter).execute();

            new GetHashMap(dist_latlong,dist_cases).execute();
        }
    }





    @Override
    public void onResume() {
        super.onResume();


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationManager service = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            boolean enabled = false;
            try {
                enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception e) {
                showSettingsAlert();
            }

            if (!enabled) {
                showSettingsAlert();
            }

        }


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            flag=0;
        }





        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
        }


    }






    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(8 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        requestLocationUpdates();
    }

    private void requestLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;


            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();


        String la=String.format(Locale.getDefault(), "%.6f", currentLatitude);
        String lo=String.format(Locale.getDefault(), "%.6f", currentLongitude);


        list.set(2,new Model(Model.GPS_TYPE,la,lo,"a",0));
        adapter.notifyDataSetChanged();

        /*
        lati.setText(la);
        longi.setText(lo);


         */


        if(internet_connection()) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            new GetDB(currentLatitude, currentLongitude, list, adapter,dist_latlong,dist_cases).execute();
        }else{

            if(!pDialog.isShowing()) {
                pDialog.setMessage("No Internet Connection");
                pDialog.setCancelable(false);
                pDialog.show();
            }

        }


    }

    @Override
    public void onStart() {
        super.onStart();

        googleApiClient.connect();
    }



    @Override
    public void onPause() {
        super.onPause();



        if(flag!=1)
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());


        alertDialog.setTitle("GPS is not Enabled!");

        alertDialog.setMessage("Do you want to turn on GPS?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }



     boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }





    private boolean checkLocationPermission() {
        int result3 = ContextCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION);
        int result4 = ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION);
        return result3 == PackageManager.PERMISSION_GRANTED &&
                result4 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean coarseLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean fineLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (coarseLocation && fineLocation)
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }





}



class GetDB extends AsyncTask<Void, Void, String> {

    HashMap<String,ArrayList<String>> dist_latlong;
    HashMap<String,String> dist_cases;

    String nearest_dist,nearby="";
    double gps_lat,gps_long,nearest=100000.0;
    String[] cases;
    int count=-1,count1=-1;
    ArrayList<Model> list;
    MultiViewTypeAdapter adapter;
    public GetDB(double lat,double longi,ArrayList<Model> li,MultiViewTypeAdapter adap,HashMap<String,ArrayList<String>> dist_latlong,HashMap<String,String> dist_cases) {

        gps_lat=lat;
        gps_long=longi;
        list=li;
        adapter=adap;
        this.dist_latlong=dist_latlong;
        this.dist_cases=dist_cases;
    }



    @Override
    protected String doInBackground(Void... params) {

        String str = "";
        Double distance;

        if (gps_lat > 0 && gps_long > 0&&dist_latlong.size()>0&&dist_cases.size()>0) {
            count=0;
            count1=0;

            for (String dist : dist_latlong.keySet()) {

                ArrayList<String> latlong = dist_latlong.get(dist);
                Double lat = Double.parseDouble(latlong.get(0));
                Double lon = Double.parseDouble(latlong.get(1));
                distance = distance(gps_lat, lat, gps_long, lon);
                if(distance<nearest)
                {
                    nearest=distance;
                    nearest_dist=dist;
                }

                if (distance <= 50) {
                    nearby += String.format(Locale.getDefault(), "%.2f KM away: %s\n", distance, dist);
                }
                if (distance <= 40)
                {
                    if(dist_cases.containsKey(dist))
                    count += Integer.parseInt(dist_cases.get(dist));
                }
            }

            }







        return str;
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);

        //list.set(3,new Model(Model.CASES_TYPE,cases[0],cases[1],cases[2],0));
        //adapter.notifyDataSetChanged();
        Random rand = new Random();
        int x=rand.nextInt(30);



        if(count!=-1) {
            if (count <= 5) {
                list.set(1, new Model(Model.RISK_TYPE, "low", "a", "a",R.drawable.s1));

                if(x<5)list.set(1, new Model(Model.RISK_TYPE, "low", "a", "a",R.drawable.s1));
                else if(x>=5&&x<10)list.set(1, new Model(Model.RISK_TYPE, "low", "a", "a",R.drawable.s2));
                else if(x>=10&&x<15)list.set(1, new Model(Model.RISK_TYPE, "low", "a", "a",R.drawable.s3));
                else if(x>=15&&x<20)list.set(1, new Model(Model.RISK_TYPE, "low", "a", "a",R.drawable.s4));
                else if(x>=20)list.set(1, new Model(Model.RISK_TYPE, "low", "a", "a",R.drawable.s5));

                adapter.notifyDataSetChanged();
            } else if (count > 5 && count <= 20) {
                list.set(1, new Model(Model.RISK_TYPE, "moderate", "a", "a",R.drawable.s1));

                if(x<5)list.set(1, new Model(Model.RISK_TYPE, "moderate", "a", "a",R.drawable.s1));
                else if(x>=5&&x<10)list.set(1, new Model(Model.RISK_TYPE, "moderate", "a", "a",R.drawable.s2));
                else if(x>=10&&x<15)list.set(1, new Model(Model.RISK_TYPE, "moderate", "a", "a",R.drawable.s3));
                else if(x>=15&&x<20)list.set(1, new Model(Model.RISK_TYPE, "moderate", "a", "a",R.drawable.s4));
                else if(x>=20)list.set(1, new Model(Model.RISK_TYPE, "moderate", "a", "a",R.drawable.s5));
                adapter.notifyDataSetChanged();
            } else {
                list.set(1, new Model(Model.RISK_TYPE, "high", "a", "a",R.drawable.s1));

                if(x<5)list.set(1, new Model(Model.RISK_TYPE, "high", "a", "a",R.drawable.s1));
                else if(x>=5&&x<10)list.set(1, new Model(Model.RISK_TYPE, "high", "a", "a",R.drawable.s2));
                else if(x>=10&&x<15)list.set(1, new Model(Model.RISK_TYPE, "high", "a", "a",R.drawable.s3));
                else if(x>=15&&x<20)list.set(1, new Model(Model.RISK_TYPE, "high", "a", "a",R.drawable.s4));
                else if(x>=20)list.set(1, new Model(Model.RISK_TYPE, "high", "a", "a",R.drawable.s5));
                adapter.notifyDataSetChanged();
            }
        }


        String temp=String.format(Locale.getDefault(), "%.2f KM AWAY", nearest);
        list.set(3,new Model(Model.NEAREST_TYPE,temp,"0","a",0));
        list.set(4,new Model(Model.NEARBY_TYPE,nearby,"0","a",0));
    }

    public double distance(double lat1,
                           double lat2, double lon1,
                           double lon2)
    {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }
}





class GetCases extends AsyncTask<Void, Void, Void> {

    ArrayList<Model> list;
    MultiViewTypeAdapter adapter;
    String confirmed;
    String recovered;
    String deceased;

    public GetCases(ArrayList<Model> list,MultiViewTypeAdapter adapter) {

        this.list=list;
        this.adapter=adapter;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall("https://api.covid19india.org/data.json");
        JSONParser parser = new JSONParser();


        if (jsonStr != null) {
            Object ob = JSONValue.parse(jsonStr);
            JSONObject main = (JSONObject)ob;
            JSONArray dd = (JSONArray)main.get("statewise");
            JSONObject data = (JSONObject)dd.get(0);

            confirmed = data.get("confirmed")+"";
            recovered = data.get("recovered")+"";
            deceased = data.get("deaths")+"";


        } else {
            Log.e(TAG, "Couldn't get json from server.");

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        list.set(5,new Model(Model.CASES_TYPE,confirmed,recovered,deceased,0));
        adapter.notifyDataSetChanged();

    }

}





class GetHashMap extends AsyncTask<Void, Void, Void> {

    HashMap<String,ArrayList<String>> dist_latlong;
    HashMap<String,String> dist_cases;

    public GetHashMap(HashMap<String,ArrayList<String>> dist_latlong,HashMap<String,String> dist_cases) {

        this.dist_latlong=dist_latlong;
        this.dist_cases=dist_cases;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall("https://api.covid19india.org/v2/state_district_wise.json");
        String textStr= sh.makeServiceCall("https://www.dropbox.com/s/xidmqnoipg1fi0a/Nearby_Cov_DB.txt?dl=1");
        JSONParser parser = new JSONParser();

////for district with cases
        if (jsonStr != null) {
            try {
                Object obj = parser.parse(jsonStr);
                JSONArray jsonarray = (JSONArray) obj;


                for (Object o : jsonarray) {
                    JSONObject jsonobject = (JSONObject) o;
                    String name = (String) jsonobject.get("state");
                    //System.out.println(name + " : ");
                    //listDataHeader.add(name);
                    JSONArray dd = (JSONArray) jsonobject.get("districtData");

                    for (Object o2 : dd) {

                        JSONObject ddobject = (JSONObject) o2;
                        //System.out.println("      " + ddobject.get("district") + " -> " + ddobject.get("confirmed"));
                        String district=ddobject.get("district")+"";
                        String cases=ddobject.get("confirmed")+"";

                        dist_cases.put(district.trim(),cases.trim());
                    }



                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");;
        }




//for district with lat long

        String[] list = textStr.split("\n");
        String[] temp;
        //Map<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();

        for(String i:list)
        {
            ArrayList<String> al = new ArrayList<String>();
            if(!i.equals(""))
            {
                temp=i.split(",");
                al.add(temp[0]);
                al.add(temp[1]);
                dist_latlong.put(temp[2].trim(),al);
            }

        }



        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);


    }

}




