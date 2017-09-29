package com.hanbit.kakaotalk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final Context context = Login.this;
        final EditText inputId = (EditText) findViewById(R.id.inputId);
        final EditText inputPass = (EditText) findViewById(R.id.inputPass);
        final MemberLogin login = new MemberLogin(context);
        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = String.valueOf(inputId.getText());
                final String pass= String.valueOf(inputPass.getText());

                Toast.makeText(context,"입력된 ID: "+id, Toast.LENGTH_LONG).show();
                Log.d("입력된 Id:",id);
                Log.d("입력된 Pass:",pass);
                new Service.IPredicate() {
                    @Override
                    public void execute() {
                        if(login.execute(id,pass)){
                            startActivity(new Intent(context,MemberList.class));
                        }else{
                            startActivity(new Intent(context,Login.class));
                        };
                    }
                }.execute();
            }
        });
        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputId.setText("");
                inputPass.setText("");
            }
        });
    }
    private abstract class LoginQuery extends Index.QueryFactory{
    SQLiteOpenHelper helper;
        public LoginQuery(Context context) {
            super(context);
            helper = new Index.SqLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class MemberLogin extends LoginQuery{

        public MemberLogin(Context context) {
            super(context);
        }
        public boolean execute(String id, String pass){
            return super.getDatabase().rawQuery(String.format(" SELECT * FROM %s WHERE %s LIKE '%s' AND %s LIKE '%s'",Cons.MEM_TBL,Cons.SEQ,id,Cons.PASS,pass),null).moveToNext();
        }
    }
}


