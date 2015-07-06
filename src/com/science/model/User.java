package com.science.model;

import java.lang.ref.SoftReference;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class User {

	public String sidString;
	public String userName;
	public String userNickName;
	public SoftReference<Bitmap> userAvatar;
	public String userOrganization;
	public String userArea;
	public String userIdentity;
	public String userEmail;
}
