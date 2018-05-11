package com.palmjoys.yf1b.act.reward.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.framework.utils.RandomUtils;
import com.palmjoys.yf1b.act.reward.model.RandAttrib;
import com.palmjoys.yf1b.act.reward.model.RewareGroupAttrib;
import com.palmjoys.yf1b.act.reward.model.RewareItemAttrib;
import com.palmjoys.yf1b.act.reward.resource.RewardConfig;

@Component
public class RewardRandManager {

	@Static
	private Storage<String, RewardConfig> rewardCfgs;
	//获取奖励物品列表
	public List<GameObject> getRewareItems(String rewareId){
		RewardConfig rewardConfig = rewardCfgs.get(rewareId, false);
		if(null == rewardConfig){
			return null;
		}
		RewareGroupAttrib[] rewareGroupAttrib = rewardConfig.getRewareGroup();
		
		RandAttrib []randAttrib = rewardConfig.getSrand();
		if(null == randAttrib || 0 ==randAttrib.length
				|| randAttrib[0].type<1 ||  randAttrib[0].type>4
				|| randAttrib[0].num <= 0){
			return null;
		}
		
		if(null==rewareGroupAttrib || 0==rewareGroupAttrib.length){
			return null;
		}
		List<GameObject> retItems = null;
		
		switch(randAttrib[0].type){
		case 1:
			retItems = this.getRewareItems_01(randAttrib[0].num, rewareGroupAttrib);
			break;
		case 2:
			retItems = this.getRewareItems_02(randAttrib[0].num, rewareGroupAttrib);
			break;
		case 3:
			retItems = this.getRewareItems_03(randAttrib[0].num, rewareGroupAttrib);
			break;
		case 4:
			retItems = this.getRewareItems_04(randAttrib[0].num, rewareGroupAttrib);
			break;
		}
		
		return retItems;
	}
	
	//每组物品按物品概率随机获取N件
	private List<GameObject> getRewareItems_01(int N, RewareGroupAttrib[] rewareGroup){
		if(null == rewareGroup || 0==rewareGroup.length || N<=0){
			return null;
		}
		List<GameObject> retObjects = new ArrayList<>();
		for(RewareGroupAttrib rewareGroupAttrib : rewareGroup){
			if(null == rewareGroupAttrib.items || 0==rewareGroupAttrib.items.length){
				continue;
			}
			
			if(N >= rewareGroupAttrib.items.length){
				//本组获取所有物品
				for(RewareItemAttrib rewareItem : rewareGroupAttrib.items){
					boolean bFind = false;
					for(GameObject findObj : retObjects){
						if(findObj.type.equalsIgnoreCase(rewareItem.type)
								&& findObj.code == rewareItem.code){
							//有同一个物品了,改变物品数量
							findObj.amount += rewareItem.amount;
							bFind = true;
							break;
						}
					}
					
					if(bFind == false){
						//新物品添加到列表中
						GameObject gameObject = new GameObject();
						gameObject.type = rewareItem.type;
						gameObject.code = rewareItem.code;
						gameObject.amount = rewareItem.amount;
						retObjects.add(gameObject);
					}
				}
				continue;
			}
			
			//保存本组物品
			List<RewareItemAttrib> allRewareItems = new ArrayList<>();
			for(RewareItemAttrib rewareItem : rewareGroupAttrib.items){
				allRewareItems.add(rewareItem);
			}
			
			for(int i=0; i<N; i++){
				//先计算本组物品总概率
				int totalRate = 0;
				for(RewareItemAttrib rewareItem : allRewareItems){
					totalRate += rewareItem.rate;
				}			
				
				int randRate = RandomUtils.getRandomIntNum(0, totalRate);
				totalRate = 0;
				for(RewareItemAttrib rewareItem : allRewareItems){
					if(randRate >= totalRate && randRate<(totalRate+rewareItem.rate)){
						//命中此物品
						boolean bFind = false;
						for(GameObject findObj : retObjects){
							if(findObj.type.equalsIgnoreCase(rewareItem.type)
									&& findObj.code == rewareItem.code){
								//有同一个物品了,改变物品数量
								findObj.amount += rewareItem.amount;
								bFind = true;
								break;
							}
						}
						
						if(bFind == false){
							//新物品添加到列表中
							GameObject gameObject = new GameObject();
							gameObject.type = rewareItem.type;
							gameObject.code = rewareItem.code;
							gameObject.amount = rewareItem.amount;
							retObjects.add(gameObject);
						}
						
						//删除命中的物品
						allRewareItems.remove(rewareItem);
						break;
					}
					totalRate += rewareItem.rate;
				}
			}
		}
		
		return retObjects;
	}
	
	//获得所有物品
	private List<GameObject> getRewareItems_02(int N, RewareGroupAttrib[] rewareGroup){
		if(null == rewareGroup || 0==rewareGroup.length || N<=0){
			return null;
		}
		List<GameObject> retObjects = new ArrayList<>();
		for(RewareGroupAttrib rewareGroupAttrib : rewareGroup){
			if(null == rewareGroupAttrib.items || 0==rewareGroupAttrib.items.length){
				continue;
			}
			for(RewareItemAttrib rewareItem : rewareGroupAttrib.items){
				boolean bFind = false;
				for(GameObject findObj : retObjects){
					if(findObj.type.equalsIgnoreCase(rewareItem.type)
							&& findObj.code == rewareItem.code){
						//有同一个物品了,改变物品数量
						findObj.amount += rewareItem.amount;
						bFind = true;
						break;
					}
				}
				
				if(bFind == false){
					//新物品添加到列表中
					GameObject gameObject = new GameObject();
					gameObject.type = rewareItem.type;
					gameObject.code = rewareItem.code;
					gameObject.amount = rewareItem.amount;
					retObjects.add(gameObject);
				}
			}
		}
		
		return retObjects;
	}
	
	//按分组概率获得一组所有物品
	private List<GameObject> getRewareItems_03(int N, RewareGroupAttrib[] rewareGroup){
		if(null == rewareGroup || 0==rewareGroup.length || N<=0){
			return null;
		}
		List<GameObject> retObjects = new ArrayList<>();
		
		int totalRate = 0;
		for(RewareGroupAttrib rewareGroupAttrib : rewareGroup){
			if(null==rewareGroupAttrib.items || 0==rewareGroupAttrib.items.length){
				continue;
			}
			totalRate += rewareGroupAttrib.rate;
		}
		
		int randRate = RandomUtils.getRandomIntNum(0, totalRate);
		totalRate = 0;
		for(RewareGroupAttrib rewareGroupAttrib : rewareGroup){
			if(null==rewareGroupAttrib.items || 0==rewareGroupAttrib.items.length){
				continue;
			}
			if(randRate>=totalRate && randRate<(totalRate+rewareGroupAttrib.rate)){
				//命中此组物品
				for(RewareItemAttrib rewareItem : rewareGroupAttrib.items){
					boolean bFind = false;
					for(GameObject findObj : retObjects){
						if(findObj.type.equalsIgnoreCase(rewareItem.type)
								&& findObj.code == rewareItem.code){
							//有同一个物品了,改变物品数量
							findObj.amount += rewareItem.amount;
							bFind = true;
							break;
						}
					}
					
					if(bFind == false){
						//新物品添加到列表中
						GameObject gameObject = new GameObject();
						gameObject.type = rewareItem.type;
						gameObject.code = rewareItem.code;
						gameObject.amount = rewareItem.amount;
						retObjects.add(gameObject);
					}
				}
				break;
			}			
			totalRate += rewareGroupAttrib.rate;
		}
		
		
		return retObjects;
	}
	
	//按分组概率获得随机一组物品,再从随机的一组中按物品概率获取N件
	private List<GameObject> getRewareItems_04(int N, RewareGroupAttrib[] rewareGroup){
		if(null == rewareGroup || 0==rewareGroup.length || N<=0){
			return null;
		}
		List<GameObject> retObjects = new ArrayList<>();
		
		int totalRate = 0;
		for(RewareGroupAttrib rewareGroupAttrib : rewareGroup){
			if(null==rewareGroupAttrib.items || 0==rewareGroupAttrib.items.length){
				continue;
			}
			totalRate += rewareGroupAttrib.rate;
		}
		
		int randRate = RandomUtils.getRandomIntNum(0, totalRate);
		totalRate = 0;
		for(RewareGroupAttrib rewareGroupAttrib : rewareGroup){
			if(null==rewareGroupAttrib.items || 0==rewareGroupAttrib.items.length){
				continue;
			}
			if(randRate>=totalRate && randRate<(totalRate+rewareGroupAttrib.rate)){
				//命中此组物品
				//保存此组原始物品
				List<RewareItemAttrib> allRewareItems = new ArrayList<>();
				for(RewareItemAttrib rewareItem : rewareGroupAttrib.items){
					allRewareItems.add(rewareItem);
				}
				
				for(int i=0; i<N; i++){
					//计算此组物品总概率值
					int theTotalRate = 0;
					for(RewareItemAttrib rewareItem : allRewareItems){
						theTotalRate += rewareItem.rate;
					}
					int theRandRate = RandomUtils.getRandomIntNum(0, theTotalRate);
					theTotalRate = 0;
					for(RewareItemAttrib rewareItem : allRewareItems){
						if(theRandRate >= theTotalRate && theRandRate<(theTotalRate+rewareItem.rate)){
							//命中此物品
							boolean bFind = false;
							for(GameObject findObj : retObjects){
								if(findObj.type.equalsIgnoreCase(rewareItem.type)
										&& findObj.code == rewareItem.code){
									//有同一个物品了,改变物品数量
									findObj.amount += rewareItem.amount;
									bFind = true;
									break;
								}
							}
							
							if(bFind == false){
								//新物品添加到列表中
								GameObject gameObject = new GameObject();
								gameObject.type = rewareItem.type;
								gameObject.code = rewareItem.code;
								gameObject.amount = rewareItem.amount;
								retObjects.add(gameObject);
							}
							
							//删除命中的物品
							allRewareItems.remove(rewareItem);
							break;
						}
					}
				}
				break;
			}			
			totalRate += rewareGroupAttrib.rate;
		}
		
		return retObjects;
	}
}
