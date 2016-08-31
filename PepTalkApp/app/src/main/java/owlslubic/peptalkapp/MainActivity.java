package owlslubic.peptalkapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import owlslubic.peptalkapp.views.ChecklistActivity;
import owlslubic.peptalkapp.views.DialogFragNewPepTalk;
import owlslubic.peptalkapp.views.PepTalkListActivity;

public class MainActivity extends AppCompatActivity{//}
       // implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.button_temp1);
        Button button2 = (Button) findViewById(R.id.button_temp2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "check out da checklist!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Pep Talks on the wAy!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PepTalkListActivity.class);
                startActivity(intent);
            }
        });


        //instantiate views
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

/*

        //nav drawer setup
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }




    //nav drawer menu options
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
*/

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        /**these bad boys ain't workin**/
/*
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_peptalks) {
            Toast.makeText(MainActivity.this, "Pep Talks on the wAy!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, PepTalkListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_checklist) {
            Toast.makeText(MainActivity.this, "check out da checklist!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_resources) {
            Toast.makeText(MainActivity.this, "resources", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_instructions) {
            Toast.makeText(MainActivity.this, "instructions", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_about) {
            Toast.makeText(MainActivity.this, "about!", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_logout) {
            Toast.makeText(MainActivity.this, "logout, bye", Toast.LENGTH_SHORT).show();


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


*/
    }
}
