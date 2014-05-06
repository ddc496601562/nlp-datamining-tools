package cn.xxt.similiarsearch.util;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.TreeSet;






import cn.xxt.similiarsearch.domain.TextVector;
import cn.xxt.similiarsearch.domain.Word;
import cn.xxt.similiarsearch.util.IndexData;
import cn.xxt.similiarsearch.util.StaticParameter;

/**
 * 计算最相似博文的工具类
 * @author xiao
 * @since  2010年8月14日
 * @version 1.0
 */
public class SimiliarCalculateThread extends Thread{
	public static final float cc=(StaticParameter.cosMultiple)/(StaticParameter.lengthOfNormalizedVector*StaticParameter.lengthOfNormalizedVector );
	public static final short minLimitValue=(short)Math.round(40/cc);
	public static final short maxLimitValue=(short)Math.round(100/cc);
	//索引数据
	int[][]  idIndex=null;
	byte[][] scoreIndex=null;
	PrintStream out=null;  //结果输出文件
	PrintStream logOut=null;//日志记录文件
	int begin;   //该线程要计算的文章的起始Id
	int articleCounter;//计算的数目
	GetTextVectorUtil articleFirst =null;
	
	
	/**
	 * 构造函数
	 * @param beginNum   该线程要计算的文章的起始Id
	 * @param endNum     结束的id
	 * @param name       线程编号
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public SimiliarCalculateThread(int beginNum,int endNum,int name) throws FileNotFoundException, UnsupportedEncodingException{
		begin=beginNum;
		articleCounter=endNum-beginNum;
		out=new PrintStream(StaticParameter.path+"searchResult"+beginNum+"--"+endNum+".txt","GBK");
		logOut=new PrintStream(StaticParameter.path+"线程"+name+"log.txt","GBK");
		idIndex=IndexData.idIndex;
		scoreIndex=IndexData.scoreIndex;
		articleFirst = new GetTextVectorUtil(StaticParameter.path+StaticParameter.calTvFile);
		articleFirst.goHead(begin - 1);
	}
	public void run() {
		logOut.println("该线程开始时间是："+(new java.util.Date()));
		if(!IndexData.isInit)
			return ;
		int counter=0;  //计算的文章数目
		short[] cosValue=new short[IndexData.maxArticleId+1];
		int indexLength;
		int wordId;
		int[]    idrow;
		byte[]  scoreRow;
		byte score;
		Word[] words;
		int articleId;
		short tempCosValue;
		TextVector tv;
		int wordLength;
		long start=System.currentTimeMillis();
		while (articleFirst.hasTextVector() && counter <= articleCounter) {
			tv = articleFirst.getNextVecto();
			articleId=tv.getTextId();
			for(int i=0;i<=IndexData.maxArticleId;i++)
				cosValue[i]=0;
			words=tv.getVector();
			wordLength=words.length;
			//计算cos值
			for(int i=0;i<wordLength;i++){
				wordId=words[i].getId();
				score=words[i].getScore();
				indexLength=idIndex[wordId].length;
				idrow=idIndex[wordId];
				scoreRow=scoreIndex[wordId];
				for(int k=0;k<indexLength;k++){
					cosValue[idrow[k]]=(short)(cosValue[idrow[k]]+scoreRow[k]*score);
				}
			}
			if(articleId<=IndexData.maxArticleId)
				cosValue[articleId]=0;
			int index=0;
			COSValue minCosValue=null;
			TreeSet<COSValue> similiarSet=new TreeSet<COSValue>(new ValueComparator());
			for(index=0;index<IndexData.maxArticleId;index++){
				tempCosValue=cosValue[index];
				if(tempCosValue<minLimitValue||tempCosValue>maxLimitValue)
					continue;
				similiarSet.add(new COSValue(index,cosValue[index]));
				if(similiarSet.size()==StaticParameter.similiarNum){
					index++;
					break;
				}
			}
			if(similiarSet.size()==StaticParameter.similiarNum){
				minCosValue=similiarSet.last();
				for (; index < IndexData.maxArticleId; index++) {
					tempCosValue = cosValue[index];
					if (!(tempCosValue > minCosValue.value)
							|| tempCosValue < minLimitValue
							|| tempCosValue > maxLimitValue) {
						continue;
					}
					similiarSet.remove(minCosValue);
					similiarSet.add(new COSValue(index, cosValue[index]));
					minCosValue = similiarSet.last();
				}
			}
			out.print(articleId+" ");
			for(COSValue cos:similiarSet){
				if(cos.value==0)
					continue;
				out.print(cos.id+","+Math.round(cos.value*cc)+";");
			}
			out.println();
			counter++;
			if (counter%5000== 0) {
				logOut.println("计算5000个的时间是："+ (System.currentTimeMillis() - start) /1000+"秒。 "+(counter/5000));
				start = System.currentTimeMillis();
			}
		}
		articleFirst.close();
		logOut.println("该线程结束时间是："+(new java.util.Date()));
		logOut.close();
		out.close();
	}
}
