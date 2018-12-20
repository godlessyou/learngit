package com.yootii.bdy.tmcase.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCasePreMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCasePreService;

@Service
public class TradeMarkCasePreServiceImpl implements TradeMarkCasePreService{
	
	@Resource
	private TradeMarkCasePreMapper tradeMarkCasePreMapper;
	
	@Resource
	private TradeMarkCaseCategoryMapper tradeMarkCaseCategoryMapper;
	
	@Override
	public ReturnInfo createTradeMarkCasePre(TradeMarkCasePre tradeMarkCasePre) {
		ReturnInfo info = new ReturnInfo();
		Integer custId = tradeMarkCasePre.getCustId();
		Integer agencyId = tradeMarkCasePre.getAgencyId();
		if(custId==null||agencyId==null){
			info.setSuccess(false);
			info.setMessage("客户ID和代理所ID不能为空");
			return info;
		}
		//创建预立案之前查询存在不存在该用户在该代理机构的预立案
		TradeMarkCasePre tmp = tradeMarkCasePreMapper.selectByCustIdAndAgencyId(custId, agencyId);
		if(tmp!=null){
			Integer casePreIdTmp = tmp.getId();
			//删除该预立案的商品和类别
			tradeMarkCaseCategoryMapper.deleteByCasePreId(casePreIdTmp);
			//删除该预立案
			tradeMarkCasePreMapper.deleteByCustIdAndAgencyId(custId, agencyId);
		}
		tradeMarkCasePreMapper.insertSelective(tradeMarkCasePre);
		//增加商品或服务
		addGoods(tradeMarkCasePre);
		info.setSuccess(true);
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("caseId", tradeMarkCasePre.getId());
		info.setData(data);//刚插入的记录，自增长的id已存在
		info.setMessage("添加案件成功");
		return info;
	}
	private void addGoods(TradeMarkCasePre tradeMarkCasePre){
		List<TradeMarkCaseCategory> goods = tradeMarkCasePre.getGoods();
		if(goods!=null&&goods.size()>0){
			for(TradeMarkCaseCategory good : goods){
				good.setCasePreId(tradeMarkCasePre.getId());
				tradeMarkCaseCategoryMapper.insertSelective(good);
			}
		}
	}
	@Override
	public ReturnInfo modifyTradeMarkCasePre(TradeMarkCasePre tradeMarkCasePre) {
		ReturnInfo info = new ReturnInfo();
		Integer casePreId = tradeMarkCasePre.getId();
		if(casePreId==null){
			info.setSuccess(false);
			info.setMessage("预立案Id不能为空");
			return info;
		}
		tradeMarkCasePreMapper.updateByPrimaryKeySelective(tradeMarkCasePre);
		//更新商品和服务
		updateGoods(tradeMarkCasePre);
		info.setSuccess(true);
		info.setMessage("修改案件成功");
		return info;
	}
	private void updateGoods(TradeMarkCasePre tradeMarkCasePre){
		Integer casePreId = tradeMarkCasePre.getId();
		if(casePreId==null){
			return;
		}
		List<TradeMarkCaseCategory> goods = tradeMarkCasePre.getGoods();
		TradeMarkCaseCategory tmpGood = new TradeMarkCaseCategory();
		tmpGood.setCasePreId(casePreId);
		List<TradeMarkCaseCategory> dbGoods = tradeMarkCaseCategoryMapper.selectByTradeMarkCaseCategory(tmpGood);
		Map<String, List<TradeMarkCaseCategory>> reMap = findListDiff(goods, dbGoods);
		if(reMap==null){
			return;
		}
		List<TradeMarkCaseCategory> goodsOnly = reMap.get("goodsOnly");
		List<TradeMarkCaseCategory> dbGoodsOnly = reMap.get("dbGoodsOnly");
		//增加界面中新增的
		if(goodsOnly!=null&&goodsOnly.size()>0){
			for(TradeMarkCaseCategory good : goodsOnly){
				good.setCasePreId(casePreId);
				tradeMarkCaseCategoryMapper.insertSelective(good);
			}
		}

		//删除界面没有的，数据库还存在的
		if(dbGoodsOnly!=null&&dbGoodsOnly.size()>0){
			for(TradeMarkCaseCategory dbGood : dbGoodsOnly){
				tradeMarkCaseCategoryMapper.deleteByPrimaryKey(dbGood.getId());
			}
		}
	}

	/*@Override
	public ReturnInfo queryTradeMarkCasePreList(TradeMarkCasePre tradeMarkCasePre,
			GeneralCondition gcon) {
		ReturnInfo info = new ReturnInfo();
		List<TradeMarkCasePre> tmcases = tradeMarkCasePreMapper.selectByTmCase(tradeMarkCasePre, gcon);
		List<Map<String,Long>> counts = tradeMarkCasePreMapper.selectByTmCaseCount(tradeMarkCasePre, gcon);
		Long total = 0L;
		for (Map<String,Long> onecount:counts) {
			total+= onecount.get("count");
		} 
		info.setTotal(total);
		info.setCurrPage(gcon.getPageNo());
		info.setData(tmcases);
		info.setSuccess(true);
		info.setMessage("查询案件列表成功");
		return info;
	}*/

	@Override
	public ReturnInfo queryTradeMarkCasePreDetail(Integer id) {
		ReturnInfo info = new ReturnInfo();
		TradeMarkCasePre tmcase = tradeMarkCasePreMapper.selectByPrimaryKey(id);
		info.setSuccess(true);
		info.setData(tmcase);
		info.setMessage("查询案件信息成功");
		return info;
	}

	@Override
	public ReturnInfo queryDetailByCustIdAndAgencyId(Integer custId,
			Integer agencyId) {
		ReturnInfo info = new ReturnInfo();
		if(custId==null||agencyId==null){
			info.setSuccess(false);
			info.setMessage("客户ID和代理所ID不能为空");
			return info;
		}
		TradeMarkCasePre tmcase = tradeMarkCasePreMapper.selectByCustIdAndAgencyId(custId,agencyId);
//		if(tmcase!= null) {
//			List<Material> materials = tmcase.getMaterials();
//			if(materials != null) {
//				for(Material m : materials) {
//					String fileName = processFileName(m.getSubject());
//					m.setFileName(fileName);
//					
//				}
//			}
//		}
		if(tmcase==null){
			TradeMarkCasePre tradeMarkCasePre = new TradeMarkCasePre();
			tradeMarkCasePre.setCustId(custId);
			tradeMarkCasePre.setAgencyId(agencyId);
			info = createTradeMarkCasePre(tradeMarkCasePre);
			if(!info.getSuccess()){
				return info;
			}
			tmcase = tradeMarkCasePre;
		}
		
		info.setSuccess(true);
		info.setData(tmcase);
		info.setMessage("查询案件信息成功");
		return info;
	}
	private String processFileName(String subject) {
		String s = null;
		if(subject != null) {
			String[] split = subject.split(",");
			for(int i=0;i<split.length;i++) {
				String[] split2 = split[i].split(" : ");
				if("fileName".equals(split2[0])) {
					s=split2[1];
				}
			}
		}
		return s;
		
	}
	
	private static Map<String, List<TradeMarkCaseCategory>> findListDiff(List<TradeMarkCaseCategory> goods,List<TradeMarkCaseCategory> dbGoods){
		//保存最后的数据
	    Map<String, List<TradeMarkCaseCategory>>  mapList = new HashMap<String, List<TradeMarkCaseCategory>>(3);
		//如果前端传入的goods为空，并且数据库dbgoods为空
	    if((goods == null || goods.isEmpty())&&(dbGoods == null || dbGoods.isEmpty())){
	    	return null;
	    }
	    //如果前端传入的goods为空
	    else if(goods == null || goods.isEmpty()){
	    	mapList.put("dbGoodsOnly", dbGoods);//dbGoods中的独有数据
	    	return mapList;
	    }
	    //如果goods不为空，但是dbgoods为空。则把前端传入的goods全插入数据库
	    else if(dbGoods == null || dbGoods.isEmpty()){
	    	mapList.put("goodsOnly", goods);
	    	return mapList;
	    }
	    //复制goods，作为备份
	    List<TradeMarkCaseCategory> goods_bak = new ArrayList<TradeMarkCaseCategory>(goods);

	    //1、获取goods中与dbGoods中不同的元素
	    goods.removeAll(dbGoods);

	    //2、获取goods和dbGoods中相同的元素
	    goods_bak.removeAll(goods);

	    //3、获取dbGoods中与goods中不同的元素
	    dbGoods.removeAll(goods_bak);

	    //经过此转换后goods中数据与dbGoods中的数据完全不同
	    //goods_bak是goods和dbGoods的交集
	    //dbGoods中的数据与goods中的数据完全不同

	    mapList.put("goodsOnly", goods);//goods中独有的数据
	    mapList.put("goodsAndDb", goods_bak);//交集的数据
	    mapList.put("dbGoodsOnly", dbGoods);//dbGoods中的独有数据
	    return mapList;
	}
	//测试
	public static void main(String[] args) {
		List<TradeMarkCaseCategory> l1 = new ArrayList<TradeMarkCaseCategory>();
		List<TradeMarkCaseCategory> l2 = new ArrayList<TradeMarkCaseCategory>();
		TradeMarkCaseCategory good1 = new TradeMarkCaseCategory();
		good1.setGoodName("hahah1");
		good1.setGoodCode("1111");
		TradeMarkCaseCategory good2 = new TradeMarkCaseCategory();
		good2.setGoodName("hahah2");
		good2.setGoodCode("2222");
		TradeMarkCaseCategory good3 = new TradeMarkCaseCategory();
		good3.setGoodName("hahah");
		
		l1.add(good1);
		l1.add(good2);
		l1.add(good3);
		
		TradeMarkCaseCategory good4 = new TradeMarkCaseCategory();
		good4.setId(1);
		good4.setGoodName("hahah1");
		good4.setGoodCode("1111");
		good4.setCasePreId(1);
		TradeMarkCaseCategory good5 = new TradeMarkCaseCategory();
		good5.setId(2);
		good5.setGoodName("hahah2");
		good5.setGoodCode("2222");
		good5.setCasePreId(1);
		TradeMarkCaseCategory good6 = new TradeMarkCaseCategory();
		good6.setId(3);
		good6.setGoodName("hahah3");
		good6.setCasePreId(1);
		l2.add(good4);
		l2.add(good5);
		l2.add(good6);
		Map<String, List<TradeMarkCaseCategory>> map = findListDiff(l1, l2);
		List<TradeMarkCaseCategory> l11= map.get("goodsOnly");
		List<TradeMarkCaseCategory> l12= map.get("goodsAndDb");
		List<TradeMarkCaseCategory> l22= map.get("dbGoodsOnly");
		for(TradeMarkCaseCategory good : l11){
			System.out.println(good.getId()+","+good.getGoodName()+","+good.getCasePreId());
		}
		System.out.println("~~~~~~~~~~~~~~");
		for(TradeMarkCaseCategory good : l12){
			System.out.println(good.getId()+","+good.getGoodName()+","+good.getCasePreId());
		}
		System.out.println("~~~~~~~~~~~~~~");
		for(TradeMarkCaseCategory good : l22){
			System.out.println(good.getId()+","+good.getGoodName()+","+good.getCasePreId());
		}
	}
	
}
