
package com.zy.aop.interfaces;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javassist.CtClass;
import javassist.Modifier;

/**
* @author zy
* @Date 2019-11-11 周一 下午 02:21:49
* @Description 切面类修改接口
* @version 
*/
public interface ClassModificationModule {
	/**
	 * 在现有(已加载好的)类中修改构造器
	 * @param ct - javassist封装Class信息的对象
	 */
	void  setConstructorToClass(CtClass ctClz) throws Exception;
	/**
	 * 在现有(已加载好的)类中添加构造器
	 * @param ct - javassist封装Class信息的对象
	 */
	void  addConstructorToClass(CtClass ctClz,CtClass[] parames,String methodName ,String methodBody) throws Exception;
	
	/**
	 * 在现有(已加载好的)类中修改方法 - 在方法体前后添加前置通知和后置通知
	 */
	void setMethodToClass(CtClass ct,Method meth,Class<?> aspectClz,List<Method> list) throws Exception;
	/**
	 * 在现有(已加载好的)类中添加方法
	 */
	void  addMethodToClass(CtClass ctClz,CtClass[] parames,String methodBody,Modifier modifier) throws Exception;
	/**
	 * 在现有(已加载好的)类中修改属性
	 */
	void  setFieldToClass(CtClass ctClz) throws Exception;
	/**
	 * 获得类的信息
	 */
	void  getClassMsg(CtClass ctClz) throws Exception;
	
	/**
	 * 根据切面信息进行字节码操作
	 * @param aspects
	 * @param point
	 */
	void handler(Map<String, Map<Class<?>, List<Method>>> aspects,Map<String, Method> point);
	
}
