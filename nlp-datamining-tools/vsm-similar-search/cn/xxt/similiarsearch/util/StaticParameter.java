package cn.xxt.similiarsearch.util;
/**
 * 存放静态的参数，放到类文件中
 * @author xiao
 * @since 2010年8月14日
 * @version 1.0
 */
public class StaticParameter {
	public static final float      lengthOfNormalizedVector=127;//归一化向量的长度
	public  static  byte     cosMultiple=100;           //余弦相似度的扩大区间，便于比较
	public  static int       similiarNum=100;           //相似的限制数
	public final  static  String   path="D:\\blog\\";
//	public  static  String   path="/home/tomcat/data/";
	public  static  String   allTvFile="blogwordinfo.txt";  //所有向量的存储文件
	public  static  String   calTvFile="blogwordinfo.txt";  //待计算的向量存储文件
}