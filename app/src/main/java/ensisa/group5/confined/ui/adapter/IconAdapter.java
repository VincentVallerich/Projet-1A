package ensisa.group5.confined.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import java.util.List;

import ensisa.group5.confined.R;
import ensisa.group5.confined.ui.model.IconImgItem;

public class IconAdapter extends BaseAdapter
{
    private Context context;
    private List<IconImgItem> iconImgItem;
    private LayoutInflater inflater;
    private String img;

    public IconAdapter(Context context,List<IconImgItem> iconImgItem)
    {
        this.context = context;
        this.iconImgItem = iconImgItem;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return iconImgItem.size();
    }

    @Override
    public Object getItem(int position){
        return iconImgItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflater.inflate(R.layout.adapter_profilicon, null);

        IconImgItem currentItem = (IconImgItem) getItem(position);
        img = currentItem.getImg();

        // img
        ImageView imgIconView = convertView.findViewById(R.id.adapter_profilimg_icon);
        int imgId = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
        imgIconView.setImageResource(imgId);

        return convertView;
    }
}
