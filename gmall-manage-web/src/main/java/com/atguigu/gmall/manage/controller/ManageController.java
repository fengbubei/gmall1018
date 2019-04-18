package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.service.ManageService;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@CrossOrigin
@Api(description = "电商管理web模块")
public class ManageController {

    @Reference
    private ManageService manageService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    //http://localhost:8082/getCatalog1
    @PostMapping("/getCatalog1")
    @ResponseBody
    public List<BaseCatalog1> getCatalog1List(){
        return  manageService.getBaseCatalog1();
    }

    @PostMapping("/getCatalog2")
    @ResponseBody
    public List<BaseCatalog2> getBaseCataLog2( String catalog1Id){
        //http://localhost:8082/getCatalog2?catalog1Id=1
        return manageService.getBaseCatalog2(catalog1Id);
    }

    @RequestMapping("/getCatalog3")
    @ResponseBody     //http://localhost:8082/getCatalog3?catalog2Id=17
    public List<BaseCatalog3> getBaseCataLog3( String catalog2Id){
        return manageService.getBaseCatalog3(catalog2Id);
    }


    @GetMapping("/attrInfoList")
    @ResponseBody    //http://localhost:8082/attrInfoList?catalog3Id=264
    public List<BaseAttrInfo> getAttrList(String catalog3Id){
        return manageService.getAttrList(catalog3Id);
    }


    @GetMapping("/spuList")
    @ResponseBody    //http://localhost:8082/spuList?catalog3Id=61
    public List<SpuInfo> getAttrList1(String catalog3Id){
        return manageService.getSpuList(catalog3Id);
    }

    // http://localhost:8082/saveAttrInfo
   @PostMapping("/saveAttrInfo")
   @ResponseBody
    public void saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){

        manageService.saveAttrInfo(baseAttrInfo);
    }

    //http://localhost:8082/getAttrValueList?attrId=23  根据attrid查询BaseAttrValue
    @PostMapping("getAttrValueList")
    @ResponseBody
    public List<BaseAttrValue> getAttrValueList(String attrId){
        List<BaseAttrValue>attrValueList = manageService.getAttrValueList(attrId);
        return attrValueList;
    }

    //http://localhost:8082/baseSaleAttrList
    @RequestMapping("/baseSaleAttrList")
    @ResponseBody
    public List<BaseSaleAttr> getBaseSaleAttrList(){
        List<BaseSaleAttr> baseSaleAttrList = manageService.getBaseSaleAttrList();
        return baseSaleAttrList;
    }

    //http://localhost:8082/saveSpuInfo
    @RequestMapping("/saveSpuInfo")
    @ResponseBody
    public void saveSpuInfo(@RequestBody SpuInfo spuInfo){

        manageService.saveSpuInfo(spuInfo);
    }

    //spuImageList
    @GetMapping("spuImageList")
    @ResponseBody
    public List<SpuImage> getSpuImgList(String spuId){
       return manageService.getSpuImgList(spuId);
    }

    //http://localhost:8082/spuSaleAttrList?spuId=67
    @GetMapping("spuSaleAttrList")
    @ResponseBody
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId){

        return manageService.getSpuSaleAttrList(spuId);
    }

    @RequestMapping("/saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(@RequestBody SkuInfo skuInfo){
        manageService.saveSkuInfo(skuInfo);
        return "ok";
    }


}
