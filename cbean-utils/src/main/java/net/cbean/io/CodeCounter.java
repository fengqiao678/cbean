package net.cbean.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CodeCounter {
	/** ��ͨ���� */
	private static long allnormalLines = 0;
	/** ע������ */
	private static long allcommentLines = 0;
	/** �հ����� */
	private static long allspaceLines = 0;
	/** ������ */
	private static long alltotalLines = 0;

	/** ��ͨ���� */
	private long normalLines = 0;
	/** ע������ */
	private long commentLines = 0;
	/** �հ����� */
	private long spaceLines = 0;
	/** ������ */
	private long totalLines = 0;

	/***
	 * ͨ��java�ļ�·������ö���
	 * 
	 * @param filePath
	 *            java�ļ�·��
	 */
	public CodeCounter(String filePath) {
		tree(filePath);
	}

	/**
	 * �����ļ��ķ���
	 * 
	 * @param filePath
	 *            �ļ�·��
	 */
	private void tree(String filePath) {
		File file = new File(filePath);
		File[] childs = file.listFiles();
		if (childs == null) {
			parse(file);
		} else {
			for (int i = 0; i < childs.length; i++) {
				// System.out.println("path:"+childs[i].getPath());
				if (childs[i].isDirectory()) {
					tree(childs[i].getPath());
				} else {
					if (childs[i].getName().matches(".*\\.java$")) {
						System.out.println("path:" + childs[i].getPath());
						System.out
								.println("��ǰ" + childs[i].getName() + "��������:");
						parse(childs[i]);
						getCodeCounter();
					}
				}
			}
		}
	}

	/**
	 * �����ļ�
	 * 
	 * @param file
	 *            �ļ�����
	 */
	private void parse(File file) {
		BufferedReader br = null;
		boolean comment = false;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				line = line.trim();// ȥ���ո�
				if (line.matches("^[\\s&&[^\\n]]*$")) {
					spaceLines++;
				} else if ((line.startsWith("/*")) && !line.endsWith("*/")) {
					commentLines++;
					comment = true;
				} else if (true == comment) {
					commentLines++;
					if (line.endsWith("*/")) {
						comment = false;
					}
				} else if (line.startsWith("//")) {
					commentLines++;
				} else {
					normalLines++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �õ�Java�ļ��Ĵ�������
	 */
	private void getCodeCounter() {
		totalLines = normalLines + spaceLines + commentLines;
		allnormalLines += normalLines;
		allspaceLines += spaceLines;
		allcommentLines += commentLines;
		alltotalLines += totalLines;
		System.out.println("��ͨ��������:" + normalLines);
		System.out.println("�հ״�������:" + spaceLines);
		System.out.println("ע�ʹ�������:" + commentLines);
		System.out.println("����������:" + totalLines);
		normalLines = 0;
		spaceLines = 0;
		commentLines = 0;
		totalLines = 0;
	}

	public static void main(String args[]) {
		CodeCounter counter = new CodeCounter(
				"D:\\Workspace\\SzTeMIPPhase5\\Workspace\\TWSAccessModule\\src");

		System.out.println("\n\n��ͨ������:" + allnormalLines);
		System.out.println("�հ״�����:" + allspaceLines);
		System.out.println("ע�ʹ�����:" + allcommentLines);
		System.out.println("��������:" + alltotalLines);
	}
}
