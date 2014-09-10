package com.ia.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class ReflectionUtil {
	

	private static final Object[] colecaoVaziaObjects = new Object[0];
	private static final Class[] colecaoVaziaClazz = new Class[0];
	private static final String stringVazia = "";
	private static final SimpleDateFormat defaultSDF = new SimpleDateFormat("dd/MM/yyyy");
	
	public static final String primeiraLetraMinuscula(String string){
		return string.substring(0, 1).toLowerCase() + string.substring(1, string.length());
	}	

	/**
	 * Extrai do parâmetro o nome da classe. 
	 * @param value
	 * @return A substring que vai do primero caracter do parâmetro até o último ponto. 
	 */
	public static final String extractVOName(String value) {
		int indexOfLastDot = value.lastIndexOf(".");
		if(indexOfLastDot != -1) return value.substring(0, indexOfLastDot);
		return null;
	}
	
	/**
	 * Extrai do parâmetro o nome do atributo. 
	 * @param value
	 * @return A substring que vai de depois do último ponto até o o fim do parâmetro. 
	 */
	public static final String getSubstringDepoisDoUltimoPonto(String value) {
		int indexOfLastDot = value.lastIndexOf(".");
		if(indexOfLastDot != -1) return value.substring(indexOfLastDot + 1, value.length());
		return value;		
	}
	
	public static final String getSubstringDepoisDoPrimeroPonto(String value) {
		int indexOfFirstDot = value.indexOf(".");
		if(indexOfFirstDot != -1) return value.substring(indexOfFirstDot + 1, value.length());
		return value;		
	}	
	
	public static final String getSubstringAntesDoPrimeiroPonto(String value) {
		return value.split("\\.")[0];
	}
	

	/**
	 * @deprecated Use {@link #getMethod(Class, String, Class[])} instead.
	 */
	@Deprecated
	public static final Method getMetodo(Class<?> clazz, String nomeMetodo, Object[] parametros, Class[] classesParametros)
			throws Exception {
		return getMethod(clazz, nomeMetodo, classesParametros);
	}

	/**
	 * Search for a method based on its signature, on <code>baseClass</code> or its superclasses.
	 * 
	 * @param baseClass
	 *            - The class to start the search.
	 * @param methodName
	 *            - The name of the method being searched.
	 * @param paramClasses
	 *            - The classes of parameters of the method being searched.
	 * @return The method with the given signature on the <code>baseClass</code> hierarchy.
	 * @throws NoSuchMethodException
	 *             if there is no such method.
	 */
	@SuppressWarnings("unchecked")
	public static final Method getMethod(Class<?> baseClass, String methodName, Class[] paramClasses) throws NoSuchMethodException {
		Method m = null;
		Class<?> currentClass = baseClass;
		do {
			try {
				m = currentClass.getDeclaredMethod(methodName, paramClasses);
			} catch (NoSuchMethodException e) {
				currentClass = currentClass.getSuperclass();
			}
		} while (m == null && currentClass != null);
		if (m == null) {
			throw new NoSuchMethodException("Method " + methodName + " does not exist on " + baseClass + " hierachy.");
		}
		return m;

	}

	/**
	 * Returns the bridge method to a given method. Specially useful if you want to find the method in the superclass that matches the
	 * signature. It is not trivial when your using generics, you need to look for the bridge in the superclass.
	 * 
	 * @param method
	 *            - The method to find the bridge.
	 * @return The bridge of the method, or null if the given method is null.
	 * @throws NoSuchMethodException
	 *             in case there is no bridged method to the given method.
	 */
	public static Method getBridgeMethod(Method method) throws NoSuchMethodException {
		if (method == null) return null;
		
		Method[] currentClassDeclaredMethods = method.getDeclaringClass().getDeclaredMethods();
		
		Method ret = null;
		
		find_bridge_for:
		for (Method bridgeCandidate : currentClassDeclaredMethods) {
			if (! bridgeCandidate.isBridge()) continue;
			if (! method.getName().equals(bridgeCandidate.getName())) continue;
			if (! (method.getParameterTypes().length == bridgeCandidate.getParameterTypes().length)) continue;
			for (int i = 0; i < method.getParameterTypes().length; i++) {
				Class<?> mp = method.getParameterTypes()[i];
				Class<?> bcmp = bridgeCandidate.getParameterTypes()[i];
				if (! bcmp.isAssignableFrom(mp)) {
					continue find_bridge_for;
				}
			}
			if (ret == null) {
				ret = bridgeCandidate;
			} else {
				throw new IllegalStateException("More than one bridge to a method was found, due to ambiguous declaration. Avoid methods with the same name and assignable parameters' classes.");
			}
		}
		if (ret != null) {
			return ret;
		} else {
			throw new NoSuchMethodException("Could not find a bridge for " + method);
		}
	}
	
	
	/**
	 * @deprecated Use {@linkplain #invokeMethod(Object, String, Class[], Object[])} instead.
	 */
	@Deprecated
	public static final Object invocaMetodo(Object vo, String nomeMetodo, Object[] parametros, Class[] classesParametros) throws Exception{
		return invokeMethod(vo, nomeMetodo, classesParametros, parametros);
	}
	
	/**
	 * Este método, quando não acha o método que tem como parametro as classes determinadas,
	 * vai tentando achar métodos para as subclasses dessas classes de parametros.
	 */
	public static final Object invocaMetodoSuperClasseParametro(Object vo, String nomeMetodo, Object[] parametros, Class[] classesParametros) throws Exception{
		int chegouObject = classesParametros.length;
		Method m = null;
		do{
			try{
				chegouObject = classesParametros.length;
				m = getMethod(vo.getClass(), nomeMetodo, classesParametros);
			}catch(NoSuchMethodException e){
				chegouObject = 0;
				for (int i = 0; i < classesParametros.length; i++) { // isso está ruim, pq ele pega a superclasse de todos os parametros..
					classesParametros[i] = classesParametros[i].getSuperclass();
					if (classesParametros[i] == null){
						chegouObject++;
						classesParametros[i] = Object.class;
					}
				}
			}
		}while(m==null && chegouObject < classesParametros.length);
		
		if(m==null)
			throw new NoSuchMethodException("O método "+nomeMetodo+" não existe");
		
		return m.invoke(vo,parametros);
	}
	
	public static final Field getConstante(Object vo, String constante) throws Exception{
		return vo.getClass().getField(constante);
		
	}
	
	/**
	 * Retorna uma String no formato Camel Case (primeira letra maiuscula). 
	 * @param string a ser convertida para formato Camel Case. 
	 * @return String passada como parâmetro já no formato Camel Case.
	 */
	public static final String camelCase(String string){
		if(string.length()==0)
			return stringVazia;
		char[] array=string.toCharArray();
		array[0]=Character.toUpperCase(array[0]);
		
		return new String(array);
	}

	public static final Object getValorAtributo(Object vo, String atributo) throws Exception{
		String[] metodos=atributo.split("\\.");
		if(metodos.length==1){
			if(vo instanceof Map){
				return ((Map)vo).get(atributo);
			}
			return invokeMethod(vo, new StringBuffer("get").append(camelCase(atributo)).toString(), colecaoVaziaClazz, colecaoVaziaObjects);
		}	
		Object ret=vo;
		
		for(int i=0;i<metodos.length;i++){
			String st=metodos[i];
			if(ret instanceof Map){
				ret=((Map<?, ?>)ret).get(st);
			}
			else{
				ret=invokeMethod(ret, new StringBuffer("get").append(camelCase(st)).toString(), colecaoVaziaClazz, colecaoVaziaObjects);
			}	
			if(ret==null)
				return null;
		}
		return ret;
	}
	
	public static final Object setAtributo(Object vo, String atributo, Object[] parametros, Class[] classesParametros) throws Exception{
		return invokeMethod(vo, new StringBuffer("set").append(camelCase(atributo)).toString(), classesParametros, parametros);
	}
	
	/**
	 * Este método, quando não acha o método que tem como parametro a classes determinada,
	 * vai tentando achar métodos que contenham como parametros as superclasse da classe
	 * do paramentro.
	 */
	public static final Object setAtributoSuperClasseParametro(Object vo, String atributo, Object valor, Class<?> clazz) throws Exception{
		return invocaMetodoSuperClasseParametro(vo,new StringBuffer("set").append(camelCase(atributo)).toString(),new Object[]{valor},new Class[]{clazz});
	}
	
	public static final Object setAtributo(Object vo, String atributo, Object valor) throws Exception{
		return invokeMethod(vo, new StringBuffer("set").append(camelCase(atributo)).toString(), (new Class[]{ getClasseAtributo(vo.getClass(), atributo) }), (new Object[]{valor}));
	}
	
	public static final Object setAtributo(Object vo, String atributo, Object valor, Class<?> clazz) throws Exception{
		return invokeMethod(vo, new StringBuffer("set").append(camelCase(atributo)).toString(), (new Class[]{clazz}), (new Object[]{valor}));
	}
	
	public static final Object setAtributo(Object vo, String atributo, String parametro,String formato) throws Exception{
		Class<?> fieldType=getClasseAtributo(vo.getClass(),atributo);
		return invokeMethod(vo, new StringBuffer("set").append(camelCase(atributo)).toString(), (new Class[]{fieldType}), getParametroMetodo(parametro,formato,fieldType));
	}
	
	public static final Object[] getParametroMetodo(String parameterValue, String format,Class<?> fieldType) throws Exception{
		Object[] retorno=new Object[1];
		
		if(parameterValue==null || parameterValue.trim().length()==0){
			return retorno;
		}
		String trimmedParameter=parameterValue.trim();
		StringBuffer erros=new StringBuffer();

		//montando parametros do metodo de definicao do atributo
		if(fieldType.equals(String.class)){
			if(!(trimmedParameter.length()==0))
				retorno[0] = parameterValue;
            
        }else if(fieldType.equals(Integer.class)){
            Integer integerValue = null;                    
            if(parameterValue != null && !(trimmedParameter.length()==0)){                        
                try{
                    integerValue = new Integer(parameterValue);
                }catch(NumberFormatException e){
					erros.append("formato de inteiro inválido: ").append(parameterValue);
                }
            }
            retorno[0]=integerValue;
								
        }else if(fieldType.equals(Byte.class)){
            Byte byteValue = null;                    
            if(parameterValue != null && !(trimmedParameter.length()==0)){                        
                try{
                    byteValue = new Byte(parameterValue);
                }catch(NumberFormatException e){
					erros.append("formato de inteiro inválido: ").append(parameterValue);
                }
            }
            retorno[0]= byteValue;
								
        }else if(fieldType.equals(Long.class)){
            Long longValue = null;                    
            if(parameterValue != null && !(trimmedParameter.length()==0)){                        
                try{
                    longValue = new Long(parameterValue);
                }catch(NumberFormatException e){
					e.printStackTrace();
					erros.append("formato de long inválido: ").append(parameterValue);
                }
            }
            retorno[0]=longValue;
            
        }else if(fieldType.equals(Float.class)){
            Float floatValue = null;                    
            if(parameterValue != null && !(trimmedParameter.length()==0)){                        
                try{
					if(format!=null){
						int numeroDecimais=Integer.parseInt(format);
						parameterValue=parameterValue.substring(0,parameterValue.length()-numeroDecimais)+"."+parameterValue.substring(parameterValue.length()-numeroDecimais);
					}
                    floatValue = new Float(parameterValue);
                }catch(NumberFormatException e){
					e.printStackTrace();
					erros.append("formato de float inválido: ").append(parameterValue);
                }
            }
            retorno[0]=floatValue;
            
        }else if(fieldType.equals(Double.class)){
            Double doubleValue = null;                    
            if(parameterValue != null && !(trimmedParameter.length()==0)){                        
                try{
					if(format!=null){
						int numeroDecimais=Integer.parseInt(format);
						parameterValue=parameterValue.substring(0,parameterValue.length()-numeroDecimais)+"."+parameterValue.substring(parameterValue.length()-numeroDecimais);
					}
                    doubleValue = new Double(parameterValue);
                }catch(NumberFormatException e){
					erros.append("formato de double inválido: ").append(parameterValue);
                }
            }
            retorno[0]=doubleValue;
            
        }else if(fieldType.equals(BigDecimal.class)){
            BigDecimal bd = null;                    
			if(parameterValue != null && !(trimmedParameter.length()==0)){
				try{
					if(format!=null){
						int numeroDecimais=Integer.parseInt(format);
						parameterValue=parameterValue.substring(0,parameterValue.length()-numeroDecimais)+"."+parameterValue.substring(parameterValue.length()-numeroDecimais);
					}
                    bd = new BigDecimal(parameterValue);
                }catch(NumberFormatException e){
					erros.append("formato de bigdecimal inválido: ").append(parameterValue);
                }
			}
			retorno[0]=bd;
			
		}else if(fieldType.equals(Boolean.class)){
			Boolean param=Boolean.valueOf(false);
			if(parameterValue.trim().equalsIgnoreCase("true") || "1".equals( parameterValue.trim() ))
				param=Boolean.valueOf(true);
				
			retorno[0]=param;
				
        }else if(fieldType.equals(Date.class)){
           	Date date = null;
			SimpleDateFormat formatter;
            if(format==null)
				formatter= defaultSDF;
            else
				formatter= new SimpleDateFormat(format);
       		try {
				date = formatter.parse(parameterValue);
       		} catch (Exception e) {
				erros.append("formato de data inválido: ").append(parameterValue);
       		}

       		retorno[0]=date;
			
		}			
		if(erros.length()==0)
			return retorno;
		
		throw new Exception(erros.toString());

	}
	
	public static final Object[] getParametroMetodo(Object vo, String atributo, String parameterValue, String format) throws Exception{
		return getParametroMetodo(parameterValue,format,getClasseAtributo(vo.getClass(),atributo));
	}

	
	public static final Object criaInstanciaComConstrutorVazio(String vo) throws Exception{
		return Class.forName(vo).newInstance();
	}
	
	public static final Object criaInstanciaComConstrutorVazio(Class<?> clazz) throws Exception{
		return clazz.newInstance();
	}
	
	public static final boolean isCollection(Class<?> obj){
		Class[] ints=obj.getInterfaces();
		for(int i=0;i<ints.length;i++){
			Class<?> clazz=ints[i];
			do{
				if(clazz.equals(Collection.class))
					return true;
				clazz=clazz.getSuperclass();
			}while(clazz!=null);
		}
		return false;
	}
	
	
	public static boolean isSuperClass(Class<?> parent, Class<?> clazz){
		if(parent==null || clazz==null)
			return false;
		
		if(clazz.equals(parent))
			return true;
		
		Class[] interfaces=clazz.getInterfaces();
		
		for(int i=0;i<interfaces.length;i++){
			return isSuperClass(parent, interfaces[i]);
		}
		
		Class<?> clazzz=clazz.getSuperclass();
		if(clazzz==null){
			return false;
		}
		
		return isSuperClass(parent, clazzz);
		
		
	}
	
	public static boolean isSuperClass(HashSet<?> parent, Class<?> clazz){
		for(Iterator<?> it=parent.iterator();it.hasNext();){
			Class<?> clazzz=(Class<?>) it.next();
			if(isSuperClass(parent, clazzz)){
				return true;
			}
		}
		return false;
		
	}

	public static final Class<?> getClasseAtributo(Class<?> clazz, String attributeName) throws Exception{
		Class<?> clazzz=clazz;
		if(!StringUtil.contains(attributeName, ".")){
			return getMethod(clazzz,"get"+camelCase(attributeName),colecaoVaziaClazz).getReturnType();
		}
		else{
			String[] att=attributeName.split("\\.");
			for(int i=0;i<att.length;i++)
				clazzz=getMethod(clazzz,"get"+camelCase(att[i]),colecaoVaziaClazz).getReturnType();
			return clazzz;
		}
	}
	
	/**
	 * @return Setter method for <i>field</i> in <i>clazz</i>.
	 */
	public static final Method getSetter(Class<?> clazz, Field field) {
		try {
			Class<?>[] args = new Class[1];
			args[0] = field.getType();
			return ReflectionUtil.getMethod(clazz, new StringBuffer("set").append(ReflectionUtil.camelCase(field.getName())).toString(), args);
		} catch (Exception e) {
			throw new IllegalStateException("Não foi possível encontrar o setter da property " + field.getName() + " na classe " + clazz, e);
		}
	}
	
	/**
	 * @return Getter method for <i>field</i> in <i>clazz</i>.
	 */
    public static final Method getGetter(Class<?> clazz, String nomeMetodo) {
		try {
			return ReflectionUtil.getMethod(clazz, new StringBuffer("get").append(ReflectionUtil.camelCase(nomeMetodo)).toString(), colecaoVaziaClazz);
		} catch (Exception e) {
			throw new IllegalStateException("Não foi possível encontrar o getter da property " + nomeMetodo + " na classe " + clazz, e);
		}
	}
    
    public static List<String> fieldsContainingAnnotation(Object o, Class<? extends Annotation> annotation) {
        return fieldsContainingAnnotation(o.getClass(), annotation);
    }
   
    public static List<String> fieldsContainingAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<String> list = new ArrayList<String>();
        Class<?> clazzAtual = clazz;
        while (!Object.class.equals(clazzAtual) && clazzAtual != null) {
            final Field[] fields = clazzAtual.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                if (field.getAnnotation(annotation) != null) {
                    list.add(field.getName());
                }
            }
            clazzAtual = clazzAtual.getSuperclass();
        }
        return list;
    }
    
    public static List<Field> getFieldsContainingAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Field> list = new ArrayList<Field>();
        Class<?> clazzAtual = clazz;
        while (!Object.class.equals(clazzAtual) && clazzAtual != null) {
            final Field[] fields = clazzAtual.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                if (field.getAnnotation(annotation) != null) {
                    list.add(field);
                }
            }
            clazzAtual = clazzAtual.getSuperclass();
        }
        return list;
    }

    public static Map<String, Object> mapAnnotationValuesOnly(Object origem, Class<? extends Annotation> annotation) {
        Collection<String> keys = fieldsContainingAnnotation(origem, annotation);
        final HashMap<String, Object> params = new HashMap<String, Object>();
        for (Iterator<String> it = keys.iterator(); it.hasNext();) {
            String field = it.next();
            final Object valorAtributo = getAttributeValue(origem, field);
            params.put(field, valorAtributo);
        }
        return params;
    }
   
    public static Object copyObjectAnnotationOnly(Object origem, Class<? extends Annotation> annotation) {
        Collection<String> keys = fieldsContainingAnnotation(origem, annotation);
       
        if (keys.size() > 0) {
            Object keysObject;
            try {
                keysObject = origem.getClass().newInstance();
            } catch (Exception e) {
                throw new IllegalStateException("Erro ao copiar objeto da classe: " + origem.getClass() );
            }
            for (Iterator<String> it = keys.iterator(); it.hasNext();) {
                String field = it.next();
                copyAttributeValue(origem,
                        keysObject, field);
            }
            return keysObject;
        } else {
            return null;
        }
    }

    /**
	 * @deprecated Use {@link #copyAttributeValue(Object,Object,String)} instead
	 */
	@Deprecated
	public static void unckeckedCopyValorAtributo(Object voOrigem, Object voDestino, String atrib) {
		copyAttributeValue(voOrigem, voDestino, atrib);
	}

	public static void copyAttributeValue(Object fromObject, Object toObject, String attribute) {
		try {
			final Object valorAtributo = ReflectionUtil.getValorAtributo(fromObject, attribute);
			ReflectionUtil.setAtributo(toObject, attribute, valorAtributo);
		} catch (Exception e) {
			throw new IllegalStateException(executionFailedErrorMessage(fromObject.getClass(), attribute), e);
		}
	}

    /**
	 * @deprecated Use {@link #getGetterValue(Object,Method)} instead
	 */
	public static Object unckeckedGetValorGetter(Object vo, Method method) {
		return getGetterValue(vo, method);
	}

	public static Object getGetterValue(Object object, Method method) {
		try {
			return method.invoke(object, colecaoVaziaObjects);
		} catch (Exception e) {
			throw new IllegalStateException(executionFailedErrorMessage(object.getClass(), method.getName()), e);
		}
	}
    
    /**
	 * @deprecated Use {@link #getAttributeValue(Object,String)} instead
	 */
	@Deprecated
	public static Object unckeckedGetValorAtributo(Object vo, String atrib) {
		return getAttributeValue(vo, atrib);
	}

	public static Object getAttributeValue(Object object, String attribute) {
		try {
			return ReflectionUtil.getValorAtributo(object, attribute);
		} catch (Exception e) {
			throw new IllegalStateException(executionFailedErrorMessage(object.getClass(), attribute), e);
		}
	}

    /**
	 * @deprecated Use {@link #setAttributeValue(Object,String,Object)} instead
	 */
	@Deprecated
	public static Object unckeckedSetValorAtributo(Object vo, String atrib, Object value) {
		return setAttributeValue(vo, atrib, value);
	}

	public static Object setAttributeValue(Object object, String attribute, Object value) {
		try {
			return ReflectionUtil.setAtributo(object, attribute, value);
		} catch (Exception e) {
			throw new IllegalStateException(executionFailedErrorMessage(object.getClass(), attribute), e);
		}
	}
   
    /**
	 * @deprecated Use {@link #invokeMethod(Object,String,Object[],Class<? extends Object>[])} instead
	 */
    @Deprecated
	public static Object unckeckedinvocaMetodo(Object vo, String method, Object[] params, Class<? extends Object>[] classesParams) {
		return invokeMethod(vo, method, classesParams, params);
	}

	@SuppressWarnings("unchecked")
	public static Object invokeMethod(Object object, String method, Class[] paramClasses, Object[] params) {
		try {
			return getMethod(object.getClass(), method, paramClasses).invoke(object, params);
		} catch (Exception e) {
			throw new IllegalStateException(executionFailedErrorMessage(object.getClass(), method), e);
		}
	}

	@SuppressWarnings("unchecked")
	public static final Object invokeStaticMethod(Class clazz, String method, Class[] paramClasses, Object[] params) {
		try {
			return getMethod(clazz, method, paramClasses).invoke(null, params);
		} catch (Exception e) {
			throw new IllegalStateException(executionFailedErrorMessage(clazz, method), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * @return An instance of <i>clazz</i> for classes implementing the Singleton pattern. 
	 */
	public static <T> T getSingletonInstance(Class<T> clazz) {
		return (T) invokeStaticMethod(clazz, "getInstance", colecaoVaziaClazz, colecaoVaziaObjects);
	}

	private static String executionFailedErrorMessage(Class<?> clazz, String method) {
		return "Error executing method '" + method + "' of class '" + clazz.getCanonicalName() +"'.";
	}
	
	public static List<Method> gettersForFieldsContainingAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
		Map<Field, Method> gettersMap = gettersMapForFieldsContainingAnnotation(clazz, annotation);
        return new ArrayList<Method>(gettersMap.values());
	}

	public static Map<Field, Method> gettersMapForFieldsContainingAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
		Map<Field, Method> map = new HashMap<Field, Method>();
        Class<?> clazzAtual = clazz;
        while (!Object.class.equals(clazzAtual) && clazzAtual != null) {
            final Field[] fields = clazzAtual.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                if (field.getAnnotation(annotation) != null) {
                	map.put(field, getGetter(clazz, field.getName()));
                }
            }
            clazzAtual = clazzAtual.getSuperclass();
        }
		return map;
	}
	
	public static List<Method> settersForFieldsContainingAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
		Map<Field, Method> settersMap = settersMapForFieldsContainingAnnotation(clazz, annotation);
        return new ArrayList<Method>(settersMap.values());
	}

	private static Map<Field, Method> settersMapForFieldsContainingAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
		Map<Field, Method> map = new HashMap<Field, Method>();
        Class<?> clazzAtual = clazz;
        while (!Object.class.equals(clazzAtual) && clazzAtual != null) {
            final Field[] fields = clazzAtual.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                if (field.getAnnotation(annotation) != null) {
                	map.put(field, getSetter(clazz, field));
                }
            }
            clazzAtual = clazzAtual.getSuperclass();
        }
		return map;
	}
	

}
