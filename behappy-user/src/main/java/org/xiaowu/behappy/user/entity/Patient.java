package org.xiaowu.behappy.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaowu.behappy.common.mybatis.base.BaseEntity;

import java.util.Date;

/**
 * <p>
 * Patient
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Patient")
@TableName("patient")
public class Patient extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户id")
	@TableField("user_id")
	private Long userId;

	@Schema(description = "姓名")
	@TableField("name")
	private String name;

	@Schema(description = "证件类型")
	@TableField("certificates_type")
	private String certificatesType;

	@Schema(description = "证件编号")
	@TableField("certificates_no")
	private String certificatesNo;

	@Schema(description = "性别")
	@TableField("sex")
	private Integer sex;

	@Schema(description = "出生年月")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@TableField("birthdate")
	private Date birthdate;

	@Schema(description = "手机")
	@TableField("phone")
	private String phone;

	@Schema(description = "是否结婚")
	@TableField("is_marry")
	private Integer isMarry;

	@Schema(description = "省code")
	@TableField("province_code")
	private String provinceCode;

	@Schema(description = "市code")
	@TableField("city_code")
	private String cityCode;

	@Schema(description = "区code")
	@TableField("district_code")
	private String districtCode;

	@Schema(description = "详情地址")
	@TableField("address")
	private String address;

	@Schema(description = "联系人姓名")
	@TableField("contacts_name")
	private String contactsName;

	@Schema(description = "联系人证件类型")
	@TableField("contacts_certificates_type")
	private String contactsCertificatesType;

	@Schema(description = "联系人证件号")
	@TableField("contacts_certificates_no")
	private String contactsCertificatesNo;

	@Schema(description = "联系人手机")
	@TableField("contacts_phone")
	private String contactsPhone;

	@Schema(description = "是否有医保")
	@TableField("is_insure")
	private Integer isInsure;

	@Schema(description = "就诊卡")
	@TableField("card_no")
	private String cardNo;

	@Schema(description = "状态（0：默认 1：已认证）")
	@TableField("status")
	private String status;
}

