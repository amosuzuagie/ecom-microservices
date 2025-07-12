package com.info.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class BuildInfoController {
    @Autowired
    private BuildInfo buildInfo;
//    @Value("${build.id:default}")
//    private String buildId;
//
//    @Value("${build.version:default}")
//    private String buildVersion;
//
//    @Value("${build.name:default}")
//    private String buildName;
//
//    @Value("${build.type:default}")
//    private String buildType;

//    @Value("${PROCESSOR_LEVEL:default}")
//    private String buildId;
//
//    @Value("${PROCESSOR_REVISION:default}")
//    private String buildVersion;
//
//    @Value("${USERNAME:default}")
//    private String buildName;
//
//    @Value("${OS:default}")
//    private String buildType;

    @GetMapping("/build-info")
    public String getBuildInfo() {
        return "Build ID: " + buildInfo.getId() + ", Version: " + buildInfo.getVersion() + ", Name: " + buildInfo.getName() + ", Type: " + buildInfo.getType() + ".";
    }
}
