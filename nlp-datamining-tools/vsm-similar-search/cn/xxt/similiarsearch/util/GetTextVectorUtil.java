package cn.xxt.similiarsearch.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import cn.xxt.similiarsearch.domain.TextVector;
import cn.xxt.similiarsearch.domain.Word;


/**
 * 获得文本向量的工具类，因为向量太多，一次读入一定数量到内存中进行处理 。
 * 
 * @author dingdongchao
 * @since 2010年7月14日
 * @version 1.0
 */
public class GetTextVectorUtil {
	private  String dirName;

	LineNumberReader reader=null;

	private boolean isUsing=false;

	/**
	 * 构造函数
	 * 
	 * @param textId
	 *          文章的id，针对谋篇文章取向量，则排除该篇文章自己 。
	 * @param num
	 */
	public GetTextVectorUtil(String dirName) {
		this.dirName=dirName;
		try {
			reader=new LineNumberReader(new FileReader(this.dirName));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件读入错误！");
		}
		isUsing=true;
	}

	/**
	 * 是否还有文章
	 * 
	 * @return
	 */
	public boolean hasTextVector() {
		return isUsing;
	}
	/**
	 * 从当前行开始跳过N行数据，若跳到结尾则直接结束
	 * @param num
	 * @throws IOException
	 */
	public void goHead(int num) {
		if (!isUsing)
			return;
		int i = 0;
		try {
			while (i < num) {
				if (reader.readLine() == null) {
					isUsing = false;
					break;
				} else {
					i++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("文件读错误！");
		}
	}
	/**
	 *从数据库中取得响亮
	 * @param num   要取到的数目，如果数据库中剩余的数目<num ,则取出所有 。
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException
	 */
	public List<TextVector> getVectorList(int num) throws IOException{
		if(!isUsing)
			return null;
		List<TextVector> list = new ArrayList<TextVector>();
		int textId;
		String line;
		int lineLength=0;
		int counter = 0;
		TextVector temp;
		while (true) {
			line = reader.readLine();
			// 读取到空行的时候
			if(line==null){
				isUsing=false;
				break;
			}
//			if (line.equals(""))
//				continue;
			lineLength=line.length();
			int index=line.indexOf(' ');
			textId = Integer.parseInt(line.substring(0,index));
			int wordsSize=(lineLength-index-1)/14;
			Word[] tempWords=new Word[wordsSize];
			for(int i=index+1,k=0;k<wordsSize;i=i+14,k++){
				tempWords[k]=new Word(Integer.parseInt(line.substring(i,i+7)),Byte.parseByte(line.substring(i+8,i+13)));
			}
			temp = new TextVector(tempWords, textId);
			list.add(temp);
			if (++counter >= num)
				break;
		}
//		long start =System.currentTimeMillis();
		//文本向量单位化
//		for(TextVector tv:list)
//			ClusterUtil.getNormalizedVector(tv);
//		System.out.println(System.currentTimeMillis()-start);
		return list;
	}
	/**
	 *从数据库中取得一个向量
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException
	 */
	public TextVector getNextVecto(){
		int textId;
		String line=null;
		int lineLength=0;
		TextVector temp;
		try {
			line = reader.readLine();
//			System.out.println(line);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("文件读错误！");
		}
		if(line==null){
			isUsing=false;
			return null;
		}
		lineLength=line.length();
		int index=line.indexOf(' ');
		textId = Integer.parseInt(line.substring(0,index));
		int wordsSize=(lineLength-index-1)/14;
		Word[] tempWords=new Word[wordsSize];
		for(int i=index+1,k=0;k<wordsSize;i=i+14,k++){
			tempWords[k]=new Word(Integer.parseInt(line.substring(i,i+7)), Byte.parseByte(line.substring(i+8,i+13)));
		}
		temp = new TextVector(tempWords, textId);
//		ClusterUtil.getNormalizedVector(temp);
		return temp;
	}
	/**
	 * 关闭对结果集的使用。
	 */
	public void close(){
		if(isUsing)
			try {
				this.reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("IO失败！");
			}
	}
}
