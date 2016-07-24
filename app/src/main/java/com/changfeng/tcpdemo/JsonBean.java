package com.changfeng.tcpdemo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonBean {

    /**
     * city : {"aqi":"22","co":"1","no2":"13","o3":"44","pm10":"19","pm25":"6","qlty":"优","so2":"15"}
     */

    private AqiBean aqi;
    /**
     * city : 昆明
     * cnty : 中国
     * id : CN101290101
     * lat : 25.051000
     * lon : 102.702000
     * update : {"loc":"2016-07-20 17:51","utc":"2016-07-20 09:51"}
     */

    private BasicBean basic;
    /**
     * cond : {"code":"300","txt":"阵雨"}
     * fl : 25
     * hum : 71
     * pcpn : 0
     * pres : 1017
     * tmp : 23
     * vis : 10
     * wind : {"deg":"230","dir":"南风","sc":"4-5","spd":"22"}
     */

    private NowBean now;
    /**
     * aqi : {"city":{"aqi":"22","co":"1","no2":"13","o3":"44","pm10":"19","pm25":"6","qlty":"优","so2":"15"}}
     * basic : {"city":"昆明","cnty":"中国","id":"CN101290101","lat":"25.051000","lon":"102.702000","update":{"loc":"2016-07-20 17:51","utc":"2016-07-20 09:51"}}
     * daily_forecast : [{"astro":{"sr":"06:31","ss":"19:59"},"cond":{"code_d":"300","code_n":"300","txt_d":"阵雨","txt_n":"阵雨"},"date":"2016-07-20","hum":"70","pcpn":"8.9","pop":"96","pres":"1005","tmp":{"max":"26","min":"17"},"vis":"10","wind":{"deg":"231","dir":"无持续风向","sc":"微风","spd":"4"}},{"astro":{"sr":"06:31","ss":"19:59"},"cond":{"code_d":"300","code_n":"300","txt_d":"阵雨","txt_n":"阵雨"},"date":"2016-07-21","hum":"68","pcpn":"4.0","pop":"89","pres":"1008","tmp":{"max":"25","min":"18"},"vis":"10","wind":{"deg":"235","dir":"无持续风向","sc":"微风","spd":"4"}},{"astro":{"sr":"06:32","ss":"19:59"},"cond":{"code_d":"300","code_n":"300","txt_d":"阵雨","txt_n":"阵雨"},"date":"2016-07-22","hum":"64","pcpn":"5.1","pop":"80","pres":"1008","tmp":{"max":"25","min":"18"},"vis":"9","wind":{"deg":"239","dir":"无持续风向","sc":"微风","spd":"6"}},{"astro":{"sr":"06:32","ss":"19:58"},"cond":{"code_d":"305","code_n":"305","txt_d":"小雨","txt_n":"小雨"},"date":"2016-07-23","hum":"67","pcpn":"1.7","pop":"72","pres":"1006","tmp":{"max":"25","min":"18"},"vis":"10","wind":{"deg":"249","dir":"无持续风向","sc":"微风","spd":"1"}},{"astro":{"sr":"06:33","ss":"19:58"},"cond":{"code_d":"305","code_n":"305","txt_d":"小雨","txt_n":"小雨"},"date":"2016-07-24","hum":"59","pcpn":"1.2","pop":"62","pres":"1005","tmp":{"max":"24","min":"18"},"vis":"10","wind":{"deg":"258","dir":"无持续风向","sc":"微风","spd":"9"}},{"astro":{"sr":"06:33","ss":"19:57"},"cond":{"code_d":"305","code_n":"306","txt_d":"小雨","txt_n":"中雨"},"date":"2016-07-25","hum":"52","pcpn":"8.5","pop":"48","pres":"1005","tmp":{"max":"23","min":"17"},"vis":"10","wind":{"deg":"235","dir":"无持续风向","sc":"微风","spd":"5"}},{"astro":{"sr":"06:34","ss":"19:57"},"cond":{"code_d":"307","code_n":"306","txt_d":"大雨","txt_n":"中雨"},"date":"2016-07-26","hum":"72","pcpn":"13.6","pop":"50","pres":"1008","tmp":{"max":"24","min":"17"},"vis":"10","wind":{"deg":"62","dir":"无持续风向","sc":"微风","spd":"7"}}]
     * hourly_forecast : [{"date":"2016-07-20 19:00","hum":"91","pop":"94","pres":"1005","tmp":"23","wind":{"deg":"204","dir":"西南风","sc":"微风","spd":"6"}},{"date":"2016-07-20 22:00","hum":"98","pop":"93","pres":"1008","tmp":"21","wind":{"deg":"207","dir":"西南风","sc":"微风","spd":"5"}}]
     * now : {"cond":{"code":"300","txt":"阵雨"},"fl":"25","hum":"71","pcpn":"0","pres":"1017","tmp":"23","vis":"10","wind":{"deg":"230","dir":"南风","sc":"4-5","spd":"22"}}
     * status : ok
     * suggestion : {"comf":{"brf":"较舒适","txt":"白天有降雨，但会使人们感觉有些热，不过大部分人仍会有比较舒适的感觉。"},"cw":{"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},"drsg":{"brf":"舒适","txt":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"},"flu":{"brf":"少发","txt":"各项气象条件适宜，无明显降温过程，发生感冒机率较低。"},"sport":{"brf":"较不宜","txt":"有降水，推荐您在室内进行健身休闲运动；若坚持户外运动，须注意保暖并携带雨具。"},"trav":{"brf":"适宜","txt":"有降水，温度适宜，在细雨中游玩别有一番情调，可不要错过机会呦！但记得出门要携带雨具。"},"uv":{"brf":"中等","txt":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}}
     */

    private String status;
    /**
     * comf : {"brf":"较舒适","txt":"白天有降雨，但会使人们感觉有些热，不过大部分人仍会有比较舒适的感觉。"}
     * cw : {"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"}
     * drsg : {"brf":"舒适","txt":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"}
     * flu : {"brf":"少发","txt":"各项气象条件适宜，无明显降温过程，发生感冒机率较低。"}
     * sport : {"brf":"较不宜","txt":"有降水，推荐您在室内进行健身休闲运动；若坚持户外运动，须注意保暖并携带雨具。"}
     * trav : {"brf":"适宜","txt":"有降水，温度适宜，在细雨中游玩别有一番情调，可不要错过机会呦！但记得出门要携带雨具。"}
     * uv : {"brf":"中等","txt":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}
     */

    private SuggestionBean suggestion;
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

    private List<DailyForecastBean> daily_forecast;
    /**
     * date : 2016-07-20 19:00
     * hum : 91
     * pop : 94
     * pres : 1005
     * tmp : 23
     * wind : {"deg":"204","dir":"西南风","sc":"微风","spd":"6"}
     */

    private List<HourlyForecastBean> hourly_forecast;

    public static JsonBean objectFromData(String str) {

        return new Gson().fromJson(str, JsonBean.class);
    }

    public static JsonBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), JsonBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<JsonBean> arrayJsonBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<JsonBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<JsonBean> arrayJsonBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<JsonBean>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public AqiBean getAqi() {
        return aqi;
    }

    public void setAqi(AqiBean aqi) {
        this.aqi = aqi;
    }

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public NowBean getNow() {
        return now;
    }

    public void setNow(NowBean now) {
        this.now = now;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SuggestionBean getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(SuggestionBean suggestion) {
        this.suggestion = suggestion;
    }

    public List<DailyForecastBean> getDaily_forecast() {
        return daily_forecast;
    }

    public void setDaily_forecast(List<DailyForecastBean> daily_forecast) {
        this.daily_forecast = daily_forecast;
    }

    public List<HourlyForecastBean> getHourly_forecast() {
        return hourly_forecast;
    }

    public void setHourly_forecast(List<HourlyForecastBean> hourly_forecast) {
        this.hourly_forecast = hourly_forecast;
    }

    public static class AqiBean {
        /**
         * aqi : 22
         * co : 1
         * no2 : 13
         * o3 : 44
         * pm10 : 19
         * pm25 : 6
         * qlty : 优
         * so2 : 15
         */

        private CityBean city;

        public static AqiBean objectFromData(String str) {

            return new Gson().fromJson(str, AqiBean.class);
        }

        public static AqiBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), AqiBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<AqiBean> arrayAqiBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<AqiBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<AqiBean> arrayAqiBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<AqiBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public CityBean getCity() {
            return city;
        }

        public void setCity(CityBean city) {
            this.city = city;
        }

        public static class CityBean {
            private String aqi;
            private String co;
            private String no2;
            private String o3;
            private String pm10;
            private String pm25;
            private String qlty;
            private String so2;

            public static CityBean objectFromData(String str) {

                return new Gson().fromJson(str, CityBean.class);
            }

            public static CityBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), CityBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<CityBean> arrayCityBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<CityBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<CityBean> arrayCityBeanFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<CityBean>>() {
                    }.getType();

                    return new Gson().fromJson(jsonObject.getString(str), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ArrayList();


            }

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }

            public String getCo() {
                return co;
            }

            public void setCo(String co) {
                this.co = co;
            }

            public String getNo2() {
                return no2;
            }

            public void setNo2(String no2) {
                this.no2 = no2;
            }

            public String getO3() {
                return o3;
            }

            public void setO3(String o3) {
                this.o3 = o3;
            }

            public String getPm10() {
                return pm10;
            }

            public void setPm10(String pm10) {
                this.pm10 = pm10;
            }

            public String getPm25() {
                return pm25;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public String getQlty() {
                return qlty;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }

            public String getSo2() {
                return so2;
            }

            public void setSo2(String so2) {
                this.so2 = so2;
            }
        }
    }

    public static class BasicBean {
        private String city;
        private String cnty;
        private String id;
        private String lat;
        private String lon;
        /**
         * loc : 2016-07-20 17:51
         * utc : 2016-07-20 09:51
         */

        private UpdateBean update;

        public static BasicBean objectFromData(String str) {

            return new Gson().fromJson(str, BasicBean.class);
        }

        public static BasicBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), BasicBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<BasicBean> arrayBasicBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<BasicBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<BasicBean> arrayBasicBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<BasicBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCnty() {
            return cnty;
        }

        public void setCnty(String cnty) {
            this.cnty = cnty;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public UpdateBean getUpdate() {
            return update;
        }

        public void setUpdate(UpdateBean update) {
            this.update = update;
        }

        public static class UpdateBean {
            private String loc;
            private String utc;

            public static UpdateBean objectFromData(String str) {

                return new Gson().fromJson(str, UpdateBean.class);
            }

            public static UpdateBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), UpdateBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<UpdateBean> arrayUpdateBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<UpdateBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<UpdateBean> arrayUpdateBeanFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<UpdateBean>>() {
                    }.getType();

                    return new Gson().fromJson(jsonObject.getString(str), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ArrayList();


            }

            public String getLoc() {
                return loc;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public String getUtc() {
                return utc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }
        }
    }

    public static class NowBean {
        /**
         * code : 300
         * txt : 阵雨
         */

        private CondBean cond;
        private String fl;
        private String hum;
        private String pcpn;
        private String pres;
        private String tmp;
        private String vis;
        /**
         * deg : 230
         * dir : 南风
         * sc : 4-5
         * spd : 22
         */

        private WindBean wind;

        public static NowBean objectFromData(String str) {

            return new Gson().fromJson(str, NowBean.class);
        }

        public static NowBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), NowBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<NowBean> arrayNowBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<NowBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<NowBean> arrayNowBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<NowBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public CondBean getCond() {
            return cond;
        }

        public void setCond(CondBean cond) {
            this.cond = cond;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
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

        public String getPres() {
            return pres;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public String getTmp() {
            return tmp;
        }

        public void setTmp(String tmp) {
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

        public static class CondBean {
            private String code;
            private String txt;

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

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
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

    public static class SuggestionBean {
        /**
         * brf : 较舒适
         * txt : 白天有降雨，但会使人们感觉有些热，不过大部分人仍会有比较舒适的感觉。
         */

        private ComfBean comf;
        /**
         * brf : 不宜
         * txt : 不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。
         */

        private CwBean cw;
        /**
         * brf : 舒适
         * txt : 建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。
         */

        private DrsgBean drsg;
        /**
         * brf : 少发
         * txt : 各项气象条件适宜，无明显降温过程，发生感冒机率较低。
         */

        private FluBean flu;
        /**
         * brf : 较不宜
         * txt : 有降水，推荐您在室内进行健身休闲运动；若坚持户外运动，须注意保暖并携带雨具。
         */

        private SportBean sport;
        /**
         * brf : 适宜
         * txt : 有降水，温度适宜，在细雨中游玩别有一番情调，可不要错过机会呦！但记得出门要携带雨具。
         */

        private TravBean trav;
        /**
         * brf : 中等
         * txt : 属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。
         */

        private UvBean uv;

        public static SuggestionBean objectFromData(String str) {

            return new Gson().fromJson(str, SuggestionBean.class);
        }

        public static SuggestionBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), SuggestionBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<SuggestionBean> arraySuggestionBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<SuggestionBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<SuggestionBean> arraySuggestionBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<SuggestionBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public ComfBean getComf() {
            return comf;
        }

        public void setComf(ComfBean comf) {
            this.comf = comf;
        }

        public CwBean getCw() {
            return cw;
        }

        public void setCw(CwBean cw) {
            this.cw = cw;
        }

        public DrsgBean getDrsg() {
            return drsg;
        }

        public void setDrsg(DrsgBean drsg) {
            this.drsg = drsg;
        }

        public FluBean getFlu() {
            return flu;
        }

        public void setFlu(FluBean flu) {
            this.flu = flu;
        }

        public SportBean getSport() {
            return sport;
        }

        public void setSport(SportBean sport) {
            this.sport = sport;
        }

        public TravBean getTrav() {
            return trav;
        }

        public void setTrav(TravBean trav) {
            this.trav = trav;
        }

        public UvBean getUv() {
            return uv;
        }

        public void setUv(UvBean uv) {
            this.uv = uv;
        }

        public static class ComfBean {
            private String brf;
            private String txt;

            public static ComfBean objectFromData(String str) {

                return new Gson().fromJson(str, ComfBean.class);
            }

            public static ComfBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), ComfBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<ComfBean> arrayComfBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<ComfBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<ComfBean> arrayComfBeanFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<ComfBean>>() {
                    }.getType();

                    return new Gson().fromJson(jsonObject.getString(str), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ArrayList();


            }

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class CwBean {
            private String brf;
            private String txt;

            public static CwBean objectFromData(String str) {

                return new Gson().fromJson(str, CwBean.class);
            }

            public static CwBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), CwBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<CwBean> arrayCwBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<CwBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<CwBean> arrayCwBeanFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<CwBean>>() {
                    }.getType();

                    return new Gson().fromJson(jsonObject.getString(str), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ArrayList();


            }

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class DrsgBean {
            private String brf;
            private String txt;

            public static DrsgBean objectFromData(String str) {

                return new Gson().fromJson(str, DrsgBean.class);
            }

            public static DrsgBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), DrsgBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<DrsgBean> arrayDrsgBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<DrsgBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<DrsgBean> arrayDrsgBeanFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<DrsgBean>>() {
                    }.getType();

                    return new Gson().fromJson(jsonObject.getString(str), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ArrayList();


            }

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class FluBean {
            private String brf;
            private String txt;

            public static FluBean objectFromData(String str) {

                return new Gson().fromJson(str, FluBean.class);
            }

            public static FluBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), FluBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<FluBean> arrayFluBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<FluBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<FluBean> arrayFluBeanFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<FluBean>>() {
                    }.getType();

                    return new Gson().fromJson(jsonObject.getString(str), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ArrayList();


            }

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class SportBean {
            private String brf;
            private String txt;

            public static SportBean objectFromData(String str) {

                return new Gson().fromJson(str, SportBean.class);
            }

            public static SportBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), SportBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<SportBean> arraySportBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<SportBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<SportBean> arraySportBeanFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<SportBean>>() {
                    }.getType();

                    return new Gson().fromJson(jsonObject.getString(str), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ArrayList();


            }

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class TravBean {
            private String brf;
            private String txt;

            public static TravBean objectFromData(String str) {

                return new Gson().fromJson(str, TravBean.class);
            }

            public static TravBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), TravBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<TravBean> arrayTravBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<TravBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<TravBean> arrayTravBeanFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<TravBean>>() {
                    }.getType();

                    return new Gson().fromJson(jsonObject.getString(str), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ArrayList();


            }

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class UvBean {
            private String brf;
            private String txt;

            public static UvBean objectFromData(String str) {

                return new Gson().fromJson(str, UvBean.class);
            }

            public static UvBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), UvBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<UvBean> arrayUvBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<UvBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<UvBean> arrayUvBeanFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<UvBean>>() {
                    }.getType();

                    return new Gson().fromJson(jsonObject.getString(str), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ArrayList();


            }

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }
    }

    public static class DailyForecastBean {
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

        public static DailyForecastBean objectFromData(String str) {

            return new Gson().fromJson(str, DailyForecastBean.class);
        }

        public static DailyForecastBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), DailyForecastBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<DailyForecastBean> arrayDailyForecastBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<DailyForecastBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<DailyForecastBean> arrayDailyForecastBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<DailyForecastBean>>() {
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

    public static class HourlyForecastBean {
        private String date;
        private String hum;
        private String pop;
        private String pres;
        private String tmp;
        /**
         * deg : 204
         * dir : 西南风
         * sc : 微风
         * spd : 6
         */

        private WindBean wind;

        public static HourlyForecastBean objectFromData(String str) {

            return new Gson().fromJson(str, HourlyForecastBean.class);
        }

        public static HourlyForecastBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), HourlyForecastBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<HourlyForecastBean> arrayHourlyForecastBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<HourlyForecastBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<HourlyForecastBean> arrayHourlyForecastBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<HourlyForecastBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


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

        public String getTmp() {
            return tmp;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
        }

        public WindBean getWind() {
            return wind;
        }

        public void setWind(WindBean wind) {
            this.wind = wind;
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
}