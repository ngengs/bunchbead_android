package com.ice.bunchbead.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ice.bunchbead.android.data.Ingredient;
import com.ice.bunchbead.android.data.RankIngredient;
import com.ice.bunchbead.android.helpers.UserHelper;
import com.ice.bunchbead.android.listener.firebase.DataSingleListener;
import com.ice.bunchbead.android.listener.utils.SimpleTextChangeListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import timber.log.Timber;

public class UpdateIngredientActivity extends AppCompatActivity {

    // FireBase variable
    private DatabaseReference mDatabase;

    // View variable
    private TextView stockNowText;
    private TextView stockMinText;
    private TextView priceText;
    private TextView priceTotalText;
    private EditText stockSizeEditText;
    private Button updateButton;
    private ProgressBar updateProgress;

    // Listener variable
    private ValueEventListener mDataIngredientListener;
    private ValueEventListener mDataRankListener;
    private SimpleTextChangeListener mTextListener;

    // Data & Process variable
    private String itemId;
    private String rankId;
    private String key;
    private Ingredient mData;
    private RankIngredient mRankIngredientData;
    private Boolean canUpdate = false;
    Boolean successUpdateIngredient = false;
    Boolean successUpdateRank = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Check user before start main
        UserHelper.checkLogin(mAuth, () -> UserHelper.runLogin(this));

        // Init layout
        setContentView(R.layout.activity_update_ingredient);

        stockNowText = findViewById(R.id.updateNowStockText);
        stockMinText = findViewById(R.id.updateMinStockText);
        priceText = findViewById(R.id.updatePriceIngredientText);
        priceTotalText = findViewById(R.id.updateTotalPriceText);
        stockSizeEditText = findViewById(R.id.inputUpdateSize);
        updateButton = findViewById(R.id.updateButton);
        updateProgress = findViewById(R.id.updateProgress);

        updateButton.setOnClickListener(v -> processUpdate());

        itemId = getIntent().getStringExtra("ITEM_ID");
        rankId = getIntent().getStringExtra("RANK_ID");
        key = getIntent().getStringExtra("KEY");
        if (itemId == null || rankId == null || key == null)
            throw new RuntimeException("Can't start with empty ingredient");

        // Init FireVase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setTitle(R.string.loading);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Listen data when start
        listenData();
        // Listen Input
        listenInput();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Remove all listener when stop
        mDatabase.child("bahan").child(itemId).removeEventListener(mDataIngredientListener);
        mDatabase.child("rank").child(rankId).child(key).removeEventListener(mDataRankListener);
        stockSizeEditText.removeTextChangedListener(mTextListener);
    }

    private void listenInput() {
        mTextListener = new SimpleTextChangeListener(this::updatePriceLayout);
        stockSizeEditText.addTextChangedListener(new SimpleTextChangeListener(this::updatePriceLayout));
    }

    private void listenData() {
        Timber.d("Listen Data");

        mDataIngredientListener = new DataSingleListener(
                dataSnapshot -> {
                    Ingredient data = dataSnapshot.getValue(Ingredient.class);
                    if (data != null) {
                        data.setId(dataSnapshot.getKey());
                        mData = data;
                    }
                    updateLayout();
                    Timber.d("On Data Ingredient Loaded %s", data);
                },
                databaseError -> Timber.d("On Data Ingredient Canceled")
        );
        mDatabase.child("bahan").child(itemId).addValueEventListener(mDataIngredientListener);

        mDataRankListener = new DataSingleListener(
                dataSnapshot -> {
                    RankIngredient data = dataSnapshot.getValue(RankIngredient.class);
                    if (data != null) {
                        data.setKey(key);
                        data.setRankId(rankId);
                    }
                    mRankIngredientData = data;
                    Timber.d("On Data Rank Loaded %s", data);
                },
                databaseError -> Timber.d("On Data Rank Canceled")
        );
        mDatabase.child("rank").child(rankId).child(key).addListenerForSingleValueEvent(mDataRankListener);
    }

    private void updateLayout() {
        updateProgress.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.GONE);
        stockSizeEditText.setEnabled(false);

        if (mData == null) return;

        updateProgress.setVisibility(View.GONE);
        updateButton.setVisibility(View.VISIBLE);
        stockSizeEditText.setEnabled(true);

        setTitle(mData.getNama().toUpperCase());
        stockSizeEditText.setHint("Tambah Jumlah (" + mData.getSatuan() + ")");
        NumberFormat numberFormat = new DecimalFormat("##.###");
        stockMinText.setText(getString(R.string.ingredients_count_unit, numberFormat.format(mData.getMin()), mData.getSatuan()));
        stockNowText.setText(getString(R.string.ingredients_count_unit, numberFormat.format(mData.getSisa()), mData.getSatuan()));
        priceText.setText(getString(R.string.price_format, NumberFormat.getInstance().format(mData.getHarga())));
        updatePriceLayout(null);
        updateButtonLayout();
    }

    private void updatePriceLayout(String input) {
        canUpdate = false;
        if (mData == null) return;
        Double totalPrice = 0.0;

        if (input != null && !input.equals("")) {
            Double updateStockNumber = Double.valueOf(input);
            if (updateStockNumber > 0) canUpdate = true;
            totalPrice = updateStockNumber * mData.getHarga();
        }
        priceTotalText.setText(getString(R.string.price_format, NumberFormat.getInstance().format(totalPrice)));
        updateButtonLayout();
    }

    private void updateButtonLayout() {
        updateButton.setEnabled(canUpdate);
    }

    private void processUpdate() {
        if (mData == null || mDataRankListener == null || !canUpdate) return;

        stockSizeEditText.setEnabled(false);
        updateButton.setVisibility(View.GONE);
        updateProgress.setVisibility(View.VISIBLE);

        String input = stockSizeEditText.getText().toString();
        Double newSisa = mData.getSisa();

        if (!input.equals("")) {
            newSisa += Double.valueOf(input);
        }
        if (newSisa <= mData.getMin()) {
            Toast.makeText(this, "Menambah bahan dengan jumlah masih kurang atau sama dengan jumlah minimal bahan", Toast.LENGTH_SHORT).show();
            stockSizeEditText.setEnabled(true);
            updateButton.setVisibility(View.VISIBLE);
            updateProgress.setVisibility(View.GONE);
            return;
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("bahan/" + itemId + "/sisa", newSisa);
        data.put("rank/" + rankId + "/" + key + "/processed", true);

        mDatabase.updateChildren(data, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                Timber.d("Update Failed");
                Toast.makeText(this, "Gagal memperbaharui data", Toast.LENGTH_SHORT).show();
                stockSizeEditText.setEnabled(true);
                updateButton.setVisibility(View.VISIBLE);
                updateProgress.setVisibility(View.GONE);
            } else {
                Timber.d("Update Success");
                Toast.makeText(this, "Berhasil memperbaharui data", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
