
#ifndef STANDARDMODE_H
#define STANDARDMODE_H

#include "mode.cpp"

class StandardMode : public Mode {
  public:
    StandardMode() {
      
    }
    void update() {
      for(int i = 0; i < LED_COUNT; i++) {
        leds[i] = fcolor;
      }
      FastLED.show();
    }
};

#endif
