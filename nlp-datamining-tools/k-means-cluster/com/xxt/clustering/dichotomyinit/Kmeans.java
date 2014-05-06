package com.xxt.clustering.dichotomyinit;

import java.util.ArrayList;
import java.util.List;

import com.xxt.clustering.bean.StaticArg;
import com.xxt.clustering.bean.TextVector;
import com.xxt.clustering.util.ArticleDifferent;
import com.xxt.clustering.util.ClusterUtil;
/**
 * 基本的K-means算法
 * @author dingdongchao
 * @since 2010年5月26日
 * @version 1.0
 */
public class Kmeans {
	/**
	 * k_means算法的变种，若是中间出现一个中心点无点，则移除这个中心点，簇的个数减少1
	 * @param textVectorSet   待聚类的集合
	 * @param textCenters     初始中心点
	 * @return                聚类后的文本向量集的集合
	 */
	public static List<List<TextVector>>  k_means(List<TextVector> textVectorSet ,TextVector[] textCenters,ArticleDifferent diff){
		ArticleDifferent differentUtil=diff;//使用余弦相似距离来度量相异度
		int  nCluster=textCenters.length;  //要分的簇的个数
		int   vectorNum=textVectorSet.size();//总集合的点的个数
		int[] cluster=new int[vectorNum];    //标志向量（点）属于哪个簇的数组
		List<TextVector> clusterCenter=new ArrayList<TextVector>();//存放中心点的数组
		for(TextVector tv:textCenters)
			clusterCenter.add(tv);
		List<List<TextVector>> listArray=new ArrayList<List<TextVector>>();//泛型数组，每个簇的集合
		int changes=100;  // 每次迭代时，点簇所属改变的个数，初始为100 ！=0
		int tempK=0;      //标志一个类归属与那个集合簇的中间量
		TextVector textVector;//循环中间变量，当前要处理的向量
		float minDistance;
		float distance;
		//迭代
		List<TextVector> tempTVList ;
		int iteratorNum=0;
		while(changes>0&&iteratorNum<StaticArg.maxIteratorNum){
			changes=0;
			nCluster=clusterCenter.size();
			listArray.clear();
			for(int i=0;i<nCluster;i++)
				listArray.add(new ArrayList<TextVector>());
			for(int i=0;i<vectorNum;i++){
				textVector=textVectorSet.get(i);
				minDistance=Float.MAX_VALUE;
				//循环处理该点与N个中心点，找到离该点最近的中心点
				for(int k=0;k<nCluster;k++){
					distance=differentUtil.getDifferent(clusterCenter.get(k),textVector);
					if(distance<minDistance){
						minDistance=distance;
						tempK=k;
					}
				}
				//将该点加入簇
				listArray.get(tempK).add(textVector);
				if(tempK!=cluster[i]){
//					System.out.println(i+"  "+tempK+"  "+cluster[i]);
					changes++;
					cluster[i]=tempK;
				}
			}
			//求新的中心点
			clusterCenter.clear();
			for(int i=0;i<nCluster;i++){
				tempTVList=listArray.get(i);
				if(tempTVList.size()!=0)
					clusterCenter.add(ClusterUtil.getCenterVector(tempTVList));
				else{
					System.err.println("待分的簇减少了一个！");
				}
			}
			iteratorNum++;
		}
		//retList是返回值，根据ID的分类
		return listArray;
	}
}
