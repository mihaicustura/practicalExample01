package ro.pub.cs.systems.eim.lab03.practicaltest01;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.sql.Timestamp;

public class PracticalTest01Service extends Service {
    public PracticalTest01Service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.d(Constants.TAG, "onCreate() method was invoked");
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Log.d(Constants.TAG, "onBind() method was invoked");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //Log.d(Constants.TAG, "onUnbind() method was invoked");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        //Log.d(Constants.TAG, "onRebind() method was invoked");
    }

    @Override
    public void onDestroy() {
        //Log.d(Constants.TAG, "onDestroy() method was invoked");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        //Log.d(Constants.TAG, "onStartCommand() method was invoked");

        class ProcessingThread extends Thread {

            private Context context;

            public ProcessingThread(Context context) {
                this.context = context;
            }

            @Override
            public void run() {
                while (true) {
                    //Log.d(Constants.TAG, "Thread.run() was invoked, PID: " + android.os.Process.myPid() + " TID: " + android.os.Process.myTid());

                    int action = (int)Math.random() % 3;
                    sendMessage(action);
                    sleep();

                    /*sendMessage(Constants.MESSAGE_INTEGER);
                    sleep();

                    sendMessage(Constants.MESSAGE_ARRAY_LIST);
                    sleep();*/
                }
            }

            private void sleep() {
                try {
                    Thread.sleep(10000);
                }
                catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }

            private void sendMessage(int messageType) {
                Intent sendIntent = new Intent();
                int pressNum = intent.getIntExtra("Number of 'press me' button presses", -1);
                int meTooNum = intent.getIntExtra("Number of 'press me, too' button presses", -1);
                double ma = (pressNum + meTooNum) / 2;
                double mg = Math.sqrt(pressNum * meTooNum);
                String message = "[" + new Timestamp(System.currentTimeMillis()) + "] " + ma + " " + mg + "\n";
                switch (messageType) {
                    case 0:
                        sendIntent.setAction("ro.pub.cs.systems.eim.lab03.practicaltest01.string");
                        sendIntent.putExtra("ro.pub.cs.systems.eim.lab03.practicaltest01.data", message);
                        break;
                    case 1:
                        sendIntent.setAction("ro.pub.cs.systems.eim.lab03.practicaltest01.integer");
                        sendIntent.putExtra("ro.pub.cs.systems.eim.lab03.practicaltest01.data", message);
                        break;
                    case 2:
                        sendIntent.setAction("ro.pub.cs.systems.eim.lab03.practicaltest01.boolean");
                        sendIntent.putExtra("ro.pub.cs.systems.eim.lab03.practicaltest01.data", message);
                        break;
                }
                context.sendBroadcast(sendIntent);
            }
        }

        ProcessingThread thread = new ProcessingThread(this);
        thread.start();

        return START_REDELIVER_INTENT;
    }
}
