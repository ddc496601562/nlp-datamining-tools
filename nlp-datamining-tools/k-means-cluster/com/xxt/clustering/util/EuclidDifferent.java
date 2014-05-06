package com.xxt.clustering.util;

import com.xxt.clustering.bean.TextVector;
import com.xxt.clustering.bean.Word;


/**
 * 以欧几里得距离来度量相异度 。
 * @author xiao
 * @since 2010年6月29日
 */
public class EuclidDifferent implements ArticleDifferent {
	
	public float getDifferent(TextVector vector1, TextVector vector2) {
		int index1 = 0;
		int index2 = 0;
		float sum = 0.0f;
		Word[] vc1 = vector1.getVector();
		Word[] vc2 = vector2.getVector();
		int del;
		// 求x 和 y 间的欧几里得距离平方
		while (index1 <vc1.length && index2 < vc2.length){
			del = vc1[index1].getId() - vc2[index2].getId();
			if (del == 0){
				sum = sum + (vc1[index1].getScore() - vc2[index2].getScore())* (vc1[index1].getScore() - vc2[index2].getScore());
				index1++;
				index2++;
			}
			else {
				if (del > 0) {
					sum = sum + vc2[index2].getScore() * vc2[index2].getScore();
					index2++;
				} else {
					sum = sum + vc1[index1].getScore() * vc1[index1].getScore();
					index1++;
				}
			}
		}
		if(index1<vc1.length-1){
			for(int i=index1;i<vc1.length;i++)
				sum=sum+vc1[i].getScore()*vc1[i].getScore();
		}
		if(index2 <vc2.length-1){
			for(int i=index2;i<vc2.length;i++)
				sum=sum+vc2[i].getScore()*vc2[i].getScore();
		}
		return (float) Math.sqrt((float)sum);
	}

}
