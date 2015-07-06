package com.science.database;

public class CollectionItem {
	// 收藏的类�?
	public static final int TYPE_NEWS = 0; // 新闻
	public static final int TYPE_BLOG = 1; // 博客
	public static final int TYPE_GROUP = 2; // 群组
	public static final int TYPE_NEWSPAPER = 3; // 报纸
	
	private int type;
	private String id;
	private String title;
	private String description;
	private String imgs;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImgs() {
		return imgs;
	}
	public void setImgs(String imgs) {
		this.imgs = imgs;
	}
	
	
    
	
	
}
