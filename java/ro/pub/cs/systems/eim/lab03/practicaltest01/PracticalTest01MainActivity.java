package ro.pub.cs.systems.eim.lab03.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    Button press;
    Button meToo;
    ButtonClickListener listener;
    TextView pressText;
    TextView meTooText;
    Button navigate;
    int result = 0;
    Intent serviceIntent;
    TextView messageTextView;
    StartedServiceBroadcastReceiver startedServiceBroadcastReceiver;
    IntentFilter startedServiceIntentFilter;

    private class Constants {
        private static final String pressMe = "Press me button";
        private static final String pressMeToo = "Press me too button";
    }

    private class StartedServiceBroadcastReceiver extends BroadcastReceiver {

        private TextView messageTextView;

        // TODO: exercise 9 - default constructor
        public StartedServiceBroadcastReceiver() {
            super();
        }

        public StartedServiceBroadcastReceiver(TextView messageTextView) {
            this.messageTextView = messageTextView;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: exercise 7 - get the action and the extra information from the intent
            // and set the text on the messageTextView
            String action = intent.getAction();
            String data = null;

            if (action.equals("ro.pub.cs.systems.eim.lab03.practicaltest01.string") || action.equals("ro.pub.cs.systems.eim.lab03.practicaltest01.integer") || action.equals("ro.pub.cs.systems.eim.lab03.practicaltest01.boolean")) {
                data = intent.getStringExtra("ro.pub.cs.systems.eim.lab03.practicaltest01.data");
            }/* else if (action.equals("ro.pub.cs.systems.eim.lab03.practicaltest01.integer")) {
                //data = Integer.toString(intent.getIntExtra("ro.pub.cs.systems.eim.lab03.practicaltest01.data", 0));
            } else if (action.equals("ro.pub.cs.systems.eim.lab03.practicaltest01.boolean")) {
                data = Boolean.toString(intent.getBooleanExtra("ro.pub.cs.systems.eim.lab03.practicaltest01.data", false));
            }*/

            if (messageTextView != null)
                messageTextView.setText(messageTextView.getText().toString() + "\n" + data);

                // TODO: exercise 9 - restart the activity through an intent
                // if the messageTextView is not available
            /*else {
                Intent startedServiceActivityIntent = new Intent(context, StartedServiceActivity.class);
                startedServiceActivityIntent.putExtra(Constants.MESSAGE, data);
                startedServiceActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(startedServiceActivityIntent);
            }*/
        }
    }

    @Override
    public void onDestroy() {
        stopService(serviceIntent);
        super.onDestroy();
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            Button button = (Button)view;
            Integer value;

            switch (button.getId()) {
                case R.id.buttonPress:
                    value = Integer.parseInt(pressText.getText().toString()) + 1;
                    pressText.setText(value.toString());
                    result++;
                    break;
                case R.id.buttonMeToo:
                    value = Integer.parseInt(meTooText.getText().toString()) + 1;
                    meTooText.setText(value.toString());
                    result++;
                    break;
                case R.id.Navigate:
                    Intent intent = new Intent("ro.pub.cs.systems.eim.lab03.practicaltest01.PracticalTest01SecondaryActivity");
                    //result = Integer.parseInt(pressText.getText().toString()) + Integer.parseInt(meTooText.getText().toString());
                    intent.putExtra("Number of 'press me' button presses", Integer.parseInt(pressText.getText().toString()));
                    intent.putExtra("Number of 'press me, too' button presses", Integer.parseInt(meTooText.getText().toString()));
                    startActivityForResult(intent, result);
                    break;
                default:
                    break;
            }

            if (result == 5) {
                serviceIntent = new Intent();
                serviceIntent.putExtra("Number of 'press me' button presses", Integer.parseInt(pressText.getText().toString()));
                serviceIntent.putExtra("Number of 'press me, too' button presses", Integer.parseInt(meTooText.getText().toString()));
                serviceIntent.setComponent(new ComponentName("ro.pub.cs.systems.eim.lab03.practicaltest01", "ro.pub.cs.systems.eim.lab03.practicaltest01.PracticalTest01Service"));
                startService(serviceIntent);
            }
            else if (result > 5) {
                serviceIntent.putExtra("Number of 'press me' button presses", Integer.parseInt(pressText.getText().toString()));
                serviceIntent.putExtra("Number of 'press me, too' button presses", Integer.parseInt(meTooText.getText().toString()));
            }
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        press = findViewById(R.id.buttonPress);
        meToo = findViewById(R.id.buttonMeToo);
        listener = new ButtonClickListener();
        pressText = findViewById(R.id.PressMe);
        meTooText = findViewById(R.id.MeToo);
        navigate = findViewById(R.id.Navigate);
        messageTextView = findViewById(R.id.broadcast);

        press.setOnClickListener(listener);
        meToo.setOnClickListener(listener);
        navigate.setOnClickListener(listener);

        if ((savedInstanceState != null) && (savedInstanceState.getString(Constants.pressMe) != null)) {
            pressText.setText(savedInstanceState.getString(Constants.pressMe));
        }

        if ((savedInstanceState != null) && (savedInstanceState.getString(Constants.pressMeToo) != null)) {
            meTooText.setText(savedInstanceState.getString(Constants.pressMeToo));
        }

        // TODO: exercise 8a - create an instance of the StartedServiceBroadcastReceiver broadcast receiver
        startedServiceBroadcastReceiver = new StartedServiceBroadcastReceiver(messageTextView);

        // TODO: exercise 8b - create an instance of an IntentFilter
        // with all available actions contained within the broadcast intents sent by the service
        startedServiceIntentFilter = new IntentFilter();
        startedServiceIntentFilter.addAction("ro.pub.cs.systems.eim.lab03.practicaltest01.string");
        startedServiceIntentFilter.addAction("ro.pub.cs.systems.eim.lab03.practicaltest01.integer");
        startedServiceIntentFilter.addAction("ro.pub.cs.systems.eim.lab03.practicaltest01.boolean");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TODO: exercise 8c - register the broadcast receiver with the corresponding intent filter
        registerReceiver(startedServiceBroadcastReceiver, startedServiceIntentFilter);
    }

    @Override
    protected void onPause() {
        // TODO: exercise 8c - unregister the broadcast receiver
        unregisterReceiver(startedServiceBroadcastReceiver);

        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //EditText usernameEditText = (EditText)findViewById(R.id.username_edit_text);
        savedInstanceState.putString(Constants.pressMe, pressText.getText().toString());
        savedInstanceState.putString(Constants.pressMeToo, meTooText.getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == result) {
                Toast.makeText(getApplicationContext(), ((Integer)resultCode).toString(), Toast.LENGTH_LONG).show();
        }
    }
}
