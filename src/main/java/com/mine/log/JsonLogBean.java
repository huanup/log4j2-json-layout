package com.mine.log;


/**
 * 输出的日志内容
 */
public class JsonLogBean {


    /**
     * 系统
     */
    private String system;

    /**
     *
     */
    private String ip;


    /**
     * 日志信息
     */
    private String message;

    /**
     * 日志级别
     */
    private String level;

    /**
     * 日志时间
     */
    private String time;


    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public JsonLogBean(String system, String ip,String message, String level, String time) {
        this.system = system;
        this.ip = ip;
        this.message = message;
        this.level = level;
        this.time = time;
    }

}