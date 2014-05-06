package com.xxt.clustering.util;

import com.xxt.clustering.bean.TextVector;


/**
 * 求相似度的接口
 * @author xiao
 *
 */
public interface ArticleSimilarity {
	float geSimilarity(TextVector vector1, TextVector vector2);
}