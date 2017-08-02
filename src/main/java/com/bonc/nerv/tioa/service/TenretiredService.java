/*
 * 文件名：TenretiredService.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ymm
 * 修改时间：2017年7月28日
 */

package com.bonc.nerv.tioa.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bonc.nerv.tioa.dao.TenretiredDao;
import com.bonc.nerv.tioa.entity.SearchTenretiredData;
import com.bonc.nerv.tioa.entity.TenretiredEntity;
import com.bonc.nerv.tioa.util.ExcelUtil_Extend;
import com.bonc.nerv.tioa.util.ResultPager;

@Service
public class TenretiredService {
   
    @Autowired
    TenretiredDao  tenretiredDao;
    
    /**
     * Description:添加分页的查询
     * @param searchData
     * @param start
     * @param length
     * @param draw
     * @return 
     * @see
     */
    public  String findTenretiredList(SearchTenretiredData searchData, Integer start, Integer length, String draw){
        PageRequest pageRequest=null;
        Map<String,Object> resultMap=new HashMap<>();
        if(start==null){
            pageRequest=ResultPager.buildPageRequest(start, length);
        }else{
            pageRequest=ResultPager.buildPageRequest(start/length+1, length);
        }
        
        //根据查询条件查询
        Specification<TenretiredEntity> querySpecifi=tenretiredSearch(searchData);
        //分页
        Page<TenretiredEntity> tenList=this.tenretiredDao.findAll(querySpecifi,pageRequest);
        resultMap.put("draw",draw);
        resultMap.put("recoredsTotal",tenList.getTotalElements());
        resultMap.put("recordsFiltered", tenList.getTotalElements());
        resultMap.put("data", tenList.getContent());
        return JSON.toJSONString(resultMap,SerializerFeature.DisableCircularReferenceDetect);
    }
    
    
     /**
      * Description: 多条件查询方法
      * @param bean
      * @return 
      * @see
      */
     private Specification<TenretiredEntity> tenretiredSearch(SearchTenretiredData bean) {
  
     //封装查询参数
      Specification<TenretiredEntity> querySpecifi=new Specification<TenretiredEntity>(){
      
      //内部类
      @Override
      public Predicate toPredicate(Root<TenretiredEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
          List<Predicate> predicates = new ArrayList<Predicate>();

          if (StringUtils.isNotBlank(bean.getTenantName())) {
              predicates.add(cb.like(root.<String> get("tenantName"), "%" + bean.getTenantName() + "%"));
          }

          if (StringUtils.isNotBlank(bean.getTenantInterface())) {
              predicates.add(cb.like(root.<String> get("tenantInterface"), "%" + bean.getTenantInterface() + "%"));
          }

          return cb.and(predicates.toArray(new Predicate[predicates.size()]));
       }
    };
     return querySpecifi;
 }
    
     
    /**
     * 根据条件导出用户记录信息
     * @return 集合
     * @see
     */
    public List<TenretiredEntity> exportTenretired(SearchTenretiredData searchData) {
        Specification<TenretiredEntity> querySpecifi = tenretiredSearch(searchData);
        return tenretiredDao.findAll(querySpecifi);
    }
    
    
    /**
     * 
     * Description: 保存新增退租用户
     * @param tenretiredEntity
     * @return 
     * @see
     */
    public String save(TenretiredEntity tenretiredEntity){
        Map<String,Object> map=new HashMap<String,Object>();
        try {
            tenretiredDao.save(tenretiredEntity);
            map.put("status", "200");
        }
        catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
        }
       
        return JSON.toJSONString(map);
    }
    
    /**
     * 删除一条已退租户记录
     * @param tlId 
     * @see
     */
    public void delete(Long tlId) {
        TenretiredEntity tenretiredEntity = tenretiredDao.findOne(tlId);
        tenretiredDao.delete(tenretiredEntity );
    }
    
    /**
     * 
     * 导出Excel表
     * @param askDate 
     * @param searchData 搜索条件
     * @return 符合条件的集合
     * @see
     */
    public void getExcel(List<TenretiredEntity> list,HttpServletResponse response){
        try {
           String[] headers={"序号","服务类型","租户","租户级别","租户负责人","联系电话","资源类型","访问IP","主机数量","存储使用量","存储使用量单位","计算资源","机房","统一平台数量","4A数量","需求","服务名","队列名","申请日期","开放日期","变更时间","退租时间","平台接口人","备注"};
           List<String[]> dataset=getTenList(list);
           ExcelUtil_Extend.exportExelMerge("测试.xls", headers, dataset, true, response, new Integer[] {1,2,3,4,5,13,14,15,22}, new Integer[] {1,2,3,4,5,13,14,15,22}, new Integer[] {2,3}, new Integer[]{4});
         System.out.println("excel导出成功！");  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }
    
    /**
     * 
     * Description: 数据放到list集合中
     * @param list
     * @return 
     * @see
     */
    public  List<String[]> getTenList(List<TenretiredEntity> list){
        List<String[]> dataset=new ArrayList<String[]>();
        for(int i=0,size=list.size();i<size;i++){
            TenretiredEntity tenretiredEntity=list.get(i);
            String serviceType=tenretiredEntity.getServiceType();
            String tenantName=tenretiredEntity.getTenantName();
            String tenantLevel=tenretiredEntity.getTenantLevel()==null?"":Integer.toString(tenretiredEntity.getTenantLevel());
            String tenantBoss=tenretiredEntity.getTenantBoss();
            String tenantTel=tenretiredEntity.getTenantTel();
            String resourceType=tenretiredEntity.getResourceType()==null?"":Integer.toString(tenretiredEntity.getTenantLevel());
            String askIp=tenretiredEntity.getAskIp();
            String hostNum=tenretiredEntity.getHostNum()==null?"":Integer.toString(tenretiredEntity.getHostNum());
            String storage=tenretiredEntity.getStorage()==null?"":Integer.toString(tenretiredEntity.getStorage());
            String storageUnit=tenretiredEntity.getStorageUnit();
            String computingResourceRate=Double.toString(tenretiredEntity.getComputingResourceRate())==null?"":Double.toString(tenretiredEntity.getComputingResourceRate());
            String computeRoom=tenretiredEntity.getComputeRoom();
            String uniplatformNum=tenretiredEntity.getUniplatformNum()==null?"":Integer.toString(tenretiredEntity.getUniplatformNum());
            String numOf4a=tenretiredEntity.getNumOf4a()==null?"":Integer.toString(tenretiredEntity.getNumOf4a());
            String demand=tenretiredEntity.getDemand();
            String serviceName=tenretiredEntity.getServiceName();
            String sequenceName=tenretiredEntity.getSequenceName();
            String askDate=tenretiredEntity.getAskDate();
            String openDate=tenretiredEntity.getOpenDate();
            String changeDate=tenretiredEntity.getChangeDate();
            String endRentDate=tenretiredEntity.getEndRentDate();
            String tenantInterface=tenretiredEntity.getTenantInterface();
            String remark=tenretiredEntity.getRemark();
            switch (tenretiredEntity.getTenantLevel()) {
                case 0:
                    tenantLevel= "小";
                    break;
                case 1:
                    tenantLevel= "中";
                    break;
                case 2:
                    tenantLevel= "大";
                    break;
            }
            
            switch (tenretiredEntity.getResourceType()) {
                case 1:
                    resourceType= "Fiume";
                    break;
                case 2:
                    resourceType= "FTP集群";
                    break;
                case 3:
                    resourceType= "Hbase";
                    break;
                case 4:
                    resourceType= "hue";
                    break;
                case 5:
                    resourceType= "Hive";
                    break;
                case 6:
                    resourceType= "IMPALA";
                    break;
                case 7:
                    resourceType= "KAFKA";
                    break;
                case 8:
                    resourceType= "MPP";
                    break;
                case 9:
                    resourceType= "Mysql";
                    break;
                case 10:
                    resourceType= "Oracle";
                    break;
                case 11:
                    resourceType= "Redis";
                    break;
                case 12:
                    resourceType= "spark";
                    break;
                case 13:
                    resourceType= "storm";
                    break;
                case 14:
                    resourceType= "接口机";
                    break;
                case 15:
                    resourceType= "虚拟机";
                    break;
                case 16:
                    resourceType= "物理机";
                    break;
                case 17:
                    resourceType= "应用服务器";
                    break;
            }
            String[] service={Integer.toString(i+1),serviceType,tenantName,tenantLevel,tenantBoss,
                              tenantTel,resourceType,askIp,hostNum,storage,storageUnit,computingResourceRate,
                              computeRoom,uniplatformNum,numOf4a,demand,serviceName,sequenceName,askDate,openDate,
                              changeDate,endRentDate,tenantInterface,remark};
                    dataset.add(service);
            }
         return dataset;
    }
    
    /**
     * 
     * 根据id删除数据
     * @param tlId
     * @return 
     * @see
     */
    public boolean validateByTlId(String  tlId) {
        int num = tenretiredDao.findByTlId(Long.parseLong(tlId));
        return num == 0 ? false : true;
    }
    
 }
    
    
