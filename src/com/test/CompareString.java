package com.test;

import java.util.Comparator;

public class CompareString implements Comparator{

	public int compare(Object obj1, Object obj2) {
		
		byte[] str1=((String) obj1).getBytes();
        byte[] str2=((String) obj2).getBytes();
        for(int i=0;i<Math.min(str1.length,str2.length);i++){
            if(str1[i]!=str2[i]){
            	//如果obj1大，则返回正数，否则返回负数
                return str1[i]-str2[i];
            }
        }
        return 0;
    }
}
