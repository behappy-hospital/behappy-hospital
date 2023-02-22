package org.xiaowu.behappy.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaowu.behappy.common.mybatis.base.BaseEntity;

import java.io.Serial;

/**
 * <p>
 * UserInfo
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "UserLoginRecord")
@TableName("user_login_record")
public class UserLoginRecord extends BaseEntity {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "userId")
	@TableField("user_id")
	private Long userId;

	@Schema(description = "ip")
	@TableField("ip")
	private String ip;

}

