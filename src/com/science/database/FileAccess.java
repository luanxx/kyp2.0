package com.science.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

public class FileAccess {
	/**
	 * 创建文件�?
	 * 
	 * @param dirName
	 */
	public static void MakeDir(String dirName) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File destDir = new File(dirName);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
		}
	}

	/**
	 * 将某个文件夹下得图片转存到另外的文件夹下
	 * 
	 * @param sourcePath
	 *            原路�?
	 * @param destinationPath
	 *            目标路径
	 */
	public static void restoreImg(String sourcePath, String imgName,
			String destinationPath) {
		try {
			FileAccess.MakeDir(destinationPath);
			String sourceFileName = sourcePath + imgName;
			Log.i("--source img file path--", sourceFileName);
			Bitmap bitmap = BitmapFactory.decodeFile(sourceFileName);
			String targetFileName = destinationPath + imgName;
			Log.i("--target img file path--", targetFileName);
			bitmap.compress(CompressFormat.PNG, 50, new FileOutputStream(
					targetFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 通过文件夹和图片名获得SD卡里的图�?
	 * 
	 * @param folder
	 * @param imgName
	 * @return
	 */
	public static Drawable getDrawableByFolder(String folder, String imgName) {
		Bitmap bitmap = BitmapFactory.decodeFile(folder + imgName);
		return new BitmapDrawable(bitmap);
	}
	
	
	public static Bitmap getBitmapByFolder(String folder, String imgName) {
		Bitmap bitmap = BitmapFactory.decodeFile(folder + imgName);
		return bitmap;
	}

	public static String getFolderByCollectionType(int type) {
		switch (type) {

		}
		return "http://news.sciencenet.cn";
	}

	/**
	 * 计算 缩放比例
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
//		Log.i("calculateInSampleSize", "reqWidth:" + reqWidth + "    reqHeight:" + reqHeight);
		final int height = options.outHeight;
		final int width = options.outWidth;
//		Log.i("calculateInSampleSize", "outWidth:" + options.outWidth + "    outHeight:" + options.outHeight);
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
//		Log.i("calculateInSampleSize", "inSampleSize:" + inSampleSize);
		return inSampleSize;
	}

	/**
	 * 按需解码图片尺寸
	 * 
	 * @param outFileName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(String outFileName,
			int reqWidth, int reqHeight) {
//		Log.i("decodeSampledBitmapFromResource", "reqWidth:" + reqWidth + "    reqHeight:" + reqHeight);
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(outFileName, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inTempStorage = new byte[16*1024];
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(outFileName, options);
	}

	public static Bitmap decodeSampledBitmapFromByteArray(byte[] b,
			int reqWidth, int reqHeight) {
//		Log.i("decodeSampledBitmapFromByteArray", "reqWidth:" + reqWidth + "    reqHeight:" + reqHeight);
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(b, 0, b.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inTempStorage = new byte[16*1024];
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(b, 0, b.length, options);
	}

	/**
	 * 删除指定路径的文�?
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 删除某一文件夹下�?有的文件(注意：如果该文件夹下还有文件夹，将不会被删除)
	 * 
	 * @param folder
	 *            文件夹的路径
	 */
	public static void deleteAllFiles(String folder) {
		File file = new File(folder);
		if (!file.exists()) {
			Log.e("deleteAllFiles", "foler doesn't exists");
			return;
		}
		if (!file.isDirectory()) {
			Log.e("deleteAllFiles", "folder isn't a Directory");
			return;
		}
		File fileList[] = file.listFiles();
		for (File f : fileList) {
			if (f.isFile()) {
				f.delete();
			}
		}
		Log.i("deleteAllFiles", "clean complete");
	}

	/**
	 * 获取某一文件夹下�?有文件的大小总和(注意：如果该文件夹下还有子文件夹，子文件夹的大小将不会被计算)
	 * 
	 * @param folder
	 *            文件夹的路径（如果传进来的是�?个文件，则直接返回它的大小）
	 * @return 问价大小的�?�和
	 */
	public static long getFolderSize(String folder) {
		File file = new File(folder);
		if (!file.exists()) {
			Log.e("getFolderSize", "foler doesn't exists");
			return -1L;
		}
		if (!file.isDirectory()) {
			Log.e("deleteAllFiles", "folder isn't a Directory");
			return file.length();
		}
		long sum = 0L;
		File fileList[] = file.listFiles();
		for (File f : fileList) {
			if (f.isFile()) {
				sum += f.length();
			}
		}
		Log.i("getFolderSize", "get completed  -- " + sum);
		return sum;
	}

	/**
	 * 转换文件大小，变成友好的易读的字符串
	 * 
	 * @param fileS
	 *            文件的大�?
	 * @return
	 */
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * 计算inSampleSize的�??
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
