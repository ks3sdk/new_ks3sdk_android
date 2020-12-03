package com.ksyun.ks3.util;

import com.ksyun.ks3.services.request.adp.Adp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月17日 上午10:23:53
 * 
 * @description
 **/
public class HttpUtils {
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final Pattern ENCODED_CHARACTERS_PATTERN;
	static {
		StringBuilder pattern = new StringBuilder();

		pattern.append(Pattern.quote("+")).append("|")
				.append(Pattern.quote("*")).append("|")
				.append(Pattern.quote("%7E")).append("|")
				.append(Pattern.quote("%2F"));

		ENCODED_CHARACTERS_PATTERN = Pattern.compile(pattern.toString());
	}

	//encode objectkey时不会编码斜杠
	public static String urlEncode(final String value, final boolean path) {
		if (value == null) {
			return "";
		}

		try {
			String encoded = URLEncoder.encode(value, DEFAULT_ENCODING);

			Matcher matcher = ENCODED_CHARACTERS_PATTERN.matcher(encoded);
			StringBuffer buffer = new StringBuffer(encoded.length());

			while (matcher.find()) {
				String replacement = matcher.group(0);

				if ("+".equals(replacement)) {
					replacement = "%20";
				} else if ("*".equals(replacement)) {
					replacement = "%2A";
				} else if ("%7E".equals(replacement)) {
					replacement = "~";
				} else if (path && "%2F".equals(replacement)) {
					replacement = "/";
				}

				matcher.appendReplacement(buffer, replacement);
			}

			matcher.appendTail(buffer);
			encoded = buffer.toString();
			return encoded;

		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}
	public static String encodeParams(Map<String,String> params){
		List<Entry<String, String>> arrayList = new ArrayList<Entry<String, String>>(
				params.entrySet());
		Collections.sort(arrayList,
				new Comparator<Entry<String, String>>() {
					public int compare(Entry<String, String> o1,
							Entry<String, String> o2) {
						return o1.getKey().compareTo(o2.getKey());
					}
				});
		List<String> list = new ArrayList<String>();
		for (Entry<String, String> entry : arrayList) {
			String value = null;
			//8203,直接从浏览器粘下来的字符串中可能含有这个非法字符
			String key = entry.getKey().replace(String.valueOf((char)8203),"");
			if (!StringUtils.isBlank(entry.getValue()))
				value = URLEncoder.encode(entry.getValue());
			if (value != null && !value.equals("")) {
				list.add(key + "=" + value);
			}else if (value == null) {
				list.add(key + "=" + "");
			}else{
				if (RequestUtils.subResource.contains(key))
			    	list.add(key);
			}
		}

		String queryParams = StringUtils.join(list.toArray(), "&");
		return queryParams;
	}
	public static String convertAdps2String(List<Adp> fops){
		StringBuffer fopStringBuffer = new StringBuffer();
		for(Adp fop : fops){
			fopStringBuffer.append(fop.getCommand());
			if(!(StringUtils.isBlank(fop.getBucket())&&StringUtils.isBlank(fop.getKey()))){
				if(StringUtils.isBlank(fop.getBucket())){
					fopStringBuffer.append(String.format("|tag=saveas&object=%s",Base64.encode(fop.getKey().getBytes())));
				}else if(StringUtils.isBlank(fop.getKey())){
					fopStringBuffer.append(String.format("|tag=saveas&bucket=%s",fop.getBucket()));
				}else{
					fopStringBuffer.append(String.format("|tag=saveas&bucket=%s&object=%s",fop.getBucket(),Base64.encode(fop.getKey().getBytes())));
				}
			}
			fopStringBuffer.append(";");
		}
		String fopString = fopStringBuffer.toString();
		if(fopString.endsWith(";")){
			fopString = fopString.substring(0,fopString.length()-1);
		}
		return fopString;
	}
}
