/**
 * @author WuTao
 *
 * 2006-7-28 11:09:09
 */
package net.cbean.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cbean.xml.CastorXmlHelper;

/**
 * BinFactory�����ļ���ӳ����󣬴˶�������Castor����XML���ɵġ�<br>
 * �ṩ������̬����loadMapping(InputStream inputStream)��loadMapping(String
 * mappingFile)����binFactory�����á�<br>
 * ����BinMapping��Ҫ����XML���ã������Ҫ����ʹ�ã������һ��cache.
 * <p>
 * 
 * @see com.shjv.tdscdma.omc.simulator.server.model.binary.Structure
 * @see com.shjv.tdscdma.omc.simulator.server.model.binary.Property
 */
public class BinMapping {
	private static final String MAPPING_FILE = "mapping/bin_mapping.castor.xml";

	private List<Structure> structureList;
	private Map<String, Structure> strucMap;

	/**
	 * @return Returns the structureList.
	 */
	public List<Structure> getStructureList() {
		return structureList;
	}

	/**
	 * @param structureList
	 *            The structureList to set.
	 */
	public void setStructureList(List<Structure> structureList) {
		this.structureList = structureList;
	}

	public Map<String, Structure> getStructureMap() {
		if (strucMap == null && structureList != null) {
			strucMap = new HashMap<String, Structure>();
			for (Structure struc : structureList) {
				strucMap.put(struc.getId(), struc);
			}
		}
		return strucMap;
	}

	public static BinMapping loadMapping(InputStream inputStream) {
		InputStream in = BinMapping.class.getClassLoader().getResourceAsStream(
				MAPPING_FILE);
		CastorXmlHelper<BinMapping> xmlHelper = new CastorXmlHelper<BinMapping>(
				in);
		return xmlHelper.loadFromXml(inputStream);
	}

	public static BinMapping loadMapping(String mappingFile)
			throws FileNotFoundException {
		File file = new File(mappingFile);
		InputStream stream;
		if (file.isFile()) {
			stream = new FileInputStream(file);
		} else {
			stream = ClassLoader.getSystemClassLoader().getResourceAsStream(
					mappingFile);
		}
		return loadMapping(stream);
	}
}
