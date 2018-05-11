package com.guse.stock.netty.handler.dispose.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

import com.guse.stock.common.Base64Util;
import com.guse.stock.common.CertificateHelper;
import com.guse.stock.common.JSONUtils;
import com.guse.stock.common.MD5Util;
import com.guse.stock.dao.IGameDao;
import com.guse.stock.dao.IGameParDao;
import com.guse.stock.dao.IStockCerDao;
import com.guse.stock.dao.IStockCountsDao;
import com.guse.stock.dao.IStockDao;
import com.guse.stock.dao.IStockLogDao;
import com.guse.stock.dao.StockCerDao;
import com.guse.stock.dao.model.Game;
import com.guse.stock.dao.model.GamePar;
import com.guse.stock.dao.model.Stock;
import com.guse.stock.dao.model.StockCer;
import com.guse.stock.dao.model.StockCerSplit;
import com.guse.stock.dao.model.StockCounts;
import com.guse.stock.dao.model.StockLog;
import com.guse.stock.netty.handler.MessageCode;
import com.guse.stock.netty.handler.dispose.AbstractDispose;

/**
 * @ClassName: PutawayDispose
 * @Description: 入库
 * @author Fily GUSE
 * @date 2017年8月28日 下午5:42:39
 * 
 */
@Component
public class InputDispose extends AbstractDispose {

	/**
	 * @Fields pt : 协议号
	 */
	public Integer pt = 1;

	@Autowired
	IGameDao gameDao;
	@Autowired
	IGameParDao gameParDao;
	@Autowired
	IStockDao stockDao;
	@Autowired
	IStockCerDao stockCerDao;
	@Autowired
	IStockLogDao stockLogDao;
	@Autowired
	IStockCountsDao stockCountDao;

	@Override
	public void processing() {
		
		// 解析业务数据
		JSONObject data = JSONUtils.toJSONObject(msg.getData());
		// 获取数据值
		long user_id = data.getLong("user_id");
		String game_id = data.getString("game_id");
		String identify = data.getString("identify");
		String ios_6 = data.getString("ios_6");
		String ios_7 = data.getString("ios_7");
		String tran_id = data.getString("tran_id");
		String version = data.getString("version");

		try {
			// 查询用户和登录用户不一致
			if(!handler.userInfo.getUid().equals(user_id)) {
				msg.setError(MessageCode.INVALIDDATA);
				return;
			}
			// 验证ios_6凭证
			String hash6 = MD5Util.string2MD5(ios_6);
			String hash7 = MD5Util.string2MD5(ios_7);
			// 验证凭证是否有效
			if(hash6.equals(hash7)) {
				msg.setError(MessageCode.FORGECERTIFICATE);
				return ;
			}
			// 解密ios6凭证
			try {
				String i_6 = new String(Base64Util.decode(ios_6));
				JSONObject i6Json = JSONUtils.toJSONObject(i_6);
				String certificate = new String(Base64Util.decode(i6Json.getString("purchase-info")));
				JSONObject cJson = JSONUtils.toJSONObject(certificate);
//				解密ios_6获取凭证交易id，游戏id，档位id，凭证时间
//				解密后凭证交易id不存在或者解密后的凭证交易id与客户端传的凭证交易id参数进行验证不一致，则该凭证无效
//				解密后游戏id不存在，则该凭证无效
//				解密后的游戏id与客户端传的游戏id参数进行验证，如果不一致，以解密后的游戏id为准
//				解密后的档位id不存在，则该凭证无效
//				解密后的档位id与客户端传的档位id参数进行验证，如果不一致，以解密后的档位id为准
//				解密后凭证时间不存在，则该凭证无效
//				解密后的凭证时间需要验证一次凭证时间有效性，验证公式：服务器时间-(凭证时间+时差8小时)>8小时，则该凭证无效
//				批注：时差8小时和最后的8小时都是可配置的，现阶段所有游戏统一使用一个配置管理
				Object tid = cJson.get("transaction-id"); // 凭证交易id
				Object gid = cJson.get("bid"); // 游戏id
				Object iid = cJson.get("product-id"); // 档位id
				Object time = cJson.get("original-purchase-date"); // 凭证时间
				if(tid==null || gid==null || iid== null || time==null || !tid.toString().equals(tran_id)) {
					msg.setError(MessageCode.INVALIDCERTIFICATEDE);
					return;
				}
				// 游戏id和档位id已凭证为准
				game_id = gid.toString();
				identify = iid.toString();
				// 验证游戏有效性
				int mistiming = 8; // 时差小时
				int validTime = 8; // 有效期时间小时
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date ptime = df.parse(time.toString().substring(0,19));
				if(new Date().getTime() - (ptime.getTime() + (mistiming*1000*60*60)) > (validTime*1000*60*60)) {
					msg.setError(MessageCode.INVALIDCERTIFICATEDE);
					return;
				}
								
			}catch(Exception e) {
				e.printStackTrace();
				msg.setError(MessageCode.CERTIFICATEDECODE);
				return;
			}
			
			// 获取凭证分表信息
			StockCerSplit split = stockCerDao.getCerSplit();
						
			// 验证凭证交易id是否重复
			for(int r = split.getCer_index(); r >= 0; r--) {
				String index = r!=0 ? r+"": "";
				if(stockCerDao.findByHash(split.getSource_table()+index, hash6) != null) {
					msg.setError(MessageCode.REPETITIONTRANCODE);
					return;
				}
			}
			
			// 凭证分表
			cerSplit(split);
			/**
			 * 	操作入库
			 * */
			// 判断游戏是否存在
			Game game = gameDao.findByIdentify(game_id);
			if (game == null) { // 游戏不存在时，创建游戏
				game = new Game("未知", game_id);
				gameDao.addGame(game);
			}
			// 判断游戏面值是否存在
			GamePar gamePar = gameParDao.findByGameAndIdentify(game.getGame_id(),identify);
			if (gamePar == null) {
				gamePar = new GamePar(game.getGame_id(), identify);
				gamePar.setPar(0d);
				gameParDao.addGamePar(gamePar);
			}
			
			// 操作时间
			Long create_time = new Date().getTime() / 1000;
			
			// 验证凭证交易ID是否存在。需要在各种凭证分表中查询
			StockCer stockCer = null;
			for(int r = split.getCer_index(); r >= 0; r--) {
				String index = r!=0 ? r+"": "";
				stockCer = stockCerDao.findByTran(split.getSource_table() + index,tran_id);
				if(stockCer != null) {
					break;
				}
			}
			// 添加库存信息
			Stock stock = null;
			// 凭证已存在时，添加凭证信息，库存信息不变
			if (stockCer != null) { 
				stock = stockDao.findByStock(stockCer.getStock_id());
			} else {
				stock = new Stock();
				stock.setUser_id(handler.userInfo.pid);
				stock.setProducer_id(handler.userInfo.uid);
				stock.setGame_id(game.getGame_id());
				stock.setPar_id(gamePar.getPar_id());
				stock.setCreate_time(create_time);
				stock.setCer_table_num(split.getCer_index());
				stockDao.addStock(stock);
				
				// 增加库存记录
				StockCounts counts = stockCountDao.findByUserGamePer(user_id, game.getGame_id(), gamePar.getPar_id());
				if(counts == null) {
					counts = new StockCounts(user_id, game.getGame_id(), gamePar.getPar_id(), 1);
					stockCountDao.addStockCounts(counts);
				} else {
					stockCountDao.accumStockCounts(counts.getId());
				}
			}
			
			// 添加凭证信息
			stockCer = new StockCer();
			stockCer.setStock_id(stock.getStock_id());
			stockCer.setIos_6(new CertificateHelper().encrypt(ios_6));
			stockCer.setIos_7(ios_7);
			stockCer.setHash(hash6);
			stockCer.setTran_id(tran_id);
			stockCer.setCreate_time(create_time);
			stockCerDao.addStockCer(split.getCer_name(),stockCer);
			// 保存凭证信息到mongoDB
			StockCerDao cerDao = new StockCerDao();
			cerDao.savePlayer(split.getCer_name(),stockCer);
			cerDao.closeDao();
			// 分表记录表数据量+1
			stockCerDao.accoumCount(split.getCer_name());
			
			// 添加入库记录
			StockLog stockLog = new StockLog();
			stockLog.setStock_id(stock.getStock_id());
			stockLog.setProducer_id(stock.getProducer_id());
			stockLog.setGame_id(stock.getGame_id());
			stockLog.setPar_id(stock.getPar_id());
			stockLog.setCreate_time(create_time);
			stockLogDao.addStockLog(stockLog);
			
			// 响应信息
			JSONObject result = new JSONObject();
			result.put("order_id", stock.getStock_id());
			
			msg.setData(JSONUtils.toJSONString(result));

		} catch (Exception e) {
			e.printStackTrace();
			msg.setError(MessageCode.SERVERERROR);
		}
	}
	
	/** 
	* @Title: cerSplit
	* @Description: 凭证分表
	* @param @param split
	* @return void
	* @throws 
	*/
	private void cerSplit(StockCerSplit split) {
		// 凭证分表
		if(split.getCount() >= split.getCeiling()) {
			// 设置分表信息
			split.setCer_index(split.getCer_index() + 1);
			split.setCer_name(split.getSource_table()+split.getCer_index());
			split.setCount(0);
			split.setId(null);
			// 创建新表,新表结构为上一个版本结构
			stockCerDao.createCerSplitTable(split.getCer_name(), split.getSource_table()+(split.getCer_index()-1));
			// 条件分表记录数据
			stockCerDao.addCerSplit(split);
		}
	}

}
