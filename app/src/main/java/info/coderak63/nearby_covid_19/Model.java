package info.coderak63.nearby_covid_19;

public class Model {


    public static final int TEXT_TYPE=0;
    public static final int IMAGE_TYPE=1;
    public static final int AUDIO_TYPE=2;

    public static final int HEADER_TYPE=3;
    public static final int RISK_TYPE=4;
    public static final int GPS_TYPE=5;
    public static final int CASES_TYPE=6;
    public static final int NEAREST_TYPE=7;
    public static final int NEARBY_TYPE=8;
    public static final int NATIVE_AD_TYPE=9;

    public int type;
    public int data;
    public String text;
    public String text2;
    public String text3;



    public Model(int type, String text ,String text2, String text3, int data)
    {
        this.type=type;
        this.data=data;
        this.text=text;
        this.text2=text2;
        this.text3=text3;

    }

}

