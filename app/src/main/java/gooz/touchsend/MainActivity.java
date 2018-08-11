package gooz.touchsend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_IP = "ext_ip";

    public static final String EXTRA_PORT = "ext_port";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connect(View view) {
        EditText editTextIp = (EditText) findViewById(R.id.text_ip);
        EditText editTextPort = (EditText) findViewById(R.id.text_port);

        String ip = editTextIp.getText().toString();
        int port = 0;
        try {
            port = Integer.parseInt(editTextPort.getText().toString());
        } catch (NumberFormatException e) {
            // TODO error

            Log.e("Touch Send", "port is not correct");
        }

        if (ip != null && port != 0) {
            Intent intent = new Intent(this, TouchActivity.class);
            intent.putExtra(EXTRA_IP, ip);
            intent.putExtra(EXTRA_PORT, port);
            startActivity(intent);
        } else {
            Log.e("Touch Send", "null properties");
        }

    }
}
