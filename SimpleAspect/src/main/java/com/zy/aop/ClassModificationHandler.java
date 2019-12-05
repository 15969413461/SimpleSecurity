
package com.zy.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.aop.handler.SimplePointCutAnalysisStrategy;
import com.zy.aop.handler.TypeTransformationHandler;
import com.zy.aop.interfaces.ClassModificationModule;
import com.zy.aop.interfaces.TypeTransformationModual;
import com.zy.aop.interfaces.anno.PointcutMessage;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

/**
* @author zy
* @Date 2019-11-11 周一 下午 02:34:12
* @Description 获得注解属性,并为切点方法生成aop方法
* @version 
*/
public class ClassModificationHandler implements ClassModificationModule {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ClassPool classPool;
	// 项目的classpath
	private String clzPath;
	// 切点定义表达式解析接口
	private PointcutMessage pointcutMessage;
	// 类型转换器
	private TypeTransformationModual typeTransforme;
	
	private Map<String, Map<Class<?>, List<Method>>> aspects;
	private Map<String, Method> pointcuts;
	
	
	public Map<String, Map<Class<?>, List<Method>>> getAspects() {
		return aspects;
	}
	public void setAspects(Map<String, Map<Class<?>, List<Method>>> aspects) {
		this.aspects = aspects;
	}
	public Map<String, Method> getPointcuts() {
		return pointcuts;
	}
	public void setPointcuts(Map<String, Method> pointcuts) {
		this.pointcuts = pointcuts;
	}

	
	public void handler(Map<String, Map<Class<?>, List<Method>>> aspects,Map<String, Method> point) {
		
		String clzName = null;
		for (String pointVal : point.keySet()) {
			// 解析@SimplePointcut的表达式
			clzName = pointcutMessage.analysis(pointVal).get(0);
			Method method = point.get(pointVal);
			
			// 通过切点与切点方法的关联获得切点方法
			List<Method> list = null;
			Map<Class<?>, List<Method>> map = aspects.get(pointVal);
			if(map == null) {
				continue;
			}
			
			for (Class<?> clz : map.keySet()) { // 此循环至多只执行一次
				list = map.get(clz);
				
				beforeSetMethod(clzName, clz, method, list);
			}
		}
	}
	
	/**
	 * 前置环境准备
	 * @param pointClz - 切点所在类的限定类名
	 * @param aspectClz - aop方法所在类的限定类名
	 * @param pointMethod - 作为切点的Method对象
	 * @param aopMethod - 有序存储aop方法的list容器
	 */
	protected void beforeSetMethod(String pointClz,Class<?> aspectClz,Method pointMethod,List<Method> aopMethod) {
		try {
			CtClass point = classPool.get(pointClz);
			setMethodToClass(point, pointMethod, aspectClz, aopMethod);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setMethodToClass(CtClass ct,Method meth,Class<?> aspectClz,List<Method> list) throws Exception {
		Method before = list.get(0);
		Method after = list.get(2);
		if(before == null && after == null) {
			return ;
		}
		String name = meth.getName();
		CtMethod ctMethod = null;
		Parameter[] parameters = meth.getParameters(); // 切点有参数
		if(parameters.length > 0) {
			// 参数类型转换
			CtClass[] parames = typeTransforme.Transformation(parameters);
			ctMethod = ct.getDeclaredMethod(name, parames);
		}else { // 切点没有参数
			ctMethod = ct.getDeclaredMethod(name);
		}
		
		// 带有包名
		String aspectName = aspectClz.getName();
		int len = parameters.length;;
		if(before != null) {
			String beforeName = before.getName();
			StringBuilder builder = new StringBuilder();
			
			// com.zy.Test aspect = new com.zy.Test();
			builder.append(aspectName).append(" s = new ").append(aspectName).append("();");
			
			builder.append("s.").append(beforeName).append("(");
			if(len == 0) { 
				// aspect.test()
				builder.append(");");
				
			}else {
				// aspect.test(parame...)
				for (int i = 0; i < len; i++) {
					// $1-参数1,$2-参数2...
					builder.append("$").append(i+1);
					if(i < parameters.length-1) {
						builder.append(",");
					}
				}
				builder.append(");");
			}
			
			ctMethod.insertBefore(builder.toString());
		}
		
		if(after != null){ 
			String afterName = after.getName();
			StringBuilder afbuilder = new StringBuilder();
			
			// com.zy.Test aspect = new com.zy.Test();
			afbuilder.append(aspectName).append(" s = new ").append(aspectName).append("();");
			afbuilder.append("s.").append(afterName).append("(");
			if(len == 0) { 
				// aspect.test()
				afbuilder.append(");");
				
			}else {
				// aspect.test(parame...)
				for (int i = 0; i < len; i++) {
					afbuilder.append("$").append(i+1);
					if(i < parameters.length-1) {
						afbuilder.append(",");
					}
				}
				afbuilder.append(");");
			}
			ctMethod.insertAfter(afbuilder.toString());
		}
		ct.writeFile(clzPath);
		logger.info("字节码已更新，by：{}",clzPath);
	}
	
	@Override
	public void addMethodToClass(CtClass ctClz, CtClass[] parames
			, String methodBody, Modifier modifier) throws Exception {}
	@Override
	public void setFieldToClass(CtClass ctClz) throws Exception {}
	@Override
	public void getClassMsg(CtClass ctClz) throws Exception {}

	@Override
	public void setConstructorToClass(CtClass ctClz) throws Exception {}
	@Override
	public void addConstructorToClass(CtClass ctClz, CtClass[] parames, String methodName, String methodBody)
			throws Exception {}
	/**
	 * ClassPool-CtClass对象的容器。必须从该对象获取CtClass对象。
	 * 如果对该对象调用get（），它将搜索由ClassPath表示的各种源以查找类文件
	 * ，然后创建表示该类文件的CtClass对象,返回给调用器
	 */
	private void  getClassPool() {
		if(classPool==null) {
			classPool = ClassPool.getDefault();
			classPool.importPackage("com.test");
			clzPath = getClass().getResource("/").getPath();
			pointcutMessage = new SimplePointCutAnalysisStrategy();
			typeTransforme = new TypeTransformationHandler();
		}
	}

	public ClassModificationHandler() {
		super();
		// 字节码操作基本环境准备,必须
		getClassPool();
	}
	
	
}	
