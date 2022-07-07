package com.techaccelarators.ifind.util;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ConfigurationProperties(prefix = "admin" )
public class AdminRoleProperties implements Serializable {
    private String name;
}
