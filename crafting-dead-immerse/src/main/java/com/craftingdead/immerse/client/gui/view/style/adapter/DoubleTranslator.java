/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view.style.adapter;

import com.craftingdead.immerse.util.StringCountUtil;

public class DoubleTranslator
    implements StyleDecoder<Double>, StyleEncoder<Double>, StyleValidator<Double> {

  @Override
  public String encode(Double value, boolean prettyPrint) {
    return value.toString();
  }

  @Override
  public Double decode(String style) {
    if (style.contains("%")) {
      return Double.valueOf(style.replace('%', '\0')) / 100;
    }
    return Double.valueOf(style);
  }

  @Override
  public int validate(String style) {
    int doubleLength = StringCountUtil.floatAtStart(style);

    if (doubleLength == 0) {
      return 0;
    }

    if (doubleLength < style.length() && style.charAt(doubleLength) == '%') {
      return doubleLength + 1;
    }
    return doubleLength;
  }
}
