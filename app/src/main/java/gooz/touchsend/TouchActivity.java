package gooz.touchsend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TouchActivity extends AppCompatActivity {

    TextView status;

    private Socket socket;

    private PrintWriter mPrintWriter;

    Handler updateConversationHandler;

    Thread serverThread = null;

    public static String SERVER_IP;

    public static int SERVER_PORT;

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        StrictMode.setThreadPolicy(policy);

        status = findViewById(R.id.textview_status);

        // Get the Intent that started this activity and extract the IP and Port
        Intent intent = getIntent();
        SERVER_IP = intent.getStringExtra(MainActivity.EXTRA_IP);
        SERVER_PORT = intent.getIntExtra(MainActivity.EXTRA_PORT, 0);

        View tc = findViewById(R.id.touch_surface);
        tc.setOnTouchListener(handleTouch);

        status.setText("Connecting");
        new Thread(new ClientThread()).start();

    }

    private void sendCoordinates(int x, int y) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(x);
            out.println(y);
            out.println(",");
            status.setText("Sent: " + "x: " + x + ", y: " + y);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            status.setText("Failed to send");
        } catch (IOException e) {
            e.printStackTrace();
            status.setText("Failed to send");
        } catch (Exception e) {
            e.printStackTrace();
            status.setText("Failed to send");
        }
    }


    class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddress, SERVER_PORT);
                mPrintWriter = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);

                TouchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("Connected");
                    }
                });
            } catch (UnknownHostException e) {
                e.printStackTrace();

                TouchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("Failed to connect (Unknown Host)");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();

                TouchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("Failed to connect (IO Exception)");
                    }
                });
            }

        }
    }


    private View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

//            Log.i("Touch Send", "x: " + x + ", y: " + y);

            sendCoordinates(x, y);

            return true;
        }
    };
}
