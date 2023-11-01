package org.jeecg.modules.im.base.util;//package org.jeecg.modules.im.base.util;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLEncoder;
//
//import net.sf.json.JSONObject;
//
//import org.apache.log4j.Logger;
//
//import com.qiuzhping.google.beans.GoogleGeocodeJSONBean;
//
///**
// * <Description functions in a word>
// * type :1-->address 2-->latlng
// * <Detail description>
// *
// * @author  Peter.Qiu
// * @version  [Version NO, 2014-9-18]
// * @see  [Related classes/methods]
// * @since  [product/module version]
// */
//public final class GoogleGeocoderUtil {
//	public static final int ADDRESS = 1;
//	public static final int LATLNG = 2;
//	private final String GOOGLEAPIURL="http://maps.googleapis.com/maps/api/geocode/json?language=en&sensor=true";
//	private int type ;//1-->address 2-->latlng
//	public int getType() {
//		return type;
//	}
//
//	public void setType(int type) {
//		this.type = type;
//	}
//	private static GoogleGeocoderUtil instance;
//
//	public static GoogleGeocoderUtil getInstance() {
//		if(instance == null){
//			instance = new GoogleGeocoderUtil();
//		}
//		return instance;
//	}
//
//	/** <Description functions in a word>
//	 * 2014-9-18
//	 * <Detail description>
//	 * @author  Peter.Qiu
//	 * @param address
//	 * @return
//	 * @return GoogleGeocodeJSONBean [Return type description]
//	 * @throws Exception
//	 * @exception throws [Exception] [Exception description]
//	 * @see [Related classes#Related methods#Related properties]
//	 */
//	public GoogleGeocodeJSONBean geocodeByAddress(String address) throws Exception{
//		if(address == null || address.equals("")){
//			return null;
//		}
//		GoogleGeocodeJSONBean bean = null;
//		BufferedReader in= null;
//		HttpURLConnection httpConn = null;
//		try {
//			log.info("Start open url");
//			String urlPath = GOOGLEAPIURL+"&address="+URLEncoder.encode(address,"UTF-8");;
//			if(this.getType() == LATLNG){
//				urlPath = GOOGLEAPIURL+"&latlng="+address;
//			}
//			log.info("url : "+urlPath);
//			URL url = new URL(urlPath);
//			httpConn = (HttpURLConnection) url.openConnection();
//			log.info("End open url");
//			httpConn.setDoInput(true);
//			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
//		    String line;
//		    String result="";
//		    while ((line = in.readLine()) != null) {
//		        result += line;
//		    }
//		    in.close();
//		    //httpConn.disconnect();
//		    JSONObject jsonObject = JSONObject.fromObject( result );
//		    bean = (GoogleGeocodeJSONBean) JSONObject.toBean( jsonObject, GoogleGeocodeJSONBean.class );
//		    if(bean != null && bean.status.equalsIgnoreCase("ok") && bean.results != null && bean.results[0].geometry.getLocation() != null){
//		    	log.info("Start display Geocode info");
//		    	log.info("Formatted Address :" + bean.results[0].getFormatted_address());
//		    	log.info("geometry Location : " + bean.results[0].geometry.getLocation().getLat() + ","+bean.results[0].geometry.getLocation().getLng());
//			    log.info("End display Geocode info");
//		    }
//		    log.info("End geocode");
//		    return bean;
//		} catch (MalformedURLException e) {
//			log.error(e);
//			throw e;
//		} catch (IOException e) {
//			log.error(e);
//			throw e;
//		} finally {
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException e) {
//					log.error(e);
//					throw e;
//				}
//			}
//			if (httpConn != null) {
//				httpConn.disconnect();
//			}
//		}
//	}
//
//	public String getGoogleLongitudeDimensions(GoogleGeocodeJSONBean googleBean) throws IOException{
//		if (googleBean != null &&  googleBean.status.equalsIgnoreCase("ok")
//				&& googleBean.results[0] != null
//				&& googleBean.results[0].formatted_address != null
//				&& googleBean.results[0].getGeometry().location != null
//				&& googleBean.results[0].getGeometry().location.getLat() != null
//				&& googleBean.results[0].getGeometry().location.getLng() != null) {
//			String formatted_Address = googleBean.results[0].formatted_address;
//			String location = googleBean.results[0].getGeometry().location.getLat()+","+googleBean.results[0].getGeometry().location.getLng();
//			return formatted_Address.trim()+"|"+location;
//		}
//		return null;
//	}
//	/** <Description functions in a word>
//	 * 2014-9-18
//	 * <Detail description>
//	 * @author  Peter.Qiu
//	 * @param args [Parameters description]
//	 * @return void [Return type description]
//	 * @throws Exception
//	 * @exception throws [Exception] [Exception description]
//	 * @see [Related classes#Related methods#Related properties]
//	 */
//	public static void main(String[] args) throws Exception {
//		try {
//			getInstance().setType(2);
//			GoogleGeocodeJSONBean bean = getInstance().geocodeByAddress("39.90403,116.407526");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//}