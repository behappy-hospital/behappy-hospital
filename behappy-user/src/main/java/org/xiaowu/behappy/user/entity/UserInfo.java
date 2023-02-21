package org.xiaowu.behappy.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaowu.behappy.common.mybatis.base.BaseEntity;

/**
 * <p>
 * UserInfo
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "UserInfo")
@TableName("user_info")
public class UserInfo extends BaseEntity {

	private static final long serialVersionUID = 5598569432685246398L;
	@Schema(description = "微信openid")
	@TableField("openid")
	private String openid;

	@Schema(description = "昵称")
	@TableField("nick_name")
	private String nickName;

	@Schema(description = "手机号")
	@TableField("phone")
	private String phone;

	@Schema(description = "用户姓名")
	@TableField("name")
	private String name;

	@Schema(description = "证件类型")
	@TableField("certificates_type")
	private String certificatesType;

	@Schema(description = "证件编号")
	@TableField("certificates_no")
	private String certificatesNo;

	@Schema(description = "证件路径")
	@TableField("certificates_url")
	private String certificatesUrl;

	@Schema(description = "认证状态（0：未认证 1：认证中 2：认证成功 -1：认证失败）")
	@TableField("auth_status")
	private Integer authStatus;

	@Schema(description = "状态（0：锁定 1：正常）")
	@TableField("status")
	private Integer status;

}

