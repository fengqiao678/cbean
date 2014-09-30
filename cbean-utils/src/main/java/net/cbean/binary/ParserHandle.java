/**
 * @author WuTao
 *
 * 2006-9-1 11:27:20
 */
package net.cbean.binary;


/**
 * �������BinParser������
 */
public interface ParserHandle {
	/**
	 * �����������ƻ�����Խ�����
	 * @param parserName
	 * @return
	 */
	public BinParser getParser(String parserName);
	
	/**
	 * ��ô�������Ϣ�����Խ�����
	 * @param <T>
	 * @param parserName
	 * @param t
	 * @return
	 */
	public <T> BinParser<T> getParser(String parserName,Class<T> t);
	
	/**
	 * ����һ���µ�Parser
	 * @param key
	 * @param parser
	 */
	public void setParser(String key, BinParser parser);
}
