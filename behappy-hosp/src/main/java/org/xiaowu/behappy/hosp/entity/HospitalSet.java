package org.xiaowu.behappy.hosp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaowu.behappy.common.mybatis.base.BaseEntity;

/**
 * <p>
 * HospitalSet
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "医院设置")
@TableName("hospital_set")
public class HospitalSet extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Schema(description = "医院名称")
	@TableField("hosname")
	private String hosname;

	@Schema(description = "医院编号")
	@TableField("hoscode")
	private String hoscode;

	@Schema(description = "api基础路径")
	@TableField("api_url")
	private String apiUrl;

	@Schema(description = "签名秘钥")
	@TableField("sign_key")
	private String signKey;

	@Schema(description = "联系人姓名")
	@TableField("contacts_name")
	private String contactsName;

	@Schema(description = "联系人手机")
	@TableField("contacts_phone")
	private String contactsPhone;

	@Schema(description = "状态")
	@TableField("status")
	private Integer status;

}

