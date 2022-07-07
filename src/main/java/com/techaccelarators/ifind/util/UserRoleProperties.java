package com.techaccelarators.ifind.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "general")
public class UserRoleProperties {
    private String name;
}
