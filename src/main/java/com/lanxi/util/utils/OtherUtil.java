package com.lanxi.util.utils;

import com.alibaba.fastjson.JSONObject;
import com.lanxi.util.entity.LogFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 其他工具类
 * 手机号码解析器 			PhoneNumAnalyst
 * 身份证号码(中国)解析器	IdAnalyst
 * @author yangyuanjian
 *
 */
public class OtherUtil {
	/**
	 * 获取手机号所属运营商
	 * @param phone 待判定手机号码
	 * @return 
	 * 			null   手机号为空
	 * 			mobile 移动手机号
	 * 			unicom 联通手机号
	 * 			union  电信手机号
	 * 			other  虚拟运营商
	 *建议使用PhoneNumAnalyst来解析
	 */
	@Deprecated
	public static String getServiceProvider(String phone){
		if(phone==null||phone.isEmpty())
			return null;
		final String  mobileRegex="(13[4-9])|(15[0-2])|(15[7-9])|(18[2-4])|(18[7-8])|(178)|(147)";
		final String  unionRegex="(133)|(153)|(189)|(18[0-1])|(173)|(177)";
		final String  unicomRegex="(13[0-2])|(15[5-6])|(18[5-6])|(145)|(176)";
		phone = phone.substring(0,3);
		if(phone.matches(mobileRegex))
			return "mobile";
		if(phone.matches(unionRegex))
			return "union";
		if(phone.matches(unicomRegex))
			return "unicom";
		return "other";
	}

	/**
	 * 手机号码解析类
	 * 依赖与网站<link></>http://www.ip138.com:8080/search.asp</link>
	 */
	public static class PhoneNumAnalyst {
		/**手机号码*/
		private String phone;
		/**归属地*/
		private String address;
		/**归属地区域码*/
		private String areaCode;
		/**归属地邮政编码*/
		private String zip;
		/**运营商*/
		private String company;
		/**手机卡类型*/
		private String cardType;

		public PhoneNumAnalyst(String phone){
			this.phone=phone.trim();
			analysis(this.phone);
		}

		private void analysis(String phone){
			String url="http://www.ip138.com:8080/search.asp?mobile=[phone]&action=mobile";
			url=url.replace("[phone]",phone);
			String companyRegex="(联通)|(移动)|(电信)";
			Document doc=Jsoup.parse(HttpUtil.get(url,"gb2312"));
			Element body=doc.body();
			Elements tbody=body.getElementsByTag("tbody");
			if(tbody==null||tbody.isEmpty())
				return ;
			Elements trs=tbody.get(1).getElementsByClass("tdc");
			if(trs==null||trs.isEmpty())
				return;
			address=trs.get(1).text().replaceAll("　","").replaceAll(" ","").replaceAll(" ","").substring(5);
			cardType=trs.get(2).text().replaceAll("　","").replaceAll(" ","").replaceAll(" ","").substring(3);
			areaCode=trs.get(3).text().replaceAll("　","").replaceAll(" ","").replaceAll(" ","").substring(2);
			zip=trs.get(4).text().replaceAll("　","").replaceAll(" ","").replaceAll(" ","").substring(2).substring(0,6);
			Matcher matcher=Pattern.compile(companyRegex).matcher(cardType);
			matcher.find();
			company=matcher.group();
			if(cardType.contains("虚拟"))
				company="虚拟"+company;
		}

		public String getPhoneInfo(){
			return "手机号码:"+phone+"\r\n归属地  :"+address+"\r\n营运商  :"+company+"\r\n卡类型  :"+cardType+"\r\n区号    :"+areaCode+"\r\n邮编    :"+zip;
		}


		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone.trim();
			analysis(this.phone);
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getAreaCode() {
			return areaCode;
		}

		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode;
		}

		public String getZip() {
			return zip;
		}

		public void setZip(String zip) {
			this.zip = zip;
		}

		public String getCompany() {
			return company;
		}

		public void setCompany(String company) {
			this.company = company;
		}

		public String getCardType() {
			return cardType;
		}

		public void setCardType(String cardType) {
			this.cardType = cardType;
		}
	}


	/**
	 * 18位身份证号码解析类
	 * 20170812后新增支持15位身份证解析
	 */
	public static class IdAnalyst {
		public static Map<String,Province> provinces;
		private static final Map<String,String> constProvinces=new HashMap<>();
		static{
			constProvinces.put("220000","吉林省");
			constProvinces.put("440000","广东省");
			constProvinces.put("230000","黑龙江省");
			constProvinces.put("450000","广西壮族自治区");
			constProvinces.put("460000","海南省");
			constProvinces.put("710000","台湾省");
			constProvinces.put("500000","重庆市");
			constProvinces.put("510000","四川省");
			constProvinces.put("520000","贵州省");
			constProvinces.put("310000","上海市");
			constProvinces.put("530000","云南省");
			constProvinces.put("320000","江苏省");
			constProvinces.put("540000","西藏自治区");
			constProvinces.put("110000","北京市");
			constProvinces.put("330000","浙江省");
			constProvinces.put("120000","天津市");
			constProvinces.put("340000","安徽省");
			constProvinces.put("130000","河北省");
			constProvinces.put("350000","福建省");
			constProvinces.put("140000","山西省");
			constProvinces.put("360000","江西省");
			constProvinces.put("150000","内蒙古自治区");
			constProvinces.put("370000","山东省");
			constProvinces.put("810000","香港特别行政区");
			constProvinces.put("820000","澳门特别行政区");
			constProvinces.put("610000","陕西省");
			constProvinces.put("620000","甘肃省");
			constProvinces.put("410000","河南省");
			constProvinces.put("630000","青海省");
			constProvinces.put("420000","湖北省");
			constProvinces.put("640000","宁夏回族自治区");
			constProvinces.put("210000","辽宁省");
			constProvinces.put("430000","湖南省");
			constProvinces.put("650000","新疆维吾尔自治区");
			getChinaArea();
		}
		/**18位身份证号码*/
		private String id;
		/**输入身份证号码*/
		private String inputId;
		/**身份证所属省份*/
		private String province;
		/**身份证所属城市*/
		private String city;
		/**身份证所属区域*/
		private String county;
		/**出生年*/
		private String year;
		/**出生月*/
		private String month;
		/**出生日*/
		private String date;
		/**出生序列*/
		private String sequence;
		/**校验码*/
		private String check;
		/**生日*/
		private String birthday;
		/**身份证所示地址*/
		private String address;
		/**性别*/
		private String sex;
		public IdAnalyst(String id){
			this.inputId=id.trim().toLowerCase();
			LogFactory.info(this, "id:"+inputId);
			LogFactory.info(this, "length:"+inputId.length());
			if(inputId==null)
		        throw new NullPointerException("输入的身份证为null!");
		    if(inputId.length()!=18&&inputId.length()!=15)
		        throw new IllegalArgumentException("请输入15或18位身份证号码!");
			if(inputId.length()==18)
				this.id=inputId;
			if(inputId.length()==15) {
				this.id=inputId.substring(0, 6);
				this.id+="19";
				this.id+=inputId.substring(6);
				this.id+=calCheck(this.id);
			}
			analysis(this.id);
			address = getProvinceName()+getCityName()+getCountyName();
		}


		/**
		 * 解析身份证信息
		 */

		private void analysis(String id){
			province = id.substring(0,2);
			city = id.substring(2,4);
			county = id.substring(4,6);
			year=id.substring(6,10);
			month=id.substring(10,12);
			date=id.substring(12,14);
			sequence = id.substring(14,17);
			sex=id.substring(16, 17);
			check=id.substring(17);
			if(validateAddress())
			    address=getProvinceName()+getCityName()+getCountyName();
            if(validateDate())
			    birthday=year+"年"+month+"月"+date+"日";
		}

		/**
		 * 获取身份证解析信息
		 * @return 身份证解析信息
		 */
		public String getIdInfo(){
		    StringBuilder builder=new StringBuilder();
		    builder.append("身份证号码		:"+this.inputId+"\r\n");
		    builder.append("18位身份证号码	:"+this.id+"\r\n");
            builder.append("性别       		:"+getSexInfo()+"\r\n");
            builder.append("年龄       		:"+(getBirthday()==null?"未知\r\n":(Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis())))-Integer.parseInt(year)+"岁\r\n")));
            builder.append("归属地    		:"+(getAddress()==null||getAddress().contains("null")?"解析地址不存在!"+getAddress().replace("null","????"):getAddress())+"\r\n");
		    builder.append("出生日期   		:"+(getBirthday()==null?"解析日期不存在":getBirthday())+"\r\n");
            builder.append("出生序列   		:"+getSequence()+"\r\n");

		    builder.append("校验结果   		\r\n");
		    builder.append("    地址校验  	:"+(validateAddress()?"通过":"不通过")+"\r\n");
		    builder.append("    生日校验  	:"+(validateDate()?"通过":"不通过")+"\r\n");
		    builder.append("    校验码校验	:"+(validateCheck()?"通过":"不通过")+"\r\n");
		    return builder.toString();
        }

		/**
		 * 校验身份证号码是否合法
		 * @return  通过->true
		 * 			不通过->false
		 */
		public Boolean validateId(){
			String regex="[0-9]{17}[0-9xX]{1}";
			if(id.length()!=18)
				return false;
			if(!id.matches(regex))
				return false;
			if(validateDate()!=null&&!validateDate())
				return false;
			if(validateAddress()!=null&&!validateAddress())
				return false;
			if(validateCheck()!=null&&!validateCheck())
				return false;
			return true;
		}

		/**
		 * 获取校验结果
		 * @return 校验结果信息
		 */
		public String validateIdInfo(){
			String regex="[0-9]{17}[0-9x]{1}";
			StringBuilder buff=new StringBuilder();
			if(id.length()!=18)
				buff.append("身份证号码长度不为18位!\r\n");
			if(!id.matches(regex))
				buff.append("身份证号码中包含非法字符!\r\n");
			if(!validateAddress()) {
                buff.append("身份证号码中归属地解析错误!\r\n");
                if(getProvinceName()==null)
                    buff.append("归属地省份不存在!");
                if(getCityName()==null)
                    buff.append("归属地城市不存在!");
                if(getCountyName()==null)
                    buff.append("归属地县城不存在!");
			}
			if(!validateDate()){
                buff.append("身份证号码中生日解析结果不匹配!\r\n");
                buff.append("身份证中生日日期不存在!");
            }
			if(!validateCheck())
				buff.append("身份证号码中校验码不匹配!\r\n");
			return buff.toString();
		}

		/**
		 * 校验身份证地址是否合法
		 * @return	通过->true
		 * 			不通过->false
		 */
		public Boolean validateAddress(){
			if(provinces!=null){
				if(getProvinceName()==null) {
//					System.err.println("province");
					return false;
				}
				if(getCityName()==null) {
//					System.err.println("city");
					return false;
				}
				if(getCountyName()==null) {
//					System.err.println("county");
					return false;
				}
				return true;
			}else{
				return null;
			}
		}

		/**
		 * 从一个网站的js文件爬取身份证归属地信息
		 */
		private static void getArea(){
			try {
				String url="http://www.jiage365.cn/js/id.js";
				String ares=HttpUtil.get(url,"utf-8");
				if(provinces==null){
					provinces=new HashMap<String,Province>();
				}
				String regex="[\u4E00-\u9FA5]{1,10}\\|[0-9]{6}";
				Pattern pattern=Pattern.compile(regex);
				Matcher matcher=pattern.matcher(ares);
				while(matcher.find()){
					String result=matcher.group();
					String[] strs=result.split("\\|");
					String name=strs[0];
					String id=strs[1];
					String provinceId=id.substring(0,2);
					String cityId=id.substring(2,4);
					String countyId=id.substring(4,6);
					if(id.endsWith("0000")){
						if(provinces!=null&&provinces.containsKey(provinceId))
							continue;
						if(provinces==null)
							provinces=new HashMap<String,Province>();
						Province province=new Province();
						province.setId(id);
						province.setName(name);
						province.setCities(new HashMap<String,City>());
						provinces.put(provinceId,province);
					}else if(id.endsWith("00")){
						if(provinces!=null&&provinces.containsKey(provinceId)&&provinces.get(provinceId).getCities().containsKey(cityId)&&provinces.get(provinceId).getCities().get(cityId).getName()!=null)
							continue;
						if(provinces==null)
							provinces=new HashMap<String,Province>();
						if(!provinces.containsKey(provinceId)){
							Province province=new Province();
							province.setId(provinceId+"0000");
							province.setName(constProvinces.get(provinceId+"0000"));
							province.setCities(new HashMap<String, City>());
							provinces.put(provinceId,province);
						}
						if(!provinces.get(provinceId).getCities().containsKey(cityId)){
							City city=new City();
							city.setId(id);
							city.setName(name);
							city.setCounties(new HashMap<String, County>());
							provinces.get(provinceId).getCities().put(cityId,city);
						}else if(provinces.get(provinceId).getCities().get(cityId).getName()==null){
							provinces.get(provinceId).getCities().get(cityId).setName(name);
						}
					}else{

						if(provinces!=null&&provinces.containsKey(provinceId)&&provinces.get(provinceId).getCities().containsKey(cityId)&&provinces.get(provinceId).getCities().get(cityId).getCounties().containsKey(countyId))
							continue;
						if(provinces==null)
							provinces=new HashMap<String,Province>();
						if(!provinces.containsKey(provinceId)){
							Province province=new Province();
							province.setId(provinceId+"0000");
							province.setName(constProvinces.get(provinceId+"0000"));
							province.setCities(new HashMap<String, City>());
							provinces.put(provinceId,province);
						}
						if(!provinces.get(provinceId).getCities().containsKey(cityId)){
							City city=new City();
							city.setId(cityId+"00");
							city.setName(null);
							city.setCounties(new HashMap<String, County>());
							provinces.get(provinceId).getCities().put(cityId,city);
						}
						if(!provinces.get(provinceId).getCities().get(cityId).getCounties().containsKey(countyId)){
							County county=new County();
							county.setId(id);
							county.setName(name);
							provinces.get(provinceId).getCities().get(cityId).getCounties().put(countyId,county);
						}
					}
				}
				File file=new File(FileUtil.getClassPath()+"/json/china.json");
				if(!file.exists())
					FileUtil.makeDirAndFile(file);
				FileOutputStream fout=new FileOutputStream(file);
				OutputStreamWriter os=new OutputStreamWriter(fout,"utf-8");
				PrintWriter printer=new PrintWriter(os);
				printer.print(JSONObject.toJSONString(provinces));
				printer.flush();
				printer.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 校验身份证上的出生日期是否合法
		 * @return	通过->true
		 * 			不通过->false
		 */
		public Boolean validateDate(){
			try {
				SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
				String date=format.format(format.parse(year+month+this.date));
                if((year+month+this.date).equals(date))
					return true;
				return false;
			} catch (Exception e) {
				return false;
			}
		}

		/**
		 * 校验身份证上的验证位是否合法
		 * @return	通过->true
		 * 			不通过->false
		 */
		public Boolean validateCheck(){
            if(check.equals(calCheck(id)))
				return true;
			return false;
		}
		/**
		 * 计算校验码
		 * @param id
		 * @return
		 */
		public String calCheck(String id) {
			int sum=0;
			String checkBit="10x98765432";
			int[] power={7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
			for(int i=0;i<17;i++){
				sum+=(id.charAt(i)-'0')*power[i];
			}
			return checkBit.charAt(sum%11)+"";
		}
		
		/**
		 * 获取身份证上的省份名称
		 * @return	省份名称(包括直辖市)
		 */
		public String getProvinceName(){
			if(provinces!=null) {
                if(provinces.get(province)!=null)
                    return provinces.get(province).getName();
                return null;
			}
			return null;
		}
		/**
		 * 获取身份证上的城市名称
		 * @return	城市名称
		 */
		public String getCityName(){
			if(getProvinceName()!=null) {
                if(provinces.get(province).getCities().get(city)!=null)
                    return provinces.get(province).getCities().get(city).getName();
                return null;
			}
			return  null;
		}
		/**
		 * 获取身份证上的县城名称
		 * @return	县城名称
		 */
		public String getCountyName(){
			if(getCityName()!=null) {
                if(provinces.get(province).getCities().get(city).getCounties().get(county)!=null)
			        return provinces.get(province).getCities().get(city).getCounties().get(county).getName();
                return null;
            }
            return null;
		}
		/**
		 * 获取身份证上的性别
		 * @return	性别 男|女
		 */
		public String getSexInfo(){
			try {
				if(Integer.parseInt(sex)%2==0)
					return "女";
				else
					return "男";
			}catch (Exception e){
				return null;
			}
		}
		/**
		 * 从国家统计局网站爬取03年以后的地区码数据
		 */
		public static void getChinaArea(){
			try {
				File file=new File(FileUtil.getClassPath()+"/json/china.json");
				if(!file.exists()){
					makeChinaJson();
				}else{
					String json=FileUtil.getFileContentString(file);
					provinces=new HashMap<String,Province>();
					Map<String,City> tempCity;
					Map<String,County> tempCounty;
					@SuppressWarnings("unchecked")
					Map<String,JSONObject> temp=JSONObject.parseObject(json,HashMap.class);
					for(Map.Entry<String,JSONObject> each:temp.entrySet()){
						tempCity=new HashMap<String,City>();
						String provinceId=each.getKey();
						JSONObject pobj=each.getValue();
						Province tp=new Province();
						tp.setId(pobj.getString("id"));
						tp.setName(pobj.getString("name"));
						tp.setCities(tempCity);
						provinces.put(provinceId,tp);
						@SuppressWarnings("unchecked")
						Map<String,JSONObject> temp1=JSONObject.parseObject(pobj.getString("cities"),HashMap.class);
						for(Map.Entry<String,JSONObject> one:temp1.entrySet()){
							tempCounty=new HashMap<String,County>();
							String cityId=one.getKey();
							JSONObject cobj=one.getValue();
							City tc=new City();
							tc.setId(cobj.getString("id"));
							tc.setName(cobj.getString("name"));
							tc.setCounties(tempCounty);
							tempCity.put(cityId,tc);
							@SuppressWarnings("unchecked")
							Map<String,JSONObject> temp2=JSONObject.parseObject(cobj.getString("counties"),HashMap.class);
							for(Map.Entry<String,JSONObject> every:temp2.entrySet()){
								String countyId=every.getKey();
								JSONObject cyobj=every.getValue();
								County tcy=new County();
								tcy.setId(cyobj.getString("id"));
								tcy.setName(cyobj.getString("name"));
								tempCounty.put(countyId,tcy);
							}
						}
					}
				}
			} catch (Exception e) {
				throw e;
			}

		};

		/**
		 * 将爬取的地区码信息封装为json避免每次调用都要较长时间
		 */
		public static void makeChinaJson(){
			try {
				ExecutorService threadPool= Executors.newFixedThreadPool(5);
				List<String> urls=getChinaAreaUrls();
				CountDownLatch count=new CountDownLatch(urls.size());
				provinces=new HashMap<>();
				for(String one:urls){
					Runnable task=()->{
//					System.out.println("one:"+one);
					Document doc= Jsoup.parse(HttpUtil.get(one ,"utf-8"));
					Element root=doc.body();
					Elements elements=root.getElementsByClass("xilan_con");
					for(Element each:elements){
							String str=each.text();
							str=str.replaceAll("　","");
							str=str.replaceAll(" ","");
							str=str.replaceAll(" ","");
							String regex="[0-9]{6}[\u4E00-\u9FA5]{1,10}";
							String regex1="[0-9]{6}";
							String regex2="([\u4E00-\u9FA5]{1,10})";
							Pattern pattern=Pattern.compile(regex);
							Matcher matcher=pattern.matcher(str);
							Pattern pattern1=Pattern.compile(regex1);
							Pattern pattern2=Pattern.compile(regex2);
							while(matcher.find()){
								Matcher matcher1=pattern1.matcher(matcher.group());
								Matcher matcher2=pattern2.matcher(matcher.group());
								matcher1.find();
								matcher2.find();
								String id=matcher1.group();
								String name=matcher2.group();
								String provinceId=id.substring(0,2);
								String cityId=id.substring(2,4);
								String countyId=id.substring(4,6);
								if(id.endsWith("0000")){
									if(provinces.containsKey(provinceId)){
										continue;
									} else {
										Province province = new Province();
										province.setId(id);
										province.setName(name);
										province.setCities(new HashMap<String, City>());
										provinces.put(provinceId,province);
									}
								}else if(id.endsWith("00")){
									Province province=provinces.get(provinceId);
									if(province.getCities().containsKey(cityId)) {
										continue;
									}
									else{
										City city=new City();
										city.setId(id);
										city.setName(name);
										city.setCounties(new HashMap<String, County>());
										province.getCities().put(cityId,city);
									}
								}else{
									City city=provinces.get(provinceId).getCities().get(cityId);
									if(city.getCounties().containsKey(countyId)){
										continue;
									}else{
										County county=new County();
										county.setId(id);
										county.setName(name);
										city.getCounties().put(countyId,county);
									}
								}
							}
							count.countDown();
							System.out.println(Thread.currentThread().getId()+" finished !");
						}
					};
					threadPool.execute(task);
				}
				threadPool.shutdown();
				getArea();
			} catch (Exception e) {
				System.err.println(Arrays.asList(e.getStackTrace()));
				throw new RuntimeException(e);
			}
		}

		/**
		 * 爬取国家统计局上地区码文件的url
		 * @return url列表
		 */
		public static List<String> getChinaAreaUrls(){
			String url="http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/";
			Document doc= Jsoup.parse(HttpUtil.get(url ,"utf-8"));
			Element root=doc.body();
			Elements elements=root.getElementsByClass("center_list_contlist");
			Elements hrefs=elements.get(0).getElementsByTag("a");
			List<String> urls=new ArrayList<>();
			for(Element each:hrefs){
				if(!each.hasAttr("href"))
					continue;
				urls.add(url+each.attr("href").substring(2));
			}
			return urls;
		}

        public void setId(String id){
		    this.id=id;
		    analysis(this.id);
        }

		public String getId() {
			return id;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getCounty() {
			return county;
		}

		public void setCounty(String county) {
			this.county = county;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getSequence() {
			return sequence;
		}

		public void setSequence(String sequence) {
			this.sequence = sequence;
		}

		public String getCheck() {
			return check;
		}

		public void setCheck(String check) {
			this.check = check;
		}

		public String getBirthday() {
			return birthday;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}
		/**
		 * 省份类
		 * @author yangyuanjian
		 *
		 */
		public static class Province{
		        private String name;
		        private String id;
		        private Map<String,City> cities;

		        public String getName() {
		            return name;
		        }

		        public void setName(String name) {
		            this.name = name;
		        }

		        public String getId() {
		            return id;
		        }

		        public void setId(String id) {
		            this.id = id;
		        }

		        public Map<String,City> getCities() {
		            return cities;
		        }

		        public void setCities(Map<String,City>  cities) {
		            this.cities = cities;
		        }

		        @Override
		        public String toString() {
		            return "Province{" +
		                    "name='" + name + '\'' +
		                    ", id=" + id +
		                    ", cities=" + cities +
		                    '}';
		        }
		    };
	    /**
	     * 城市类
	     * @author yangyuanjian
	     *
	     */
		public static class City{
	        private String name;
	        private String id;
	        private Map<String,County> counties;

	        public String getId() {
	            return id;
	        }

	        public void setId(String id) {
	            this.id = id;
	        }

	        public String getName() {
	            return name;
	        }

	        public void setName(String name) {
	            this.name = name;
	        }

	        public Map<String,County> getCounties() {
	            return counties;
	        }

	        public void setCounties(Map<String,County> counties) {
	            this.counties = counties;
	        }

	        @Override
	        public String toString() {
	            return "City{" +
	                    "name='" + name + '\'' +
	                    ", id=" + id +
	                    ", counties=" + counties +
	                    '}';
	        }
	    };
	    /**
	     * 区域类
	     * @author yangyuanjian
	     *
	     */
	    public static class County{
	      private String name;
	      private String    id;

	        public String getName() {
	            return name;
	        }

	        public void setName(String name) {
	            this.name = name;
	        }

	        public String getId() {
	            return id;
	        }

	        public void setId(String id) {
	            this.id = id;
	        }

	        @Override
	        public String toString() {
	            return "County{" +
	                    "name='" + name + '\'' +
	                    ", id=" + id +
	                    '}';
	        }
	    };
};
	
}
