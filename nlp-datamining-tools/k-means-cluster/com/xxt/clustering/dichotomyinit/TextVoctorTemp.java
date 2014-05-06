package com.xxt.clustering.dichotomyinit;

import com.xxt.clustering.bean.TextVector;


/**
 * 临时的文本向量，用于在求距离时候的标记用，在初始点投放中使用
 * @author dingdongchao
 * @since 2010年5月26日
 * @version 1.0
 */
class TextVoctorTemp{
	private int arrayId;
	private TextVector vector;
	public TextVoctorTemp(TextVector vector ,int arrayId){
		this.vector=vector;
		this.arrayId=arrayId;
	}
	public int getArrayId() {
		return arrayId;
	}
	public TextVector getVector() {
		return vector;
	}
}