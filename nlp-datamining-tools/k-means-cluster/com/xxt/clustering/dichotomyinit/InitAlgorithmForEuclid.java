package com.xxt.clustering.dichotomyinit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.xxt.clustering.bean.InputErrorException;
import com.xxt.clustering.bean.StaticArg;
import com.xxt.clustering.bean.TextVector;
import com.xxt.clustering.util.ArticleDifferent;
import com.xxt.clustering.util.ClusterUtil;
import com.xxt.clustering.util.EuclidDifferent;


/**
 * 几种求距离初始点投放的函数
 * 注释风格：一般会在一句话的后面用"//"来注释该语句；若语句太长，则会在该语句上面注释 。
 * @author dingdongchao
 * @since  2010年5月26日
 * @version 1.0
 */
public class InitAlgorithmForEuclid{
	/**
	 * 求向量集的初始中心点（针对于基于欧几里得距离的点）
	 * @param vectors  初始向量集
	 * @param setNum  要划分的簇的个数
	 * @return  初始划分的集合的质心向量
	 * @exception  如果输入的集合为空则出现异常
	 */
	public static TextVector[] dichotmyInit(List<TextVector> vectors, int setNum){
		ArticleDifferent differentUtil=new EuclidDifferent(); //使用欧几里得距离来度量
//		long start ;
		if(vectors==null||vectors.size()<setNum)
			throw new InputErrorException("初始点投放输入数据错误！！");
		
		int allSize=vectors.size(); //所有点的集合的个数
		List<TextVoctorTemp> vectorsTemp=new ArrayList<TextVoctorTemp>(); // 构造项的向量集合
		for(int i=0;i<allSize;i++){
			vectorsTemp.add(new TextVoctorTemp(vectors.get(i),i));
		}
		float[][]  distance=new float[allSize][]; //所有向量集间的距离
		//计算所有向量集间的距离
		float sumDistance=0.0f; //总距离
		float tempDistance=0.0f; 
		//求两两点间的距离，要求:x->y之间的距离和 y->x距离相同 ，所以只存一份
//		start=System.currentTimeMillis();
		float[] tempArray;
		for(int i=0;i<allSize;i++){
			tempArray=new float[allSize-i];
			for(int k=i;k<allSize;k++){
				tempDistance=differentUtil.getDifferent(vectors.get(i), vectors.get(k));  
				tempArray[k-i]=tempDistance;
				sumDistance=sumDistance+tempDistance;
			}	
			distance[i]=tempArray;
		}
//		System.out.println("距离计算时间 "+(System.currentTimeMillis()-start));
		
		float avgDistance=sumDistance/((allSize*(allSize-1))/2);//平均距离,点自身之间的距离0.0不算在内
		List<VectorCluster> sets =new ArrayList<VectorCluster>();//划分后的集合簇，初始为全集
//		double distanceLimit=avgDistance*StaticArg.dichotmyMultiple;
		//一个簇分裂的先决条件：簇内的元素个数达到numLimit(太小的簇不分裂)，簇内元素的平均距离>minDistance（太紧凑的簇也不分裂）.
		float minDistance=avgDistance*StaticArg.minAvgDistance;  //簇可以分裂的距离平均值的阀值
		int numLimit=(int)((vectors.size()/setNum)*StaticArg.numLimit);//簇可以分裂的元素个数的阀值
		VectorCluster maxVectorCluster=getVectorCluster(vectorsTemp,distance,avgDistance*StaticArg.dichotmyMultiple);
		sets.add(maxVectorCluster);
		int num=1;
		//定义以下循环中的变量，在循环外定义，防止其循环内的多次定义、销毁，提高效率。
		List<TextVoctorTemp> vectorsTemA;
		List<TextVoctorTemp> vectorsTemB;
		List<TextVoctorTemp> vectorsMax;
		TextVoctorTemp textVectorA;
		TextVoctorTemp textvectorB;
		int aArrayId;
		int bArrayId;
		float distanceA;
		float distanceB;
		// end
		//分裂的迭代步骤
//		start=System.currentTimeMillis();
		while(num< setNum){
			vectorsMax=maxVectorCluster.getVectorSet();
            //得到距离最远的两个点，放到新的集合中 。注意：两个顺序不能变：先移除index大的元素，index小的元素的index不会改变 。
			// textVectorA、textvectorB分别是簇中聚类最大的两个向量（点） 。
			textVectorA=vectorsMax.get(maxVectorCluster.getManDisMax()); 
			vectorsTemA=new ArrayList<TextVoctorTemp>();
			aArrayId=textVectorA.getArrayId();  //a向量的数组Id
			textvectorB=vectorsMax.get(maxVectorCluster.getManDisMin());
			vectorsTemB=new ArrayList<TextVoctorTemp>();
			bArrayId=textvectorB.getArrayId(); //b向量的数组Id
			//扫描集合中的所有向量（点），根据和A点、B点的距离分到A簇、B簇中（哪个小分到哪个簇）
			int max,min,vectorId;
			
			for(TextVoctorTemp vector:vectorsMax){
				//分别计算该点到A、B点的距离。
				vectorId=vector.getArrayId();
				max=vectorId>aArrayId?vectorId:aArrayId;
				min=vectorId<=aArrayId?vectorId:aArrayId;
				distanceA=distance[min][max-min] ;
				max=vectorId>bArrayId?vectorId:bArrayId;
				min=vectorId<=bArrayId?vectorId:bArrayId;
				distanceB=distance[min][max-min] ;
//				if(max==min)
//					System.out.println("***");
				//将该点归并到距离小的哪一个点中
				if(distanceB<distanceA)
					vectorsTemB.add(vector);
				else
					vectorsTemA.add(vector);
			}
			//移除原来的簇，将新分裂出来的两个簇加入进去
			sets.remove(maxVectorCluster);
			sets.add(getVectorCluster(vectorsTemA,distance,avgDistance*StaticArg.dichotmyMultiple));
			sets.add(getVectorCluster(vectorsTemB,distance,avgDistance*StaticArg.dichotmyMultiple));
			if(vectorsTemB.size()==0||vectorsTemA.size()==0)
				return null;
			num++;
			if(num>=setNum)
				break;
			//扫描簇集合，找到下一个待分裂的的点集
			Iterator<VectorCluster> iterator=sets.iterator();
			VectorCluster temp;
			//找到平均距离较大且点的个数多余一定数量的点，若找不到，就找点的个数多余一定数量的簇 。
			while(iterator.hasNext()){
				temp=iterator.next();
				if(temp.size()>numLimit&&temp.getAvgDistance()>minDistance){
					maxVectorCluster=temp;
					break;
				}	
			}
			//如果未找到待分裂的簇，则结束分裂
			if(maxVectorCluster==null){
				System.err.println("未达到要求的中心个数");
				break;
			}
			while(iterator.hasNext()){
				temp=iterator.next();
				if(temp.size()>numLimit){
					if(temp.getAvgDistance()>maxVectorCluster.getAvgDistance())
						maxVectorCluster=temp;
				}
			}
		}
//		System.out.println("迭代求初始的时间： "+(System.currentTimeMillis()-start));
		List<List<TextVector>>  retSets=new ArrayList<List<TextVector>>(); // 初始划分的集合
		List<TextVector> tempVector;
		for(VectorCluster vc:sets){
			tempVector=new ArrayList<TextVector>();
			for(TextVoctorTemp tt:vc.getVectorSet()){
				tempVector.add(tt.getVector());
			}
			retSets.add(tempVector);
		}
		//求初始划分的集合的中心
		TextVector[]  clusterCenter=new TextVector[setNum];
//		start=System.currentTimeMillis();
		for(int i=0;i<setNum;i++){
			clusterCenter[i]=ClusterUtil.getCenterVector(retSets.get(i));
		}
//		System.out.println("计算中心的时间： "+(System.currentTimeMillis()-start));
		return clusterCenter;
	}
	/**
	 * 用一个向量集合构造的一个划分,包含信息：最大距离、最大距离的两个点（针对欧几里得距离）
	 * @param vectors        待构造的原始向量集
	 * @param distance       向量机的所有距离
	 * @param allSize        最原始的向量集的个数
	 * @param avgDistance    平均距离
	 * @return
	 */
	private static VectorCluster getVectorCluster(List<TextVoctorTemp> vectors,float[][] distance,float limitDistance){
		//对于距离的上限，防止离群点的影响
		float sumDistance=0.0f;
		int size=vectors.size();
		int max=0;// 
		int min=0;// 集合簇中满足条件的最大距离的两个点在集合中的位置。 
		float maxDistance=Float.MIN_VALUE;
		float tempDistance=0.0f;
		//遍历所有点距离，找到最大的符合条件的两个向量
		int row=0;
		for(int i=0;i<size-1;i++){
			row=vectors.get(i).getArrayId();
			for(int k=i+1;k<size;k++){
				tempDistance=distance[row][vectors.get(k).getArrayId()-row]; //得到（i,k）间的距离
				sumDistance=sumDistance+tempDistance;
				//找寻最大的距离
				if(tempDistance<limitDistance &&tempDistance> maxDistance){ 
					min=i;
					max=k;
					maxDistance=tempDistance;
				}
			}		
		}
		//平均距离不计算同一点之间的距离0.0f
		return new VectorCluster(vectors,sumDistance*2/(size*(size-1)),max,min,distance);
	}
}