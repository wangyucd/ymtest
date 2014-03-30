package com.sedion.advanced.util;

import net.sf.json.JSONObject;

import com.sedion.advanced.model.WeixinQRCode;
import com.sedion.wechatapi.util.CommonUtil;

/**  
*   
* 项目名称：wechatapi  
* 类名称：CreateQRCode  
* 类描述：创建二维码工具 
* 创建人：WQ  
* 创建时间：2014-3-7 下午3:35:29  
* @version       
*/
public class CreateQRCode extends CommonUtil{
	/**
	 * 创建临时带参数二维码
	 * 
	 * @param accessToken 接口访问凭证
	 * @param expireSeconds 二维码的有效时间，以秒为单位，最大不超过1800秒
	 * @param sceneId 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
	 * @return WeixinQRCode
	 */
	public static WeixinQRCode createTemporaryQRCode(String accessToken,
			int expireSeconds,int sceneId) {
		WeixinQRCode weixinQRCode=null;
		String requestUrl=TEMPORARY_QRCODE_URL.replace("ACCESS_TOKEN", accessToken);
		// 需要提交的json数据
		String jsonMsg="{\"expire_seconds\": %d,\"action_name\": \"QR_SCENE\"," +
				"\"action_info\": {\"scene\": {\"scene_id\": %d}}}";
		// 创建临时带参数二维码
		JSONObject jsonObject = httpRequest(requestUrl, "POST",
				String.format(jsonMsg, expireSeconds, sceneId));
		if (null!=jsonObject) {
			try {
				weixinQRCode=new WeixinQRCode();
				weixinQRCode.setTicket(jsonObject.getString("ticket"));
				weixinQRCode.setExpireSeconds(jsonObject.getInt("expire_seconds"));
				log.info("创建临时带参数二维码成功 ticket:{} expire_seconds:{}",
						weixinQRCode.getTicket(),weixinQRCode.getExpireSeconds());
			} catch (Exception e) {
				weixinQRCode=null;
				int errorCode=jsonObject.getInt("errcode");
				String errorMsg=jsonObject.getString("errmsg");
				log.error("创建临时带参数二维码失败 errCode:{} errormsg:{} ",errorCode,errorMsg);
			}
		}
		return weixinQRCode;
	}
	
	
	/**
	 * 创建永久带参数二维码
	 * 
	 * @param accessToken 接口访问凭证
	 * @param sceneId 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
	 * @return String
	 */
	public static String createPermanentQRCode(String accessToken,int sceneId) {
		String ticket=null;
		String requestUrl=PERMANENT_QRCODE_URL.replace("ACCESS_TOKEN", accessToken);
		// 需要提交的json数据
		String jsonMsg="{\"action_name\": \"QR_SCENE\",\"action_info\": {\"scene\": {\"scene_id\": %d}}}";
		// 创建临时带参数二维码
		JSONObject jsonObject = httpRequest(requestUrl, "POST",
				String.format(jsonMsg , sceneId));
		if (null!=jsonObject) {
			try {
				ticket=jsonObject.getString("ticket");
				log.info("创建永久带参数二维码成功 ticket:{}",ticket);
			} catch (Exception e) {
				int errorCode=jsonObject.getInt("errcode");
				String errorMsg=jsonObject.getString("errmsg");
				log.error("创建永久带参数二维码失败 errCode:{} errormsg:{} ",errorCode,errorMsg);
			}
		}
		return ticket;
	}
	
	public static void main(String[] args) {
		// 获取接口访问凭证
		String accessToken=getAccessToken("wx13c0a227486f7e64", "864e16284d38c05c62cddc1be000351e").getAccesstoken();
		// 创建临时二维码
		// gQHN7zoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL0pFTUxKVDdsS1Q1UWFOeGtvbTJ3AAIEIn4ZUwMECAcAAA==
		WeixinQRCode Temporaryqrcode=createTemporaryQRCode(accessToken, 1800, 1);
		System.err.println(Temporaryqrcode.getTicket());
		// 创建永久二维码
		// gQGx8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL1JrTlIyajNsZ2o3NzlyNXFfRzJ3AAIEGKUZUwMEPAAAAA==
		String Permanentqrcode=createPermanentQRCode(accessToken, 2);
		System.err.println(Permanentqrcode);
	}
	
	
}
