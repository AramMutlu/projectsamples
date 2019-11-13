package com.example.loraapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.loraapp.R;
import com.example.loraapp.global.model.command.NewMessage;
import com.example.loraapp.global.model.message.Alarm;
import com.example.loraapp.interfaces.OnMessageClick;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<NewMessage> newMessages;
    private OnMessageClick onMessageClick;

    public MessageAdapter(List<NewMessage> newMessages) {
        this.newMessages = newMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NewMessage newMessage = newMessages.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm", new Locale("nl", "NL"));

        //TODO: not so important for now
        //holder.vAlarmColor.setBackgroundColor(message.getAlertCode().getColor());
        final Alarm alarm = (Alarm) newMessage.getAlarm();
        Date date = new java.util.Date((long) alarm.getDateTime()*1000);
        holder.tvDate.setText(dateFormat.format(date));
        holder.tvMessage.setText(alarm.getText());
        holder.rlMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create callback
                if (onMessageClick != null) {
                    onMessageClick.onClick(newMessage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newMessages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rlMessage)
        RelativeLayout rlMessage;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvMessage)
        TextView tvMessage;
        @BindView(R.id.alarmColor)
        View vAlarmColor;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    /**
     * This method updates the recyclerview when data is added or removed
     *
     * @param newMessages list with the recent newMessages
     */
    public void refresh(List<NewMessage> newMessages) {
        this.newMessages = newMessages;
        notifyDataSetChanged();
    }

    public void setOnMessageClick(OnMessageClick onMessageClick) {
        this.onMessageClick = onMessageClick;
    }
}
