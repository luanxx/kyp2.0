package com.science.util;

public class DataUrlKeys {
	public static boolean isLogined = false; // 是否登录
	public static int uid = 0;// 用户的uid
	public static String username = null;// 用户名
	public static String getId = null;// 评论文章的id
	public static String type = null;// 用户评论文章的类型
	public static boolean isComment = false;// 判断是否为评论时的登录

	public static int currentFontSizeFlag = 1; // 字体大小标志位，0代表normal，1代表larger，2代表largest

	// 获取新闻列表，获取20条，后面需加上加密字符串来访问
	public static final String NEWS_LIST_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=news&nums=20&pass=";
	// 获取更多新闻列表，点击更多查看,$since_id后的更多20条
	public static final String MORE_NEWS_LIST_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=news&nums=20&since_id=$since_id&pass=f013fa37b8dbf031e7f1f9f053907b1448340d2a";
	public static final String BLOG_LIST_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=bloglist&nums=20&pass=";
	// 获取更多博客列表，点击更多查看,$since_id后的更多20条
	public static final String MORE_BLOG_LIST_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=bloglist&nums=20&since_id=$since_id&pass=f013fa37b8dbf031e7f1f9f053907b1448340d2a";
	public static final String GROUP_LIST_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=bbslist&pg=1&nums=20&pass=";
	// 获取更多群组列表，点击更多查看,$since_id后的更多20条
	public static final String MORE_GROUP_LIST_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=bbslist&nums=20&since_id=$since_id&pass=f013fa37b8dbf031e7f1f9f053907b1448340d2a";
	// 具体某篇新闻的URL，使用时将“$pass”替换成pass字符串即可
	public static final String NEWS_DETAIL_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=onenews&nums=1&pass=$pass&id=";
	// 具体某篇博客的URL，使用时将"$id"替换成id字符串即可
	public static final String BLOG_DETAIL_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=getblogarticlebyid&id=$id&nums=5&pass=";
	// 具体某篇群组的URL，使用时将"$id"替换成id字符串即可
	public static final String GROUP_DETAIL_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=getbbsbyid&id=$id&nums=5&pass=";
	// 科学报图片的URL
	public static final String NEWSPAPER_LIST_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=kxsb&mydatetime=$mydatetime&nums=25&pass=";
	public static final String NEWPAPER_DETAIL_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=kxsbnews&id=$id&nums=15&pass=$pass";
	public static final String NEWSPAPER_DETAIL_BY_ID_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=kxsbnews&id=$id&nums=15&pass=$pass";
	public static final String NEWPAPER_CONTENT_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=kxsbonenews&id=$id&nums=15&pass=$pass";
	// 某条具体新闻评论列表的URL
	public static final String NEWS_COMMENT_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=newscomment&nums=20&pass=$pass&id=";
	// 具体某篇科学报的新闻评论的URL，后面需加上id来访问
	public static final String NEWSPAPER_CONTENT_COMMENT_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=kxsbnewscomment&nums=20&pass=$pass&id=";
	// 某篇博客的评论的URL,使用时将"$blogid"替换成id字符串，后面需加上pass字符串
	public static final String BLOG_COMMENT_URL = "http://bbs.sciencenet.cn/api/interfaceIphone.php?type=getblogcomment&blogid=$blogid&page=1&perpage=10&pass=";
	// 某篇群组的评论的URL,使用时将"$tid"替换成id字符串，后面需加上pass字符串
	public static final String GROUP_COMMENT_URL = "http://bbs.sciencenet.cn/api/interfaceIphone.php?type=getforumcomment&tid=$tid&page=1&perpage=10&pass=";
	// 检查用户登录的URL
	public static final String CHECK_UESER_LOGIN_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=uc_user_login&bbsusername=$bbsusername&password=$password&nums=5&pass=";
	// 发布新闻评论URL
	public static final String RELEASE_NEWS_COMMENT_URL = "http://news.sciencenet.cn/xml/iphoneInterface.aspx?type=addnewscomment&nums=1&pass=$pass&id=";
	// 发布博客评论URL
	public static final String RELEASE_BLOG_COMMENT_URL = "http://bbs.sciencenet.cn/api/interfaceIphone.php?type=addblogcomment&blogid=$blogid&pass=";
	// 发布论坛评论
	public static final String RELEASE_GROUP_COMMENT_URL = "http://bbs.sciencenet.cn/api/interfaceIphone.php?type=addforumcomment&tid=$tid&pass=";
	// 发布科学报文章评论
	public static final String RELEASE_NEWSPAPER_COMMENT_URL = "http://news.sciencenet.cn/xml/iphoneInterface.aspx?type=addkxsbnewscomment&nums=1&pass=$pass&id=";
	// SD卡图片缓存的路径
	// 新闻列表的图片缓存路径
	public static final String NEWS_LIST_IMG_CACHE_FOLDER = "/mnt/sdcard/sciencenet/images/newsitem/";
	// 报纸新闻列表的图片缓存路径
	public static final String NEWSPAPER_LIST_IMG_CACHE_FOLDER = "/mnt/sdcard/sciencenet/images/newspaperitem/";
	// 科学报的图片缓存路径
	public static final String SCIENCENEWSPAPER_CACHE_FOLDER = "/mnt/sdcard/sciencenet/images/sciencenetnewspaper/";
	// 收藏的路径
	// 收藏的html文件路径
	public static final String COLLECTION_HTML_FOLDER = "/mnt/sdcard/sciencenet/collection/html/";
	// 收藏的html文件里面的图片路径
	public static final String COLLECTION_HTML_IMG_FOLDER = "/mnt/sdcard/sciencenet/collection/html_img/";
	// 收藏的item的小图的路径
	public static final String COLLECTION_ITEM_IMG_FOLDER = "/mnt/sdcard/sciencenet/collection/item_img/";
	// 收藏的报刊的item的小图路径
	public static final String COLLECTION_NEWSPAPER_ITEM_IMG_FOLDER = "/mnt/sdcard/sciencenet/collection/newspaper_item_img/";
	// 搜索
	// 搜索新闻的url，使用时将$searchContent替换成要搜索的内容即可
	public static final String SEARCH_NEWS_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=news&nums=20&key=$searchContent&pass=f013fa37b8dbf031e7f1f9f053907b1448340d2a";
	// 搜索博客的url，使用时将$searchContent替换成要搜索的内容即可
	public static final String SEARCH_BLOG_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=bloglist&nums=20&key=$searchContent&pass=f013fa37b8dbf031e7f1f9f053907b1448340d2a";
	// 搜索群组的url，使用时将$searchContent替换成要搜索的内容即可
	public static final String SEARCH_GROUP_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=bbslist&nums=20&key=$searchContent&pass=f013fa37b8dbf031e7f1f9f053907b1448340d2a";
	// 搜索更多新闻的url
	public static final String SEARCH_MORE_NEWS_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=news&nums=20&since_id=$since_id&key=$searchContent&pass=f013fa37b8dbf031e7f1f9f053907b1448340d2a";
	// 搜索更多博客的url
	public static final String SEARCH_MORE_BLOG_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=bloglist&nums=20&since_id=$since_id&key=$searchContent&pass=f013fa37b8dbf031e7f1f9f053907b1448340d2a";
	// 搜索更多群组的url
	public static final String SEARCH_MORE_GROUP_URL = "http://www.sciencenet.cn/xml/iphoneInterface.aspx?type=bbslist&nums=20&since_id=$since_id&key=$searchContent&pass=f013fa37b8dbf031e7f1f9f053907b1448340d2a";
	
	// 广告
	// 新闻广告接口
	public static final String AD_NEWS = "http://www.sciencenet.cn/kxwad/js/a17.xml";
	// 博客广告接口
	public static final String AD_BLOG = "http://www.sciencenet.cn/kxwad/js/a18.xml";
	// 群组广告接口
	public static final String AD_GROUP = "http://www.sciencenet.cn/kxwad/js/a19.xml";
}
