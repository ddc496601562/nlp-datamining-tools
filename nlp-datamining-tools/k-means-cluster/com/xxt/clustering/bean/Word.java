package com.xxt.clustering.bean;
/**
 * 文本向量中一个单词的类
 * @author dingdongchao
 * @since 2010年5月26日
 * @version 1.0
 */
public class Word {
	private int id;         //单词的ID 
	private String content; //单词
	private float score ;   //单词的得分（即现在所用的词频）
	public Word(int id,float score ){
		this.id=id;
		this.score=score;
	}
	public int getId() {
		return id;
	}
	public float getScore() {
		return score;
	}
	public String toString(){
		return +id+"："+this.score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}