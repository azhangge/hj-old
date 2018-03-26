package com.hjedu.common.controller;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.common.dao.DataTransferDAO;
import com.hjedu.common.entity.MasterTable;
import com.hjedu.common.entity.RelTable;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class DataTransfer {
	private static final long serialVersionUID = 1L;
	
	DataTransferDAO dataTransferDAO = SpringHelper.getSpringBean("DataTransferDAO");
	

    @SuppressWarnings("unused")
	@PostConstruct
    public void init() {
    	
    }

    public void transferData(){
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	DataTransferDAO dataTransferDAO = SpringHelper.getSpringBean("DataTransferDAO");
		List<MasterTable> mtl = dataTransferDAO.findToBeUpdatedMasterTableList();
		if(mtl!=null && mtl.size()>0){
			for(MasterTable mt:mtl){
//				MasterTable mt=mtl.get(5);
				String aTable = mt.getaTable();

				List<String> idList = dataTransferDAO.findATableIdList(aTable);//查询主表所有数据
				List<RelTable> rtl = dataTransferDAO.findToBeUpdatedRelTableListByFTableName(mt.getaTable());//查询主表对应的所有从表信息
				//判断是不否有从表
				if(rtl!=null && rtl.size()>0){
					//循环更新所主表ID
					for(String id:idList){
						String newId;
						if(id.equals("10000000000000000")){
							newId=businessId;
						}else{
							newId=UUID.randomUUID().toString();
						}
						
						//先更新主表ID
						dataTransferDAO.updateRelTableService(mt.getaTable(), mt.getaKey(), newId, id);
						//循环更新 所有从表信息
						if(rtl!=null && rtl.size()>0){
							for(RelTable rt:rtl){
								String dataType = "";
								if(StringUtil.isNotEmpty(rt.getDataType())){
									dataType = rt.getDataType();
									if(dataType.equals(",") || dataType.equals(";")){
										dataTransferDAO.updateRelTableStrService(rt.getaTable(),rt.getfKey(), newId, id);
									}
								}else{
									dataTransferDAO.updateRelTableService(rt.getaTable(),rt.getfKey(), newId, id);
								}
							}
						}
					}
				}else{
					//批量更新主表键值
					dataTransferDAO.updateMTableIDsService(mt.getaTable(), mt.getaKey());
				}
				
				
				
				//主表已更新设置状态 
				mt.setStatus(1);
				dataTransferDAO.updateMasterTable(mt);
			}
		}
    } 
    
}
