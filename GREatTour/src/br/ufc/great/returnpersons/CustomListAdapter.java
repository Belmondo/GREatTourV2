package br.ufc.great.returnpersons;





import br.ufc.great.greattour.R;
import br.ufc.great.returnpersons.AppController;
import br.ufc.great.returnpersons.Pesquisador;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;

    private List<Pesquisador> pesquisadorItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Pesquisador> pesquisadorItems) {
        this.activity = activity;

        this.pesquisadorItems = pesquisadorItems;
    }

    @Override
    public int getCount() {

        return pesquisadorItems.size();
    }

    @Override
    public Object getItem(int location) {

        return pesquisadorItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.Nome);


        TextView Titulacao = (TextView) convertView.findViewById(R.id.titulacao);

        TextView Lattes = (TextView) convertView.findViewById(R.id.lattes);

        TextView expertise = (TextView) convertView.findViewById(R.id.expertise);



        Pesquisador p = pesquisadorItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(p.getFotoUrl(), imageLoader);

        // title
        title.setText(p.getNome());

        Lattes.setText(p.getLattes());



        Titulacao.setText(p.getAbout_en());

        expertise.setText(p.getExpertise_Area());






        return convertView;
    }

}
