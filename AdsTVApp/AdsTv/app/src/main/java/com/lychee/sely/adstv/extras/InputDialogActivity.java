package com.lychee.sely.adstv.extras;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import com.lychee.sely.adstv.R;

public class InputDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_dialog);

        final String title = getIntent().getStringExtra("TITLE");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent resultData = new Intent();
                resultData.putExtra("INPUT_VALUE", input.getText().toString().trim());
                setResult(Activity.RESULT_OK, resultData);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent resultData = new Intent();
                resultData.putExtra("INPUT_VALUE", "");
                setResult(Activity.RESULT_OK, resultData);
                finish();

            }
        });

        builder.show();
    }
}
