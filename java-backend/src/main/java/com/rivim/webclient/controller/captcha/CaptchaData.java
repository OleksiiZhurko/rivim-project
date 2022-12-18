package com.rivim.webclient.controller.captcha;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CaptchaData {

  private String code;

  private String img;
}
