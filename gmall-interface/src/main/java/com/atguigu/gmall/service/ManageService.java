package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.*;

import java.util.List;

public interface ManageService {
    public List<BaseCatalog1> getBaseCatalog1();

    public List<BaseCatalog2> getBaseCatalog2(String catalog1Id);

    public List<BaseCatalog3> getBaseCatalog3(String catalog2Id);

    //pulic List<BaseAttrInfo> getBaseAttrInfo()
    public List<BaseAttrInfo> getAttrList(String catalog3Id);

    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    List<BaseAttrValue> getAttrValueList(String attrId);

    List<SpuInfo> getSpuList(String catalog3Id);


    List<BaseSaleAttr> getBaseSaleAttrList();

    void saveSpuInfo(SpuInfo spuInfo);
}
