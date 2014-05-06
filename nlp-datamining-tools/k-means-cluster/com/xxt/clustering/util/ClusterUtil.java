package com.xxt.clustering.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.xxt.clustering.bean.StaticArg;
import com.xxt.clustering.bean.TextVector;
import com.xxt.clustering.bean.Word;


/**
 * 文本向量相关的工具类
 * @author dingdongchao
 * @since 2010年5月26日
 * @version 1.0
 */
public class ClusterUtil {
	/**
	 * 求向量集的质心
	 * @param allVector  向量簇
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextVector getCenterVector(List<TextVector> allVector) {
		int hashMax = 10000;
		float[][] wHash = new float[hashMax][1];
		float[] temp;
		int hashIndex;
		int index;
		int wId;
		for (TextVector tv : allVector) {
			for (Word w : tv.getVector()) {
				wId = w.getId();
				hashIndex = wId % hashMax;
				index = wId / hashMax;
				if (index <= wHash[hashIndex].length - 1) {
					wHash[hashIndex][index] = wHash[hashIndex][index]
							+ w.getScore();
				} else {
					temp = new float[index + 1];
					System.arraycopy(wHash[hashIndex], 0, temp, 0,
							wHash[hashIndex].length);
					wHash[hashIndex] = temp;
					temp[index] = w.getScore();
				}
			}
		}
		int size=allVector.size();
		List<Word> wList = new ArrayList<Word>();
		for (int i = 0; i < wHash.length; i++) {
			for (int k = 0; k < wHash[i].length; k++){
				if(wHash[i][k]>StaticArg.minDouble||wHash[i][k]<-1*StaticArg.minDouble)
					wList.add(new Word(i + k * hashMax, wHash[i][k]/size));
			}
		}
		
		Collections.sort(wList, new WordWeightCompare());
		if(wList.size()>StaticArg.maxLength)
			wList=wList.subList(0,StaticArg.maxLength);
		Collections.sort(wList, new WordIdCompare());
		return new TextVector(wList.toArray(new Word[0]),-1);
	}
	/**
	 * 讲一个向量归一化处理，长度设为1
	 * @param  tv 待处理的向量
	 * @return  返回处理后的向量，是在元向量的基础上修改
	 */
	public static TextVector getNormalizedVector(TextVector tv){
		Word[] words=tv.getVector();
		float sum=0.0f;
		for(int i=0;i<words.length;i++)
			sum=sum+words[i].getScore()*words[i].getScore();
		sum=(float)Math.sqrt(sum);
		for(int i=0;i<words.length;i++){
			words[i].setScore((words[i].getScore()*StaticArg.lengthOfNormalizedVector)/sum);
		}
		return tv;
	}
	/**
	 * 抽样的方法，在样本中抽取一定的样品。按照序号，每个一定的数量抽取一个，由于文章按时间先后排列的，这样抽样会具有一定的代表性
	 * @param source    原样本集
	 * @param detal     抽样比率
	 * @return          抽样后的样本集 。
	 */
	public static List<TextVector> getSampling(List<TextVector> source ,double detal){
		List<TextVector> ret=new ArrayList<TextVector>();
		int interval=((int)(1/detal))>1?((int)(1/detal)):1;
		int size=source.size();
		for(int i=0;i<size;i++){
			if(i%interval==0)
				ret.add(source.get(i));
		}
		return ret ;
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
