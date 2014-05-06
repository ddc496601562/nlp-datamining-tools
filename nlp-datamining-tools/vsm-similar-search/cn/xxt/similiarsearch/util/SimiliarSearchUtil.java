package cn.xxt.similiarsearch.util;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;






import cn.xxt.similiarsearch.domain.TextVector;
import cn.xxt.similiarsearch.domain.Word;

/**
 * 在线相似搜索的一些工具方法的合集
 * @author xiao
 * @since 2010年8月14日
 */
public class SimiliarSearchUtil {
	public static final int  MAX_WORD_ID=200000;     //构建索引的最大单词ID，若出现超过该Id的将出现错误。
	public static final int  MAX_ARTICLE_ID=3000000; //构建索引时，估计的最大文章id数字，超过将出现错误
	public static PrintStream allLogOut=null;
	/**
	 * 初始化内存索引的程序
	 * @throws SQLException   读取向量数据时候发生异常
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("deprecation")
	public static void indexInit() throws IOException, InterruptedException{
		PrintStream logOut=SimiliarSearchUtil.allLogOut;
		logOut.println("程序开始,开始时间是："+(new java.util.Date()).toLocaleString());
		boolean[]  isUse=new boolean[MAX_ARTICLE_ID];   //标志是该文章是否被统计过，排除文章的重复统计
		for(int i=0;i<MAX_ARTICLE_ID;i++)
			isUse[i]=false;
		int articleId;  
		int wordId;
		List<TextVector> allList ;
		int[]  wordCounter=new  int[MAX_WORD_ID];
		long start=System.currentTimeMillis();
		//对博文构造倒排索引
		GetTextVectorUtil articleFirst = new GetTextVectorUtil(StaticParameter.path+StaticParameter.allTvFile);
		//求出每个单词包含多少篇文章，即与多少篇文章包含这个单词，第一次统计各个单词分别在多少篇文章中出现
		int maxArticleId=0;
		int maxWordId=0;
		while (articleFirst.hasTextVector()) {
			allList=articleFirst.getVectorList(50000);
			for(TextVector tv:allList){
				articleId=tv.getTextId();
				//寻找最大的文章的Id
				if(articleId>maxArticleId)
					maxArticleId=articleId;
				//寻找最大的单词的Id
				if(tv.getTextId()>maxArticleId)
					maxArticleId=tv.getTextId();//找到最大的文章的id 
				if(isUse[articleId])
					continue;
				for(Word w:tv.getVector()){
					wordId=w.getId();
					wordCounter[wordId]++;
					if(wordId>maxWordId)
						maxWordId=wordId;
				}
				isUse[articleId]=true;
			}
			logOut.println("已取:"+allList.size());
		}
		articleFirst.close();
		//申请索引所需要的空间
		int[][]   idIndex=new int[maxWordId+1][];
		byte[][] scoreIndex=new byte[maxWordId+1][];
		for(int i=1;i<=maxWordId;i++){
			idIndex[i]=new int[wordCounter[i]];
			scoreIndex[i]=new byte[wordCounter[i]];
		}
		articleFirst = new GetTextVectorUtil(StaticParameter.path+StaticParameter.allTvFile);
		//创建内存索引
		for(int i=1;i<=maxWordId;i++)
			wordCounter[i]=0;
		for(int i=0;i<MAX_ARTICLE_ID;i++)
			isUse[i]=false;
		while (articleFirst.hasTextVector()) {
			allList=articleFirst.getVectorList(50000);
			for(TextVector tv:allList){
				articleId=tv.getTextId();
				if(isUse[articleId])
					continue;
				for(Word w:tv.getVector()){
					wordId=w.getId();
					idIndex[wordId][wordCounter[wordId]]=articleId;
					scoreIndex[wordId][wordCounter[wordId]]=(byte)(w.getScore());
					wordCounter[wordId]++;
				}
				isUse[articleId]=true;
			}
			logOut.println("已取:"+allList.size());
		}
		articleFirst.close();
		System.out.println("创建索引的时间为："+(System.currentTimeMillis()-start)/1000);
		allList=null;
		isUse=null;
		wordCounter=null;
		System.out.println("索引创建完毕，清理内存时间！");
		articleFirst.close();
		IndexData.idIndex=idIndex;
		IndexData.scoreIndex=scoreIndex;
		IndexData.maxArticleId=maxArticleId;
		IndexData.maxWordId=maxWordId;
		IndexData.isInit=true;
	}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, InterruptedException{
		int begin=Integer.parseInt(args[0]);
		int end=Integer.parseInt(args[1]);
		int counter=Integer.parseInt(args[2]);  //线程数
		SimiliarSearchUtil.allLogOut=new PrintStream(StaticParameter.path+"log.txt","GBK");
		PrintStream logOut=SimiliarSearchUtil.allLogOut;
		indexInit();
		int oneNum=(end-begin)/counter;
		for(int i=0;i<counter-1;i++){
			logOut.println("启动线程"+(i+1));
			Thread t = new SimiliarCalculateThread(begin+oneNum*i,begin+oneNum*(i+1)-1,i+1);
			t.start();
		}
		logOut.println("启动线程"+(counter));
		Thread t = new SimiliarCalculateThread(begin+oneNum*(counter-1),end,counter);
		t.start();
		logOut.println("启动线程完毕！"+(new java.util.Date()).toLocaleString());
	}
}
