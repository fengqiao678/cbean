/**
 * @author WuTao
 *
 * 2006-8-2 9:51:03
 */
package net.cbean.binary;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ΪBinMarshaller��BinUnmarshaller�ṩһЩ�����Ĵ��������������ģʽ�������ţ��Ժ���ʱ����Կ��Ǹĳ�ί��ģʽ
 */
public abstract class BinAbstractFactory {

	public static final String PROPERTY_REGEX = "(\\$[\\w\\.]+)([+-]*)([\\d]*)";  //���� $length-2
	public static final String METHOD_REGEX = "([\\w\\.]+)\\.([\\w]+)\\(([\\$\\w,\\.]*)\\)";  //����classpath.class.method($arg1,$arg2...)
	private static final Pattern METHOD_PATTERN = Pattern.compile(METHOD_REGEX);
	private static final Pattern PROPERTY_PATTERN = Pattern.compile(PROPERTY_REGEX);
	
	protected ParserHandle parserHandle ;
	protected BinMapping mapping;
	protected ByteBuffer data;
	
	/**
	 * �������Գ��ȡ����Գ���len֧�����֡����������ϵ���������ֵ�����߾�̬�������õĽ����<br>
	 * ���ʹ�þ�̬�������ã����뱣֤��̬�����ķ���ֵ��int��
	 * @param structure
	 * @param strLen
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected Long getLength(final Structure structure, String strLen) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(strLen==null) {
			return null;
		}
		long length = 0;
		if(strLen.matches("\\d+")){  //ֱ�ӵ���ֵ
			length = Long.parseLong(strLen);
		}else if("@END".equals(strLen.toUpperCase())){ //ֱ��������˵����Property���治Ӧ�ô�������Property�ˡ�
			length = data.limit()-data.position();
		}else if(strLen.matches(PROPERTY_REGEX)){  //ʹ���Ѿ����ڵ����ԣ�֧��+-��ֵ�Ĺ�ʽ
			Matcher matcher = PROPERTY_PATTERN.matcher(strLen.subSequence(0, strLen.length()));
			matcher.find();
			
			Object lengthObj = getArgValue(structure.getId(), matcher.group(1));
			if(lengthObj instanceof Long){
				length =  (Long) lengthObj;
			}else if(lengthObj instanceof Integer){
				length = (long) (Integer)lengthObj;
			}
			if(!matcher.group(3).equals("")){
				int second = Integer.parseInt(matcher.group(3));
				if("+".equals(matcher.group(2))){
					length = length + second;
				}else if("-".equals(matcher.group(2))){
					length = length - second;
				}
			}
		}else if(strLen.matches(METHOD_REGEX)){  //�ⲿ��̬����
			Object obj = invokeStaticMethod(structure, strLen);
			if(obj instanceof Long){
				length = (Long) obj;
			}else if (obj instanceof Integer){
				length = (Integer)obj;
			}else{
				throw new IllegalStateException("Can't parse to length: "+obj);
			}
		}else if(strLen==null || "".equals(strLen)){
			length = 0;
		}else{
			throw new IllegalArgumentException("Can't parse propety length: "+strLen);
		}
		return length;
	}
	
	/**
	 * �������ԵĽ�������ͨ��type���ô�ParserManager��ȡ�á�
	 * @param structure
	 * @param parserStr
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected BinParser getParser(final Structure structure, final String parserStr) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		BinParser parser;
		if(parserStr.matches("\\w+")){  //ֱ�ӵ��ִ�
			parser = parserHandle.getParser(parserStr);
		}else if(parserStr.matches(METHOD_REGEX)){  //�ⲿ��̬����
			Object obj = invokeStaticMethod(structure, parserStr);
			if(obj instanceof BinParser){
				parser = (BinParser) obj;
			}else{
				throw new IllegalStateException("Can't parse to type: "+obj);
			}
		}else{
			throw new IllegalArgumentException("Can't parse propety type: "+parserStr);
		}
		
		return parser;
	}
	
	/**
	 * ִ�и�Structure��ص�һ����̬Java�����ű�<br>
	 *    ��ʽΪ��classpath.class.method($arg1,$arg2...)
	 * @param structure
	 * @param methodStr
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	protected Object invokeStaticMethod(final Structure structure, final String methodStr) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Matcher matcher = METHOD_PATTERN.matcher(methodStr.subSequence(0, methodStr.length()));
		matcher.find();
		Class clazz = Class.forName(matcher.group(1));
		List args = new ArrayList();
		List<Class> parameterTypesList = new ArrayList<Class>();
		if(!matcher.group(3).equals("")){
			for(String arg : matcher.group(3).split(",")){
				Object argValue = getArgValue(structure.getId(),arg);
				args.add(argValue);
				if(argValue==null){
					throw new IllegalArgumentException("Can't get parameter value! parameter name: "+structure.getId()+'.'+arg);
				}
				parameterTypesList.add(argValue.getClass());
			}
		}
		
		Class[] parameterTypes = new Class[parameterTypesList.size()];
		for(int i=0; i<parameterTypes.length; i++){
			parameterTypes[i] = parameterTypesList.get(i);
		}
		Method method = clazz.getMethod(matcher.group(2), parameterTypes);
		
		Object obj = method.invoke(null, args.toArray());
		
		return obj;
	}
	
	/**
	 * ȡ����ֵ�����Ǳ�structure�ڲ��Ĳ������Ͳ���Ҫдstructure��id��
	 * @param structureId
	 * @param strLen
	 * @return
	 */
	abstract Object getArgValue(final String structureId, String argStr);
}
