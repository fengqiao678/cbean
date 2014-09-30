/**
 * @author WuTao
 *
 * 2006-7-28 11:11:55
 */
package net.cbean.binary;

import java.util.ArrayList;
import java.util.List;

/**
 * BinFactory�����ļ���Structure��ӳ���࣬��ʾһ���ṹ�壬�ڲ��ж��Property���
 */
public class Structure {
	/**
	 * structure��Ψһ��ʶ��ͬһ�������ļ��в����ظ�
	 */
	private String id;
	/**
	 * ��Ӧ��java�࣬��ʹ�÷�����һ��ʵ�������Բ����ǽӿڻ���abstract��
	 */
	private String clazz;
	/**
	 * collection / map<br>
	 * ���class��ʵ��Collection�ӿڵģ���������Ե�ֵ��collection��<br>
	 * ���class��ʵ��Map�ӿڵģ����������ֵ��map��
	 */
	private String collection;
	private List<Property> properties = new ArrayList<Property>();
	/**
	 * @return Returns the clazz.
	 */
	public String getClazz() {
		return clazz;
	}
	/**
	 * @param clazz The clazz to set.
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the properties.
	 */
	public List<Property> getProperties() {
		return properties;
	}
	/**
	 * @param properties The properties to set.
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	/**
	 * @return Returns the collection.
	 */
	public String getCollection() {
		return collection;
	}
	/**
	 * @param collection The collection to set.
	 */
	public void setCollection(String collection) {
		this.collection = collection;
	}
}
