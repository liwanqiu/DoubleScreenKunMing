package com.changfeng.tcpdemo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TodayWeather {

    /**
     * sr : 06:31
     * ss : 19:59
     */

    private AstroBean astro;
    /**
     * code_d : 300
     * code_n : 300
     * txt_d : 阵雨
     * txt_n : 阵雨
     */

    private CondBean cond;
    /**
     * astro : {"sr":"06:31","ss":"19:59"}
     * cond : {"code_d":"300","code_n":"300","txt_d":"阵雨","txt_n":"阵雨"}
     * date : 2016-07-20
     * hum : 70
     * pcpn : 8.9
     * pop : 96
     * pres : 1005
     * tmp : {"max":"26","min":"17"}
     * vis : 10
     * wind : {"deg":"231","dir":"无持续风向","sc":"微风","spd":"4"}
     */

    private String date;
    private String hum;
    private String pcpn;
    private String pop;
    private String pres;
    /**
     * max : 26
     * min : 17
     */

    private TmpBean tmp;
    private String vis;
    /**
     * deg : 231
     * dir : 无持续风向
     * sc : 微风
     * spd : 4
     */

    private WindBean wind;

    public static TodayWeather objectFromData(String str) {

        return new Gson().fromJson(str, TodayWeather.class);
    }

    public static TodayWeather objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), TodayWeather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<TodayWeather> arrayTodayWeatherFromData(String str) {

        Type listType = new TypeToken<ArrayList<TodayWeather>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<TodayWeather> arrayTodayWeatherFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<TodayWeather>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public AstroBean getAstro() {
        return astro;
    }

    public void setAstro(AstroBean astro) {
        this.astro = astro;
    }

    public CondBean getCond() {
        return cond;
    }

    public void setCond(CondBean cond) {
        this.cond = cond;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public TmpBean getTmp() {
        return tmp;
    }

    public void setTmp(TmpBean tmp) {
        this.tmp = tmp;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public WindBean getWind() {
        return wind;
    }

    public void setWind(WindBean wind) {
        this.wind = wind;
    }

    public static class AstroBean {
        private String sr;
        private String ss;

        public static AstroBean objectFromData(String str) {

            return new Gson().fromJson(str, AstroBean.class);
        }

        public static AstroBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), AstroBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<AstroBean> arrayAstroBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<AstroBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<AstroBean> arrayAstroBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<AstroBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public String getSr() {
            return sr;
        }

        public void setSr(String sr) {
            this.sr = sr;
        }

        public String getSs() {
            return ss;
        }

        public void setSs(String ss) {
            this.ss = ss;
        }
    }

    public static class CondBean {
        private String code_d;
        private String code_n;
        private String txt_d;
        private String txt_n;

        public static CondBean objectFromData(String str) {

            return new Gson().fromJson(str, CondBean.class);
        }

        public static CondBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), CondBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<CondBean> arrayCondBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<CondBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<CondBean> arrayCondBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<CondBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public String getCode_d() {
            return code_d;
        }

        public void setCode_d(String code_d) {
            this.code_d = code_d;
        }

        public String getCode_n() {
            return code_n;
        }

        public void setCode_n(String code_n) {
            this.code_n = code_n;
        }

        public String getTxt_d() {
            return txt_d;
        }

        public void setTxt_d(String txt_d) {
            this.txt_d = txt_d;
        }

        public String getTxt_n() {
            return txt_n;
        }

        public void setTxt_n(String txt_n) {
            this.txt_n = txt_n;
        }
    }

    public static class TmpBean {
        private String max;
        private String min;

        public static TmpBean objectFromData(String str) {

            return new Gson().fromJson(str, TmpBean.class);
        }

        public static TmpBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), TmpBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<TmpBean> arrayTmpBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<TmpBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<TmpBean> arrayTmpBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<TmpBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }
    }

    public static class WindBean {
        private String deg;
        private String dir;
        private String sc;
        private String spd;

        public static WindBean objectFromData(String str) {

            return new Gson().fromJson(str, WindBean.class);
        }

        public static WindBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), WindBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<WindBean> arrayWindBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<WindBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<WindBean> arrayWindBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<WindBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public String getDeg() {
            return deg;
        }

        public void setDeg(String deg) {
            this.deg = deg;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public String getSc() {
            return sc;
        }

        public void setSc(String sc) {
            this.sc = sc;
        }

        public String getSpd() {
            return spd;
        }

        public void setSpd(String spd) {
            this.spd = spd;
        }
    }
}
