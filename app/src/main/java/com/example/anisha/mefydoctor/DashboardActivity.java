package com.example.anisha.mefydoctor;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView statusTv,startTv,endTv;
    ToggleButton onlineToggle;
    Button monday,tuesday,wednesday,thursday,friday,saturday,sunday,savebtn;
    EditText clientEdt,addrEdt,contactEdt,feeEdt;
    ListView managelist;
    ProgressDialog progress;
    private ArrayAdapter<String>adapter;
    String docId;

    LinearLayout dashLayout,dashFirstcardview,dashSecLayout,manageclinicLayout;
    private DatePickerDialog.OnDateSetListener mDateSetListener,mDateSetEndListener;
//    private static final String TAG = "RegistrationActivity";

private ArrayList<String> data = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        statusTv = (TextView)findViewById(R.id.statusTv);
        startTv = (TextView) findViewById(R.id.startTv);
        endTv = (TextView) findViewById(R.id.endTv);
        monday = (Button)findViewById(R.id.monday);
        tuesday = (Button)findViewById(R.id.tuesday);
        wednesday = (Button)findViewById(R.id.wednesday);
        thursday = (Button)findViewById(R.id.thursday);
        friday = (Button)findViewById(R.id.friday);
        saturday = (Button)findViewById(R.id.saturday);
        sunday = (Button)findViewById(R.id.sunday);
        savebtn = (Button)findViewById(R.id.savebtn);

        clientEdt = (EditText)findViewById(R.id.clientEdt);
        addrEdt = (EditText)findViewById(R.id.addrEdt);
        contactEdt = (EditText)findViewById(R.id.contactEdt);
        feeEdt = (EditText)findViewById(R.id.feeEdt);

        dashLayout = (LinearLayout)findViewById(R.id.dashLayout);
        dashFirstcardview = (LinearLayout)findViewById(R.id.dashFirstcardview);
        dashSecLayout = (LinearLayout)findViewById(R.id.dashSecLayout);
        manageclinicLayout = (LinearLayout)findViewById(R.id.manageclinicLayout);

//        dashleftbtn = (Button)findViewById(R.id.dashleftbtn);

        generatelist();

        managelist = (ListView)findViewById(R.id.managecliniclist);

        onlineToggle = (ToggleButton)findViewById(R.id.onlineToggle);

        onlineToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (onlineToggle.isChecked()){
                   statusTv.setText("Online");
               }
               else{
                   statusTv.setText("Offline");
               }
            }
        });

        dashFirstcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashLayout.setVisibility(View.GONE);
                dashSecLayout.setVisibility(View.VISIBLE);

                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Add Clinic");
            }
        });


        startTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DashboardActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTv.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        endTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DashboardActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTv.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

//        dashleftbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dashSecLayout.setVisibility(View.GONE);
//                dashLayout.setVisibility(View.VISIBLE);
//            }
//        });

        managelist.setAdapter(new MyListAdaper(DashboardActivity.this,R.layout.manageclinic,data));
        managelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DashboardActivity.this,"List item clicked"+position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashSecLayout.setVisibility(View.GONE);
                manageclinicLayout.setVisibility(View.VISIBLE);

                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Manage Clinic");
            }
        });

//        monday.setOnClickListener(new View.OnClickListener() {
//            private boolean mondaychanged;
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//               if (mondaychanged){
//                   monday.setBackgroundColor(R.drawable.buttoncolor);
//               }
//              else{
//                   monday.setBackgroundColor(R.drawable.buttoncoloroff);
//               }
//                mondaychanged = !mondaychanged;
//            }
//        });
//        tuesday.setOnClickListener(new View.OnClickListener() {
//            private boolean tueschanged;
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//                if (tueschanged){
//                    tuesday.setBackgroundColor(R.drawable.buttoncolor);
//                }
//                else{
//                    tuesday.setBackgroundColor(R.drawable.buttoncoloroff);
//                }
//                tueschanged = !tueschanged;
//            }
//        });
//        wednesday.setOnClickListener(new View.OnClickListener() {
//            private boolean wedchanged;
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//                if (wedchanged){
//                    wednesday.setBackgroundColor(R.drawable.buttoncolor);
//                }
//                else{
//                    wednesday.setBackgroundColor(R.drawable.buttoncoloroff);
//                }
//                wedchanged = !wedchanged;
//            }
//        });
//        thursday.setOnClickListener(new View.OnClickListener() {
//            private boolean thurchanged;
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//                if (thurchanged){
//                    thursday.setBackgroundColor(R.drawable.buttoncolor);
//                }
//                else{
//                    thursday.setBackgroundColor(R.drawable.buttoncoloroff);
//                }
//                thurchanged = !thurchanged;
//            }
//        });
//        friday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                friday.setBackgroundColor(R.drawable.buttoncolor);
//            }
//        });
//        saturday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saturday.setBackgroundColor(R.drawable.buttoncolor);
//            }
//        });
//        sunday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sunday.setBackgroundColor(R.drawable.buttoncolor);
//            }
//        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }
//private void generatelist(){
//        for (int i = 0;i<55; i++){
//            data.add("this is row number"+i);
//        }
//}

    public void generatelist(){
        progress = new ProgressDialog(this);
        progress.setMessage("please wait...");
        progress.setCancelable(false);
        progress.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

//    String url = getResources().getString(R.string.json_get_url);
        String url = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/doctor";

        System.out.println("url---------------->>>>>>>>>>"+ url);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONArray>(){

                    @Override
                    public void onResponse(JSONArray response) {
                        progress.dismiss();
                        System.out.println("response------------------------------------"+ response.toString());

                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject=response.getJSONObject(i);
                                 docId = jsonObject.getString("doctorId");
                                System.out.println("doctor id----------->>>>>>>"+docId);
                                data.add(jsonObject.getString("name")+"\n"+jsonObject.getString("speciality")+"\n"+jsonObject.getString("address"));
//                            imgitem.add(jsonObject.getString("profileImage"));


                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
//                        ArrayAdapter adapter = null;
//                        adapter.notifyDataSetChanged();
                    }
                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                System.out.println("error-----------------------------------"+ error);
            }
        })

        {
            /* Passing some request headers */
            @Override
            public Map<String,String> getHeaders() {
                System.out.println("header----------------------------------------------------------------------<><><><><><><>");
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
                headers.put("Accept","*/*");
                return headers;
            }
        };


        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);
        System.out.println("wating-----<><><><><><><><><><><><><><><><><><><><>><><><><>><");
        requestQueue.add(jsonArrayRequest);
    }

    private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
//                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
                viewHolder.title = (TextView) convertView.findViewById(R.id.manageclinicTv);
                viewHolder.button = (Button) convertView.findViewById(R.id.managebtn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Button was clicked for list item " + position +docId, Toast.LENGTH_SHORT).show();

                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }
    public class ViewHolder{
        TextView title;
        Button button;
    } 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

// if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
