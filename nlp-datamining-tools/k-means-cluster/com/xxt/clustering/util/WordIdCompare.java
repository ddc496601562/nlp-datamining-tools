package com.xxt.clustering.util;

import java.util.Comparator;

import com.xxt.clustering.bean.Word;
/**
 * 单词比较器，根据单词的id来比较大小，以便排序
 * id小的排序在前
 * 用途：按照单词的Id升序排列
 * @author dingdongchao
 * @since 2010年5月26日
 * @version 1.0
 */
public class WordIdCompare implements Comparator<Word>{
	public int compare(Word o1, Word o2) {
		if(o1.getId()==o2.getId())
			return 0;
		else{
			if(o1.getId()>o2.getId())
				return 1;
			else 
				return -1;
		}
	}

}
