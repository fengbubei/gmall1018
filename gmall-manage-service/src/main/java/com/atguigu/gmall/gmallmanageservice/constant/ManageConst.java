package com.atguigu.gmall.gmallmanageservice.constant;

import org.springframework.stereotype.Component;


public class ManageConst {

    public static final String SKUKEY_PREFIX="sku:";
    public static final String SKUKEY_SUFFIX=":info";
    public static final int skukey_timeout=24*60*60;
    public static final int SKULOCK_EXPIRE_PX=10000;
    public static final String SKULOCK_SUFFIX=":lock";

}
