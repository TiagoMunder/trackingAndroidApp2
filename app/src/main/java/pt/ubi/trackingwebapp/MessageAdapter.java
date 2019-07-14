package pt.ubi.trackingwebapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {
    private Context mContext;
    private static final String TAG = "EventListAdapter";
    private int mResource;
    private int lastPosition = -1;


    private static class ViewHolder {
        TextView messageBody;
        View avatar;
    }

    public MessageAdapter(Context context, int resource, ArrayList<Message> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String messageBody = getItem(position).getMessageBody();
        String owner = getItem(position).getMessageOwner();
        Boolean sendByUs = getItem(position).getSendByUs();

        Message message = new Message(messageBody, owner, sendByUs);

        final View result;
        ViewHolder holder;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);

            holder= new ViewHolder();
            if(sendByUs) {
                convertView = inflater.inflate(R.layout.my_message,null);
                holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
                holder.messageBody.setText(messageBody);

            } else {
                convertView = inflater.inflate(R.layout.others_messages,null);
                holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
                holder.avatar = (View) convertView.findViewById(R.id.avatar);
                holder.messageBody.setText(messageBody);
                GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
                drawable.setColor(Color.parseColor("#636161"));

            }
            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position> lastPosition) ? R.anim.loading_down_anim : R.anim.loading_up_anim);
        result.startAnimation(animation);
        lastPosition = position;
        if(sendByUs) {
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.messageBody.setText(messageBody);
        } else {
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.messageBody.setText(messageBody);
            holder.avatar = (View) convertView.findViewById(R.id.avatar);
            GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
            drawable.setColor(Color.parseColor("#636161"));

        }



        return convertView;
    }
}
