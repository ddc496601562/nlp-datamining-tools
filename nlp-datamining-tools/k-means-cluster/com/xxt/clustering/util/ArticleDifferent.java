package com.xxt.clustering.util;

import com.xxt.clustering.bean.TextVector;


/**
 * 计算两片文章相异度的接口
 * @author xiao
 * @since 2010年6月29日
 */
public interface ArticleDifferent {
	/**
	 * 获得两篇文章的相异度
	 * @param vector1   文章1
	 * @param vector2   文章2
	 * @return  相异度
	 */
	public float getDifferent(TextVector vector1, TextVector vector2);
}
