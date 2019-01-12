package com.mine.log;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;


import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.layout.PatternSelector;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.util.PropertiesUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;

import static org.apache.logging.log4j.core.layout.PatternLayout.DEFAULT_CONVERSION_PATTERN;
import static org.apache.logging.log4j.core.layout.PatternLayout.newSerializerBuilder;


/**
 * Created by zhanghuan on 2019/1/3.
 */
@Plugin(name = "JsonPartternLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class JsonPartternLayout extends AbstractStringLayout {

    private PatternLayout patternLayout;

    private String system;

    private static String ip;

    private static SimpleDateFormat utcFormater;

    static {
        utcFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));//时区定义并进行时间获取
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            //ignore
        }
    }


    public JsonPartternLayout(final Configuration config, final RegexReplacement replace, final String eventPattern,
             final PatternSelector patternSelector, final Charset charset, final boolean alwaysWriteExceptions,
             final boolean disableAnsi, final boolean noConsoleNoAnsi, final String headerPattern,
             final String footerPattern, final String system) {
        super(config, charset,
                newSerializerBuilder()
                        .setConfiguration(config)
                        .setReplace(replace)
                        .setPatternSelector(patternSelector)
                        .setAlwaysWriteExceptions(alwaysWriteExceptions)
                        .setDisableAnsi(disableAnsi)
                        .setNoConsoleNoAnsi(noConsoleNoAnsi)
                        .setPattern(headerPattern)
                        .build(),
                newSerializerBuilder()
                        .setConfiguration(config)
                        .setReplace(replace)
                        .setPatternSelector(patternSelector)
                        .setAlwaysWriteExceptions(alwaysWriteExceptions)
                        .setDisableAnsi(disableAnsi)
                        .setNoConsoleNoAnsi(noConsoleNoAnsi)
                        .setPattern(footerPattern)
                        .build());

        this.patternLayout = PatternLayout.newBuilder()
                .withPattern(eventPattern)
                .withPatternSelector(patternSelector)
                .withConfiguration(config)
                .withRegexReplacement(replace)
                .withCharset(charset)
                .withDisableAnsi(disableAnsi)
                .withAlwaysWriteExceptions(alwaysWriteExceptions)
                .withNoConsoleNoAnsi(noConsoleNoAnsi)
                .withHeader(headerPattern)
                .withFooter(footerPattern)
                .build();
        this.system = system;

    }

    /**
     *
     * @param event
     * @return
     */
    public String toSerializable(LogEvent event) {
        String msg = this.patternLayout.toSerializable(event);
        String time = utcFormater.format(new Date(event.getTimeMillis()));
        return JSON.toJSONString(new JsonLogBean(system, ip,msg, event.getLevel().name(), time)) + "\n";
    }


    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     *
     */
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<JsonPartternLayout> {

        @PluginBuilderAttribute
        private String pattern = DEFAULT_CONVERSION_PATTERN;

        @PluginElement("PatternSelector")
        private PatternSelector patternSelector;

        @PluginConfiguration
        private Configuration configuration;

        @PluginElement("Replace")
        private RegexReplacement regexReplacement;

        // LOG4J2-783 use platform default by default
        @PluginBuilderAttribute
        private Charset charset = Charset.defaultCharset();

        @PluginBuilderAttribute
        private boolean alwaysWriteExceptions = true;

        @PluginBuilderAttribute
        private boolean disableAnsi = !useAnsiEscapeCodes();

        @PluginBuilderAttribute
        private boolean noConsoleNoAnsi;

        @PluginBuilderAttribute
        private String header;

        @PluginBuilderAttribute
        private String footer;

        @PluginBuilderAttribute
        private String system;

        private Builder() {
        }

        private boolean useAnsiEscapeCodes() {
            PropertiesUtil propertiesUtil = PropertiesUtil.getProperties();
            boolean isPlatformSupportsAnsi = !propertiesUtil.isOsWindows();
            boolean isJansiRequested = !propertiesUtil.getBooleanProperty("log4j.skipJansi", true);
            return isPlatformSupportsAnsi || isJansiRequested;
        }


        public JsonPartternLayout build() {
            // fall back to DefaultConfiguration
            if (configuration == null) {
                configuration = new DefaultConfiguration();
            }
            return new JsonPartternLayout(configuration, regexReplacement, pattern, patternSelector, charset,
                    alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi, header, footer, system);
        }
    }

}