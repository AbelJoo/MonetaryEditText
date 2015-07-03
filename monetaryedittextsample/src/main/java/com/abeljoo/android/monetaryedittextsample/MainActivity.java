package com.abeljoo.android.monetaryedittextsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abeljoo.android.monetaryedittext.MonetaryEditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MonetaryEditText edit = (MonetaryEditText) findViewById(R.id.editText);
        edit.setOnAbnormalListener(new MonetaryEditText.OnAbnormalListener() {
            @Override
            public void onAbnormal(MonetaryEditText.AbnormalType abnType) {
                switch (abnType) {
                    case TooLarge:
                        Toast.makeText(MainActivity.this, "TooLarge", Toast.LENGTH_SHORT).show();
                        break;
                    case TooSmall:
                        Toast.makeText(MainActivity.this, "TooSmall", Toast.LENGTH_SHORT).show();
                        break;
                    case TooManyDecimal:
                        Toast.makeText(MainActivity.this, "TooManyDecimal", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
