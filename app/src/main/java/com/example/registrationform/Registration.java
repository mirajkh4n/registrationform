package com.example.registrationform;

import static com.example.registrationform.R.id.progress_dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Registration extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    EditText name,email,pass,confpass,number;
    Button register;
    RadioButton male,female;
    String gender="";
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.name);
        email= findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        confpass = findViewById(R.id.confpass);
        number = findViewById(R.id.phoneNumber);
        register = findViewById(R.id.register);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
          @SuppressLint("ResourceType")
          @Override
          public void onClick(View view) {

              // get data from edittext into String variables
           final   String fullname = name.getText().toString();
           final  String emailaddress = email.getText().toString();
           final  String password = pass.getText().toString();
           final String cellphone = number.getText().toString();
             final String confirmpass = confpass.getText().toString();

            if (male.isChecked()){
                gender = "Male";
            }
            if (female.isChecked()){
                gender= "Female";
            }
            if (fullname.isEmpty() || emailaddress.isEmpty() || password.isEmpty()|| cellphone.isEmpty()){
                Toast.makeText(Registration.this, "Please fill all fields correctly ",
                        Toast.LENGTH_SHORT).show();

            }
            else if (!password.equals(confirmpass)){
                Toast.makeText(Registration.this, "Password is not matching",
                        Toast.LENGTH_SHORT).show();
            }
            else if (password.length() <5) {
                pass.requestFocus();
                pass.setError("AT LEAST 6 CHARACTER");
            }
            else if (!fullname.matches("[a-zA-Z]+")) {
                name.requestFocus();
                name.setError("ENTER ONLY ALPHABETICAL CHARACTER ");
            }
            else if (!emailaddress.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                email.requestFocus();
                email.setError("INVALID PATTERN ");

            }
            else if (!cellphone.matches("^[+][0-9]{10,13}$")) {
                number.requestFocus();
                number.setError("CORRECT FORMAT: 92xxxx");
            }

            else {
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // check if phone is not register
                        if (snapshot.hasChild(cellphone)){
                            Toast.makeText(Registration.this, "phone is already registered",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            firebaseAuth.createUserWithEmailAndPassword(cellphone,password);
                            // sending data to firebase realtime database
                            databaseReference.child("users").child(cellphone).child("fullname").setValue(fullname);
                            databaseReference.child("users").child(cellphone).child("email").setValue(emailaddress);
                            databaseReference.child("users").child(cellphone).child("password").setValue(password);
                            Toast.makeText(Registration.this, "user registered successfully",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

          // boolean chek = validation (fullName,emailaddress,password,cellphone);
//           if (chek==true){
//               Toast.makeText(getApplicationContext(), "VALID INFORMATION", Toast.LENGTH_SHORT).show();
//           }
//           else {
//               Toast.makeText(getApplicationContext(), "SORRY CHECK INFORMATION AGAIN", Toast.LENGTH_SHORT).show();
//           }
              // initialize progress dialog
              progressDialog = new ProgressDialog(Registration.this);
            // show dialog
            progressDialog.show();
              // set  content view
              progressDialog.setContentView(R.layout.progress_dailog);
              // set transparent background
              progressDialog.getWindow().setBackgroundDrawableResource(
                      android.R.color.transparent
              );
          }
      });
    }

    @Override
    public void onBackPressed() {
        // dismiss progress dialog
        progressDialog.dismiss();
    }
    //    private boolean validation(String fullname, String emailaddress, String password, String cellphone) {
//        if (fullname.length() == 0) {
//            name.requestFocus();
//            name.setError("FILED CANNTO BE EMPTY");
//            return false;
//        }
//
//
//
//
//        else {
//            return true;
//        }
//
//    }
}