package com.changfeng.tcpdemo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chang on 2016/8/9.
 */
public class Suggestion {

    @SerializedName("说明")
    private String description;
    @SerializedName("升级方法")
    private String updateMethod;
    @SerializedName("提示语")
    private List<SuggestionBean> suggestion;

    public static class SuggestionBean {
        @SerializedName("天气")
        private String weather;
        @SerializedName("提示语")
        private String suggestion;

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(String suggestion) {
            this.suggestion = suggestion;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateMethod() {
        return updateMethod;
    }

    public void setUpdateMethod(String updateMethod) {
        this.updateMethod = updateMethod;
    }

    public List<SuggestionBean> getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(List<SuggestionBean> suggestion) {
        this.suggestion = suggestion;
    }
}
