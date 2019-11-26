package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.adrian.reseeipt.Adapters.SingleReceiptViewAdapter;
import com.adrian.reseeipt.Constants.ReceiptCategoryConstants;
import com.adrian.reseeipt.Database.DatabaseHandler;
import com.adrian.reseeipt.Model.Receipt;

import java.util.ArrayList;
import java.util.List;

public class ReceiptListActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_RECEIPT = 10;
    public static final int RESULT_SAVED = 11;
    public static final int RESULT_CANCELLED = 12;

    private LinearLayout addReceiptButton;
    private LinearLayout backToDashboardButton;
    private TextView receiptCategoryText;
    private TextView resultsCountText;
    private EditText searchQueryEditText;
    private LinearLayout searchButton;
    private LinearLayout cancelSearchButton;

    Intent intent;

    RecyclerView recyclerView;
    SingleReceiptViewAdapter adapter;
    List<Receipt> receiptList = new ArrayList<>();
    DatabaseHandler databaseHandler;
    String category;
    Spinner sortSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_list);

        addReceiptButton = findViewById(R.id.addReceiptButton);
        backToDashboardButton = findViewById(R.id.backToDashboardButton);
        receiptCategoryText = findViewById(R.id.receiptCategoryText);
        resultsCountText = findViewById(R.id.resultsCountText);
        searchQueryEditText = findViewById(R.id.searchQueryEditText);
        searchButton = findViewById(R.id.searchButton);
        cancelSearchButton = findViewById(R.id.cancelSearchButton);
        sortSpinner = findViewById(R.id.sortSpinner);
        databaseHandler = new DatabaseHandler(this);

        intent = getIntent();

        category = intent.getStringExtra(MainDashboardActivity.INTENT_CATEGORY);

        if (category.equals(ReceiptCategoryConstants.ALL)){
            receiptCategoryText.setText("All Receipts");
            receiptList = databaseHandler.getAllReceipt();

        } else {
            receiptCategoryText.setText(category);
            receiptList = databaseHandler.getAllReceiptByCategory(category);
        }



        recyclerView = findViewById(R.id.receiptListRecyclerView);
        adapter = new SingleReceiptViewAdapter(this, receiptList);
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        setResultCount();


        backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptListActivity.this, MainDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addReceiptButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptListActivity.this, AddReceiptActivity.class);
                startActivityForResult(intent, REQUEST_ADD_RECEIPT);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchQueryEditText.getText().toString();
                adapter.searchQuery(query);
                searchQueryEditText.setText(query);
                setResultCount();
            }
        });

        cancelSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clearQuery();
                searchQueryEditText.setText("");
                setResultCount();
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.sortReceipt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);

        if (requestCode == REQUEST_ADD_RECEIPT){
            if (resultCode == RESULT_SAVED){
                if (category.equals(ReceiptCategoryConstants.ALL)){
                    receiptList = databaseHandler.getAllReceipt();
                    adapter.setItemList(receiptList);
                } else {
                    receiptList = databaseHandler.getAllReceiptByCategory(category);
                    adapter.setItemList(receiptList);
                }

                setResultCount();
            } else if (resultCode == RESULT_CANCELLED) {

            }
        }
    }

    private void setResultCount(){
        int count = adapter.getItemCount();
        System.out.println(count);
        if (count == 1){
            resultsCountText.setText(count + " Receipt Found");
        } else {
            resultsCountText.setText(count + " Receipts Found");
        }
    }


}
