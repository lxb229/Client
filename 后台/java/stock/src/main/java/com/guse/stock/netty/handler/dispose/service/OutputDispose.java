package com.guse.stock.netty.handler.dispose.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guse.stock.base.ConfigReader;
import com.guse.stock.common.CertificateHelper;
import com.guse.stock.common.JSONUtils;
import com.guse.stock.common.StockCerUtil;
import com.guse.stock.dao.IGameDao;
import com.guse.stock.dao.IGameParDao;
import com.guse.stock.dao.IPluginOrderDao;
import com.guse.stock.dao.IStockCerDao;
import com.guse.stock.dao.IStockDao;
import com.guse.stock.dao.IStockUseLogDao;
import com.guse.stock.dao.IUserAuthoriseDao;
import com.guse.stock.dao.IUserDao;
import com.guse.stock.dao.StockCerDao;
import com.guse.stock.dao.model.Game;
import com.guse.stock.dao.model.GamePar;
import com.guse.stock.dao.model.Stock;
import com.guse.stock.dao.model.StockCer;
import com.guse.stock.dao.model.StockUseLog;
import com.guse.stock.dao.model.User;
import com.guse.stock.dao.vo.StockUseLogVo;
import com.guse.stock.netty.handler.MessageCode;
import com.guse.stock.netty.handler.dispose.AbstractDispose;

/** 
* @ClassName: OutputDispose 
* @Description: 出库
* @author Fily GUSE
* @date 2017年8月28日 下午5:44:31 
*  
*/
@Component
public class OutputDispose extends AbstractDispose {

	
	/** 
	* @Fields pt : 协议号
	*/
	public Integer pt = 2;
	
	@Autowired
	private IUserAuthoriseDao dao;
	
	@Autowired
	private IGameDao gameDao;
	
	@Autowired
	private IGameParDao gameParDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IPluginOrderDao pluginOrderDao;
	
	@Autowired
	private IStockDao stockDao;
	
	@Autowired
	private IStockCerDao stockCerDao;
	
	@Autowired
	private IStockUseLogDao stockUseLogDao;
	
	@Override
	public void processing() {
		ConfigReader reader = new ConfigReader();
		String gameIds = reader.read("gameIds");
		
		// 获取客户端请求参数
		String data  = msg.getData();
		JSONObject object = JSONUtils.toJSONObject(data);
		String user_id = object.getString("user_id"); //用户id
		String identify = object.getString("identify"); //面值标识
		String game_id = object.getString("game_id"); //游戏标识
		String device_number = object.getString("device_number"); //设备号
		String version  = object.getString("version"); // 版本号
		// 调用PHP接口验证用户信息
		
		// 验证成功之后直接获取用户对象
		// 获取用户数据
		User user = userDao.finbByUserId(user_id);
		if( StringUtils.isBlank(game_id) ) {
			// 如果游戏id为空直接返回游戏id为空数据错误信息
			msg.setCode(MessageCode.NULLGAMEID);
			return;
		} else if (StringUtils.isBlank(identify)) {
			// 如果面值id为空直接返回面值id为空数据错误信息
			msg.setCode(MessageCode.NULLPERID);
			return;
		}
		
		// 获取游戏信息
		Game game = gameDao.findByIdentify(game_id);
		if(game == null) {
			// 如果游戏为空，直接返回游戏为空数据错误信息
			msg.setCode(MessageCode.NOGAMEID);
			return;
		}
		
		// 获取游戏面额信息
		GamePar gamePar = gameParDao.findByGameAndIdentify(game.getGame_id(), identify);
		if (gamePar == null) {
			// 如果游戏面值为空，直接返回游戏面值为空数据错误信息
			msg.setCode(MessageCode.NOPERID);
			return;
		}
		String gameId = game.getGame_id()+";";
		
		// 排序信息
		String orderbys = " stock_id asc ";
		if(gameIds.indexOf(gameId) > 0) {
			orderbys = "stock_id desc";
		}
		
		// 获取库存信息
		Stock stock = stockDao.findByUserGamePer(user.getId(), game.getGame_id(), gamePar.getPar_id(), orderbys);
		if(stock == null) {
			// 如果库存不存在，直接返回库存不存在数据错误信息
			msg.setCode(MessageCode.NOSTOCK);
			return;
		}
		// 获取凭证对象  从mongodb中获取凭证
//		StockCer stockCer = stockCerDao.findByStock(stock.getStock_id());
		// 拼装出凭证对象集合名称
		String cerColletion = "pl_stock_cer"+stock.getCer_table_num();
		StockCer stockCer = null;
		StockCerDao cerDao = new StockCerDao(); 
		try {
			stockCer = cerDao.seachStockCer(cerColletion,stock.getStock_id());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			cerDao.closeDao();
		}
		if(stockCer == null) {
			// 如果凭证不存在，直接返回凭证不存在数据错误信息
			msg.setCode(MessageCode.NOCERTIFICATE);
			return;
		}
		
		//保存出库记录
		StockCerUtil util  = new StockCerUtil();
		StockUseLog stockUseLog = new StockUseLog();
		stockUseLog.setNumber(util.randomOrder());
		stockUseLog.setUser_id(user.getId());
		stockUseLog.setStock_id(stock.getStock_id());
		stockUseLog.setPoundage(0d);
		stockUseLog.setMark("");
		stockUseLog.setCreate_time(System.currentTimeMillis()/1000);
		stockUseLogDao.addStockUseLog(stockUseLog);
		if(stockUseLog.getId() != null && stockUseLog.getId() > 0) {
			StockUseLogVo useLogVo = new StockUseLogVo();
			// 将数据库中的ios_6库存解密之后返回客户端
			CertificateHelper helper = new CertificateHelper();
			useLogVo.setIos_6(helper.decrypt(stockCer.getIos_6()));
			useLogVo.setIos_7(stockCer.getIos_7());
			useLogVo.setOrderid(stockUseLog.getNumber());
			useLogVo.setStockid(stockUseLog.getStock_id());
			JSONObject jsonObj = JSONUtils.toJSONObject(useLogVo);
			msg.setData(jsonObj.toString());
			
		} else {
			// 如果出库记录保存失败，直接返回出库记录保存失败数据错误信息
			msg.setCode(MessageCode.SAVEERRORSTOCKUSELOG);
			return;
		}
	}

}
