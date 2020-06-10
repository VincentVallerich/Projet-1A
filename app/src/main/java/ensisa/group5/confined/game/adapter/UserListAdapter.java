package ensisa.group5.confined.game.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import ensisa.group5.confined.R;
import ensisa.group5.confined.game.model.UserListItem;
import ensisa.group5.confined.ui.model.TaskListItem;

public class UserListAdapter extends BaseAdapter
{
    private Context context;
    private List<UserListItem> userListItem;
    //private LayoutInflater inflater;


    public UserListAdapter(Context context, List<UserListItem> userListItem)
    {
        this(context, userListItem, false, false);
    }

    public UserListAdapter(Context context, List<UserListItem> userListItem, boolean longClick)
    {
        this(context, userListItem, longClick, false);
    }

    public UserListAdapter(Context context, List<UserListItem> userListItem, boolean longClick, boolean showCheckBoxOnClick)
    {
        this.context = context;
        this.userListItem = userListItem;
        //this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.userListItem.size();
    }

    @Override
    public Object getItem(int position){
        return userListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.adapter_userlist, null);
        UserListItem currentItem = (UserListItem) getItem(position);
        // main text
        String userPseudo = currentItem.getPseudo();
        TextView pseudo = convertView.findViewById(R.id.adapter_userlist_pseudo);
        pseudo.setText(userPseudo);
        // description
        int userScore = currentItem.getScore();
        TextView description = convertView.findViewById(R.id.adapter_userlist_score);
        description.setText(String.valueOf(userScore));
        return convertView;
    }
}
