package com.xxt.clustering.dichotomyinit;

import java.util.List;

import com.xxt.clustering.bean.InputErrorException;
/**
 * 用于初始划分中所用的类
 * @author dingdongchao
 * @since 2010年5月26日
 * @version 1.0
 */
class VectorCluster{
	private List<TextVoctorTemp> vectorSet;
	private float avgDistance;  //簇的平均相似度（相异度）
	private int manDisMax=0;    //待分列的两个基准点 ---arrayId
	private int manDisMin=0;    //待分列的两个基准点 ---arrayId
	private float[][] smae;     //所有相似度(相异度)的集合
	public VectorCluster(List<TextVoctorTemp> vectorSet,float avgDistance,int max ,int min,float[][] same){
		if(vectorSet==null)
			throw new InputErrorException("不能构造空的簇");
		this.avgDistance=avgDistance;
		this.vectorSet=vectorSet;
		this.manDisMax=max;
		this.manDisMin=min;
		this.smae=same;
	}
	
	
	public float[][] getSmae() {
		return smae;
	}
	public int getManDisMax() {
		return manDisMax;
	}
	public int getManDisMin() {
		return manDisMin;
	}

	public float getAvgDistance() {
			return avgDistance;
	}
	public List<TextVoctorTemp> getVectorSet() {
		return vectorSet;
	}
	public int size(){
		return vectorSet.size();
	}
}
