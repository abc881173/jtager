package com.jtager.libs.utils;

import android.annotation.SuppressLint;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@SuppressLint("DefaultLocale")
public class PingYinUtil {
	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 * 
	 * @param inputString
	 * @return
	 */
	public static String getPingYin(String inputString) {
//		if (inputString.equals("1我最近的沟通") || inputString.equals("2公司") || inputString.equals("3合作伙伴")) {
//			return "#";
//		}
		if ("".equals(inputString)) {
			return "";
		}
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		char[] input = inputString.trim().toCharArray();
		String output = "";
		if (input != null && input.length > 0) {
			if ((input[0] > 97 && input[0] < 122) || (input[0] > 65 && input[0] < 90)) {
				output = String.valueOf(input[0]).toLowerCase();
			} else {
				try {
					for (int i = 0; i < input.length; i++) {
						if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
							String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
							output += temp[0];
						} else
							output += java.lang.Character.toString(input[i]);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			}

		}
		return output.toUpperCase();
	}

	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 * 
	 * @param chines汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chines) {
//		if (chines.equals("1我最近的沟通") || chines.equals("2公司") || chines.equals("3合作伙伴")) {
//			return "#";
//		}
		if ("".equals(chines)) {
			return "";
		}
		chines = chines.substring(0, 1);
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					if (null == nameChar || nameChar.length == 0) {
						return "";
					}
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName.toUpperCase();
	}

}