package lockdown.org.automessagingapp;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
public class DisplayActivity extends AppCompatActivity
{
    ListView lvShow;
    SQLiteDatabase db;
    ArrayList al = new ArrayList();
    protected void onCreate(Bundle b)
    {
        super.onCreate(b);
        setContentView(R.layout.activity_display);
        lvShow = findViewById(R.id.lvShow);
        db = openOrCreateDatabase("messagedb",MODE_PRIVATE,null);
        load();
        lvShow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                String data = al.get(position).toString();
                String arr[] = data.split("\n");
                final String imessage = arr[0];
                AlertDialog.Builder ab = new AlertDialog.Builder(DisplayActivity.this);
                ab.setTitle("Action");
                ab.setMessage("Do you want to delete ?");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try
                        {
                            db.execSQL("delete from messages where incomingmessage='"+imessage+"'");
                            al.clear();
                            load();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(DisplayActivity.this,"Error : "+e,Toast.LENGTH_LONG).show();
                        }
                    }
                });
                ab.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                ab.show();
                return false;
            }
        });
    }
    public void load()
    {
        Cursor cur = db.rawQuery("select * from messages", null);
        while(cur.moveToNext())
        {
            String incomingmessage = cur.getString(0);
            String outgoingmessage = cur.getString(1);
            al.add(incomingmessage+"\n"+outgoingmessage);
        }
        cur.close();
        ArrayAdapter aa = new ArrayAdapter(DisplayActivity.this,android.R.layout.simple_list_item_1,al);
        lvShow.setAdapter(aa);
    }
}