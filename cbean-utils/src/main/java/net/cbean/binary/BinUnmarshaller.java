/**
 * @author WuTao
 *
 * 2006-7-28 11:07:28
 */
package net.cbean.binary;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.cbean.exceptions.ParseException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * ������ת���ɶ���Ľ�����������unmarshal(final byte[] input)����������һ�������ķ��Ͷ���<br>
 * �������̲߳���ȫ�ģ��б�ҪΪÿ���������new����һ��BinUnmarshaller����
 */
public class BinUnmarshaller<V> extends BinAbstractFactory{
	private static final Log log = LogFactory.getLog(BinUnmarshaller.class);
	
	private Map<String,Object> arguments = new HashMap<String,Object>();
	private Map<String,Long> childStructreLength = new HashMap<String,Long>();
	
	
	public BinUnmarshaller(BinMapping mapping,ParserHandle parserHandle){
		this.mapping = mapping;
		this.parserHandle = parserHandle;
	}
	
	@SuppressWarnings("unchecked")
	public V unmarshal(final byte[] input) throws ParseException {
		data = ByteBuffer.wrap(input);
		
		Structure structure = mapping.getStructureList().get(0); //�ӵ�һ��Structrue��ʼ
		
		try {
			return (V) workRound( structure);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParseException("Can't unmarshal byte array to Object!",e);
		}
	}
	
	/**
	 * ����
	 * @param data
	 * @param structure
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	private Object workRound(final Structure structure) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Class clazz = Class.forName(structure.getClazz());
		Object obj = clazz.newInstance();
		
		boolean isCollection = !(structure.getCollection()==null || "".equals(structure.getCollection()));
		
		Long structureLength = childStructreLength.get(structure.getId());
		long endPosition = (structureLength==null) ? 
							data.limit() :
							structureLength + data.position();
							
		while(data.position()<endPosition){   //����ִ��collection��������������collection��ֻ��ִ��һ�Σ��ڷ�����������ж�
			for(Property prop : structure.getProperties()){
				Object value = null;
				
				Long length = getLength(structure,prop.getLength());
				if(length!=null && data.position()+length>endPosition){
					throw new IllegalArgumentException(structure.getId()+"."+prop.getName()+" data out of range. end postion is "+endPosition+", but want to get "+length+" bytes from index "+data.position());
				}
				
				
				Structure child = this.mapping.getStructureMap().get(prop.getType());

				if(length==null && child==null){
					throw new IllegalArgumentException("there is no length at "+structure.getId()+"."+prop.getName());
				}
				
				if(child!=null){
					childStructreLength.put(child.getId(), length);  //������������ӽṹ�ĳ��ȣ������´�ѭ��ʱ�����õ�
					value = workRound(child);  //�ݹ飬������structure
				}else{
					//������ͨ����
					if(length>0){
						byte[] pdata = new byte[(int) length.longValue()];
						data.get(pdata);
						BinParser parser = getParser(structure, prop.getType());
						value = parser.decode(pdata);
					}else if(prop.getValue().matches(PROPERTY_REGEX)){
						value = getArgValue(structure.getId(),prop.getValue());
					}
				}
				
				arguments.put(structure.getId()+"."+prop.getName(), value);
				
				if(!isCollection && !prop.isIgnore()){
					BeanUtils.copyProperty(obj, prop.getName(), value);
				}
			}
			
			String collectionTag = structure.getCollection();
			if(collectionTag!=null && "MAP".equals(collectionTag.toUpperCase())){
				Object key = arguments.get(structure.getId()+"."+"key");
				Object value = arguments.get(structure.getId()+"."+"value");
				((Map)obj).put(key, value);
			}else if(collectionTag!=null && "COLLECTION".equals(collectionTag.toUpperCase())){
				Object value = arguments.get(structure.getId()+"."+"value");
				((Collection)obj).add(value);
			}
			
			if(data.position()>endPosition){
				throw new IllegalStateException("please check if data input correct! expect end position is "+endPosition+" but now is "+data.position());
			}
			//������Collection�ṹ��ȴִ�г���һ�ε�ѭ������ʾ���ĳ��ȳ���Ԥ��ֵ�����ǲ���Ӱ�������ֻ����һ�ξ�������
			if(!isCollection){
				if(data.position()!=endPosition){
					log.warn("structure "+structure.getId()+" end unnormal! end position:"+endPosition+" but now is "+data.position());
				}
				break;
			}
		}
		return obj;
	}

	
	@Override
	Object getArgValue(final String structureId, String argStr) {
		argStr = argStr.substring(1);
		Object argObj = this.arguments.get(
				(argStr.indexOf('.')>0) ? argStr : structureId+'.'+argStr);
		if(argObj==null){
			throw new IllegalArgumentException("can't get argument of "+structureId+'.'+argStr);
		}
		return argObj;
	}
}
