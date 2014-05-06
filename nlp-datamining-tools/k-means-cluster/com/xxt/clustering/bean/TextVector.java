package com.xxt.clustering.bean;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 文本向量，表示一个文本的格式
 * 
 * @author dingdongchao
 * @since 2010年5月26日
 * @version 1.0
 */
public class TextVector {

	private long   textId; // 文本的Id ，id为负数时表示是一个虚拟的向量（点）
	
	private String  title; //文章标题
	
	private int hotSocre; //热度的评价得分，综合评论和点击量考虑
	
	private Word[] vector; //包含的词的数组

	public TextVector(Word[] vector, long id) {
		if (vector==null ||vector.length==0)
			throw new InputErrorException("请构造有词的文本向量! "+id);
		this.vector = vector;
		this.textId = id;
	}

	public Word[] getVector() {
		return vector;
	}
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append(this.textId);
		for(Word w:this.vector)
			sb.append(" "+w.toString());
		return sb.toString();
	}

	// 得到该文本向量的迭代器
	public Iterator<Word> getIterator() {
		return new VectorIterator();
	}
	public long getTextId() {
		return textId;
	}
	// 文本向量的迭代器类
	class VectorIterator implements Iterator<Word> {
		private int index = 0;

		public boolean hasNext() {
			return this.index <TextVector.this.vector.length;
		}

		public Word next() {
			if (index <TextVector.this.vector.length) {
				index++;
				return TextVector.this.vector[index];
			} else
				throw new NoSuchElementException("迭代器迭代到末尾");
		}

		public void remove() {
		}

	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getHotSocre() {
		return hotSocre;
	}

	public void setHotSocre(int hotSocre) {
		this.hotSocre = hotSocre;
	}

}
