package com.izv.dam.newquip.vistas.main;


import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.adaptadores.AdaptadorNota;
import com.izv.dam.newquip.contrato.ClickListener;
import com.izv.dam.newquip.contrato.ClickListenerLong;
import com.izv.dam.newquip.contrato.ContratoMain;
import com.izv.dam.newquip.databinding.ItemBinding;
import com.izv.dam.newquip.dialogo.OnBorrarDialogListener;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.dialogo.DialogoBorrar;
import com.izv.dam.newquip.vistas.notas.VistaNota;

public class VistaQuip extends AppCompatActivity implements ContratoMain.InterfaceVista, ClickListener, ClickListenerLong,
        OnBorrarDialogListener,
        NavigationView.OnNavigationItemSelectedListener {

    private AdaptadorNota adaptador;
    private PresentadorQuip presentador;
    FloatingActionButton fab;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        ejecutar();
    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        presentador = new PresentadorQuip(this);


        //RECYCLERVIEW
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        adaptador = new AdaptadorNota(null);
        mRecyclerView.setAdapter(adaptador);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adaptador.setOnItemClickListener(this);
        adaptador.setOnItemLongClickListener(this);

        //DATA BINDING
        /*
        ItemBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //Nota nota = new Nota(52354,"titulo","notaaa","","","","aaa","","aaa","");
        Nota nota = new Nota(52354,"titulo","notaaa","","","","aaa","","aaa","");
        binding.setNota(nota);
        */



    }


    private void ejecutar() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentador.onAddNota();
            }
        });
    }

    @Override
    protected void onPause() {
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }


    @Override
    public void mostrarAgregarNota() {
        //Podemos quitar el toast de "add"
        //Toast.makeText(VistaQuip.this, "add", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNota.class);
        startActivity(i);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    @Override
    public void mostrarDatos(Cursor c) {
        adaptador.changeCursor(c);
    }

    @Override
    public void mostrarEditarNota(Nota n) {
        //Podemos quitar el toast de "edit"
        //Toast.makeText(VistaQuip.this, "edit", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNota.class);
        Bundle b = new Bundle();
        b.putParcelable("nota", n);
        i.putExtras(b);
        startActivity(i);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    @Override
    public void mostrarConfirmarBorrarNota(Nota n) {
        DialogoBorrar fragmentBorrar = DialogoBorrar.newInstance(n);
        fragmentBorrar.show(getSupportFragmentManager(), "Dialogo borrar");
    }


    @Override
    public void onBorrarPossitiveButtonClick(Nota n) {
        presentador.onDeleteNota(n);
    }

    @Override
    public void onBorrarNegativeButtonClick() {

    }

    /**
     * Navigation Drawer
     */
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quip, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final Toast toast = Toast.makeText(getApplicationContext(), "onQueryTextSubmit", Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 10);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final Toast toast = Toast.makeText(getApplicationContext(), "onQueryTextChange", Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 10);
                return false;
            }
        });
        /*
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName= new ComponentName(context, --.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        */
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id_menu = item.getItemId();
        switch (id_menu) {
            case R.id.order_l:
                break;
            case R.id.order_i:
                break;
            case R.id.order_v:
                break;
            case R.id.order_a:

                break;
            case 0:
                break;
            default:
                Log.v("MenuItem", "default");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        presentador.onEditNota(position);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Toast.makeText(VistaQuip.this, "delete", Toast.LENGTH_SHORT).show();
        presentador.onShowBorrarNota(position);
    }
}