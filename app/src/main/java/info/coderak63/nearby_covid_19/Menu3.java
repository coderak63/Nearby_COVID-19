package info.coderak63.nearby_covid_19;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import info.coderak63.nearby_covid_19.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Menu3 extends Fragment {

    private String TAG = MainActivity.class.getSimpleName();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild  = new HashMap<String, List<String>>();

    ProgressDialog pDialog;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_3, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Global Cases");


        // get the listview
        expListView = (ExpandableListView) getView().findViewById(R.id.lvExp2);

        pDialog = new ProgressDialog(getContext());

        if(internet_connection()) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            new GetGlobal(listDataHeader,listDataChild,expListView,getContext()).execute();
        }else{


            pDialog.setMessage("No Internet Connection");
            //pDialog.setCancelable(false);
            pDialog.show();
        }


        //new GetDistrict(listDataHeader,listDataChild,expListView,getContext()).execute();
        // preparing list data
        //prepareListData();

        //listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);

        // setting list adapter
        //expListView.setAdapter(listAdapter);

        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            int previousGroup=-1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition!=previousGroup)
                    expListView.collapseGroup(previousGroup);
                previousGroup=groupPosition;

            }
        });

    }


/*

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Uttar Pradesh");
        listDataHeader.add("Bihar");
        listDataHeader.add("Maharastra");

        // Adding child data
        List<String> up = new ArrayList<String>();
        up.add("Noida\n56");
        up.add("Mirzapur\n2");
        up.add("Sonbhadra\n1");
        up.add("Varanasi\n8");


        List<String> bh = new ArrayList<String>();
        bh.add("Siwan\n23");
        bh.add("Patna\n12");
        bh.add("Shravasti\n7");
        bh.add("Aara\n5");


        List<String> mh = new ArrayList<String>();
        mh.add("Mumbai\n234");
        mh.add("Pune\n89");
        mh.add("Thane\n97");


        listDataChild.put(listDataHeader.get(0), up); // Header, Child data
        listDataChild.put(listDataHeader.get(1), bh);
        listDataChild.put(listDataHeader.get(2), mh);
    }
*/



    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}




class GetGlobal extends AsyncTask<Void, Void, Void> {

    ExpandableListAdapter2 listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Context context;
    ProgressDialog pDialog;

    public GetGlobal(List<String> listDataHeader, HashMap<String, List<String>> listDataChild, ExpandableListView expListView, Context context) {

        this.listDataHeader=listDataHeader;
        this.listDataChild=listDataChild;
        this.expListView=expListView;
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog


        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();


    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall("https://pomber.github.io/covid19/timeseries.json");
        JSONParser parser = new JSONParser();


        if (jsonStr != null) {
            try {
                Object obj = parser.parse(jsonStr);
                JSONObject jsonobject=(JSONObject)obj;


                for (Object o : jsonobject.keySet()) {
                    String country = o+"";
                    listDataHeader.add(country);

                    JSONArray jsonarray = (JSONArray)jsonobject.get(country);
                    JSONObject last = (JSONObject)jsonarray.get(0);

                    List<String> temp = new ArrayList<String>();
                    for (Object o2 : jsonarray) {

                        last = (JSONObject) o2;

                    }
                    String confirmed=last.get("confirmed")+"";
                    String deaths=last.get("deaths")+"";
                    String recovered=last.get("recovered")+"";

                    String data="Confirmed\n"+confirmed+"\nDeaths\n"+deaths+"\nRecovered\n"+recovered;
                    temp.add(data);
                    listDataChild.put(country,temp );

                }
            }
            catch (ParseException e) {
                listDataHeader.add("exception");
                listDataChild.put("E",new ArrayList<String>() );

                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");

            listDataHeader.add("Couldn't get json from server");
            listDataChild.put("E",new ArrayList<String>() );
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);


        if (pDialog.isShowing())
            pDialog.dismiss();

        Collections.sort(listDataHeader);

        listAdapter = new ExpandableListAdapter2(context, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

    }

}










