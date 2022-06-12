package ru.antipant.config;

import org.aeonbits.owner.Config;
import org.checkerframework.checker.units.qual.K;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/local.properties",
        "classpath:config/remote.properties"
})
public interface ProjectConfig extends Config {

    @Key("baseUrl")
    String baseUrl();
    @Key("browser")
    @DefaultValue("chrome")
    String browser();
    @Key("browserVersion")
    @DefaultValue("100.0")
    String browserVersion();
    @Key("browserSize")
    @DefaultValue("1920x1080")
    String browserSize();
    @Key("browserMobileView")
    String browserMobileView();
    @Key("remoteDriverUrl")
    String remoteDriverUrl();
    @Key("videoStorage")
    String videoStorage();
    @Key("userLogin")
    String userLogin();
    @Key("userPassword")
    String userPassword();
}
