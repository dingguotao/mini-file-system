package com.iclouding.mfs.common.conf;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * Configuration
 * 配置管理类
 * @author: iclouding
 * @date: 2021/8/10 21:20
 * @email: clouding.vip@qq.com
 */
public class Configuration {

    public static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    private static Properties properties = new Properties();

    static {
        try {
            // 加载classpath下配置

            Enumeration<URL> resources = Configuration.class.getClassLoader().getResources("");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                File file = new File(url.getFile());
                String[] configFiles = file.list((dir, name) -> name.endsWith("properties"));
                for (String s : configFiles) {
                    properties.load(Configuration.class.getClassLoader().getResourceAsStream(s));
                }
            }

            // 加载用户目录下的配置
            File userPath = new File(System.getProperty("user.dir"));
            if (userPath.exists()) {
                String[] configFiles = userPath.list((dir, name) -> name.endsWith("properties"));
                for (String s : configFiles) {
                    properties.load(Configuration.class.getClassLoader().getResourceAsStream(s));
                }
            }

            // 加载用户config下配置
            File userConfigPath = new File(System.getProperty("user.dir") + File.pathSeparator + "config");
            if (userConfigPath.exists()) {
                String[] configFiles = userConfigPath.list((dir, name) -> name.endsWith("properties"));
                for (String s : configFiles) {
                    properties.load(Configuration.class.getClassLoader().getResourceAsStream(s));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
