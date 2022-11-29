package com.muiedhossain.easychat20.Messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muiedhossain.easychat20.ChatList.User;
import com.muiedhossain.easychat20.R;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMessage;
    private EditText edtMessageInput;
    private TextView txtChattingWith;
    private ProgressBar progressBar;
    private ImageView imgToolBar , sendMessage;

    private MessageAdapter messageAdapter;

    private ArrayList<Message> messages;


    String usernameOfRoommate , emailOfRoommate, chatRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        usernameOfRoommate = getIntent().getStringExtra("username_of_roommate");
        emailOfRoommate = getIntent().getStringExtra("email_of_roommate ");

        recyclerViewMessage = findViewById(R.id.recyclerMessages);
        edtMessageInput = findViewById(R.id.edtInputText);
        txtChattingWith = findViewById(R.id.txtChattingWith);
        progressBar = findViewById(R.id.progressMessages);
        imgToolBar = findViewById(R.id.image_toolbar);
        sendMessage = findViewById(R.id.img_send_message);

        txtChattingWith.setText(usernameOfRoommate);

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages,getIntent().getStringExtra("my_img"),getIntent().getStringExtra("img_of_roommate"),MessageActivity.this);

        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessage.setAdapter(messageAdapter);

        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("img_of_roommate")).placeholder(R.drawable.account_img).error(R.drawable.account_img).into(imgToolBar);
        setUpChatRoom();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edtMessageInput.getText().toString().equals(""))
                {
                   sendMessage.isHovered();
                }else{
                    FirebaseDatabase.getInstance().getReference("messages/"+ chatRoomId).push()
                            .setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail()
                                    ,emailOfRoommate,edtMessageInput.getText().toString()));
                    edtMessageInput.setText("");
                }


            }
        });


    }

    private void setUpChatRoom(){
        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUsername = snapshot.getValue(User.class).getUsername();

                if (usernameOfRoommate.compareTo(myUsername) > 0){
                    //Himu compare to marjan = tahole(himu 1st e msg dise) chatroom id = Himumarjan
                  chatRoomId = myUsername + usernameOfRoommate;
                }else if (usernameOfRoommate.compareTo(myUsername) == 0)
                {
                    ///Himu to another Himu = "H" to 2nd person's 1st latter, "i" to 2nd person's 2nd latter........
                    chatRoomId = myUsername + usernameOfRoommate;
                }else {
                    //marjan compare to Himu = tahole(marjan 1st e msg dise) chatroom id = marjanHimu
                    chatRoomId = usernameOfRoommate + myUsername;
                }
                attachMessageListener(chatRoomId);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void attachMessageListener(String chatRoomId){
        FirebaseDatabase.getInstance().getReference("messages/"+chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    messages.add(dataSnapshot.getValue(Message.class));
                }
                messageAdapter.notifyDataSetChanged();
                recyclerViewMessage.scrollToPosition(messages.size()-1);
                recyclerViewMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
/////problem 105,24,95
