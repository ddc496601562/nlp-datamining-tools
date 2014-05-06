package com.xxt.clustering.bean;
/**
 * 保存程序运行时的一些静态（全局）变量
 * @author dingdongchao
 * @version 1.0
 * @since 2010年5月27日
 */
public class StaticArg {
	public static final int     maxLength=100;                //向量的最大长度
	public static final float   minDouble=0.00001f;           //用于检测两个浮点数相等的界限
	public static final int     dichotmyMultiple=3;           //二分裂算法中寻找最大距离时的最大限制倍数（不能超过该该倍数与平均距离的乘积）
	public static final float   numLimit=(float)2/3 ;         // 一个簇可分裂的size的最小标准
	public static final float   minAvgDistance=(float)2/5;    //一个簇分裂的最小平均距离，平均值低于该值与整体平均距离乘积的簇不予分裂
	public static final float   lengthOfNormalizedVector=127*127;//归一化向量的长度的平方
	public static final int     sampleNum=3600;               //初始点取样的数量
	public static final int     maxIteratorNum=200;           //kemans算法的最大迭代次数
	public static final int     maxCluterNum=10;              //聚类的设置的最大簇数目
	public static final int     maxKeyWordForOneCluter=15;    //每个簇提取的关键字数目
	public static final int     centerVectorNum=40;           //每个簇的中心向量数目
	public static final byte    cosMultiple=100;              //余弦相似度的扩大区间，便于比较
	public static final float   ePsArgument=1.0f;             //密度半径相关的参数
}