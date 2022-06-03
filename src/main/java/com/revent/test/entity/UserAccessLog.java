package com.revent.test.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class UserAccessLog {

    private Long id;

    private String status;

    private String userAgent;

    private String ip;

    private Date date;
}
