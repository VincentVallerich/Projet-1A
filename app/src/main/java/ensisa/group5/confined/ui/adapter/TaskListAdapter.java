package ensisa.group5.confined.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import ensisa.group5.confined.R;
import ensisa.group5.confined.ui.model.TaskListItem;

public class TaskListAdapter extends BaseAdapter
{
    private Context context;
    private List<TaskListItem> taskListItem;
    private LayoutInflater inflater;

    public TaskListAdapter(Context context, List<TaskListItem> taskListItem)
    {
        this.context = context;
        this.taskListItem = taskListItem;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return this.taskListItem.size();
    }

    @Override
    public Object getItem(int position){
        return taskListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflater.inflate(R.layout.adapter_tasklist, null);

        TaskListItem currentItem = (TaskListItem) getItem(position);

        // img
        String taskImg = currentItem.getImg();
        ImageView img = convertView.findViewById(R.id.adapter_tasklist_icon);
        int imgId = context.getResources().getIdentifier(taskImg, "drawable", context.getPackageName());
        img.setImageResource(imgId);

        // main text
        String taskName = currentItem.getName();
        TextView name = convertView.findViewById(R.id.adapter_tasklist_name);
        name.setText(taskName);

        // description
        String taskDescription = currentItem.getDescription();
        TextView description = convertView.findViewById(R.id.adapter_tasklist_description);
        description.setText(taskDescription);

        // importance
        int taskImportance = currentItem.getImportance();
        RatingBar importance = convertView.findViewById(R.id.adapter_tasklist_importance);
        importance.setRating(taskImportance);

        // score
        int taskScore = currentItem.getScore();
        RatingBar score = convertView.findViewById(R.id.adapter_tasklist_score);
        score.setRating(taskScore);

        // frequency
        String taskFrequency = currentItem.getFrequency();
        TextView frequency = convertView.findViewById(R.id.adapter_tasklist_frequency);
        frequency.setText(taskFrequency);

        return convertView;
    }
}
