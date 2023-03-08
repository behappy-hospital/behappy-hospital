package org.xiaowu.behappy.manager.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.common.core.exception.HospitalException;
import org.xiaowu.behappy.common.core.util.HttpRequestHelper;
import org.xiaowu.behappy.manager.entity.HospitalSet;
import org.xiaowu.behappy.manager.entity.Schedule;
import org.xiaowu.behappy.manager.mapper.ScheduleMapper;
import org.xiaowu.behappy.manager.service.ApiService;
import org.xiaowu.behappy.manager.service.HospitalService;
import org.xiaowu.behappy.manager.util.BeanUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
@Slf4j
public class ApiServiceImpl implements ApiService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private HospitalService hospitalService;

    @Value("classpath:hospital.json")
    private Resource hospitalResource;

    @Override
    public String getHoscode() {
        HospitalSet hospitalSet = hospitalService.getFirst();
        return hospitalSet.getHoscode();
    }

    @Override
    public String getSignKey() {
        HospitalSet hospitalSet = hospitalService.getFirst();
        return hospitalSet.getSignKey();
    }

    private String getApiUrl() {
        HospitalSet hospitalSet = hospitalService.getFirst();
        return hospitalSet.getApiUrl();
    }

    @Override
    public JSONObject getHospital() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));
        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/hospital/show");
        if(200 == respone.getIntValue("code")) {
            return respone.getJSONObject("data");
        }
        return null;
    }

    @Override
    public boolean saveHospital(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        paramMap.put("hosname",jsonObject.getString("hosname"));
        paramMap.put("hostype",jsonObject.getString("hostype"));
        paramMap.put("provinceCode",jsonObject.getString("provinceCode"));
        paramMap.put("cityCode", jsonObject.getString("cityCode"));
        paramMap.put("districtCode",jsonObject.getString("districtCode"));
        paramMap.put("address",jsonObject.getString("address"));
        paramMap.put("intro",jsonObject.getString("intro"));
        paramMap.put("route",jsonObject.getString("route"));
        //图片
        paramMap.put("logoData", jsonObject.getString("logoData"));

        JSONObject bookingRule = jsonObject.getJSONObject("bookingRule");
        paramMap.put("bookingRule",bookingRule.toJSONString());

        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));

        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/saveHospital");
        System.out.println(respone.toJSONString());

        if(null != respone && 200 == respone.getIntValue("code")) {
            return true;
        } else {
            throw new HospitalException(respone.getString("message"), 201);
        }
    }

    @Override
    public Map<String, Object> findDepartment(int pageNum, int pageSize) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        //paramMap.put("depcode",depcode);
        paramMap.put("page",pageNum);
        paramMap.put("limit",pageSize);
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));
        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/department/list");
        if(null != respone && 200 == respone.getIntValue("code")) {
            JSONObject jsonObject = respone.getJSONObject("data");

            result.put("total", jsonObject.getLong("totalElements"));
            result.put("pageNum", pageNum);
            result.put("list", jsonObject.getJSONArray("content"));
        } else {
            throw new HospitalException(respone.getString("message"), 201);
        }
        return result;
    }

    @Override
    public boolean saveDepartment(String data) {
        JSONArray jsonArray = new JSONArray();
        if(!data.startsWith("[")) {
            JSONObject jsonObject = JSONObject.parseObject(data);
            jsonArray.add(jsonObject);
        } else {
            jsonArray = JSONArray.parseArray(data);
        }

        for(int i=0, len=jsonArray.size(); i<len; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("hoscode",this.getHoscode());
            paramMap.put("depcode",jsonObject.getString("depcode"));
            paramMap.put("depname",jsonObject.getString("depname"));
            paramMap.put("intro",jsonObject.getString("intro"));
            paramMap.put("bigcode", jsonObject.getString("bigcode"));
            paramMap.put("bigname",jsonObject.getString("bigname"));

            paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
            paramMap.put("sign",HttpRequestHelper.getSign(paramMap, this.getSignKey()));
            JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/saveDepartment");
            System.out.println(respone.toJSONString());

            if(null == respone || 200 != respone.getIntValue("code")) {
                throw new HospitalException(respone.getString("message"), 201);
            }
        }
        return true;
    }

    @Override
    public boolean removeDepartment(String depcode) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        paramMap.put("depcode",depcode);
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));
        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/department/remove");
        System.out.println(respone.toJSONString());
        if(null != respone && 200 == respone.getIntValue("code")) {
            return true;
        } else {
            throw new HospitalException(respone.getString("message"), 201);
        }
    }

    @Override
    public Map<String, Object> findSchedule(int pageNum, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        //paramMap.put("depcode",depcode);
        paramMap.put("page",pageNum);
        paramMap.put("limit",pageSize);
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));
        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/schedule/list");
        System.out.println(respone.toJSONString());
        if(null != respone && 200 == respone.getIntValue("code")) {
            JSONObject jsonObject = respone.getJSONObject("data");

            result.put("total", jsonObject.getLong("totalElements"));
            result.put("pageNum", pageNum);
            result.put("list", jsonObject.getJSONArray("content"));
        } else {
            throw new HospitalException(respone.getString("message"), 201);
        }
        return result;
    }

    @Override
    public boolean saveSchedule(String data) {
        DateTime now = new DateTime().minusDays(7);
        JSONArray jsonArray = new JSONArray();
        if(!data.startsWith("[")) {
            JSONObject jsonObject = JSONObject.parseObject(data);
            jsonArray.add(jsonObject);
        } else {
            jsonArray = JSONArray.parseArray(data);
        }

        for(int i=0, len=jsonArray.size(); i<len; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Long id = jsonObject.getLong("hosScheduleId");
            Schedule schedule = new Schedule();
            schedule.setId(id);
            schedule.setHoscode(this.getHoscode());
            schedule.setDepcode(jsonObject.getString("depcode"));
            schedule.setTitle(jsonObject.getString("title"));
            schedule.setDocname(jsonObject.getString("docname"));
            schedule.setSkill(jsonObject.getString("skill"));
            // 设置workDate为当天前7天递增
            //schedule.setWorkDate(jsonObject.getString("workDate"));
            now = now.plusDays(1);
            schedule.setWorkDate(now.toString("yyyy-MM-dd"));
            schedule.setWorkTime(jsonObject.getInteger("workTime"));
            schedule.setReservedNumber(jsonObject.getInteger("reservedNumber"));
            schedule.setAvailableNumber(jsonObject.getInteger("availableNumber"));
            schedule.setAmount(jsonObject.getString("amount"));
            schedule.setStatus(1);

            Schedule targetSchedule = scheduleMapper.selectById(id);
            if(null != targetSchedule) {
                //值copy不为null的值，该方法为自定义方法
                BeanUtils.copyBean(schedule, targetSchedule, Schedule.class);
                scheduleMapper.updateById(targetSchedule);
            } else {
                scheduleMapper.insert(schedule);
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("id",schedule.getId());
            paramMap.put("hoscode",schedule.getHoscode());
            paramMap.put("depcode",schedule.getDepcode());
            paramMap.put("title",schedule.getTitle());
            paramMap.put("docname",schedule.getDocname());
            paramMap.put("skill", schedule.getSkill());
            paramMap.put("workDate",schedule.getWorkDate());
            paramMap.put("workTime", schedule.getWorkTime());
            paramMap.put("reservedNumber",schedule.getReservedNumber());
            paramMap.put("availableNumber",schedule.getAvailableNumber());
            paramMap.put("amount",schedule.getAmount());
            paramMap.put("status",schedule.getStatus());
            paramMap.put("hosScheduleId",schedule.getId());
            paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
            paramMap.put("sign",HttpRequestHelper.getSign(paramMap, this.getSignKey()));

            JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/saveSchedule");
            System.out.println(respone.toJSONString());
            if(null == respone || 200 != respone.getIntValue("code")) {
                throw new HospitalException(respone.getString("message"), 201);
            }
        }
        return false;
    }

    @Override
    public boolean removeSchedule(String hosScheduleId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        paramMap.put("hosScheduleId",hosScheduleId);
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));
        JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/schedule/remove");
        System.out.println(respone.toJSONString());
        if(null != respone && 200 == respone.getIntValue("code")) {
            return true;
        } else {
            throw new HospitalException(respone.getString("message"), 201);
        }
    }

    @Override
    public void  saveBatchHospital() throws IOException {
        File file = hospitalResource.getFile();
        String jsonData = this.jsonRead(file);
        JSONArray jsonArray = JSONArray.parseArray(jsonData);
        for(int i=0, len=jsonArray.size(); i<len; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("hoscode",jsonObject.getString("hoscode"));
            paramMap.put("hosname",jsonObject.getString("hosname"));
            paramMap.put("hostype",jsonObject.getString("hostype"));
            paramMap.put("provinceCode",jsonObject.getString("provinceCode"));
            paramMap.put("cityCode", jsonObject.getString("cityCode"));
            paramMap.put("districtCode",jsonObject.getString("districtCode"));
            paramMap.put("address",jsonObject.getString("address"));
            paramMap.put("logoData",jsonObject.getString("logoData"));
            String intro = jsonObject.getString("intro");
            paramMap.put("intro",intro);
            String route = jsonObject.getString("route");
            paramMap.put("route",route);

            JSONObject bookingRule = jsonObject.getJSONObject("bookingRule");
            JSONArray bookingRuleJSONArray = bookingRule.getJSONArray("rule");


            Map<String, Object> bookingRuleMap = new HashMap<>();
            bookingRuleMap.put("cycle",bookingRule.getString("cycle"));
            bookingRuleMap.put("releaseTime",bookingRule.getString("releaseTime"));
            bookingRuleMap.put("stopTime",bookingRule.getString("stopTime"));
            bookingRuleMap.put("quitDay",bookingRule.getString("quitDay"));
            bookingRuleMap.put("quitTime",bookingRule.getString("quitTime"));
            bookingRuleMap.put("rule",bookingRuleJSONArray.toArray());
            paramMap.put("bookingRule",JSONObject.toJSONString(bookingRuleMap));

            paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
            paramMap.put("sign", HttpRequestHelper.getSign(paramMap, this.getSignKey()));

            JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/saveHospital");
            System.out.println(respone.toJSONString());
            if(null == respone || 200 != respone.getIntValue("code")) {
                throw new HospitalException(respone.getString("message"), 201);
            }
        }
    }

    private String jsonRead(File file){
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, StandardCharsets.UTF_8);
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (Exception e) {

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }

    public static String getImgStr(String imgFile){
        byte[] data = getImageBytes(imgFile);
        return new String(Base64.encodeBase64(data));
    }

    public static byte[] getImageBytes(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = is.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            return outStream.toByteArray();
        } catch (ConnectException e) {
            log.error("获取图片时连接异常，url:{}",imgUrl,e);
        } catch (MalformedURLException e) {
            log.error("url出现异常，url:{}",imgUrl,e);
        } catch (IOException e) {
            log.error("读取图片时发生异常，url:{}",imgUrl,e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
        return null;
    }
}
