package cn.xxt.similiarsearch.util;

import java.util.Comparator;

class  COSValue{
	public int    id;
	public short  value;
	public COSValue(){
		
	}
	public COSValue(int id ,short value){
		this.id=id;
		this.value=value;
	}
}

class ValueComparator  implements Comparator<COSValue>{
	public int compare(COSValue o1, COSValue o2) {
		if(o1.value-o2.value!=0)
			return o2.value-o1.value;
		else
			return o1.id-o2.id;
	}
}