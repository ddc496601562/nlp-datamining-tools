package com.xxt.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xxt.clustering.bean.TextVector;
import com.xxt.clustering.bean.Word;
import com.xxt.clustering.dichotomyinit.InitAlgorithmForEuclid;
import com.xxt.clustering.dichotomyinit.Kmeans;
import com.xxt.clustering.util.ArticleDifferent;
import com.xxt.clustering.util.ClusterUtil;
import com.xxt.clustering.util.CosDifferent;
import com.xxt.clustering.util.WordWeightCompare;

/**
 * 分类最外层的工具包
 * @author xiao
 * @since 2010年8月16日
 * @version 1.0
 */
public class ClusteringTool {
	/**
	 * 对于给定的集合进行聚类
	 * @param textVectorSet   待聚类的元素集合
	 * @param num       聚类的个数
	 * @param diff      聚类时使用的相异度方法
	 * @param samSize   取样的个数设置
	 * @return
	 */
	public List<List<TextVector>>   doClustering(List<TextVector> list ,int num,ArticleDifferent diff,int samSize){
		int cluteringNum=list.size();
		cluteringNum=cluteringNum<num?cluteringNum:num;
		//初始点投放取样
		List<TextVector> samplingVector=ClusterUtil.getSampling(list, (double)(samSize)/list.size());
		//初始点设置
		TextVector[] center=InitAlgorithmForEuclid.dichotmyInit(samplingVector,cluteringNum);
		//k_means聚类
		List<List<TextVector>> result=Kmeans.k_means(list,center,diff);
		return result;
	}
	/**
	 * 对于给定的集合进行聚类
	 * @param textVectorSet   待聚类的元素集合
	 * @param num       聚类的个数
	 * @param diff      聚类时使用的相异度方法
	 * @param samSize   取样的个数设置为默认的3000
	 * @return
	 */
	public List<List<TextVector>>   doClustering(List<TextVector> list ,int num,ArticleDifferent diff){
		return doClustering(list,num,diff,3000);
	}
	/**
	 * 对于给定的集合进行聚类
	 * @param textVectorSet   待聚类的元素集合
	 * @param num       聚类的个数
	 * @param diff      聚类时使用的相异度方法，默认为cos值的相异度
	 * @param samSize   取样的个数设置为默认的3000
	 * @return
	 */
	public List<List<TextVector>>   doClustering(List<TextVector> list ,int num,int samSize){
		 return doClustering(list,num,new CosDifferent(),samSize);
	}
	/**
	 * 对于给定的集合进行聚类
	 * @param textVectorSet   待聚类的元素集合
	 * @param num       聚类的个数
	 * @param diff      聚类时使用的相异度方法，默认为cos值的相异度
	 * @param samSize   取样的个数设置
	 * @return
	 */
	public List<List<TextVector>>   doClustering(List<TextVector> list ,int num){
		return doClustering(list,num,new CosDifferent(),3000);
	}
	/**
	 * 取得前N个关键词(通过tf来进行排序),不建议使用
	 * @param cluster  聚簇
	 * @param num   关键词的数目
	 * @return
	 */
	public static List<Word> getKeyWordByTf(List<TextVector> cluster,int  num){
		Float value;
		Map<Integer, Float> mapWord = new HashMap<Integer, Float>();
		for (TextVector tv : cluster) {
			// 统计文章的分散度
			Word[] words = tv.getVector();
			for (Word w : words) {
				value = mapWord.get(w.getId());
				if (value == null)
					mapWord.put(w.getId(),w.getScore());
				else
					mapWord.put(w.getId(), value + w.getScore());
			}
		}
		List<Word> wList=new ArrayList<Word>();
		for (Entry<Integer, Float> b : mapWord.entrySet()) {
			wList.add(new Word(b.getKey(), b.getValue()));
		}
		Collections.sort(wList, new WordWeightCompare());
		// 提取关键词语,放在wList中，按照关键词出现的次数将序排列
		if (wList.size() > num)
			wList = wList.subList(0, num);
		return wList;
	}
	/**
	 * 取得前N个关键词(通过分散度来进行排序)
	 * @param cluster   聚簇
	 * @param num   关键词的数目
	 * @return
	 */
	public static List<Word> getKeyWordByDispersion(List<TextVector> cluster,int num) {
		Integer value;
		Map<Integer, Integer> mapWord = new HashMap<Integer, Integer>();
		for (TextVector tv : cluster) {
			// 统计文章的分散度
			Word[] words = tv.getVector();
			for (Word w : words) {
				value = mapWord.get(w.getId());
				if (value == null)
					mapWord.put(w.getId(), 1);
				else
					mapWord.put(w.getId(), value + 1);
			}
		}
		List<Word> wList=new ArrayList<Word>();
		for (Entry<Integer, Integer> b : mapWord.entrySet()) {
			wList.add(new Word(b.getKey(), b.getValue()));
		}
		Collections.sort(wList, new WordWeightCompare());
		// 提取关键词语,放在wList中，按照关键词出现的次数将序排列
		if (wList.size() > num)
			wList = wList.subList(0, num);
		return wList;
	}
	/**
	 * 取一个向量集（点）中最靠近中心的num个向量
	 * @param source   原向量集
	 * @param num      取的数目,若该数目小于原集合的size的返回元集合所有元素 。
	 * @return         中心的num个向量
	 */
	public static  List<TextVector>  getNearVector(List<TextVector> source ,int num){
		ArticleDifferent differentUtil=new CosDifferent(); //使用欧几里得距离来度量
		List<TextVector> ret=new ArrayList<TextVector>();
		List<TextVector> temp=new ArrayList<TextVector>();
		if(source.size()<=num){
			ret.addAll(source);
			return ret;
		}
		temp.addAll(source);
		TextVector min;
		TextVector center=ClusterUtil.getCenterVector(source);
		float distance1,distance2;
		while(ret.size()<num){
			min=temp.get(0);
			distance1=differentUtil.getDifferent(min, center);
			for(TextVector tv:temp){
				distance2=differentUtil.getDifferent(tv, center);
				if(distance2<distance1){
					min=tv;
					distance1=distance2;
				}
			}
			temp.remove(min);
			ret.add(min);
		}
		return ret;
	}
	
}
