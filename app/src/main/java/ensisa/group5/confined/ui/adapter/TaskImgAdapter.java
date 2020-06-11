package ensisa.group5.confined.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import ensisa.group5.confined.R;
import ensisa.group5.confined.ui.model.TaskImgItem;

public class TaskImgAdapter extends BaseAdapter
{
    private Context context;
    private List<TaskImgItem> taskImgItem;
    private LayoutInflater inflater;

    private String img;

    public TaskImgAdapter(Context context, List<TaskImgItem> taskImgItem)
    {
        this.context = context;
        this.taskImgItem = taskImgItem;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return taskImgItem.size();
    }

    @Override
    public Object getItem(int position){
        return taskImgItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflater.inflate(R.layout.adapter_taskimg, null);

        TaskImgItem currentItem = (TaskImgItem) getItem(position);
        img = currentItem.getImg();

        // img
        ImageView imgIconView = convertView.findViewById(R.id.adapter_taskimg_icon);
        int imgId = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
        imgIconView.setImageResource(imgId);

        return convertView;
    }
}