package com.xxt.clustering.util;

import java.util.Comparator;

import com.xxt.clustering.bean.Word;

/**
 * 根据词频的比较器
 * @author dingdongchao
 * @since 2010年5月26日
 * @version 1.0
 */
public class WordWeightCompare implements Comparator<Word> {
	/**
	 * 单词之间的比较器，主要用于两个单词的词频进行比较
	 * 词频大的排序在前 。
	 * 用途：按词频的大小降序排列，取词频大的前N个
	 */
	public int compare(Word o1, Word o2) {
		if(o1.getScore()-o2.getScore()>0)
			return -1;
		if(o1.getScore()-o2.getScore()<0)
			return 1;
		else
			return 0;
	}


}
