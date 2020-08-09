package lockdown.org.automessagingapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    EditText etIncomingText, etOutgoingText;
    Button btnSave, btnView;
    SQLiteDatabase db;
    protected void onCreate(Bundle b)
    {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.RECEIVE_SMS") != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{"android.permission.RECEIVE_SMS","android.permission.SEND_SMS"},1);
            }
        }
        db = openOrCreateDatabase("messagedb",MODE_PRIVATE,null);
        db.execSQL("Create table if not exists messages (incomingmessage varchar(100),outgoingmessage varchar(100))");
        etIncomingText = findViewById(R.id.etIncomingText);
        etOutgoingText = findViewById(R.id.etOutgoingText);
        btnSave = findViewById(R.id.btnSave);
        btnView = findViewById(R.id.btnView);
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String incomingmessage = etIncomingText.getText().toString();
                String outgoingmessage = etOutgoingText.getText().toString();
                if(incomingmessage.length()==0)
                {
                    Toast.makeText(MainActivity.this, "Enter incoming message", Toast.LENGTH_SHORT).show();
                }
                else if (outgoingmessage.length()==0)
                {
                    Toast.makeText(MainActivity.this, "Enter outgoing message", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try
                    {
                        db.execSQL("insert into messages(incomingmessage,outgoingmessage) values('"+incomingmessage+"','"+outgoingmessage+"')");
                        Toast.makeText(MainActivity.this, "Message inserted", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(MainActivity.this, "Error : "+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent i=new Intent(MainActivity.this,DisplayActivity.class);
                startActivity(i);
            }
        });
    }
}