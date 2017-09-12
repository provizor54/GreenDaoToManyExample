package com.example.user.greendaotomanyexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.greendaotomanyexample.entity.DaoMaster;
import com.example.user.greendaotomanyexample.entity.DaoSession;
import com.example.user.greendaotomanyexample.entity.JoinProductsWithOrders;
import com.example.user.greendaotomanyexample.entity.Order;
import com.example.user.greendaotomanyexample.entity.Product;

import org.greenrobot.greendao.database.Database;

import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private Button btnRead;
    private Button btnWrite;
    private EditText etText;
    private long id = -1;
    private DaoSession daoSession;
    private List<Product> products;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRead = findViewById(R.id.btnRead);
        btnWrite = findViewById(R.id.btnWrite);
        random = new Random();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        if (daoSession.getProductDao().count() == 0) {
            daoSession.getProductDao().insert(new Product());
        } else {
            products = daoSession.getProductDao().loadAll();
        }

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = new Order();

                products.get(0).getOrdersWithThisProduct().add(order);
                daoSession.getProductDao().insertOrReplace(products.get(0));

                List<Product> products = daoSession.getProductDao().loadAll();

                Log.d("myLogs", "Size of orders after write: " + products.get(0).getOrdersWithThisProduct().size());
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Product> products = daoSession.getProductDao().loadAll();

                for (Product product : products) {
                    Log.d("myLogs", "Size orders: " + String.valueOf(product.getOrdersWithThisProduct().size()));
                }
            }
        });
    }
}
