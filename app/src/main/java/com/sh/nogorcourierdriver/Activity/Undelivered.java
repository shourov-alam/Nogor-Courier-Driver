package com.sh.nogorcourierdriver.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sh.nogorcourierdriver.Model.Post_Model;
import com.sh.nogorcourierdriver.R;
import com.sh.nogorcourierdriver.Adapter.ViewHolderPost;

public class Undelivered extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager llm;
    public FirebaseRecyclerAdapter<Post_Model, ViewHolderPost> firebaseRecyclerAdapter;
    public FirebaseRecyclerOptions<Post_Model> options; // seraching in the profile ;
    DatabaseReference ref;
    SwipeRefreshLayout sp;
    boolean test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undelivered);
        recyclerView = findViewById(R.id.myList);
        sp=findViewById(R.id.swipe);

        ref = FirebaseDatabase.getInstance().getReference("Posts");




        llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);



        if (FirebaseAuth.getInstance().getCurrentUser() !=null) {

            loadData();


        }else {


            Toast.makeText(getApplicationContext(),"User Not  Found",Toast.LENGTH_LONG).show();
        }
        sp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                test=false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sp.setRefreshing(false);
                        if(!test) {
                            Toast.makeText(getApplicationContext(), "Check internet connection & try again", Toast.LENGTH_LONG).show();
                        }

                    }
                }, 12000);
            }
        });




    }


    @Override
    public void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            firebaseRecyclerAdapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            firebaseRecyclerAdapter.startListening();
        }
    }


    public void loadData() {

        //  String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query fireBaseQusery  = ref.orderByChild("delivery_selection").equalTo(FirebaseAuth.getInstance().getUid()) ;

        fireBaseQusery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){

                    Toast.makeText(getApplicationContext(),"No order is given yet",Toast.LENGTH_LONG).show();

                    sp.setRefreshing(false);
                    test=true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        options = new FirebaseRecyclerOptions.Builder<Post_Model>().setQuery(fireBaseQusery , Post_Model.class).build() ;

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post_Model, ViewHolderPost>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderPost viewHolderPost, final  int i, @NonNull final Post_Model post_model) {


                // setThe data to the row
                //        String imageLink , String itemdes ,String name  ,String quatitys  ,String  category ;


                //     String name , String quantity , String mail , String returnDate , String stats ;
                viewHolderPost.setDetails(post_model.getCon_now(),post_model.getType(),post_model.getBdt(),post_model.getWeight(),post_model.getDate2(),
                        post_model.getPost_type() ,
                        post_model.getArea(),post_model.getAddress(),post_model.getD_area(),

                        post_model.getD_address(),post_model.getPhone(),post_model.getCondition_amount(),post_model.getPid(),
                        post_model.getInstruction(),post_model.getR_name(),post_model.getR_phone(),post_model.getName()


                );

                test=true;

                sp.setRefreshing(false);

                viewHolderPost.back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ref.child(getRef(i).getKey()).child("driver_check1").setValue(FirebaseAuth.getInstance().getUid());
                        ref.child(getRef(i).getKey()).child("delivery_selection").setValue("null");

                        ref.child(getRef(i).getKey()).child("con_now")
                                .setValue("Your Product has returned from receiver");



                        FirebaseDatabase.getInstance().getReference("Driver_Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                                String b1=dataSnapshot.child("salary_driver").child("salary").getValue(String.class);

                                int jjj= Integer.valueOf(post_model.getBdt())*60;

                                int jjj2= jjj/100;


                                int f2=Integer.valueOf(b1)+jjj2;

                                FirebaseDatabase.getInstance().getReference("Driver_Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("salary_driver").
                                        child("salary").setValue(String.valueOf(f2));


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });


                viewHolderPost.done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ref.child(getRef(i).getKey()).child("driver_check1").setValue(FirebaseAuth.getInstance().getUid());
                        ref.child(getRef(i).getKey()).child("delivery_selection").setValue("null");

                        ref.child(getRef(i).getKey()).child("con_now")
                                .setValue("Your Product has delivered Successfully");



                        FirebaseDatabase.getInstance().getReference("Driver_Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                String b=dataSnapshot.child("amount").child("cod").getValue(String.class);



                                int f1=Integer.valueOf(b)+Integer.valueOf(post_model.getCondition_amount());

                                FirebaseDatabase.getInstance().getReference("Driver_Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("amount").
                                        child("cod").setValue(String.valueOf(f1));


                                String b1=dataSnapshot.child("salary_driver").child("salary").getValue(String.class);

                                int jjj= Integer.valueOf(post_model.getBdt())*60;

                                int jjj2= jjj/100;


                                int f2=Integer.valueOf(b1)+jjj2;

                                FirebaseDatabase.getInstance().getReference("Driver_Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("salary_driver").
                                        child("salary").setValue(String.valueOf(f2));


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                    }
                });

                viewHolderPost.call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        String p = "tel:" + post_model.getPhone();
                        i.setData(Uri.parse(p));
                        startActivity(i);



                    }
                });
                viewHolderPost.call1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(Intent.ACTION_DIAL);
                        String p = "tel:" + post_model.getR_phone();
                        i.setData(Uri.parse(p));
                        startActivity(i);


                    }
                });
               /* viewHolderPost.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        ref.child(getRef(i).getKey()).removeValue();

                    }
                });

                viewholderForItemList.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        LayoutInflater layoutInflater = LayoutInflater.from(User_Posts.this);

                        final View view = layoutInflater.inflate(R.layout.add_post, null);
                        final EditText title=view.findViewById(R.id.title);
                        final EditText des=view.findViewById(R.id.des);

                        title.setText(post_model.getTitle());
                        des.setText(post_model.getDes());
                        new AlertDialog.Builder(User_Posts.this)


                                .setCancelable(true)
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {







                                        ref.child(getRef(i).getKey()).child("title").setValue(title.getText().toString());
                                        ref.child(getRef(i).getKey()).child("des").setValue(des.getText().toString());


                                    }
                                }).setView(view).show();




                    }
                });
*/











            }





            @NonNull
            @Override
            public ViewHolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_post_item, parent, false);
                ViewHolderPost viewHolder = new ViewHolderPost(itemView);

                return viewHolder;
            }
        } ;

        recyclerView.setLayoutManager(llm);

        firebaseRecyclerAdapter.startListening();

        //setting adapter

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


}

