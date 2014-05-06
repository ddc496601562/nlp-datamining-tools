package com.xxt.clustering.util;

import com.xxt.clustering.bean.StaticArg;
import com.xxt.clustering.bean.TextVector;
import com.xxt.clustering.bean.Word;

/**
 * 求文章的预先相似度
 * @author xiao
 *
 */
public class CosSimilarity implements ArticleSimilarity {

	public float geSimilarity(TextVector vector1, TextVector vector2) {
		int index1 = 0;
		int index2 = 0;
		float sum = 0.0f;
		Word[] vc1 = vector1.getVector();
		Word[] vc2 = vector2.getVector();
		int del;
		// 求x*y
		while (index1 < vc1.length && index2 < vc2.length) {
			del = vc1[index1].getId() - vc2[index2].getId();
			if (del == 0) {
				sum = sum + vc1[index1].getScore() * vc2[index2].getScore();
				index1++;
				index2++;
			} else {
				if (del > 0)
					index2++;
				else
					index1++;
			}
		}
		// 求||x|| ，向量的长度
		float sum1 = 0.0f;
		for (Word w : vc1)
			sum1 = sum1 + w.getScore() * w.getScore();
		sum1 = (float) Math.sqrt(sum1);
		// 求||y|| ，向量的长度
		float sum2 = 0.0f;
		for (Word w : vc2)
			sum2 = sum2 + w.getScore() * w.getScore();
		sum2 = (float) Math.sqrt(sum2);
		// 求向量的夹角,因为此例子中所有的余弦均为正值，
		float ret = (sum / (sum1 * sum2));
		// 因为牵涉到的文本向量的夹角余弦值在【0,1】上，扩到到【0,StaticArg.cosMultiple】上，减小比较带来的误差 。
		return ret*StaticArg.cosMultiple;
	}

}
