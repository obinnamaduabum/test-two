package com.revent.test;

import lombok.Data;

import java.util.Date;

@Data
public class UserAccessLogoPojo {

    private Long id;

    private String status;

    private String userAgent;

    private String ip;

    private Long count;

    private Date date;
}
