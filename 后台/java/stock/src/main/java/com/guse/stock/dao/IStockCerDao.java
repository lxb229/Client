package com.guse.stock.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.stock.dao.model.StockCer;
import com.guse.stock.dao.model.StockCerSplit;

/** 
* @ClassName: IStockCerDao 
* @Description: 凭证数据接口
* @author Fily GUSE
* @date 2017年8月30日 下午8:13:07 
*  
*/
@Repository
public interface IStockCerDao {
	
	/** 
	* @Title: findByTran 
	* @Description: 根据交易id获取凭证信息 
	* @param @param tran_id
	* @param @return
	* @return StockCer 
	* @throws 
	*/
	@Select("select * from ${tableName} t where t.tran_id=#{tran_id} limit 1")
	public StockCer findByTran(@Param("tableName")String tableName,@Param("tran_id") String tran_id);
	
	/** 
	* @Title: findByHash 
	* @Description: 根据hash获取凭证信息 
	* @param @param hash
	* @param @return
	* @return StockCer 
	* @throws 
	*/
	@Select("select * from ${tableName} t where t.hash=#{hash} ")
	public StockCer findByHash(@Param("tableName")String tableName,@Param("hash") String hash);
	
	/** 
	* @Title: addStockCer 
	* @Description: 添加凭证信息 
	* @param @param stockCer
	* @param @return
	* @return int 
	* @throws 
	*/
	@Insert("insert into ${tableName}(stock_id, ios_6, ios_7, hash, tran_id, create_time) "
			+ "values(#{stockCer.stock_id}, #{stockCer.ios_6}, #{stockCer.ios_7}, #{stockCer.hash}, #{stockCer.tran_id}, #{stockCer.create_time})")
	public void addStockCer(@Param("tableName")String tableName,@Param("stockCer")StockCer stockCer);
	
	/** 
	* @Title: getCerSplit 
	* @Description: 获取最新的分表信息 
	* @param @return
	* @return StockCerSplit 
	* @throws 
	*/
	@Select("select * from pl_stock_cer_split order by id desc limit 1")
	public StockCerSplit getCerSplit();
	
	/** 
	* @Title: addCount 
	* @Description: 子表统计数加1 
	* @param @param cerSplitId
	* @return void 
	* @throws 
	*/
	@Update("update pl_stock_cer_split set count = count+1 where cer_name=#{cer_name}")
	public void accoumCount(@Param("cer_name")String cer_name);
	
	/** 
	* @Title: addCerSplit 
	* @Description: 创建凭证子表 
	* @param @param split
	* @return void 
	* @throws 
	*/
	@Insert("insert into pl_stock_cer_split(cer_index, cer_name, count, ceiling) "
			+ " values(#{split.cer_index}, #{split.cer_name}, #{split.count}, #{split.ceiling})")
	@Options(useGeneratedKeys = true, keyProperty = "split.id")
	public void addCerSplit(@Param("split")StockCerSplit split);
	
	/** 
	* @Title: createCerSplitTable 
	* @Description: 创建凭证分表 
	* @param @param tableName 表名
	* @param @param sourceTable 源表
	* @return void 
	* @throws 
	*/
	@Update("create table ${tableName} like ${sourceTable}")
	public void createCerSplitTable(@Param("tableName")String tableName,@Param("sourceTable") String sourceTable);

}
