package poly.hacks.improv2018;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private static final int I_CAN_HAS_CAMERA_PLZ = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupScreen();

        ImageView flameGuy = (ImageView)findViewById(R.id.login_flameguy);
        final ArrayList<Integer> bypass = new ArrayList<>();
        bypass.add(4);
        flameGuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bypass.get(0).equals(0)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, bypass.get(0) + " taps remaining...", Toast.LENGTH_SHORT).show();
                    bypass.add(bypass.get(0) - 1);
                    bypass.remove(0);
                }
            }
        });
        ImageButton qrButton = (ImageButton)findViewById(R.id.login_qr_image);
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
            }
        });
    }

    private void startScan() {
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(LoginActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withOnlyQRCodeScanning()
                .withCenterTracker(R.drawable.ic_qrcode_scan_guide, R.drawable.ic_qrcode_scan_success)
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        Toast.makeText(LoginActivity.this, barcode.rawValue, Toast.LENGTH_LONG).show();
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    void setupScreen() {
        // hide ActionBar
        getSupportActionBar().hide();

        // set the background image
        RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.login_layout);
        mainLayout.setBackgroundResource(R.mipmap.background_purple);

        // display a startup dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Welcome to PolyHacks 2018!")
                .setMessage("When you're ready, scan your badge by tapping the " +
                        "QR code icon on the next screen.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // be sure we have the proper permissions...
                        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                                Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    I_CAN_HAS_CAMERA_PLZ);
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }
}
