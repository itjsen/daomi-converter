package com.daomi.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConvertToSilkDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String srcUrl;

    private String format;
}
