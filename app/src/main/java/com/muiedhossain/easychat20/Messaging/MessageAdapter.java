package com.muiedhossain.easychat20.Messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.muiedhossain.easychat20.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private ArrayList<Message> messages;
    private String senderImage;
    private String receiverImage;
    private Context context;

    public MessageAdapter(ArrayList<Message> messages, String senderImage, String receiverImage, Context context) {
        this.messages = messages;
        this.senderImage = senderImage;
        this.receiverImage = receiverImage;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_item_holder,parent,false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {

        holder.txtMessage.setText(messages.get(position).getMessageContent());

        ConstraintLayout constraintLayout = holder.ccll;
        if (messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            Glide.with(context).load(senderImage).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profileImage);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profile_cardView,ConstraintSet.LEFT);
            constraintSet.clear(R.id.txt_message_content,ConstraintSet.LEFT);
            constraintSet.connect(R.id.profile_cardView,ConstraintSet.RIGHT,R.id.ccLayout,ConstraintSet.RIGHT,0);//constraint right to rightOf = parrent
            constraintSet.connect(R.id.txt_message_content,ConstraintSet.RIGHT,R.id.profile_cardView,ConstraintSet.LEFT,0);//constraint right to rightOf = profile_cardView
            constraintSet.applyTo(constraintLayout);
        }else {
            //messages.get(position).getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            Glide.with(context).load(receiverImage).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profileImage);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profile_cardView,ConstraintSet.RIGHT);
            constraintSet.clear(R.id.txt_message_content,ConstraintSet.RIGHT);
            constraintSet.connect(R.id.profile_cardView,ConstraintSet.LEFT,R.id.ccLayout,ConstraintSet.LEFT,0);//constraint right to rightOf = parrent
            constraintSet.connect(R.id.txt_message_content,ConstraintSet.LEFT,R.id.profile_cardView,ConstraintSet.RIGHT,0);//constraint right to rightOf = profile_cardView
            constraintSet.applyTo(constraintLayout);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        ConstraintLayout ccll;
        TextView txtMessage;
        ImageView profileImage;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            ccll = itemView.findViewById(R.id.ccLayout);
            txtMessage = itemView.findViewById(R.id.txt_message_content);
            profileImage = itemView.findViewById(R.id.small_profile_image);
        }
    }

}
