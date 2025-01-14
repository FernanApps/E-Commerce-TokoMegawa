package com.tinieblas.tokomegawa.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tinieblas.tokomegawa.Constants;
import com.tinieblas.tokomegawa.R;
import com.tinieblas.tokomegawa.databinding.FragmentHotSalesBinding;
import com.tinieblas.tokomegawa.domain.models.ModelohotSales;

import java.util.ArrayList;
import java.util.List;

public class HotSalesFragment extends Fragment {

    HotSalesFragment context;
    private final List<ModelohotSales> ListProducts = new ArrayList<>();
    GridView gridView;
    //hotSalesAdapterGridView adapterGridView;

    //hotSalesAdapterRecycler hotSalesAdapterRecycler;
    private FragmentHotSalesBinding fragmentHotSalesBinding;
    //private final List<ModelohotSales> ListProducts1 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hot_sales , container, false);


        // Inflate the layout for this fragment
        context = this;
        fragmentHotSalesBinding = fragmentHotSalesBinding.inflate(inflater, container, false);

        //Button button1 = (Button) view.findViewById(R.id.button1);
        //Button button2 = (Button) view.findViewById(R.id.button2);

        gridView = fragmentHotSalesBinding.gridViewProducts.findViewById(R.id.gridViewProducts);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //System.out.println("==> "+ListProducts.get(i));
                //Toast.makeText(context.getContext(), "==> " + ListProducts.get(i).getTitulo(), Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.INTENT_NAME_MODELO, ListProducts.get(i));
                //Intent intent = new Intent(context.getContext(), HomeFragment.class);

                // llamamos a carrito que seria la key y en el otro parametro le pasamos el arrayList
                // y lo llenamos
                //intent.putExtra(Constants.INTENT_NAME, ListProducts.get(i));

                /*bundle.putString("titulo", ListProducts.get(i).getTitulo());
                bundle.putString("descripcion", ListProducts.get(i).getDescripcion());
                bundle.putString("img1", ListProducts.get(i).getImagen1());
                bundle.putString("img2", ListProducts.get(i).getImagen2());
                bundle.putString("img3", ListProducts.get(i).getImagen3());
                bundle.putString("img4", ListProducts.get(i).getImagen4());
                bundle.putFloat("descuento", ListProducts.get(i).getDescuento());
                bundle.putFloat("precio", ListProducts.get(i).getPrecio());
                bundle.putString("delivery", ListProducts.get(i).getDelivery());*/

                getParentFragmentManager().setFragmentResult("key", bundle);
                //System.out.println(ListProducts);
                //Log.e("lista",ListProducts.toString());
                //replaceFragment(new DetailsProductsFragment());

                /*Intent intent = new Intent(context.getContext(), DetailsProductsFragment.class);
                intent.putExtra("ModelohotSales", ListProducts.get(i));
                startActivity(intent);*/
            }
        });
        //getData();
        //datos();


        return fragmentHotSalesBinding.getRoot();
    }

    /*public void getData() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://my-json-server.typicode.com/SrDeLasTinieblas/Peliculas/productos",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Aca estamos diciendo que lo que esta en el carrito lo ponga una list
                            Type typeList = new TypeToken<List<ModelohotSales>>() {}.getType();

                            List<ModelohotSales> productsListResponse = new Gson().fromJson(response, typeList);

                            ListProducts.addAll(productsListResponse);

                            adapterGridView = new hotSalesAdapterGridView(context.getContext(), ListProducts);
                            gridView.setAdapter(adapterGridView);

                        } catch (Exception e) {
                            Log.d("JSONException", e.getMessage());
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println(error.networkResponse + " error");
                    }
                }
        );
        // Aqui enviamos la solicitud de la peticion
        requestQueue.add(request);
    }*/

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutHome, fragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);

    }

    public void datos(){
        /*Bundle bundle = new Bundle();
        bundle.putString("lista1", ListProducts1.toString());*/
        //Toast.makeText(context.getContext(), "oo"+ListProducts1, Toast.LENGTH_SHORT).show();
    }



    /*public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }*/
}

























