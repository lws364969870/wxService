package com.lws.domain.utils.pwd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * <strong>Title : CheckPWD</strong><br>
 * <strong>Description : 密码强度验证</strong><br>
 * <strong>Create on : Dec 6, 2011 7:34:45 PM</strong><br>
 * @author liulang@mochasoft.com.cn<br>
 * @version v1.0<br>
 * <br>
 *          详细验证步骤说明：<br>
 *          1：判断密码是否为null或空，如果不是进入下一步，否则返回结果（密码安全性不符合）<br>
 *          2：判断密码字符串长度是否符合要求，默认是大于等于6位，如果是进入下一步，否则返回结果（密码安全性不符合）<br>
 *          3：循环取出密码串中从0位置到长度下限++的子串，进行下面4到8的操作并记录结果到一个double[]中 4：评估密码中包含的字符类型是否符合要求，如果低于下限返回0，否则返回6-10的double<br>
 *          5：评估密码至少包含的不同字符数（不区分大、小写），返回int，如果字符数小于下限返回0，否则返回6-10的double<br>
 *          6：评估密码字符串是否包含a-z,z-a这样的连续字符，返回一个double，如果连续字符占整个串长度的40%以上返回0，否则返回(1-连续字符占整个串长度的百分比) * 10<br>
 *          7：评估密码字符串是否匹配键盘输入习惯，返回0-10的整数，值越大表示越不符合键盘输入习惯<br>
 *          8：根据3、4、5、6的评估结果综合评估出密码的安全评估值（0-10的double）<br>
 *          9：循环3中double[]的值，如果全是0返回0，否则从第一个不是0的位置开始累加，如果后一个位置为0则加长度修正值1，如果累加结果大于10，循环结束返回10<br>
 *          9：判断7产生的安全评估值是否大于安全评估值的下限（默认7），是返回true，否则返回false <br>
 *          <strong>修改历史:</strong><br>
 *          修改人-------------修改日期-------------修改描述<br>
 *          --------------------------------------------<br>
 *          liulang-----------2011/12/7---------<br>
 *          &nbsp;&nbsp;1：增加容错处理；<br>
 *          &nbsp;&nbsp;2：修改键盘输入习惯匹配评估算法的逻辑；<br>
 *          &nbsp;&nbsp;3：增加一个更加严格的键盘习惯匹配算法maches2（可匹配类似1z2x3c4v5b这样的特殊字符串）<br>
 * <br>
 */
public final class CheckPWD {
	private static Logger logger = Logger.getLogger(CheckPWD.class);

	/**
	 * 安全密码评估值的下限，取值范围：0-10，默认：7
	 */
	public static double PASSWORD_STRONG = 7;

	/**
	 * 密码字符串包含的字符类型（a-z,A-Z,0-9,特殊字符）在密码安全性评估中所占比例，默认：0.15
	 */
	public static double CHAR_TYPE_NUM_THRESHOLD = 0.15;

	/**
	 * 密码字符串包含的不同字符数（不区分大、小写，同一键位的视为同一字符）在密码安全性评估中所占比例，默认：0.35
	 */
	public static double MIN_CONTAIN_CHAR_NUM_THRESHOLD = 0.35;

	/**
	 * 密码字符串匹配键盘输入习惯在密码安全性评估中所占比例，默认：0.3
	 */
	public static double KEYSET_MATCHER_THRESHOLD = 0.3;

	/**
	 * 评估密码字符串中a-z,z-a这样的连续字符在密码安全性评估中所占比例，默认：0.2
	 */
	public static double SEQUENTIAL_CHARS_THRESHOLD = 0.2;

	/**
	 * 键盘输入习惯匹配规则严格模式
	 */
	public static int KEYSET_MATCHER_STRICT_MODE = 1;

	/**
	 * 键盘输入习惯匹配规则不严格模式
	 */
	public static int KEYSET_MATCHER_UNDEMANDING_MODE = 0;

	/**
	 * 密码最小长度，默认为6
	 */
	private static int MIN_LENGTH = 6;

	/**
	 * 密码至少包含的不同字符数（不区分大、小写），（例如："aaa"包含一个字符，"aba"包含2个字符，"aA"包含1个字符等），默认为4
	 */
	private static int MIN_CONTAIN_CHAR_NUM = 4;

	/**
	 * 密码至少包含的字符类型数（a-z,A-Z,0-9,特殊字符），默认为2
	 */
	private static int MIN_CHAR_TYPE_NUM = 2;

	/**
	 * 中等强度密码键盘规则匹配严格度，默认0.6
	 */
	private static double THRESHOLD_MEDIUM = 0.6;

	/**
	 * 高安全度密码键盘规则匹配严格度，默认0.4
	 */
	private static double THRESHOLD_STRONG = 0.4;

	/**
	 * 高安全度密码键盘规则匹配严格度，默认0.4
	 */
	private static double MAX_SEQUENTIAL_CHARS_THRESHOLD = 0.4;

	/**
	 * 字母顺序A-Z
	 */
	private static String A_Z = "abcdefghijklmnopqrstuvwxyz";

	/**
	 * 字母顺序Z-A
	 */
	private static String Z_A = "zyxwvutsrqponmlkjihgfedcba";

	/**
	 * shift键产生的字符与非shift键时对应的字符
	 */
	private static Map CONVERSION_MAP = new HashMap();

	/**
	 * 将键盘按键格式话为4*10的矩阵的坐标
	 */
	private static Map DICTIONARY_MAP = new HashMap();
	private static String[] dictionary1 = "1 2 3 4 5 6 7 8 9 0".split("\\s+");
	private static String[] dictionary2 = "q w e r t y u i o p".split("\\s+");
	private static String[] dictionary3 = "a s d f g h j k l ;".split("\\s+");
	private static String[] dictionary4 = "z x c v b n m , . /".split("\\s+");
	private static String[][] DICTIONARY_MATRIX = { dictionary1, dictionary2, dictionary3, dictionary4 };

	static {
		CONVERSION_MAP.put("!", "1");
		CONVERSION_MAP.put("@", "2");
		CONVERSION_MAP.put("#", "3");
		CONVERSION_MAP.put("$", "4");
		CONVERSION_MAP.put("%", "5");
		CONVERSION_MAP.put("^", "6");
		CONVERSION_MAP.put("&", "7");
		CONVERSION_MAP.put("*", "8");
		CONVERSION_MAP.put("(", "9");
		CONVERSION_MAP.put(")", "0");
		CONVERSION_MAP.put(":", ";");
		CONVERSION_MAP.put("<", ",");
		CONVERSION_MAP.put(">", ".");
		CONVERSION_MAP.put("?", "/");

		for (int i = 0, ln = DICTIONARY_MATRIX.length; i < ln; i++) {
			String[] dic = DICTIONARY_MATRIX[i];
			for (int j = 0, lnt = dic.length; j < lnt; j++) {
				int[] row_cell = { i, j };
				DICTIONARY_MAP.put(dic[j], row_cell);
			}
		}
	}

	private CheckPWD() {

	}

	/**
	 * 严格的键盘输入习惯匹配规则，匹配连续或者非连续的(可以匹配：1a2s3d4f5g这样的有规律的串)，比matches方法的匹配规则更严格
	 * @param matcherList
	 * @param row_cellList
	 * @param index
	 */
	private static void maches2(List matcherList, List row_cellList, int index) {
		for (; index < row_cellList.size(); index++) {
			int[] row_cell_t = (int[]) row_cellList.get(index);
			if (row_cell_t != null) {
				boolean flag = true;
				for (int i = 0; i < matcherList.size(); i++) {
					List list = (List) matcherList.get(i);
					for (int j = 0; j < list.size(); j++) {
						int[] row_cell = (int[]) list.get(j);
						if (((Math.abs(row_cell_t[0] - row_cell[0]) <= 1) && (Math.abs(row_cell_t[1] - row_cell[1]) <= 1))) {
							list.add(row_cell_t);
							flag = false;
							break;
						}
					}
					if (!flag)
						break;
				}
				if (flag) {
					List arrt = new ArrayList();
					arrt.add(row_cell_t);
					matcherList.add(arrt);
				}
			}
		}
	}

	/**
	 * 键盘输入习惯匹配规则，匹配连续输入(不能匹配：1a2s3d4f5g这样的有规律的串)，比maches2方法的匹配规则宽松
	 * @param matcherList
	 * @param row_cellList
	 * @param index
	 */
	private static void matches(List matcherList, List row_cellList, int index) {
		for (int i = 0; i < matcherList.size(); i++) {
			List list = (List) matcherList.get(i);
			for (int ln = row_cellList.size(); index < ln; index++) {
				int[] row_cell = (int[]) list.get(list.size() - 1);
				int[] row_cell_t = (int[]) row_cellList.get(index);
				// 如果相邻的键盘字符（某一个键的左右、上下、斜向相邻）被连续输入，在原匹配链条上增加新的输入
				if ((row_cell != null) && (row_cell_t != null) && ((Math.abs(row_cell_t[0] - row_cell[0]) <= 1) && (Math.abs(row_cell_t[1] - row_cell[1]) <= 1))) {
					list.add(row_cell_t);
				} else { // 如果新字符和上一匹配链条的结尾字符距离较远，这结束上一匹配链条，以该字符为首增加新的匹配链条
					List arrt = new ArrayList();
					arrt.add(row_cell_t);
					matcherList.add(arrt);
					index++;
					break;
				}
			}
		}
	}

	/**
	 * 键盘规则匹配器，返回double
	 * @param password 密码字符串
	 * @return
	 */
	public static double keysetMatcher(String password, int matchesMode) {
		String t_password = new String(password);
		t_password = t_password.toLowerCase();
		t_password = canonicalizeWord(password);
		logger.debug("****************将密码字符串转换为键盘矩阵的对应坐标 start******************");
		char[] pwdCharArr = t_password.toCharArray();
		List row_cellList = new ArrayList();
		int Num = 0;
		int startIndex = -1;
		for (int i = 0, ln = pwdCharArr.length; i < ln; i++) {
			int[] row_cell = (int[]) DICTIONARY_MAP.get(String.valueOf(pwdCharArr[i]));
			if (row_cell != null) {
				row_cellList.add(row_cell);
				Num++;
				if (startIndex == -1)
					startIndex = i;
			} else
				row_cellList.add(null);
		}
		logger.debug("****************将密码字符串转换为键盘矩阵的对应坐标 end******************");

		logger.debug("****************初始化匹配链条 start******************");
		int index = startIndex + 1;
		int[] row_cell0 = (int[]) row_cellList.get(startIndex);
		List matcherList = new ArrayList();
		List arr0 = new ArrayList();
		arr0.add(row_cell0);
		matcherList.add(arr0);
		logger.debug("****************初始化匹配链条 end******************");
		// 根据匹配规则进行匹配
		if (KEYSET_MATCHER_UNDEMANDING_MODE == matchesMode)
			matches(matcherList, row_cellList, index); // 不严格的匹配模式
		else if (KEYSET_MATCHER_STRICT_MODE == matchesMode)
			maches2(matcherList, row_cellList, index); // 严格的匹配模式

		double rValue = 0;
		for (double threshold = THRESHOLD_STRONG; threshold <= THRESHOLD_MEDIUM; threshold += 0.1) {
			boolean flag = true;
			int nMinimumMeaningfulMatchLength = (int) (Num * threshold);
			// 特殊字符（~ ` - _ = + [ { ] } \ | ' "）所占比率上限
			if (threshold <= ((t_password.length() - Num) * 1.0 / t_password.length()))
				flag = false;
			if (flag) {
				for (int i = 0; i < matcherList.size(); i++) {
					List list = (List) matcherList.get(i);
					if (list.size() >= nMinimumMeaningfulMatchLength) {
						flag = false;
						return rValue;
					}
				}
			}
			if (flag) {
				if (THRESHOLD_MEDIUM == THRESHOLD_STRONG)
					rValue = 10;
				else
					rValue = 6 + 4 * (THRESHOLD_MEDIUM - threshold) * 1.0 / (THRESHOLD_MEDIUM - THRESHOLD_STRONG);
				break;
			}
		}
		return rValue;
	}

	/**
	 * 替换密码中的shift键产生的字符转换为非shift键时对应的字符
	 * @param password 密码字符串
	 * @return 替换后的密码字符串
	 */
	private static String canonicalizeWord(String password) {
		StringBuffer sb = new StringBuffer();
		if (password != null && password.length() > 0) {
			for (int i = 0; i < password.length(); i++) {
				String cs = String.valueOf(password.charAt(i));
				if (CONVERSION_MAP.get(cs) != null)
					sb.append(CONVERSION_MAP.get(cs));
				else
					sb.append(cs);
			}
		}
		return sb.toString();
	}

	/**
	 * 搜索字符str中是否包含有regex指定的正则表达式匹配的子串
	 * @param str 待搜索字符串
	 * @param regex 正则表达式字符串
	 * @return 包含匹配子串返回true，否则返回false
	 */
	private static boolean stringFind(String str, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if (m.find())
			return true;
		return false;
	}

	/**
	 * 评估密码中包含的字符类型是否符合要求，如果低于下限返回0，否则返回6 + (字符类型总数 - 下限) * 2
	 * @param password 密码字符串
	 * @param num 密码至少包含的字符类型数（a-z,A-Z,0-9,特殊字符），默认为2
	 * @return
	 */
	public static double checkCharTypeNum(String password, int num) {
		int typeNum = 0;
		if (stringFind(password, "[a-z]+"))
			typeNum++;
		if (stringFind(password, "[0-9]+"))
			typeNum++;
		if (stringFind(password, "[A-Z]+"))
			typeNum++;
		if (stringFind(password, "\\p{Punct}+"))
			typeNum++;
		double rValue = 0;
		if (typeNum >= num) {
			if (num == 4)
				rValue = 10;
			else
				rValue = 6 + (typeNum - num) * 1.0 / (4 - num) * 4;
		}
		return rValue;
	}

	/**
	 * 评估a-z,z-a这样的连续字符，返回一个double，如果连续字符占整个串长度的40%以上返回0，否则返回(1-连续字符占整个串长度的百分比) * 10
	 * @param password
	 * @return
	 */
	public static double checkSequentialChars(String password) {
		String t_password = new String(password);
		t_password = t_password.toLowerCase();
		double rValue = 0;
		int i = 2;
		int n = t_password.length();
		for (; i < n; i++) {
			boolean flag = true;
			for (int j = 0; j + i < n; j++) {
				String str = t_password.substring(j, j + i);
				if ((A_Z.indexOf(str) != -1) || (Z_A.indexOf(str) != -1)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				if (i * 1.0 / n > MAX_SEQUENTIAL_CHARS_THRESHOLD)
					rValue = 0;
				else
					rValue = (1 - i * 1.0 / n) * 10;
				break;
			}
		}
		return rValue;
	}

	/**
	 * 评估密码至少包含的不同字符数（不区分大、小写），返回int，如果字符数小于下限返回0，否则返回6 + (包含字符数 - 下限)，如果大于10，返回10
	 * @param password 密码字符串
	 * @param num 密码至少包含的字符类型数（a-z,A-Z,0-9,特殊字符），默认为2
	 * @return
	 */
	public static double checkMinContainCharNum(String password, int num) {
		String t_password = new String(password);
		// t_password = t_password.replaceAll("(.)\\1+", "$1");
		t_password = t_password.toLowerCase();
		t_password = canonicalizeWord(password);
		Map map = new HashMap();
		int snum = 0;
		for (int i = 0, ln = t_password.length(); i < ln; i++) {
			String cs = String.valueOf(t_password.charAt(i));
			if (map.get(cs) == null) {
				map.put(cs, cs);
				snum++;
			}
		}
		double rValue = 0;
		if (snum >= num)
			rValue = 6 + (snum - num);
		return (rValue > 10 ? 10 : rValue);
	}

	/**
	 * 评估密码强度，根据密码长度,包含的字符类型,包含不同字符数，包含的连续字符，键盘输入习惯综合评估密码强度，返回0-10的double
	 * @param password
	 * @return
	 */
	public static double evalPWD(String password, int matchesMode) {
		if (password == null || "".equals(password.replaceAll("\\s+", "")))
			return 0; // 判断密码为null或""
		if (MIN_LENGTH > password.length())
			return 0; // 判断密码长度是否符合

		double[] val = new double[1000];
		int n = 0;
		for (int i = MIN_LENGTH; i <= password.length(); i++) {
			String t_password = password.substring(0, i);
			// 评估密码中包含的字符类型是否符合要求
			double typeNumCheckResult = checkCharTypeNum(t_password, MIN_CHAR_TYPE_NUM);
			// 评估密码至少包含的不同字符数（不区分大、小写）
			double minContainCharNumCheckResult = checkMinContainCharNum(t_password, MIN_CONTAIN_CHAR_NUM);
			// 评估a-z,z-a这样的连续字符
			double sequentialCharsCheckResult = checkSequentialChars(t_password);
			// 评估键盘输入习惯
			double keysetMatcherResult = keysetMatcher(t_password, matchesMode);
			logger.debug("评估密码中包含的字符类型结果：" + typeNumCheckResult);
			logger.debug("评估密码至少包含的不同字符数结果：" + minContainCharNumCheckResult);
			logger.debug("评估a-z,z-a这样的连续字符结果：" + sequentialCharsCheckResult);
			logger.debug("评估键盘输入习惯结果：" + keysetMatcherResult);
			if (typeNumCheckResult == 0 || minContainCharNumCheckResult == 0 || sequentialCharsCheckResult == 0 || keysetMatcherResult == 0) {
				val[n] = 0;
			} else {
				val[n] = typeNumCheckResult * CHAR_TYPE_NUM_THRESHOLD + minContainCharNumCheckResult * MIN_CONTAIN_CHAR_NUM_THRESHOLD + sequentialCharsCheckResult * SEQUENTIAL_CHARS_THRESHOLD
						+ keysetMatcherResult * KEYSET_MATCHER_THRESHOLD;
			}
			n++;
		}
		double rValue = 0;
		boolean flag = false;
		for (int i = 0; i < n; i++) {
			if (val[i] != 0) {
				rValue += val[i];
				flag = true;
			}
			if (flag && val[i] == 0)
				rValue += 1;
			if (rValue >= 10) {
				rValue = 10;
				break;
			}
		}
		return rValue;

		// //评估密码中包含的字符类型是否符合要求
		// double typeNumCheckResult = checkCharTypeNum(password,MIN_CHAR_TYPE_NUM);
		// //评估密码至少包含的不同字符数（不区分大、小写）
		// double minContainCharNumCheckResult = checkMinContainCharNum(password, MIN_CONTAIN_CHAR_NUM);
		// //评估a-z,z-a这样的连续字符
		// double sequentialCharsCheckResult = checkSequentialChars(password);
		// //评估键盘输入习惯
		// double keysetMatcherResult = keysetMatcher(password, matchesMode);
		// logger.debug("评估密码中包含的字符类型结果："+typeNumCheckResult);
		// logger.debug("评估密码至少包含的不同字符数结果："+minContainCharNumCheckResult);
		// logger.debug("评估a-z,z-a这样的连续字符结果："+sequentialCharsCheckResult);
		// logger.debug("评估键盘输入习惯结果："+keysetMatcherResult);
		// if(typeNumCheckResult == 0 || minContainCharNumCheckResult == 0 || sequentialCharsCheckResult == 0 || keysetMatcherResult == 0) {
		// return 0;
		// }else {
		// return typeNumCheckResult*CHAR_TYPE_NUM_THRESHOLD + minContainCharNumCheckResult*MIN_CONTAIN_CHAR_NUM_THRESHOLD +
		// sequentialCharsCheckResult*SEQUENTIAL_CHARS_THRESHOLD + keysetMatcherResult*KEYSET_MATCHER_THRESHOLD;
		// }
	}

	/**
	 * 用严格模式校验密码强度，根据密码评估结果判断密码是否可用，默认评估结果大于7的可用
	 * @param password
	 * @return
	 */
	public static boolean checkPwdInStrictMode(String password) {
		double rValue = evalPWD(password, KEYSET_MATCHER_STRICT_MODE);
		if (rValue >= PASSWORD_STRONG)
			return true;
		return false;
	}

	/**
	 * 非严格模式校验密码强度，根据密码评估结果判断密码是否可用，默认评估结果大于7的可用
	 * @param password
	 * @return
	 */
	public static boolean checkPwdIndemandingMode(String password) {
		double rValue = evalPWD(password, KEYSET_MATCHER_UNDEMANDING_MODE);
		if (rValue >= PASSWORD_STRONG)
			return true;
		return false;
	}

	public static void main(String[] args) {
		//String[] sArr = { "1a2s3d4f5g6h", "zcvjlufw123433546", "~]sdfa^9mi|", "`%0,uTs85vkj", "liulanggood123", "PASSword_123", "yanghao1234", "yanghao123", "yanghao1981" };
		String[] sArr ={"975jd.21233d"};
		for (int i = 0; i < sArr.length; i++) {
			System.out.println("严格模式下校验" + sArr[i] + "的密码强度:" + evalPWD(sArr[i], KEYSET_MATCHER_STRICT_MODE));
			System.out.println("非格模式下校验" + sArr[i] + "的密码强度:" + evalPWD(sArr[i], KEYSET_MATCHER_UNDEMANDING_MODE));
		}
	}
}