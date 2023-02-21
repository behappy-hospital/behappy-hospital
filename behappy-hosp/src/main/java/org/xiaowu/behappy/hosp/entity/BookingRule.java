package org.xiaowu.behappy.hosp.entity;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * <p>
 * RegisterRule
 * </p>
 */
@Data
@Schema(description = "预约规则")
@Document("BookingRule")
public class BookingRule {

	@Schema(description = "预约周期")
	private Integer cycle;

	@Schema(description = "放号时间")
	private String releaseTime;

	@Schema(description = "停挂时间")
	private String stopTime;

	@Schema(description = "退号截止天数（如：就诊前一天为-1，当天为0）")
	private Integer quitDay;

	@Schema(description = "退号时间")
	private String quitTime;

	@Schema(description = "预约规则")
	private List<String> rule;

	/**
	 * Map转换为Hospital对象时，预约规则bookingRule为一个对象属性，rule为一个数组属性，因此在转换时我们要重新对应的set方法，不然转换不会成功
	 * @param rule
	 */
	public void setRule(String rule) {
		if(StrUtil.isNotEmpty(rule)) {
			this.rule = JSONArray
					.parseArray(rule)
					.toList(String.class);
		}
	}

}

