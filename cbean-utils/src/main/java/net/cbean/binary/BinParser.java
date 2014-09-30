/**
 * @author WuTao
 *
 * 2006-9-1 11:16:40
 */
package net.cbean.binary;

/**
 * ������������������֮���ת����ϵ
 */
public interface BinParser<V> {
	/**
	 * �⿪������
	 * @param data
	 * @return
	 */
	public V decode(byte[] data);
	
	/**
	 * ת���ɶ�����
	 * @param value
	 * @return
	 */
	public byte[] encode(V value);
	
	/**
	 * ת���ɶ���������
	 * @param value
	 * @param length �����Ƴ���
	 * @return
	 */
	public byte[] encode(V value,int length);
}
