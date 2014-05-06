package com.xxt.clustering.util;

import com.xxt.clustering.bean.StaticArg;
import com.xxt.clustering.bean.TextVector;
import com.xxt.clustering.bean.Word;

/**
 * 以余弦相似取倒数来度量文章见的相异度 。
 * @author xiao
 * @since 2010年6月29日
 */
public class CosDifferent implements ArticleDifferent {
	public float getDifferent(TextVector vector1, TextVector vector2) {
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
		// 因为牵涉到的文本向量的夹角余弦值在【0,1】区间上，且是相异度，微调后取反得到相似度 。
		return (float) (1 / (ret + (StaticArg.minDouble * 0.1)));
	}

}
