
package com.zy.aop.handler;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.zy.aop.interfaces.TypeTransformationModual;

import javassist.CtClass;

/**
* @author zy
* @Date 2019-11-13 周三 下午 02:50:35
* @Description java基本数据类型与javassist数据类型的转换
* @version 
*/
public class TypeTransformationHandler implements TypeTransformationModual {

	@Override
	public CtClass[] Transformation(Class<?>... clzs) {
		List<CtClass> list = new ArrayList<CtClass>();
		for (Class<?> class1 : clzs) {
			try {
				list.add( Transformation(class1) );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (CtClass[]) list.toArray();
	}
	@Override
	public CtClass[] Transformation(Parameter[]  parames) {
		CtClass transformation = null;
		int len = parames.length;
		CtClass[] cts = new CtClass[len];
		for (int i = 0; i < len; i++) {
			try {
				
				transformation = Transformation( parames[i].getType() );
				cts[i] = transformation;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cts;
	}
	@Override
	public CtClass Transformation(Class<?> clz) throws Exception {
		if(clz == byte.class) {
			return CtClass.byteType;
		}
		if(clz == short.class) {
			return CtClass.shortType;
		}
		if(clz == int.class) {
			return CtClass.intType;
		}
		if(clz == float.class) {
			return CtClass.floatType;
		}
		if(clz == double.class) {
			return CtClass.doubleType;
		}
		if(clz == long.class) {
			return CtClass.longType;
		}
		if(clz == boolean.class) {
			return CtClass.booleanType;
		}
		if(clz == char.class) {
			return CtClass.charType;
		}
		throw new IllegalArgumentException("by: "+clz);
	}
}
