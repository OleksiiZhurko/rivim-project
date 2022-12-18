package com.rivim.webclient.controller.captcha;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

@Component
public class CaptchaProducer {

  public static final String CAPTCHA_CODE = "code";

  public static final String CAPTCHA_VALUE = "captcha";

  private static final String BASE64_STRING = "data:image/png;base64, ";

  private final Random random = new Random();

  private static final int TOTAL_CHARS = 4;
  private static final int IMG_HEIGHT = 25;
  private static final int IMG_WIDTH = 80;
  private static final int COLOR_BOUND = 255;
  private static final int FONT_SIZE = 18;
  private static final Font FONT_STYLE = new Font("Arial", Font.BOLD, FONT_SIZE);

  public CaptchaData captchaGenerator() throws IOException {
    String captchaCode = (Long.toString(Math.abs(random.nextLong()), 36))
        .substring(0, TOTAL_CHARS);
    BufferedImage captcha = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
    Graphics2D image = captcha.createGraphics();

    image.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);
    image.setColor(Color.WHITE);
    image.setFont(FONT_STYLE);

    for (int i = 0; i < TOTAL_CHARS; i++) {
      image.setColor(getRandomColor());
      image.drawString(captchaCode.substring(i, i + 1), positionX(i), positionY());
    }

    var osImage = new ByteArrayOutputStream();
    ImageIO.write(captcha, "jpeg", osImage);
    image.dispose();

    return CaptchaData.builder()
        .code(captchaCode)
        .img(BASE64_STRING + Base64.getEncoder().encodeToString(osImage.toByteArray()))
        .build();
  }

  private Color getRandomColor() {
    return new Color(
        random.nextInt(COLOR_BOUND),
        random.nextInt(COLOR_BOUND),
        random.nextInt(COLOR_BOUND)
    );
  }

  private int positionX(int iter) {
    return 20 * iter;
  }

  private int positionY() {
    return random.nextInt(21 - 15) + 15;
  }
}
