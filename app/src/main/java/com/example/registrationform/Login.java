package com.example.registrationform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
   EditText EmailAddress,pass,Loginpassword,number;
   Button login,signUp;
   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://registrationform-650d9-default-rtdb.firebaseio.com/");
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailAddress = findViewById(R.id.number);
        Loginpassword = findViewById(R.id.Loginpassword);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.Login);
        signUp = findViewById(R.id.SignUp);
        firebaseAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(new Intent(Login.this,Registration.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final   String cellphone = number.getText().toString();
                final   String logmail = EmailAddress.getText().toString();
                final   String logpass = Loginpassword.getText().toString();

             if (logmail.isEmpty() || logpass.isEmpty()){

                // firebaseAuth.signInWithEmailAndPassword(cellphone,logpass);
                 Toast.makeText(Login.this, "please enter your mail or password",
                         Toast.LENGTH_SHORT).show();


             }
//             else if (!logmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
//                 EmailAddress.requestFocus();
//                 EmailAddress.setError("ENTER VALID EMAIL");
//             }
             else if (logpass.length()<=5){
                 Loginpassword.requestFocus();
                 Loginpassword.setError("MINIMUM 6 CHARACTER REQUIRED");
             }
             else {

                 databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {

                         // check if mobile/phone is exist in firebase database
                         if (snapshot.hasChild(cellphone)){

                             // mobile is exist in firebase database
                             // now get password of user from firebase data and match it with user entered password
                             final String getpassword = snapshot.child(cellphone).child("paswword").getValue(String.class);
                             if (getpassword.equals(pass)){
                                 Toast.makeText(Login.this, "Successfully logged in",
                                         Toast.LENGTH_SHORT).show();
                                 // open main activity
                                 startActivity(new Intent(Login.this,MainActivity.class));
                                 finish();
                             }
                             else {
                                 Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                             }
                         }
                         else {
                             Toast.makeText(Login.this, "Wrong mobile number", Toast.LENGTH_SHORT).show();
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });
             }
            }
        });
    }



}