package com.tinieblas.tokomegawa.ui.adptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tinieblas.tokomegawa.R;
import com.tinieblas.tokomegawa.databinding.ActivityMyCartBinding;
import com.tinieblas.tokomegawa.databinding.ItemsMycartBinding;
import com.tinieblas.tokomegawa.domain.models.ProductosItem;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    private final Context mContext;
    private List<ProductosItem> mCarrito;
    private final TextView textSubTotal;
    private final TextView textTotal;
    private final AsyncListDiffer<ProductosItem> differ;
    private RecyclerView recyclerView;

    private ViewSwitcher viewSwitcher;

    public CarritoAdapter(Context mContext, List<ProductosItem> mCarrito,
                          TextView textSubTotal, TextView textTotal,
                          RecyclerView recyclerView,
                          ViewSwitcher viewSwitcher

    ) {
        this.mContext = mContext;
        this.mCarrito = mCarrito;
        this.textSubTotal = textSubTotal;
        this.textTotal = textTotal;
        this.recyclerView = recyclerView; // Agrega esta línea para asignar el RecyclerView
        this.mCarrito = mCarrito != null ? mCarrito : new ArrayList<>(); // Inicializar la lista mCarrito

        this.viewSwitcher = viewSwitcher;
        // Configurar el diferenciador de listas
        differ = new AsyncListDiffer<>(this, new CarritoDiffCallback());
    }

    public void setCarrito(List<ProductosItem> carrito) {
        differ.submitList(carrito);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemsMycartBinding binding = ItemsMycartBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductosItem carrito = differ.getCurrentList().get(position);
        holder.bind(carrito);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemsMycartBinding binding;

        public ViewHolder(ItemsMycartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductosItem carrito) {
            double totalPrice = carrito.getPrecioUnitario() * carrito.getAmount();
            BigDecimal bd = new BigDecimal(totalPrice).setScale(2, RoundingMode.HALF_UP);
            double roundedPrice = bd.doubleValue();

            binding.textTituloCarrito.setText(carrito.getNombreProducto());
            binding.tvDescripcionMyCart.setText(carrito.getDescripcionProducto());
            binding.textPrecioMyCart.setText("S/. " + roundedPrice);
            binding.textViewAmount.setText(String.valueOf(carrito.getAmount()));

            Glide.with(itemView.getContext()).load(carrito.getImagen1()).into(binding.imageView2);

            binding.buttonArrowDerecha1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aumentarCantidad(carrito);
                }
            });
            binding.buttonArrowDerecha3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    disminuirCantidad(carrito);
                }
            });
        }

        private void aumentarCantidad(ProductosItem carrito) {
            int cantidadActual = carrito.getAmount();
            carrito.setAmount(cantidadActual + 1);
            notifyDataSetChanged();
            // Actualiza el carrito en SharedPreferences
            actualizarCarritoEnSharedPreferences();
        }

        private void disminuirCantidad(ProductosItem carrito) {
            int cantidadActual = carrito.getAmount();
            if (cantidadActual > 1) {
                carrito.setAmount(cantidadActual - 1);
                notifyDataSetChanged();
                // Actualiza el carrito en SharedPreferences
                actualizarCarritoEnSharedPreferences();
            }
        }

        public void actualizarCarritoEnSharedPreferences() {
            // Obtén el adaptador del RecyclerView
            RecyclerView recyclerView = (RecyclerView) itemView.getParent();
            CarritoAdapter adapter = (CarritoAdapter) recyclerView.getAdapter();
            List<ProductosItem> carrito = adapter.differ.getCurrentList();

            // Convierte la lista de productos a JSON utilizando Gson
            Gson gson = new Gson();
            String carritoJson = gson.toJson(carrito);

            // Guarda el carrito actualizado en SharedPreferences
            SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("productos_carrito", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lista_productos_carrito", carritoJson);
            editor.apply();

            // Calcula el subtotal y lo muestra en el TextView
            adapter.calcularSubTotal();
            adapter.calcularTotal();


        }



    }
    private void calcularSubTotal() {
        double subTotal = 0;
        for (ProductosItem producto : differ.getCurrentList()) {
            double precio = producto.getPrecioUnitario() * producto.getAmount();
            subTotal += precio;
            Toast.makeText(mContext, "amount " + producto.getAmount(), Toast.LENGTH_SHORT).show();
        }

        BigDecimal bd = new BigDecimal(subTotal).setScale(2, RoundingMode.HALF_UP);
        double roundedPrice = bd.doubleValue();
        // Actualiza el valor en el adaptador en lugar de actualizar directamente el TextView
        textSubTotal.setText("S/. " + roundedPrice);

    }

    private void calcularTotal() {
        double subTotal = 0;
        for (ProductosItem producto : differ.getCurrentList()) {
            double precio = producto.getPrecioUnitario() * producto.getAmount();
            Toast.makeText(mContext, "amount " + producto.getAmount(), Toast.LENGTH_SHORT).show();
            subTotal += precio;
        }

        // Calcula el descuento y el monto de entrega
        double descuento = 10.0; // Ejemplo: 10% de descuento
        double delivery = 5.0; // Ejemplo: S/. 5 de entrega

        // Aplica el descuento al subtotal
        double descuentoAmount = subTotal * (descuento / 100);
        double subtotalConDescuento = subTotal - descuentoAmount;

        // Calcula el total sumando el subtotal con descuento y el monto de entrega
        double total = subtotalConDescuento + delivery;

        BigDecimal bd = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP);
        double roundedPrice = bd.doubleValue();
        // Actualiza el valor en el adaptador en lugar de actualizar directamente el TextView
        textTotal.setText("S/. " + roundedPrice);
    }



    private static class CarritoDiffCallback extends DiffUtil.ItemCallback<ProductosItem> {
        @Override
        public boolean areItemsTheSame(ProductosItem oldItem, ProductosItem newItem) {
            return oldItem.getIdProducto() == newItem.getIdProducto();
        }

        @Override
        public boolean areContentsTheSame(ProductosItem oldItem, ProductosItem newItem) {
            return oldItem.getAmount() == newItem.getAmount();
        }
    }

    public final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            ProductosItem producto = differ.getCurrentList().get(position);
            borrarUnProductoDelCarrito(producto);

        }
    };
    @SuppressLint("SetTextI18n")
    public void borrarUnProductoDelCarrito(ProductosItem producto) {
        CarritoAdapter adapter = (CarritoAdapter) recyclerView.getAdapter();

        // Obtén la lista actual de productos en el carrito
        List<ProductosItem> carrito = new ArrayList<>(adapter.differ.getCurrentList());

        // Elimina el producto de la lista
        carrito.remove(producto);

        if (carrito.isEmpty()) {
            // El carrito está vacío
            adapter.setCarrito(new ArrayList<>());
            Toast.makeText(mContext, "El carrito está vacío", Toast.LENGTH_SHORT).show();
        } else {
            // El carrito todavía tiene productos
            adapter.setCarrito(carrito);
        }

        // Convierte la lista de productos a JSON utilizando Gson
        Gson gson = new Gson();
        String carritoJson = gson.toJson(carrito);

        // Guarda el carrito actualizado en SharedPreferences
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("productos_carrito", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lista_productos_carrito", carritoJson);
        editor.apply();

        // Actualiza la lista y el subtotal en el adaptador
        adapter.setCarrito(carrito);
        adapter.calcularSubTotal();
        adapter.calcularTotal();

        // Muestra un mensaje de confirmación
        Toast.makeText(mContext, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
    }





}
