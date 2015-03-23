package com.hege.pts.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.util.Xml.Encoding;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hege.pts.ProductDetailActivity;
import com.hege.pts.R;
import com.hege.pts.data.AdvertItemBean;
import com.hege.pts.data.CaseDetailBean;
import com.hege.pts.data.CasesListBean;
import com.hege.pts.data.CategoryBean;
import com.hege.pts.data.CategoryDetailBean;
import com.hege.pts.data.CategoryListBean;
import com.hege.pts.data.ChatMessage;
import com.hege.pts.data.ChatMessage.Type;
import com.hege.pts.data.NewsListBean;
import com.hege.pts.data.TitlePhotoBean;
import com.hege.pts.data.VideoBean;
import com.hege.pts.data.YearMessageBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

public class WebService {

	/**
	 * 传入一个输入流返回它的字符串形式
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String getString(InputStream instream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int ch = -1;
		byte[] buffer = new byte[1024 * 4];
		while ((ch = instream.read(buffer)) != -1) {
			baos.write(buffer, 0, ch);
		}
		baos.flush();
		String responseXml = baos.toString(HTTP.UTF_8); // 依据需求可以选择要要的字符编码格式
		baos.close();
		instream.close();
		return responseXml;
	}

	public static void getAdvertBean(ArrayList<AdvertItemBean> advertListBean,
			int id) throws ClientProtocolException, IOException, JSONException {
		String str = getJsonStr("http://news.pts80.net/hege/api/?action=ad&control=ad&way="
				+ id);
		parseJsonStr(str, advertListBean);
	}

	private static void parseJsonStr(String json,
			ArrayList<AdvertItemBean> advertListBean) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		for (int i = 0; i < jsonArray.length(); i++) {
			AdvertItemBean advertItemBean = new AdvertItemBean();
			JSONObject objects = (JSONObject) jsonArray.get(i);
			JSONArray photos = objects.getJSONArray("photo");
			String is_on = objects.getString("is_on");
			String url = objects.getString("url");

			advertItemBean.setPhoto(photos.getString(1));
			advertItemBean.setIs_on(is_on);
			advertItemBean.setUrl(url);

			advertListBean.add(advertItemBean);
		}
	}

	public static void addImage(Context context, ArrayList<View> img_list,
			ArrayList<AdvertItemBean> advertListBean) {
		for (int i = 0; i < advertListBean.size(); i++) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.item_viewpager_main_advert, null);
			ImageView iv_advert = (ImageView) view.findViewById(R.id.iv_1);
			// 以后为view设置监听
			Uri url = Uri.parse("http://news.pts80.net/hege/"
					+ advertListBean.get(i).getPhoto());
			//Picasso.with(context).load(url).fit().into(iv_advert);
			ImageLoader.getInstance().displayImage(url.toString(), iv_advert);
			img_list.add(view);
		}
	}

	public static void getListPhoto(ArrayList<String> listPhoto)
			throws ClientProtocolException, IOException, JSONException {
		String str = getJsonStr("http://news.pts80.net/hege/api/?action=introduce&control=honors");
		parseJsonStr2(str, listPhoto);
	}

	private static void parseJsonStr2(String json, ArrayList<String> listPhoto)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject objects = (JSONObject) jsonArray.get(i);
			String photo = objects.getString("photo");
			listPhoto.add(photo);
		}
	};

	public static void addImage2(Context context, ArrayList<View> img_list,
			ArrayList<String> listPhoto) {
		img_list.clear();
		for (int i = 0; i < listPhoto.size(); i++) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.item_viewpager_main_advert, null);
			ImageView iv_advert = (ImageView) view.findViewById(R.id.iv_1);
			// 以后为view设置监听
			Uri url = Uri.parse("http://news.pts80.net/hege/"
					+ listPhoto.get(i));
			//Picasso.with(context).load(url).fit().into(iv_advert);
			ImageLoader.getInstance().displayImage(url.toString(), iv_advert);
			img_list.add(view);
		}
	}

	public static String getUrl(int id) throws IOException, JSONException {
		String str = getJsonStr("http://news.pts80.net/hege/api/?action=introduce&control=html&way="
				+ id);
		return parseJsonStrUrl(str);
	}

	private static String parseJsonStrUrl(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("url");
		String Url = (String) jsonArray.getString(1);
		return Url;
	}

	public static void getEvents(ArrayList<YearMessageBean> yearMessageBean)
			throws ClientProtocolException, IOException, JSONException {
		String str = getJsonStr("http://news.pts80.net/hege/api/?action=introduce&control=events");
		parseJsonStrEvents(str, yearMessageBean);
	}

	private static void parseJsonStrEvents(String json,
			ArrayList<YearMessageBean> yearMessageBean) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		for (int i = 0; i < jsonArray.length(); i++) {
			YearMessageBean event = new YearMessageBean();
			JSONObject object = (JSONObject) jsonArray.get(i);
			event.setYear(object.getString("year"));
			event.setInformation(object.getString("digest"));
			yearMessageBean.add(event);
		}
	}

	public static void getTitlePhoto(ArrayList<TitlePhotoBean> titlePhotoList)
			throws ClientProtocolException, IOException, JSONException {
		String str = getJsonStr("http://news.pts80.net/hege/api/?action=introduce&control=description");
		parseJsonStrTitlePhoto(str, titlePhotoList);
	}

	private static void parseJsonStrTitlePhoto(String json,
			ArrayList<TitlePhotoBean> titlePhotoList) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		for (int i = 0; i < jsonArray.length(); i++) {
			TitlePhotoBean titlePhoto = new TitlePhotoBean();
			JSONObject object = (JSONObject) jsonArray.get(i);
			titlePhoto.setPhoto(object.getJSONArray("photo").getString(1));
			titlePhoto.setTitle(object.getString("title"));
			titlePhotoList.add(titlePhoto);
		}
	}

	public static void getNewsData(ArrayList<NewsListBean> newsListData)
			throws ClientProtocolException, IOException, JSONException {
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=news&control=index");
		parseJsonNews(json, newsListData);
	}

	private static void parseJsonNews(String json,
			ArrayList<NewsListBean> newsListData) throws JSONException,
			ClientProtocolException, IOException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		newsListData.clear();
		for (int i = 0; i < jsonArray.length(); i++) {
			NewsListBean news = new NewsListBean();
			JSONObject object = (JSONObject) jsonArray.get(i);

			news.setId(object.getString("id"));
			String jsonUrl = getJsonStr("http://news.pts80.net/hege/api/?action=news&control=index&id="
					+ news.getId());
			parseJsonNews(jsonUrl, news);
			news.setTitle(object.getString("title"));
			news.setData(object.getString("add_time"));

			newsListData.add(news);
		}
	}

	private static void parseJsonNews(String jsonUrl, NewsListBean news)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonUrl);
		JSONArray objects = jsonObject.getJSONArray("url");
		news.setUrl(objects.getString(1));
	}

	private static String getJsonStr(String url) throws IOException,
			ClientProtocolException, JSONException {
		String str = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);

		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			str = WebService.getString(instream);
		}
		return str;
	}

	public static void getVideoList(ArrayList<VideoBean> videoListBean)
			throws ClientProtocolException, IOException, JSONException {
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=video&control=list");
		parseJsonVideos(json, videoListBean);
	}

	private static void parseJsonVideos(String json,
			ArrayList<VideoBean> videoListBean) throws JSONException,
			IOException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		for (int i = 0; i < jsonArray.length(); i++) {
			VideoBean video = new VideoBean();
			JSONObject object = (JSONObject) jsonArray.get(i);

			video.setTitle(object.getString("title"));
			video.setPhoto(object.getJSONArray("photo").getString(1));
			video.setUrl(object.getString("url"));

			videoListBean.add(video);
		}
	}

	public static void getCategoryList(
			ArrayList<CategoryListBean> categoryListBean, int id)
			throws ClientProtocolException, IOException, JSONException {
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=product&control=index&cid="
				+ id);
		parseJsonCategorys(json, categoryListBean);
	}

	/**
	 * 获取cateogroy列表
	 * @param json
	 * @param categoryListBean
	 * @throws JSONException
	 */
	private static void parseJsonCategorys(String json,
			ArrayList<CategoryListBean> categoryListBean) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = jsonArray.getJSONObject(i);
			CategoryListBean categoryBean = new CategoryListBean();
			categoryBean.setCode(object.getString("code"));
			categoryBean.setId(object.getString("id"));
			categoryBean.setPhoto(object.getString("photo"));
			categoryBean.setPriseNum(object.getString("good"));
			categoryListBean.add(categoryBean);
		}
	}

	public static void getCategoryDetailList(
			CategoryDetailBean categoryDetailBean, String id)
			throws ClientProtocolException, IOException, JSONException {
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=product&control=index&id="
				+ id);
		parseJsonCategoryDetails(json, categoryDetailBean);
	}

	private static void parseJsonCategoryDetails(String json,
			CategoryDetailBean categoryDetailBean) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject object = jsonObject.getJSONObject("list");

		categoryDetailBean.setTitle(object.getString("title"));
		categoryDetailBean.setCode(object.getString("code"));
		categoryDetailBean.setTel(object.getString("tel"));
		categoryDetailBean.setUrl(object.getString("url"));

		JSONArray photo_string = object.getJSONArray("photo_string");
		JSONObject o = photo_string.getJSONObject(0);
		JSONArray jsonphotos = o.getJSONArray("s");
		String[] photos = new String[jsonphotos.length()];
		for (int j = 0; j < photos.length; j++) {
			photos[j] = jsonphotos.getString(j);
		}
		categoryDetailBean.setPhotos(photos);
	}

	public static boolean sendMessage(Map<String, String> map)
			throws ClientProtocolException, IOException, JSONException {
		HttpEntity responseEntity = coneParams("http://news.pts80.net/hege/api/?action=message&control=message",map);

		boolean isOk = false;
		if (responseEntity != null) {
			InputStream instream = responseEntity.getContent();
			String str = WebService.getString(instream);
			isOk = parseStrIsOk(str);
		}
		return isOk;
	}

	private static boolean parseStrIsOk(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		int returns = jsonObject.getInt("returns");
		Log.i("MainActivity", returns+"");
		if(1==returns){
			return true;
		}else{
			return false;
		}
	}

	public static boolean sendPraise(String id, int way) throws ClientProtocolException, IOException, JSONException {
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=good&control=good&id="+id+"&way="
				+ way);
		boolean isOk = parseStrIsOk(json);
		return isOk;
	}

	public static void getCaseList(ArrayList<CasesListBean> caseListBean,int index) throws ClientProtocolException, IOException, JSONException {
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=case&control=list");
		parseStrCase(json,caseListBean,index);
	}

	private static void parseStrCase(String json,ArrayList<CasesListBean> caseListBean,int index) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray objects = jsonObject.getJSONArray("category");
		
		//获得种类
		JSONObject object = objects.getJSONObject(index);  ////////取种类
		String categoryName = object.getString("name");
		
		JSONObject listObject = jsonObject.getJSONObject("list");
		JSONArray cases = listObject.getJSONArray(categoryName);
		for(int i=0;i<cases.length();i++){
			JSONObject caseObject = cases.getJSONObject(i);
			CasesListBean casesBean = new CasesListBean();
			casesBean.setId(caseObject.getString("id"));
			casesBean.setTitle(caseObject.getString("title"));
			
			casesBean.setPhoto(caseObject.getJSONArray("photo").getString(1));
			casesBean.setPraise(caseObject.getString("good"));
			
			caseListBean.add(casesBean);
		}
	}

	public static void getCaseDetailList(CaseDetailBean caseDetailBean,
			String id) throws ClientProtocolException, IOException, JSONException {
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=case&control=list&id="+id);
		parseStrCaseDetail(json,caseDetailBean);
	}

	private static void parseStrCaseDetail(String json,
			CaseDetailBean caseDetailBean) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		caseDetailBean.setTitle(jsonObject.getString("title"));
		caseDetailBean.setTel(jsonObject.getString("tel"));
		caseDetailBean.setContent(jsonObject.getString("content"));
		caseDetailBean.setCate(jsonObject.getString("cate"));
		
		JSONArray jsonArray = jsonObject.getJSONArray("photo_string");
		JSONObject object = jsonArray.getJSONObject(0);
		JSONArray photoArray = object.getJSONArray("s");
		String[] photos = new String[photoArray.length()];
		for(int i=0;i<photoArray.length();i++){
			photos[i] = photoArray.getString(i);
		}
		caseDetailBean.setPhotos(photos);
	}
	
	//用于点赞
    public static class PraiseRunnable implements Runnable{
		private int praiseCate; //点赞类别
		private String id; //产品id
		private Context context;
    	private IsOkBack isOkBack;
		public PraiseRunnable(Context context,String id,int praiseCate){
			this.context = context;
    		this.praiseCate = praiseCate;
			this.id = id;
		}
    	@Override
		public void run() {			
			try {
				boolean isOk = WebService.sendPraise(id,praiseCate);
				Looper.prepare();
				if(isOk){
					Toast.makeText(context, "点赞成功!", 0).show();
					if(isOkBack!=null){
						isOkBack.isOk(true);
					}
				}else{
					Toast.makeText(context, "点赞失败!", 0).show();
					if(isOkBack!=null){
						isOkBack.isOk(false); //用回调来传值
					}
				}
				Looper.loop();
			} catch (ClientProtocolException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
    	
    	public void setInterfaceIsOk(IsOkBack isOkBack){
    		this.isOkBack = isOkBack;
    	}
    	public interface IsOkBack{
    		public void isOk(boolean status);
    	}
	}

	public static void getFirstChatMessage(ArrayList<ChatMessage> chatMessageList) throws ClientProtocolException, IOException, JSONException {
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=feedback&control=list");
		parseStrFirstChat(json,chatMessageList);
	}

	private static void parseStrFirstChat(String json,
			ArrayList<ChatMessage> chatMessageList) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		for(int i=0;i<jsonArray.length();i++){
			ChatMessage chatMessage = new ChatMessage();
			JSONObject object = jsonArray.getJSONObject(i);
			String type = object.getString("cid");
			if("1".equals(type)){
				chatMessage.setType(Type.OUTCOMING);
			}else{
				chatMessage.setType(Type.INCOMING);
			}
			chatMessage.setMsg(object.getString("content"));
			String sDate = object.getString("add_time");
			Date date = new Date(Long.parseLong(sDate));
			chatMessage.setDate(date);
			chatMessageList.add(chatMessage);
		}
		
	}

	public static boolean sendChatMessage(Map<String, String> map) throws ClientProtocolException, IOException, JSONException {
		//获取/记录机器码，机器型号
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=code&control=add&code="+map.get("id")+"&way=android");
		JSONObject object = new JSONObject(json);
		String id = object.getString("id");
		map.put("id", id);
		HttpEntity responseEntity = coneParams("http://news.pts80.net/hege/api/?action=feedback&control=add",map);
		
		boolean isOk = false;
		if (responseEntity != null) {
			InputStream instream = responseEntity.getContent();
			String str = WebService.getString(instream);
			isOk = parseStrIsOk(str);
		}
		return isOk;
	}

	private static HttpEntity coneParams(String url,Map<String, String> map)
			throws UnsupportedEncodingException, IOException,
			ClientProtocolException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			NameValuePair nameValuePairs = new BasicNameValuePair(
					entry.getKey(), entry.getValue());
			parameters.add(nameValuePairs);
		}
		// 请求实体HttpEntity也是一个接口，我们用它的实现类UrlEncodedFormEntity来创建对象，注意后面一个String类型的参数是用来指定编码的
		HttpEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
		httpPost.setEntity(entity);
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity responseEntity = response.getEntity();
		return responseEntity;
	}

	public static void getHistoryChatMessage(Map<String, String> map, ArrayList<ChatMessage> chatMessageList) throws UnsupportedEncodingException, ClientProtocolException, IOException, JSONException {
		HttpEntity responseEntity = coneParams("http://news.pts80.net/hege/api/?action=feedback&control=list",map);
		if (responseEntity != null) {
			InputStream instream = responseEntity.getContent();
			String str = WebService.getString(instream);
			parseStrFirstChat(str,chatMessageList);
		}
	}

	public static void findProductData(int id, String mKey, ArrayList<CategoryListBean> categoryListBean) throws JSONException, ClientProtocolException, IOException {
		String key = URLEncoder.encode(mKey, "utf-8");
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=product&control=index&cid="
				+ id+"&keyword="+key);
		parseJsonCategorys(json, categoryListBean);
	}

	public static String[] findVersion() throws ClientProtocolException, IOException, JSONException {
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=version&control=index");
		String[] verAUrl = new String[2];
		JSONObject object = new JSONObject(json);
		verAUrl[0] = object.getString("version");
		verAUrl[1] = object.getString("url");
		return verAUrl;
	}

	public static void getCategory(ArrayList<CategoryBean> mCategoryList) throws ClientProtocolException, IOException, JSONException {
		String json = getJsonStr("http://news.pts80.net/hege/api/?action=product&control=cate");
		parseJsonCategory(json, mCategoryList);
	}

	/**
	 * 获得categorys
	 * @param json
	 * @param mCategoryList
	 * @throws JSONException 
	 */
	private static void parseJsonCategory(String json,
			ArrayList<CategoryBean> mCategoryList) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = jsonArray.getJSONObject(i);
			CategoryBean categoryBean = new CategoryBean();
			
			categoryBean.setId(object.getString("id"));
			categoryBean.setPhoto(object.getString("photo"));
			categoryBean.setName(object.getString("name"));
			mCategoryList.add(categoryBean);
		}
	}
}
