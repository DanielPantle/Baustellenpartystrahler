
#ifndef LEDSTRIP_H
#define LEDSTRIP_H

#include "definitions.h"

class LedStrip {
  private:
    int brightness = 255;
  
  public:
    LedStrip() {
      FastLED.addLeds<NEOPIXEL, LED_PIN>(leds, LED_COUNT);
      FastLED.setBrightness(brightness);
    }
    
    void setBrightness(int brightness) {
      this->brightness = brightness;
      FastLED.setBrightness(brightness);
    }
};

#endif
