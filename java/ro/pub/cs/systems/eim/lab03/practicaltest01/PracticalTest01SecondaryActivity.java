package ro.pub.cs.systems.eim.lab03.practicaltest01;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PracticalTest01SecondaryActivity extends AppCompatActivity {

    TextView buttonPresses;
    Button ok;
    Button cancel;
    ButtonClickListener listener;

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Button button = (Button)view;

            switch (button.getId()) {
                case R.id.Ok:
                    //startActivityForResult(intent, "android.intent.action.MAIN");
                    setResult(Integer.parseInt(buttonPresses.getText().toString()), new Intent());
                    finish();
                    break;
                case R.id.Cancel:
                    setResult(Activity.RESULT_CANCELED, new Intent());
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_secondary);

        buttonPresses = findViewById(R.id.NumberPressed);
        ok = findViewById(R.id.Ok);
        cancel = findViewById(R.id.Cancel);
        listener = new ButtonClickListener();

        ok.setOnClickListener(listener);
        cancel.setOnClickListener(listener);

        Intent intent = getIntent();
        if (intent != null) {
            Integer value = intent.getIntExtra("Number of button presses", 0);
            buttonPresses.setText(value.toString());
        }
    }
}
