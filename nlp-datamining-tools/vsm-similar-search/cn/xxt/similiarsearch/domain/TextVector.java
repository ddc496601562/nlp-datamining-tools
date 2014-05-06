package cn.xxt.similiarsearch.domain;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 文本向量，表示一个文本的格式
 * 
 * @author dingdongchao
 * @since 2010年7月15日
 * @version 1.2
 */
public class TextVector implements Comparator<TextVector>{

	private int   textId; // 文本的Id ，id为负数时表示是一个虚拟的向量（点）
	
	private Word[] vector; //包含的词的数组

	public TextVector(Word[] vector, int id) {
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
	public int getTextId() {
		return textId;
	}
	
	public boolean equals(TextVector tv)
	{
		if(this.hashCode()==tv.hashCode())
			return true;
		if(this.textId==tv.getTextId())
			return true;
		else
			return false;
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
	public int compare(TextVector o1, TextVector o2) {
		return (int)(o1.textId-o2.textId);
	}
}
