package com.example.goldlone;

import android.app.SearchManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register_Details extends AppCompatActivity {
    private static final String TAG = Register_Details.class.getSimpleName();

    private RegisterSqliteConnection rDatabase;
    private ArrayList<Register_data> allHistory=new ArrayList<>();
    private Register_Adapter rAdapter;

    RecyclerView contactView;
    SearchView searchView;

    String keyword="",keyword2="";

    LinearLayout lin_home;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__details);
        initview();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        contactView.setLayoutManager(linearLayoutManager);
        contactView.setHasFixedSize(true);
        rDatabase = new RegisterSqliteConnection(this);
        allHistory = rDatabase.listContacts();

        if (allHistory.size() > 0) {
            contactView.setVisibility(View.VISIBLE);
            rAdapter = new Register_Adapter(this, allHistory);
            contactView.setAdapter(rAdapter);


        } else {
            contactView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no Data in the database", Toast.LENGTH_LONG).show();
        }
        keyword=getIntent().getStringExtra("name");
        keyword2=getIntent().getStringExtra("amount");
        if(keyword !=null )
        {
            searchContact( keyword);
        }
    }
     public void initview()
      {
          lin_home=findViewById(R.id.lin_home);
          setFadeIn();
          contactView = (RecyclerView) findViewById(R.id.product_list);
          registerForContextMenu(contactView);

      }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(rDatabase != null){
            rDatabase.close();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchView);
        searchView = (SearchView)searchItem.getActionView();
        SearchView searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextSubmit(String query) {
                  searchContact(query);
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                  searchContact(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_all) {
            SweetAlertDialog progressDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            progressDialog.setCancelable(false);
            progressDialog.setTitleText("Are you sure you want to Delete All...?");
            progressDialog.setCancelText("No");
            progressDialog.setConfirmText("Yes");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    rDatabase.deleteAll();
                    start ();
                }
            });progressDialog.show();
           // rDatabase.deleteAll();

            //refresh the activity page.
           /* this.finish();
            this.startActivity(this.getIntent());*/
            //Toast.makeText(this, "Clicked " , Toast.LENGTH_SHORT).show();*/
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void searchContact(String keyword) {
        RegisterSqliteConnection databaseHelper = new RegisterSqliteConnection(getApplicationContext());

        List<Register_data> contacts =  databaseHelper.search(keyword);
        if (contacts != null) {
            contactView.setAdapter(new Register_Adapter(this, (ArrayList<Register_data>) contacts));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void searchBarcode(String keyword, String keyword2) {
        RegisterSqliteConnection databaseHelper = new RegisterSqliteConnection(getApplicationContext());

        List<Register_data> contacts =  databaseHelper.searchBarcode(keyword,keyword2);
        if (contacts != null) {
            contactView.setAdapter(new Register_Adapter(this, (ArrayList<Register_data>) contacts));
        }
    }
    private void start() {
        this.finish();
        this.startActivity(this.getIntent());
    }
    private void setFadeIn() {
        AnimationSet set = new AnimationSet(true);
        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(2000);
        fadeIn.setFillAfter(true);
        set.addAnimation(fadeIn);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.3f);
        lin_home.setLayoutAnimation(controller);

    }
}