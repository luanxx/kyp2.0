package com.science.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.util.Log;

import com.science.services.MyApplication;

public class Url {
	public static String BASEURL= MyApplication.platform;
	public static String HOTPAGEURL0=Url.BASEURL+"hot/hotList?typeid=0&classid=";
	public static String HOTPAGEURL1=Url.BASEURL+"hot/hotList?typeid=0&classid=";
	public static String HOTPAGEURL2=Url.BASEURL+"hot/hotList?typeid=0&classid=";
	public static String HOTPAGEURL3=Url.BASEURL+"hot/hotList?typeid=0&classid=";
	
	public static String HotpageBaseUrl = Url.BASEURL + "hot/hotList?";
	
	public static String HOTPAGEDETIALURL0=Url.BASEURL+"hot/hotList?typeid=1&classid=";
	public static String HOTPAGEDETIALURL1=Url.BASEURL+"hot/hotList?typeid=1&classid=";
	public static String HOTPAGEDETIALURL2=Url.BASEURL+"hot/hotList?typeid=1&classid=";
	public static String HOTPAGEDETIALURL3=Url.BASEURL+"hot/hotList?typeid=1&classid=";
	
	public static String ProgramList=Url.BASEURL+"GetProject/GetProjectByWeek?";
	public static String ProjectTop = Url.BASEURL + "getProject/getTopProject?";
	public static String ProjectUsual  = Url.BASEURL + "getProject/getUsualProject?";
	public static String ProjectExpireUrl = Url.BASEURL + "getProject/getEndProject?";
	public static String ProjectContentBaseUrl = Url.BASEURL + "GetProject/GetProjectDetail?";
	public static String programAvailableList=Url.BASEURL+"/GetProject/GetProjectAvailable?label=";
	
	
	public static String DocumentLIST = Url.BASEURL+"getPaper/getPaperTitle?";
	public static String DocumentContentBase=Url.BASEURL+"GetPaper/GetPaperTitle?cnkiId=";
	public static String DocumentDetailBase = Url.BASEURL + "GetPaper/GetPaperDetail?";
	public static String LOGINURL=Url.BASEURL+"/index/login?";
	public static String DOWNLOADKEYWORDS=Url.BASEURL+"personalTags/getPersonalTags?";
	public static String UpdateKeywords=Url.BASEURL+"personalTags/setPersonalTags?";
	public static String addCollection=Url.BASEURL+"/collection/addCollection?";
	public static String removeCollection=Url.BASEURL+"/collection/deleteCollection?";
	public static String getCollection=Url.BASEURL+"/collection/getCollection?";
	public static String getMessage=Url.BASEURL+"/message/getMessageInBox?";
	public static String updateCid=Url.BASEURL+"/Getui/setcid?cid=";
	public static String updateTags=Url.BASEURL+"/getui/getPushtags?";
	
	public static String CommentList = Url.BASEURL + "comment/getComment?";
	public static String ReleaseComment = Url.BASEURL + "comment/setComment?";
	public static String DeleteComment = Url.BASEURL + "comment/deleteComment?";
	public static String LikeCommontUrl = Url.BASEURL + "comment/likeComment?";
	public static final int MAX_ID = Integer.MAX_VALUE;
	
	public static String RegisterURL = Url.BASEURL + "Register/register?";

	public static String AddShoucangURL = Url.BASEURL + "collection/addCollection?";
	public static String DeleteShoucangURL = Url.BASEURL + "collection/deleteCollection?";
	
	public static String SendToEmailBaseUrl = Url.BASEURL + "mail/sendMail?";
	
//	public static String HOTPAGEURL0="http://10.107.7.48:8080/hot/hotList?typeId=0&plateId=";
//	public static String HOTPAGEURL1="http://10.107.7.48:8080/hot/hotList?typeId=0&plateId=";
//	public static String HOTPAGEURL2="http://10.107.7.48:8080/hot/hotList?typeId=0&plateId=";
//	public static String HOTPAGEURL3="";
//	
//	public static String LOGINURL="http://10.107.7.48:8080/index/login?name=";
	
	
	public static String TechTrendKywdAlysBaseUrl = Url.BASEURL + "trend/keywordAnalysis?";
	public static String TechTrendAcdmAffrBaseUrl = Url.BASEURL + "/trend/industryTrends?";
	
	public static String LogoutBaseUrl        = Url.BASEURL + "index/logout?";
	
	public static String CoopResourceListBaseUrl = Url.BASEURL + "resource/getResList?";
	
	public static String CheckUpdateUrl = Url.BASEURL + "version/checkAppVersion?";
	
	public static String ContentLikeUrl = Url.BASEURL + "like/addLike?";
	
	public static String EditMyInfoUrl = Url.BASEURL + "getMyData/updateData?";
	
	public static String GetMyInfoUrl = Url.BASEURL + "getMyData/getData?";
	public static String SubmitFeedbackUrl = Url.BASEURL + "feedback/submitFb?";
	public static String SendMessageUrl = Url.BASEURL + "message/sendMessage?";
	public static String FindPasswordUrl = Url.BASEURL + "Register/sendMail?";
	public static String UploadDevideIdUrl = Url.BASEURL + "Device/SetDeviceId?";
	public static String UploadCidUrl = Url.BASEURL + "getui/setcid?";
	public static String UploadMyAvatar = Url.BASEURL + "getMyData/updateFacePic?";
	public static String composeUrl(String ... args)
	{

		StringBuffer sb = new StringBuffer();
		String sid = MyApplication.sidString;
		if(sid.isEmpty())
			sid = "null";
		
		for(int i = 0; i < args.length;i++)
		{
			sb.append(args[i]);
			if(i == 0)
				sb.append("sid=" + sid);
		
			    
		}
		
		
		return sb.toString();
	}
	
	
	
	public static void changeBaseURL(String baseUrl){
		
		BASEURL= baseUrl;
		HOTPAGEURL0=Url.BASEURL+"hot/hotList?typeid=0&classid=";
		HOTPAGEURL1=Url.BASEURL+"hot/hotList?typeid=0&classid=";
		HOTPAGEURL2=Url.BASEURL+"hot/hotList?typeid=0&classid=";
		HOTPAGEURL3=Url.BASEURL+"hot/hotList?typeid=0&classid=";
		
		HotpageBaseUrl = Url.BASEURL + "hot/hotList?";
		
		HOTPAGEDETIALURL0=Url.BASEURL+"hot/hotList?typeid=1&classid=";
		HOTPAGEDETIALURL1=Url.BASEURL+"hot/hotList?typeid=1&classid=";
		HOTPAGEDETIALURL2=Url.BASEURL+"hot/hotList?typeid=1&classid=";
		HOTPAGEDETIALURL3=Url.BASEURL+"hot/hotList?typeid=1&classid=";
		
		ProgramList = Url.BASEURL+"GetProject/GetProjectByWeek?";
		ProjectTop = Url.BASEURL + "getProject/getTopProject?";
		ProjectUsual = Url.BASEURL + "getProject/getUsualProject?";
		ProjectExpireUrl = Url.BASEURL + "getProject/getEndProject?";
		ProjectContentBaseUrl = Url.BASEURL + "GetProject/GetProjectDetail?";
		programAvailableList=Url.BASEURL+"/GetProject/GetProjectAvailable?label=";
		
		
		DocumentLIST = Url.BASEURL+"getPaper/getPaperTitle?";
		DocumentContentBase=Url.BASEURL+"GetPaper/GetPaperTitle?cnkiId=";
		DocumentDetailBase = Url.BASEURL + "GetPaper/GetPaperDetail?";
		LOGINURL=Url.BASEURL+"/index/login?";
		DOWNLOADKEYWORDS=Url.BASEURL+"personalTags/getPersonalTags?";
		UpdateKeywords=Url.BASEURL+"personalTags/setPersonalTags?";
		addCollection=Url.BASEURL+"/collection/addCollection?";
		removeCollection=Url.BASEURL+"/collection/deleteCollection?";
		getCollection=Url.BASEURL+"/collection/getCollection?";
		getMessage=Url.BASEURL+"/message/getMessageInBox?";
		updateCid=Url.BASEURL+"/Getui/setcid?cid=";
		updateTags=Url.BASEURL+"/getui/getPushtags?";
		
		CommentList = Url.BASEURL + "comment/getComment?";
		ReleaseComment = Url.BASEURL + "comment/setComment?";
		DeleteComment = Url.BASEURL + "comment/deleteComment?";
		LikeCommontUrl = Url.BASEURL + "comment/likeComment?";
		
		RegisterURL = Url.BASEURL + "Register/register?";
		
		AddShoucangURL = Url.BASEURL + "collection/addCollection?";
		DeleteShoucangURL = Url.BASEURL + "collection/deleteCollection?";
		
		SendToEmailBaseUrl = Url.BASEURL + "mail/sendMail?";
//		public static String HOTPAGEURL0="http://10.107.7.48:8080/hot/hotList?typeId=0&plateId=";
//		public static String HOTPAGEURL1="http://10.107.7.48:8080/hot/hotList?typeId=0&plateId=";
//		public static String HOTPAGEURL2="http://10.107.7.48:8080/hot/hotList?typeId=0&plateId=";
//		public static String HOTPAGEURL3="";
	//	
//		public static String LOGINURL="http://10.107.7.48:8080/index/login?name=";
		
		
		TechTrendKywdAlysBaseUrl = Url.BASEURL + "trend/keywordAnalysis?";
		TechTrendAcdmAffrBaseUrl = Url.BASEURL + "/trend/industryTrends?";
		
		LogoutBaseUrl        = Url.BASEURL + "index/logout?";
		
		CoopResourceListBaseUrl = Url.BASEURL + "resource/getResList?";
		
		CheckUpdateUrl = Url.BASEURL + "version/checkAppVersion?";
		
		ContentLikeUrl = Url.BASEURL + "like/addLike?";
		
		EditMyInfoUrl = Url.BASEURL + "getMyData/updateData?";
		
		GetMyInfoUrl = Url.BASEURL + "getMyData/getData?";
	}
	
	
	
	
	
	public static String composeLogoutUrl(){
		return composeUrl(Url.LogoutBaseUrl);
		
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param type_id
	 * @param class_id
	 * @param id1
	 * @param id2
	 * @return热点
	 */
	
	public static String composeHotPageUrl(int type_id,int class_id,int id){
		return composeUrl(Url.HotpageBaseUrl,
				         "&typeid=",""+type_id,"&classid=",""+class_id,"&id=",""+id);
	}
	
	
	
	
	
	/**
	 * 
	 * @return获取置顶项目的URL
	 */
	public static String composeHotProjectUrl(int id){
		
		return composeUrl(Url.ProjectTop,"&id=","" + id);
	}
	
	
	/**
	 * 
	 * @param src1
	 * @param src2
	 * @param type
	 * @return 进行筛选项目
	 */

	public static String composeUsualProject(int src1,int src2,int type,int id,String date){
		
			try {
				date = URLEncoder.encode(date,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		return composeUrl(Url.ProjectUsual,
				         "&projSource1=",""+src1,"&projSource2=","" + src2,
				         "&projType=","" + type,"&id=","" + id,"&startdate=",date);
	}
	
	
	public static String composeExpireProjectUrl(int src1,int src2,int type,int id,String date){
	    
		try {
			date = URLEncoder.encode(date, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return composeUrl(Url.ProjectExpireUrl,
		         "&projSource1=",""+src1,"&projSource2=","" + src2,
		         "&projType=","" + type,"&id=","" + id,"&startdate=",date);
	}
	
	
    public static String composeDocListUrl(String pdate,int doc_id,int doc_type,String doc_keywords)
    {
    	
        try {
			doc_keywords = URLEncoder.encode(doc_keywords,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return composeUrl(Url.DocumentLIST,"&pdate=",pdate,
    			   "&id=",Integer.toString(doc_id),"&source=",Integer.toString(doc_type),
    			   "&keyWords=",doc_keywords);
    	
    }
    
    
    
    public static String composeMoreHotListUrl(int typeId,int plateId,int Id1,int Id2)
    {
    	String str_id1,str_id2;
    	if(Id1 == -1)
    		str_id1 = "null";
    	else
    		str_id1 = Integer.toString(Id1);
    	

    	if(Id2 == -1)
    		str_id2 = "null";
    	else
    		str_id2 = Integer.toString(Id2);
    	
    	
    	return composeUrl(Url.HOTPAGEDETIALURL0,
    			          "&typeId=",Integer.toString(typeId),"&plateId=",str_id1,
    			          "&Id1=",Integer.toString(Id1),"&Id2=",str_id2);
    }
	
	
	
    
    public static String composeCommentListUrl(String article_type,int article_id,int comment_id)
    {
    	if(comment_id < 0)
    		comment_id = DefaultUtil.MAX_VALUE;
    	
    	return composeUrl(Url.CommentList,"&articleType=",article_type,
    			          "&articleId=",Integer.toString(article_id),"&id=",Integer.toString(comment_id));
    }
    
    /***
     * 
     * @param comment_id (INT 这是第二层和第三层回复需要提供的,第一层可填-1) 
     * @param root_id    (INT 同上，这是第一层回复的customerid吗,第一层可填-1) 
     * @param article_type (INT) 
     * @param article_id  (INT) 
     * @param mark        (INT 0表示前面无回复，1表示前面有一个回复。2表示前面有两个回复) 
     * @param content      (INT 0表示前面无回复，1表示前面有一个回复。2表示前面有两个回复) 
     * @return
     */
//    public static String compseReleaseCommentUrl(int comment_id,int root_id,int article_type,int article_id,int mark,String content){
//    	
//    	content = URLEncoder.encode(content);
//    	return composeUrl(Url.ReleaseComment,"sid=",MyApplication.sidString,"&commentid=",Integer.toString(comment_id),
//    			           "&rootid=",Integer.toString(root_id),"&articleType=",Integer.toString(article_type),
//    			           "&articleId=",Integer.toString(article_id),"&mark=",Integer.toString(mark),
//    			           "&content=",content);
//    	
//    }
    
    
    
  /**
   * 
   * @param upper_comment (INT 对文章评论，提供-1;对评论评论，提供相应commentid)
   * @param upper_customer (INT 对文章评论，提供-1;对评论评论，提供相应customerid) 
   * @param article_type
   * @param article_id
   * @param content
   * @return
   */
    
  public static String compseReleaseCommentUrl(int upper_comment,int upper_customer,String article_type,int article_id,String content){
	
	content = URLEncoder.encode(content);
	return composeUrl(Url.ReleaseComment,"&upper_comment=",Integer.toString(upper_comment),
			           "&upper_customer=",Integer.toString(upper_customer),"&articleType=",article_type,
			           "&articleId=",Integer.toString(article_id),
			           "&content=",content);
	
}
    
    
    
    
    
  
  /**
   * 获取评论点赞的URL
   */
    
  public static String composeUploadCommentLikeUrl(int comment_id){
	  
	  return composeUrl(Url.LikeCommontUrl,"&commentid=","" + comment_id);
  }
  
  
  
  
    
    /**
     * 
     * @param comment_id 表明删除的评论（源评论）
     *
     * @return
     */
    public static String composeDeleteCommentUrl(int comment_id)
    {
    	return composeUrl(Url.DeleteComment,"&commentid=",
    			          Integer.toString(comment_id));
    }
    
    
    public static String composeRegisterUrl(String nickname,String password,String email){
    	
    	try {
			nickname = URLEncoder.encode(nickname, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return composeUrl(Url.RegisterURL,"&username=",email,
    			         "&nickname=",nickname,"&password=",password);
    }
    
    
    

    
    
    
    public static String composeAddShoucangUrl(String article_type,int article_id,String url,String title){
    	
    	try {
			url = URLEncoder.encode(url,"UTF-8");
			title = URLEncoder.encode(title, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return composeUrl(Url.AddShoucangURL,
    			          "&articleType=",article_type,
    			          "&articleId=",Integer.toString(article_id),
         		          "&url=",url,"&title=",title);
    
    }
    
    
    public static String composeDeleteShoucangUrl(String article_type,int article_id)
    {
       	return composeUrl(Url.DeleteShoucangURL,
		          "&articleType=",article_type,
		          "&articleId=",Integer.toString(article_id));
    }
    
    
    
    
    
    /**
     * 
     * @param mailTo 发给谁
     * @param title  文章的标题
     * @param content 内容（一般含有url）
     * @return发送文章到自己的邮箱
     */
    
    public static String composeSendToEmailUrl(String mail_to,String title,String content){
    	
    	
    	try {
    		title = URLEncoder.encode(title,"UTF-8");
			content = URLEncoder.encode(content,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return composeUrl(Url.SendToEmailBaseUrl,"&mailTo=",
    			mail_to,"&title=",title,"&content=",content);
    }
    
    
    
    /**
     * 科技趋势---关键词分析
     */
    
    public static String composeTechTrendKywdAlysUrl(String keyword){
    	
    	return composeUrl(Url.TechTrendKywdAlysBaseUrl,"&keyword=",keyword);
    }
    
    
    
    
    
    
    /***
     * 科技趋势---业界动态
     */
    public static String composeTechTrendAcdmAffrUrl(int src1,int src2,int id){
    	
    	return composeUrl(Url.TechTrendAcdmAffrBaseUrl,"&projSource1=",
    			          "" + src1,"&projSource2=","" + src2,"&id=","" + id);
    }
    
    
    
    
    
    
    public static String composeCoopResourceListUrl(int location,int res_type,String keyword,int page){
    	
    	return composeUrl(Url.CoopResourceListBaseUrl,"&location=",
    			          "" + location,"&resType=","" + res_type,"&keyword=",keyword,"&page=","" + page);
    }
    
    
    
    public static String composeCheckUpdateUrl(double version){
    	
    	return composeUrl(Url.CheckUpdateUrl,"&versionNum=","" + version);
    }
    
    
    public static String composeGetCollectionUrl(String id){
    	
    	return composeUrl(Url.getCollection,"&id=",id);
    }

    
    public static String composeLikeContentUrl(String article_type,int id){
    	
    	return composeUrl(Url.ContentLikeUrl,"&articleType=","" + article_type,
    			         "&articleId=","" + id);
    	
    }
    
    public static String composeEditMyInfoUrl(String nickname,String profession,String workunit,String worklocation,String email ){
    	
    	try {
			nickname = URLEncoder.encode(nickname,"UTF-8");
			profession = URLEncoder.encode(profession,"UTF-8");
			workunit = URLEncoder.encode(workunit,"UTF-8");
			worklocation = URLEncoder.encode(worklocation,"UTF-8");
			email = URLEncoder.encode(email,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return composeUrl(Url.EditMyInfoUrl,"&nickname=",nickname,"&profession=",
    			         profession,"&workunit=",workunit,"&worklocation=",worklocation,"&email=",email);
    }
    
    
    
    
    
    public static String composeGetMyInfoUrl(){
    	
    	return composeUrl(Url.GetMyInfoUrl);
    }
    
    public static String composeSubmitFeedbackUrl(String fd_content,String eid){
    	
    	try {
			fd_content = URLEncoder.encode(fd_content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return composeUrl(Url.SubmitFeedbackUrl,"&fdContent=",fd_content,"&eid=",eid);
    }
    
public static String composeSendMessageUrl(int receiver_id,String message){
    	
    	try {
    		message = URLEncoder.encode(message, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return composeUrl(Url.SendMessageUrl,"&receiver_id=",""+receiver_id,"&message=",message);
    }
public static String composeFindPasswordUrl(String mail_to,String content){
	
	
	try {
		content = URLEncoder.encode(content,"UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return composeUrl(Url.FindPasswordUrl,"&mailTo=",
			mail_to,"&content=",content);
}



   public static String composeGetPersonalTags(){
	   return composeUrl(Url.DOWNLOADKEYWORDS);
   }


public static String composeGetMyMessageUrl(int page){
	 return composeUrl(Url.getMessage,"&page=","" + page);
}


  
   public static String composeUpdateKeywordsUrl(String keyword){
	try {
		keyword = URLEncoder.encode(keyword, "UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return composeUrl(Url.UpdateKeywords,"&tagsString=",keyword);
  }

   public static String composeUploadDevideId(String eid){
	   return composeUrl(Url.UploadDevideIdUrl,"&eid=",eid);
  }
   
   public static String composeUploadCid(String cid){
	   return composeUrl(Url.UploadCidUrl,"&cid=",MyApplication.cid);
   }


  public static String composeUploadMyAvatar(){
	  return composeUrl(Url.UploadMyAvatar);
  }



}
