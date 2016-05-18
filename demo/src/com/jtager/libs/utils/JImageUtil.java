package com.jtager.libs.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class JImageUtil {

	/**
	 * 压缩函数
	 * 
	 * @param context
	 * @param input
	 * @param type
	 * @return
	 */
	public final static String compressPic(Context context, String input,
			JCompressTypes type) {
		if (input == null || type == null) {
			return null;
		}

		File file = new File(input);
		if (!file.exists()) {
			return null;
		}
		String outFilePath = context.getExternalCacheDir().getAbsolutePath()
				+ "/image/" + type.getCacheDir();
		File outDir = new File(outFilePath);
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		outFilePath += "/" + file.getName();
		File outFile = new File(outFilePath);
		if (outFile.exists()) {
			return outFilePath;
		}

		if (!compressPicToJPEG(input, outFilePath, type)) {
			return null;
		}
		return outFilePath;
	}

	/**
	 * 图片压缩函数
	 * 
	 * @param input
	 * @param output
	 * @param type
	 * @return
	 */
	public final static boolean compressPicToJPEG(String input, String output,
			JCompressTypes type) {
		try {
			Bitmap outBitmap = getOutBitmap(input, type);
			int pos = output.lastIndexOf(".");
			output = output.substring(0, pos) + ".jpg";
			FileOutputStream fos = new FileOutputStream(output);
			outBitmap.compress(CompressFormat.JPEG, type.quality, fos);
			outBitmap.recycle();
			outBitmap = null;
			fos.flush();
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据type获取尺寸采样bitmap
	 * 
	 * @param input
	 *            //原文件路径
	 * @param type
	 *            //压缩参数包括质量跟尺寸..
	 * @return
	 */
	private final static Bitmap getOutBitmap(String input, JCompressTypes type) {
		Bitmap bitmap = null;
		if (type.height != -1 && type.width != -1) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(input, options);
			options.inSampleSize = calculateInSampleSize(options, type.width,
					type.height);
			options.inJustDecodeBounds = false;
			try {
				bitmap = BitmapFactory.decodeFile(input, options);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				bitmap = BitmapFactory.decodeFile(input);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return bitmap;
	}

	/**
	 * 计算尺寸压缩比例
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(Options options, int reqWidth,
			int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round(height * 1.0f / reqHeight);
			final int widthRatio = Math.round(width * 1.0f / reqWidth);
			inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static enum JCompressTypes {
		/** 原图默认70质量压缩 */
		BIG(70), 
		/** 原图默认90质量压缩 */
		BIG_90(90), 
		/** 原图默认80质量压缩 */
		BIG_80(80),
		/** 720*1280 70质量压缩 */
		STANDARD_720_1280(720, 1280),
		/** 480*800 70质量压缩 */
		STANDARD_480_800(480, 800),
		/** 240*320 70质量压缩 */
		SMALL(240, 320),
		/** 150*150 70质量压缩 */
		PHOTO(150, 150);

		private JCompressTypes(int quality) {
			this.quality = quality;
			cacheDir = "pic_" + quality;
		}

		private JCompressTypes(int width, int height) {
			this(70);
			this.width = width;
			this.height = height;
			cacheDir += "_" + width + "_" + height;
		}

		private JCompressTypes(int quality, int width, int height) {
			this(quality);
			this.width = width;
			this.height = height;
			cacheDir += "_" + width + "_" + height;
		}

		String cacheDir = "";
		int quality = 70;
		int width = -1;
		int height = -1;

		public String getCacheDir() {
			return cacheDir;
		}

	}
}
