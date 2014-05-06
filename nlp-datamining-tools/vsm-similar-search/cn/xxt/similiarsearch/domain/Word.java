package cn.xxt.similiarsearch.domain;
/**
 * 文本向量中一个单词的类
 * @author dingdongchao
 * @since 2010年7月15日
 * @version 1.0
 */
public class Word {
	private int id;         //单词的ID 
	private byte score ;   //单词的得分（即现在所用的词频）
	public Word(int id,byte score ){
		this.id=id;
		this.score=score;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public byte getScore() {
		return score;
	}
	public void setScore(byte score) {
		this.score = score;
	}
	
}