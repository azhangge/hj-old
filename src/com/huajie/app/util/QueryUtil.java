package com.huajie.app.util;

import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.Query;

public class QueryUtil {
	/**
	 * 设置分页查询参数
	 * @param qu
	 * @param firstSize 起始行数
	 * @param pageSize 每页行数
	 */
	public static void setQuerySize(Query qu,int firstSize,int pageSize){
		if(pageSize>0){
			if(firstSize>=0){
				qu.setFirstResult(firstSize);
			}
			qu.setMaxResults(pageSize);
		}
	}
	
	/**
	 * 拼接模糊匹配查询条件
	 * @param filterMap 模糊匹配字段和值的map
	 * @param tableName 表名缩写
	 * @return
	 */
	public static String getQueryStrByFilter(Map<String, Object> filterMap,String tableName){
		String q = "";
		if(filterMap!=null){
			for (Entry<String, Object> entry : filterMap.entrySet()) {
				if(entry!=null){
					String filterProperty = entry.getKey();
					String filterValue = entry.getValue().toString();
					if (StringUtil.isNotEmpty(filterValue)) {
						q = q + " and "+tableName+"." + filterProperty + " like '%" + filterValue + "%' ";
					}
				}
			}
		}
		return q;
	}
	
	/**
	 * 拼接排序条件
	 * @param field 排序字段
	 * @param tableName 表名缩写
	 * @param type 排序方式（1倒序，0正序）
	 * @return
	 */
	public static String getOrderStrByOrder(String field,String tableName,int type){
		String str = "";
		if(StringUtil.isNotEmpty(field)){
			String t = type==1?"desc":"";
			str =  "order by "+tableName+"."+field+" "+t;
		}
		return str;
	}
	
	/**
	 * 获取结果集数量
	 * @param qu
	 * @return
	 */
	public static long getNumByQuery(Query qu){
		long num = 0;
		if(qu.getResultList()!=null){
			String n = String.valueOf(qu.getResultList().get(0));
			try {
				num = Long.parseLong(n);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return num;
	}
}
