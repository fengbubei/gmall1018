package com.atguigu.gmall.gmallmanageservice.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.gmallmanageservice.constant.ManageConst;
import com.atguigu.gmall.gmallmanageservice.mapper.*;
import com.atguigu.gmall.service.ManageService;
import com.atguigu.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import javax.swing.tree.VariableHeightLayoutCache;
import java.util.List;

@Service
@Component
public class ManagerServiceImpl implements ManageService {
    @Autowired
    private BaseCatalog1Mapper baseCatalog1Mapper;

    @Autowired
    private BaseCatalog2Mapper baseCatalog2Mapper;

    @Autowired
    private BaseCatalog3Mapper baseCatalog3Mapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<BaseCatalog1> getBaseCatalog1() {
        try{
            Jedis jedis = redisUtil.getJedis();
            jedis.set("1","testRedis");
        }catch (Exception e){
            e.printStackTrace();
        }

        return baseCatalog1Mapper.selectAll();
    }

   /* @Override
    public List<BaseCatalog2> getCatalog2(String catalog1Id) {
        BaseCatalog2 baseCatalog2=new BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);

        List<BaseCatalog2> baseCatalog2List = baseCatalog2Mapper.select(baseCatalog2);
        return baseCatalog2List;
    }*/

    @Override
    public List<BaseCatalog2> getBaseCatalog2(String catalog1Id) {
        BaseCatalog2 baseCatalog2 = new BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);
        List<BaseCatalog2> select = baseCatalog2Mapper.select(baseCatalog2);
        return select;
    }

    @Override
    public List<BaseCatalog3> getBaseCatalog3(String catalog2Id) {
        BaseCatalog3 baseCatalog3 = new BaseCatalog3();
        baseCatalog3.setCatalog2Id(catalog2Id);
        return baseCatalog3Mapper.select(baseCatalog3);
    }

    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3Id) {
       /* BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        return baseAttrInfoMapper.select(baseAttrInfo);*/
       return baseAttrInfoMapper.getBaseAttrInfoListByCatalog3Id(catalog3Id);
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //1.先判断baseAttrInfo中的id是否存在，若存在，修改，不存在，保存
        if (!StringUtils.isEmpty(baseAttrInfo.getId())) {//修改
            baseAttrInfoMapper.updateByPrimaryKey(baseAttrInfo);
        } else {
            //2.主键要自增，保存的时候需要判断主键为“”
            if (baseAttrInfo.getId() == "") {
                baseAttrInfo.setId(null);
            }
            baseAttrInfoMapper.insertSelective(baseAttrInfo);
        }

        //3.不论保存或者修改，都先用baseAttrInfo的id，作为attrid删除BaseAttrValue的所有值
        BaseAttrValue baseAttrValue1 = new BaseAttrValue();
        baseAttrValue1.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValue1);

        if (baseAttrInfo.getAttrValueList() != null && baseAttrInfo.getAttrValueList().size() != 0) {
            for (BaseAttrValue baseAttrValue : baseAttrInfo.getAttrValueList()) {
                if (baseAttrValue.getId() == "") {
                    baseAttrValue.setId(null);
                }
                //4.插入现在的新的BaseAttrValue值
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insertSelective(baseAttrValue);
            }
        }

    }

    @Override
    public List<BaseAttrValue> getAttrValueList(String attrId) {
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(attrId);
        List<BaseAttrValue> baseAttrValueList = baseAttrValueMapper.select(baseAttrValue);
        return baseAttrValueList;
    }

    @Override
    public List<SpuInfo> getSpuList(String catalog3Id) {
        SpuInfo spuInfo = new SpuInfo();
        spuInfo.setCatalog3Id(catalog3Id);
        List<SpuInfo> spuInfoList = spuInfoMapper.select(spuInfo);
        return spuInfoList;
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        List<BaseSaleAttr> baseSaleAttrList = baseSaleAttrMapper.selectAll();
        return baseSaleAttrList;
    }

    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        if (spuInfo.getId() == null || spuInfo.getId().length() == 0) {
            //if (spuInfo.getId()==null || spuInfo.getId().length()==0){
            //保存
            spuInfo.setId(null);
            spuInfoMapper.insert(spuInfo);
        } else {
            spuInfoMapper.updateByPrimaryKeySelective(spuInfo);
        }
        //先删除图片列表
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuInfo.getId());
        spuImageMapper.delete(spuImage);

        //插入图片
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if (spuImageList != null && spuImageList.size() > 0) {
            for (SpuImage image : spuImageList) {
                image.setId(null);
                image.setSpuId(spuInfo.getId());
                spuImageMapper.insertSelective(image);
            }
        }

        //插入销售属性和销售属性值
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if (spuSaleAttrList.size() > 0 && spuSaleAttrList != null) {
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                spuSaleAttr.setId(null);
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insert(spuSaleAttr);

                // 添加销售属性值
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if (spuSaleAttrValueList != null && spuSaleAttrValueList.size() > 0) {
                    // 循环遍历
                    for (SpuSaleAttrValue saleAttrValue : spuSaleAttrValueList) {
                        saleAttrValue.setId(null);
                        saleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValueMapper.insertSelective(saleAttrValue);
                    }
                }
            }
        }

    }

    @Override
    public List<SpuImage> getSpuImgList(String spuId) {
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);
        return spuImageMapper.select(spuImage);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId) {
        /*SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuId);
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.select(spuSaleAttr);*/
        //Long id = spuId;
        long id = Long.parseLong(spuId);
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.selectSpuSaleAttrList(id);
        return spuSaleAttrList;
    }

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        if(skuInfo.getId()!=null && skuInfo.getId().length()>0){
            //修改
            skuInfoMapper.updateByPrimaryKeySelective(skuInfo);
        }else{//增加
            skuInfoMapper.insertSelective(skuInfo);
        }

        //删除图片
        String skuId = skuInfo.getId();
        SkuImage skuImage1 = new SkuImage();
        skuImage1.setSkuId(skuId);
        skuImageMapper.delete(skuImage1);
        //插入图片地址
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if(skuImageList.size()>0 && skuImageList!=null){
            for (SkuImage skuImage : skuImageList) {
                if(StringUtils.isEmpty(skuImage.getId())){
                    skuImage.setId(null);
                }
                // skuId 必须赋值
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insertSelective(skuImage);
            }
        }

        //插入平台属性信息
        SkuAttrValue skuAttrValue1 = new SkuAttrValue();
        skuAttrValue1.setSkuId(skuId);
        skuAttrValueMapper.delete(skuAttrValue1);
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if(skuAttrValueList!=null && skuAttrValueList.size()>0){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                if(StringUtils.isEmpty(skuAttrValue.getId())){
                    skuAttrValue.setId(null);
                }
                skuAttrValue.setSkuId(skuId);
                skuAttrValueMapper.insertSelective(skuAttrValue);
            }
        }

        //插入销售属性值
        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuSaleAttrValue.setSkuId(skuId);
        skuSaleAttrValueMapper.delete(skuSaleAttrValue);
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if(skuSaleAttrValueList.size()>0 && skuSaleAttrValueList!=null){
            for (SkuSaleAttrValue saleAttrValue : skuSaleAttrValueList) {
                if(StringUtils.isEmpty(saleAttrValue.getId())){
                    saleAttrValue.setId(null);
                }
                saleAttrValue.setSkuId(skuId);
                skuSaleAttrValueMapper.insertSelective(saleAttrValue);
            }
        }

    }

    @Override
    public SkuInfo getSkuInfo(String skuId) {
        SkuInfo skuInfo = null;
        //先检查redis中有没有需要的信息，没有，将查询到的信息放到redis中
        Jedis jedis = redisUtil.getJedis();
        String skuInfoKey = ManageConst.SKUKEY_PREFIX + skuId + ManageConst.SKUKEY_SUFFIX;
        if(jedis.exists(skuInfoKey)){//如果redis中有，就从redis中取
            String skuInfoJson  = jedis.get(skuInfoKey);
            if(!StringUtils.isEmpty(skuInfoJson)){
                 skuInfo = JSON.parseObject(skuInfoJson, SkuInfo.class);
            }
        }else{//生成锁
            String skuLockKey = ManageConst.SKUKEY_PREFIX + skuId + ManageConst.SKULOCK_SUFFIX;
            jedis.set(skuLockKey,"ok", "nx", "px", ManageConst.SKULOCK_EXPIRE_PX);
            if("ok".equals(skuLockKey)){
                skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
                SkuImage skuImage = new SkuImage();
                skuImage.setSkuId(skuId);
                List<SkuImage> skuImageList = skuImageMapper.select(skuImage);
                skuInfo.setSkuImageList(skuImageList);
                String jsonString = JSON.toJSONString(skuInfo);
                jedis.setex("skuInfoKey", ManageConst.skukey_timeout, jsonString );//将数据放入redis中
            }else{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getSkuInfo(skuId);
            }


        }
        jedis.close();
       return skuInfo;
    }


}
