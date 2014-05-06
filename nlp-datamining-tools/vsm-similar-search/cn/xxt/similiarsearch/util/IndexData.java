package cn.xxt.similiarsearch.util;
/**
 * 创建的索引的数据信息 。
 * @author xiao
 * @since 2010年8月14日
 */
public class IndexData {
	public static final int  valueNum=5;     //可用值数组的个数
	public static int        maxArticleId;  //最大的文章id
	public static int        maxWordId;     //最大的单词的id
	public static int[][]    idIndex;       //文章id的索引
	public static byte[][]   scoreIndex;    //单词权重的索引
	public static boolean    isInit=false;  //索引是否初始化可用
}
