package com.sh.nogorcourierdriver.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.nogorcourierdriver.Model.Driver_Model;
import com.sh.nogorcourierdriver.R;

public class Driver_Profile extends AppCompatActivity {

    EditText name,phone;
    TextView cod,salary,pick;
    Button save;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__profile);

        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        save=findViewById(R.id.save);

        pick=findViewById(R.id.pick);
        salary=findViewById(R.id.salary);
        cod=findViewById(R.id.cod);

        firebaseDatabase =FirebaseDatabase.getInstance();


        databaseReference2=firebaseDatabase.getReference("Driver_Profile").child(FirebaseAuth.getInstance().getUid()).child("salary_driver");
        databaseReference=firebaseDatabase.getReference("Driver_Profile").child(FirebaseAuth.getInstance().getUid()).child("profile");
        //databaseReference1=firebaseDatabase.getReference("Driver_Salary").child(FirebaseAuth.getInstance().getUid());


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Driver_Model driver_model = dataSnapshot.getValue(Driver_Model.class);

                    name.setText(driver_model.getName());
                    phone.setText(driver_model.getPhone());

                }else {

                    Toast.makeText(getApplicationContext(),"Enter name & phone",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference1=firebaseDatabase.getReference("Driver_Profile").child(FirebaseAuth.getInstance().getUid()).child("amount");
        //databaseReference1=firebaseDatabase.getReference("Driver_Salary").child(FirebaseAuth.getInstance().getUid());


        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){


                    cod.setText("Cod: "+dataSnapshot.child("cod").getValue(String.class));
                    pick.setText("Pick: "+dataSnapshot.child("pick").getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


databaseReference2.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        salary.setText("Salary: "+dataSnapshot.child("salary").getValue(String.class));
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});



         save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Driver_Model driver_model =new Driver_Model(name.getText().toString(),phone.getText().toString(),FirebaseAuth.getInstance().getUid());

                databaseReference.setValue(driver_model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
                    }
                });


            }
        });






    }
}
