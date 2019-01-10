package com.mine.log;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * Created by zhanghuan on 2019/1/2.
 */
public class LogTest {


    private static final Logger logger = LogManager.getLogger(LogTest.class);


    @org.junit.Test
    public void test() {
        logger.info("输出信息……");
        logger.trace("随意打印……");
        logger.debug("调试信息……");
        logger.warn( "警告信息……");
        try {
            new Thread(new Runnable() {
                public void run() {
                    logger.warn("test……");
                }
            }).start();
//            throw new Exception("错误消息啊");
            LogTest.class.getClass().forName("123");
        } catch (Exception e) {
            logger.error("处理业务逻辑的时候发生一个错误……", e);
        }
    }
}
