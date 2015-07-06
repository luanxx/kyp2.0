package com.science.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AppUtil {

	public static int ITEM_IMG_WIDTH = 60;
	public static int ITEM_IMG_HEIGHT = 60;
	public static int GALLERY_WIDTH;
	public static int GALLERY_HEIGHT;

	/**
	 * 退出程序
	 * 
	 * @param context
	 */
	public static void QuitHintDialog(final Context context) {
		new AlertDialog.Builder(context)
				.setMessage("确定退出科研·派吗？")
				.setTitle("科研·派")
				//.setIcon(R.drawable.ic_launcher)
				.setPositiveButton("退出", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Editor sharedata = context.getSharedPreferences(
									"data", 0).edit();
							sharedata.putInt("font_size",
									DataUrlKeys.currentFontSizeFlag);
							sharedata.commit(); // 将当前字号保存起来
							((Activity) context).finish();
							android.os.Process.killProcess(android.os.Process.myPid());
						} catch (Exception e) {
							//Log.e("close", "close error");
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create().show();
	}

	/**
	 * 改变字体的大小
	 * 
	 * @param webView
	 */
	public static void changeFont(WebView webView) {
		int toSetFlag = (++DataUrlKeys.currentFontSizeFlag) % 3;
		switch (toSetFlag) {
		case 0:
			webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
			break;
		case 1:
			webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
			break;
		case 2:
			webView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
			break;
		default:
			break;
		}
		DataUrlKeys.currentFontSizeFlag = toSetFlag;
	}

	/**
	 * 设置一个WebView的字体的大小
	 * 
	 * @param webView
	 */
	public static void setFont(WebView webView) {
		switch (DataUrlKeys.currentFontSizeFlag) {
		case 0:
			webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
			break;
		case 1:
			webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
			break;
		case 2:
			webView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
			break;
		default:
			break;
		}
	}

	/**
	 * 返回当前字号的字符串表示
	 * 
	 * @return
	 */
	public static String getCurrentFontString() {
		switch (DataUrlKeys.currentFontSizeFlag) {
		case 0:
			return "小";
		case 1:
			return "中";
		case 2:
			return "大";
		default:
			return "未知";
		}
	}

	/**
	 * 设置WebView的布局显示，如果详细内容包含table标签，则不设置webview为单列
	 * 
	 * @param webView
	 * @param content
	 * @return
	 */
	public static void setWebViewLayout(WebView webView, String content) {
		if (content.contains("<table")) {
			webView.getSettings().setLayoutAlgorithm(
					LayoutAlgorithm.NARROW_COLUMNS);
		} else {
			webView.getSettings().setLayoutAlgorithm(
					LayoutAlgorithm.SINGLE_COLUMN);
		}
	}

	final static String[] hex = { "%00", "%01", "%02", "%03", "%04", "%05",
			"%06", "%07", "%08", "%09", "%0a", "%0b", "%0c", "%0d", "%0e",
			"%0f", "%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17",
			"%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f", "%20",
			"%21", "%22", "%23", "%24", "%25", "%26", "%27", "%28", "%29",
			"%2a", "%2b", "%2c", "%2d", "%2e", "%2f", "%30", "%31", "%32",
			"%33", "%34", "%35", "%36", "%37", "%38", "%39", "%3a", "%3b",
			"%3c", "%3d", "%3e", "%3f", "%40", "%41", "%42", "%43", "%44",
			"%45", "%46", "%47", "%48", "%49", "%4a", "%4b", "%4c", "%4d",
			"%4e", "%4f", "%50", "%51", "%52", "%53", "%54", "%55", "%56",
			"%57", "%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e", "%5f",
			"%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67", "%68",
			"%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f", "%70", "%71",
			"%72", "%73", "%74", "%75", "%76", "%77", "%78", "%79", "%7a",
			"%7b", "%7c", "%7d", "%7e", "%7f", "%80", "%81", "%82", "%83",
			"%84", "%85", "%86", "%87", "%88", "%89", "%8a", "%8b", "%8c",
			"%8d", "%8e", "%8f", "%90", "%91", "%92", "%93", "%94", "%95",
			"%96", "%97", "%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e",
			"%9f", "%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7",
			"%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af", "%b0",
			"%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7", "%b8", "%b9",
			"%ba", "%bb", "%bc", "%bd", "%be", "%bf", "%c0", "%c1", "%c2",
			"%c3", "%c4", "%c5", "%c6", "%c7", "%c8", "%c9", "%ca", "%cb",
			"%cc", "%cd", "%ce", "%cf", "%d0", "%d1", "%d2", "%d3", "%d4",
			"%d5", "%d6", "%d7", "%d8", "%d9", "%da", "%db", "%dc", "%dd",
			"%de", "%df", "%e0", "%e1", "%e2", "%e3", "%e4", "%e5", "%e6",
			"%e7", "%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef",
			"%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7", "%f8",
			"%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff" };

	/**
	 * 将特殊字符转换为HEX编码
	 * 
	 * @param s
	 * @return
	 */
	public static String encode(String s) {
		StringBuffer sbuf = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; i++) {
			int ch = s.charAt(i);
			if ('A' <= ch && ch <= 'Z') { // 'A'..'Z'
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') { // 'a'..'z'
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') { // '0'..'9'
				sbuf.append((char) ch);
			} else if (ch == ' ') { // space
				sbuf.append('+');
			} else if (ch == '-'
					|| ch == '_' // unreserved
					|| ch == '.' || ch == '!' || ch == '~' || ch == '*'
					|| ch == '\'' || ch == '(' || ch == ')') {
				sbuf.append((char) ch);
			} else if (ch <= 0x007f) { // other ASCII
				sbuf.append(hex[ch]);
			} else if (ch <= 0x07FF) { // non-ASCII <= 0x7FF
				sbuf.append(hex[0xc0 | (ch >> 6)]);
				sbuf.append(hex[0x80 | (ch & 0x3F)]);
			} else { // 0x7FF < ch <= 0xFFFF
				sbuf.append(hex[0xe0 | (ch >> 12)]);
				sbuf.append(hex[0x80 | ((ch >> 6) & 0x3F)]);
				sbuf.append(hex[0x80 | (ch & 0x3F)]);
			}
		}
		return sbuf.toString();
	}

	/**
	 * 补全XML
	 * 
	 * @param oldStream
	 * @return
	 */
	public static InputStream transferInputStream(InputStream oldStream) {
		InputStream newStream = null;
		String tmpString = null;

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[4096];
		int count = -1;
		try {
			while ((count = oldStream.read(data, 0, 4096)) != -1)
				outStream.write(data, 0, count);
		} catch (IOException e) {
			e.printStackTrace();
		}

		data = null;
		try {
			tmpString = new String(outStream.toByteArray(), "GBK");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		;

		tmpString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><DATA>"
				+ tmpString;
		tmpString += "</DATA>";
		Log.i("xmlstring", tmpString);
		try {
			newStream = new ByteArrayInputStream(tmpString.getBytes("UTF-8"));
			Log.e("afterString", newStream.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return newStream;
	}

	/**
	 * 如果系统的输入法软键盘已显示隐，则藏系统输入法软键盘
	 * 
	 * @param context
	 */
	public static void hideIM(Context context, EditText et) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
			Log.i("Apputil", "--hide IM--");
		}
	}
	
    public static void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
    } 
    
    public static String BlockCodeToBlockText(String code)
    {
    	String name="";
    	switch (Integer.parseInt(code)) {
    	
		case 0:
			 name="访问学者";
			break;
		case 1:
			 name="招聘信息";
			break;
		case 2:
			name="学术前沿";
			break;
		case 3:
			name="项目解读";
			break;
		case 4:
			name = "创业项目";
			break;
		case 5:
			name="热门项目";
			break;
		case 6:
			name="即将到期项目";
			break;
		case 7:
			name="中文文献";
			break;
		case 8:
			name="英文文献";
			break;
		case 9:
			name="工作文献";
			break;
		case 10:
			name="NSF文献";
			break;
		case 11:
			name="业界动态";
			break;	
		case 12:
			name = "关键词分析";
			break;
		default:
			break;
		}
    	
    	return name;
    }
}
