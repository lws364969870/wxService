package com.lws.domain.utils;

import java.util.Random;

public final class RandomUtils
{
  public static String getRandom(int num)
  {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < num; ++i) {
      sb.append(String.valueOf(random.nextInt(10)));
    }
    return sb.toString();
  }
}