package org.xiaowu.behappy.api.user.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="登录对象")
public class LoginVo {

    @Schema(description = "openid")
    private String openid;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "密码")
    private String code;

    @Schema(description = "IP")
    private String ip;
}
