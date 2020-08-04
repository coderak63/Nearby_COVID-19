package info.coderak63.nearby_covid_19;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import info.coderak63.nearby_covid_19.R;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Model> dataSet;
    Context mContext;
    int total_types;
    MediaPlayer mPlayer;
    private boolean fabStateVolume = false;

    public static class TextTypeViewHolder extends RecyclerView.ViewHolder {


        TextView txtType;
        CardView cardView;

        public TextTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);

        }

    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {


        TextView txtType;
        ImageView image;

        public ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.image = (ImageView) itemView.findViewById(R.id.background);

        }

    }

    public static class AudioTypeViewHolder extends RecyclerView.ViewHolder {


        TextView txtType;
       // FloatingActionButton fab;

        public AudioTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
           // this.fab = (FloatingActionButton) itemView.findViewById(R.id.fab);

        }

    }



    public static class HeaderTypeViewHolder extends RecyclerView.ViewHolder {


        CardView cardView;

        public HeaderTypeViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);

        }

    }



    public static class RiskTypeViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        CardView cardView;
        TextView risk;

        public RiskTypeViewHolder(View itemView) {
            super(itemView);

            this.image = (ImageView) itemView.findViewById(R.id.background);
            this.risk = (TextView) itemView.findViewById(R.id.type);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);

        }

    }


    public static class GpsTypeViewHolder extends RecyclerView.ViewHolder {


        TextView latitude,longitude;

        public GpsTypeViewHolder(View itemView) {
            super(itemView);

            this.latitude = (TextView) itemView.findViewById(R.id.lati);
            this.longitude = (TextView) itemView.findViewById(R.id.longi);

        }

    }


    public static class CasesTypeViewHolder extends RecyclerView.ViewHolder {


        TextView total,cured,deceased;

        public CasesTypeViewHolder(View itemView) {
            super(itemView);

            this.total = (TextView) itemView.findViewById(R.id.total);
            this.cured = (TextView) itemView.findViewById(R.id.cured);
            this.deceased = (TextView) itemView.findViewById(R.id.deceased);

        }

    }

    public static class NearestTypeViewHolder extends RecyclerView.ViewHolder {


        TextView nearest;

        public NearestTypeViewHolder(View itemView) {
            super(itemView);

            this.nearest = (TextView) itemView.findViewById(R.id.distance);

        }

    }

    public static class NearbyTypeViewHolder extends RecyclerView.ViewHolder {


        TextView nearby;

        public NearbyTypeViewHolder(View itemView) {
            super(itemView);

            this.nearby = (TextView) itemView.findViewById(R.id.nearby);

        }

    }


    public static class NativeAdTypeViewHolder extends RecyclerView.ViewHolder {


        PublisherAdView nativeAd;

        public NativeAdTypeViewHolder(View itemView) {
            super(itemView);

            this.nativeAd = (PublisherAdView) itemView.findViewById(R.id.publisherAdView);

        }

    }






    public MultiViewTypeAdapter(ArrayList<Model> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Model.TEXT_TYPE:

                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_type, parent, false);
                    return new TextTypeViewHolder(view);

            case Model.IMAGE_TYPE:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_type, parent, false);
                return new ImageTypeViewHolder(view);

            case Model.AUDIO_TYPE:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_type, parent, false);
                return new AudioTypeViewHolder(view);

            case Model.HEADER_TYPE:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
                return new HeaderTypeViewHolder(view);
            case Model.RISK_TYPE:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.risk, parent, false);
                return new RiskTypeViewHolder(view);
            case Model.GPS_TYPE:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gps, parent, false);
                return new GpsTypeViewHolder(view);
            case Model.CASES_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cases, parent, false);
                return new CasesTypeViewHolder(view);
                case Model.NEAREST_TYPE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearest, parent, false);
                    return new NearestTypeViewHolder(view);
            case Model.NEARBY_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby, parent, false);
                return new NearbyTypeViewHolder(view);
            case Model.NATIVE_AD_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.native_ad, parent, false);
                PublisherAdView adView=(PublisherAdView)view.findViewById(R.id.publisherAdView);
                PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                adView.loadAd(adRequest);
                return new NativeAdTypeViewHolder(view);

        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return Model.TEXT_TYPE;
            case 1:
                return Model.IMAGE_TYPE;
            case 2:
                return Model.AUDIO_TYPE;
            case 3:
                return Model.HEADER_TYPE;
            case 4:
                return Model.RISK_TYPE;
            case 5:
                return Model.GPS_TYPE;
            case 6:
                return Model.CASES_TYPE;
            case 7:
                return Model.NEAREST_TYPE;
            case 8:
                return Model.NEARBY_TYPE;
            case 9:
                return Model.NATIVE_AD_TYPE;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Model object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case Model.TEXT_TYPE:
                    ((TextTypeViewHolder) holder).txtType.setText(object.text);

                    break;
                case Model.IMAGE_TYPE:
                    ((ImageTypeViewHolder) holder).txtType.setText(object.text);
                    //((ImageTypeViewHolder) holder).image.setImageResource(object.data);
                    break;
                case Model.AUDIO_TYPE:

                    ((AudioTypeViewHolder) holder).txtType.setText(object.text);

                    break;
                case Model.HEADER_TYPE:

                    break;
                case Model.RISK_TYPE:
                    if(object.text.equals("low")) {
                        ((RiskTypeViewHolder) holder).image.setImageResource(object.data);
                        ((RiskTypeViewHolder) holder).risk.setText("Risk : Low");
                        ((RiskTypeViewHolder) holder).risk.setTextColor(Color.parseColor("#5DB613"));
                    }else if(object.text.equals("moderate")) {
                        ((RiskTypeViewHolder) holder).image.setImageResource(object.data);
                        ((RiskTypeViewHolder) holder).risk.setText("Risk : Moderate");
                        ((RiskTypeViewHolder) holder).risk.setTextColor(Color.parseColor("#FFD300"));
                    }else if(object.text.equals("high")) {
                        ((RiskTypeViewHolder) holder).image.setImageResource(object.data);
                        ((RiskTypeViewHolder) holder).risk.setText("Risk : High");
                        ((RiskTypeViewHolder) holder).risk.setTextColor(Color.parseColor("#FF0266"));
                    }else{
                        ((RiskTypeViewHolder) holder).image.setImageResource(object.data);
                        ((RiskTypeViewHolder) holder).risk.setText(object.text);
                        ((RiskTypeViewHolder) holder).risk.setTextColor(Color.BLACK);
                    }
                    break;
                case Model.GPS_TYPE:
                    ((GpsTypeViewHolder) holder).latitude.setText(object.text);
                    ((GpsTypeViewHolder) holder).longitude.setText(object.text2);
                    break;
                case Model.CASES_TYPE:
                    ((CasesTypeViewHolder) holder).total.setText(object.text);
                    ((CasesTypeViewHolder) holder).cured.setText(object.text2);
                    ((CasesTypeViewHolder) holder).deceased.setText(object.text3);
                    break;
                case Model.NEAREST_TYPE:
                    ((NearestTypeViewHolder) holder).nearest.setText(object.text);
                    break;
                case Model.NEARBY_TYPE:
                    ((NearbyTypeViewHolder) holder).nearby.setText(object.text);
                    break;
                case Model.NATIVE_AD_TYPE:
                    //PublisherAdView adView=(PublisherAdView)findViewById(R.id.publisherAdView);
                    //adView.setAdSizes(AdSize.BANNER);
                    //adView.setAdUnitId("/6499/example/banner");

                    //PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                    //((NativeAdTypeViewHolder) holder).nativeAd.loadAd(adRequest);
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
