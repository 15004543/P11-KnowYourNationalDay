package sg.edu.rp.c347.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> al;
    ArrayAdapter<String> aa;
    boolean save = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView)findViewById(R.id.lv);
        al = new ArrayList<>();
        al.add("Singapore National Day is on 9 Aug");
        al.add("Singapore is 52 Years old");
        al.add("Theme is '#OneNationTogether'");
        aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")
                    .setCancelable(false)
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "See You Again!",
                                    Toast.LENGTH_LONG).show();
                            SharedPreferences preferences = getSharedPreferences("saved", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("data", false);
                            editor.apply();
                            finish();
                        }
                    })
                    .setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "Goodluck Have Fun!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (item.getItemId() == R.id.action_send) {
            String [] list = new String[] { "Email", "SMS" };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    .setItems(list, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String message = "";
                            for(int i = 0; i < al.size(); i++){
                                message += (i+1) + ". " + al.get(i) + "\n";
                            }
                            if (which == 0) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL,
                                        new String[]{"15004543@rp.edu.sg"});
                                email.putExtra(Intent.EXTRA_SUBJECT,
                                        "-");
                                email.putExtra(Intent.EXTRA_TEXT,
                                        "Hi Marcus, \n" + message);
                                email.setType("message/rfc822");
                                startActivity(Intent.createChooser(email,
                                        "Choose an Email client :"));
                                Toast.makeText(MainActivity.this, "Email has been sent",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                try{
                                    SmsManager.getDefault().sendTextMessage("+6597289596", null,
                                            message, null, null);
                                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                    sendIntent.setData(Uri.parse("sms:+6597289596"));

                                    sendIntent.putExtra("sms_body", "");
                                    startActivity(sendIntent);
                                    Toast.makeText(MainActivity.this, "SMS has been sent",
                                            Toast.LENGTH_LONG).show();
                                } catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "SMS has not been sent",
                                            Toast.LENGTH_LONG).show();
                                }



                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if(item.getItemId() == R.id.action_quiz){
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout quiz =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);
            final RadioGroup rg1 = (RadioGroup)quiz.findViewById(R.id.rg1);
            final RadioGroup rg2 = (RadioGroup)quiz.findViewById(R.id.rg2);
            final RadioGroup rg3 = (RadioGroup)quiz.findViewById(R.id.rg3);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test Yourself!")
                    .setView(quiz)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String result = "";
                            int selectedButtonId1 = rg1.getCheckedRadioButtonId();
                            int selectedButtonId2 = rg2.getCheckedRadioButtonId();
                            int selectedButtonId3 = rg3.getCheckedRadioButtonId();
                            if(selectedButtonId1 == R.id.radioButton){
                                result += "Answer 1: Wrong\n";
                            }
                            else{
                                result += "Answer 1: Correct\n";
                            }
                            if(selectedButtonId2 == R.id.radioButton3){
                                result += "Answer 2: Correct\n";
                            }
                            else{
                                result += "Answer 2: Wrong\n";
                            }
                            if(selectedButtonId3 == R.id.radioButton5){
                                result += "Answer 3: Correct\n";
                            }
                            else{
                                result += "Answer 3: Wrong\n";
                            }
                            Toast.makeText(MainActivity.this, result,
                                    Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Don't Know Lah", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "Buck Up Pls!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("saved", MODE_PRIVATE);
        save = preferences.getBoolean("data", false);
        Log.i("Saved Resume", save + "");
        if(!save) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.accesscode, null);
            final EditText etPassphrase = (EditText) passPhrase
                    .findViewById(R.id.editTextPassPhrase);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Login")
                    .setView(passPhrase)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (!etPassphrase.getText().toString().equals("738964")) {
                                Toast.makeText(MainActivity.this, "Wrong Access Code!",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                SharedPreferences preferences = getSharedPreferences("saved", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("data", true);
                                editor.apply();
                            }
                        }
                    })
                    .setNegativeButton("No Access Code", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "Get the Access Code!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        SharedPreferences preferences = getSharedPreferences("saved", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean("data", true);
//        editor.commit();
//    }
}
