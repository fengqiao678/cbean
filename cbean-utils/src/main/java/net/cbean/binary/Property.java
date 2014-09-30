/**
 * @author WuTao
 *
 * 2006-7-28 11:40:49
 */
package net.cbean.binary;

/**
 * BinFactory�����ļ���Property��ӳ���࣬��ʾStructure�е�һ���ֶ�
 */
public class Property {
	/**
	 * �������ƣ���ӦJavaBean��Property��<br>
	 *   ����Property��structureΪcollection��nameֵΪvalue�����Ի����Collection.add()<br>
	 *   ����Property��structrueΪmap��nameֵΪkey��value������Ϊ�����map.put(key,value)
	 */
	private String name;
	/**
	 * �������ͣ���ӦParserManager��֧�ֵ����ͣ����߿��Զ�Ӧstructure id,<br>
	 * ��ʾ������Դ洢����һ���ṹ��
	 *  ��̬���� - Ҳ�������ⲿ��̬Java�����ķ���ֵ�����Ǵ˾�̬java�����ķ���ֵ����һ��Ҫ��BinParser<br>
	 */
	private String type;
	/**
	 * ���Եĳ��ȣ�֧�ֵ��������£�<br>
	 *   ��ֵ - ʮ��������<br>
	 *   ���� - ����������ϵ��Ѿ���������������ֵ�����ҿ�����+-��ʽ��<br>
	 *   ��̬���� - Ҳ�������ⲿ��̬Java�����ķ���ֵ�����Ǵ˾�̬java�����ķ���ֵ����һ��Ҫ��Integer<br>
	 *   \@end - ��ʾ�ӵ�ǰλ�õ�byte[]�����ĳ���<br>
	 *   ���� - ��ʾ���������һ��structure���������structure�ǿ�����ָ�����ȵ�
	 */
	private String length;
	/**
	 * ��ʶ�����Բ���ΪJavaBean��Property
	 */
	private boolean ignore = false;
	/**
	 * ����ת���ɶ�����ʱ��Ĭ��ֵ��֧�ֵ��������£�<br>
	 *   \@size - ��ʾ���ṹ�����Դ��������µĶ����Ƴ���<br>
	 *   ���� - ����������ϵ��Ѿ��������������Զ�����ֵ<br>
	 *   ignore - �������ֵ�����ڶ�������д��<br>
	 *   ������ֵ - ʮ��������ֵ�ִ��������ÿո�ֿ�<br>
	 *   ��̬���� - ��length��ͬ
	 */
	private String value;
	
	/**
	 * @return Returns the ignore.
	 */
	public boolean isIgnore() {
		return ignore;
	}
	/**
	 * @param ignore The ignore to set.
	 */
	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}
	/**
	 * @return Returns the length.
	 */
	public String getLength() {
		return length;
	}
	/**
	 * @param length The length to set.
	 */
	public void setLength(String length) {
		this.length = length;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
}
