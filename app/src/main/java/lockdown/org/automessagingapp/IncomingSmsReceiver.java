package lockdown.org.automessagingapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.SmsManager;
import java.util.HashMap;

public class IncomingSmsReceiver extends BroadcastReceiver
{
    SQLiteDatabase db;
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        Object[] pdusObj = (Object[])bundle.get("pdus");
        SmsMessage sm = SmsMessage.createFromPdu((byte[])pdusObj[0]);
        String sendermobileno = sm.getDisplayOriginatingAddress();
        String message = sm.getDisplayMessageBody();
        HashMap hm = new HashMap();
        db = context.openOrCreateDatabase("messagedb",Context.MODE_PRIVATE,null);
        Cursor cur = db.rawQuery("select * from messages",null);
        while(cur.moveToNext())
        {
            hm.put(cur.getString(0),cur.getString(1));
        }
        if(hm.containsKey(message))
        {
            String omessage = hm.get(message).toString();
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(sendermobileno,null,omessage,null,null);
        }
    }
}
