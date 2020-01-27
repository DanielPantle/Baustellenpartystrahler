
#ifndef BLINKMODE_H
#define BLINKMODE_H

#include "mode.cpp"

class BlinkMode : public Mode {
  private:
    long timer = -1;
    int state = 0;
  public:
    BlinkMode() {
    }
    void update() {
      if((state == 0 && timer >= speed * 3000) || (state == 1 && timer >= speed2 * 3000) || timer == -1) {
        timer = 0;
        
        if(state == 0) {
          for(int i = 0; i < LED_COUNT; i++) {
            leds[i] = bcolor;
          }
          state = 1;
        }
        else {
          for(int i = 0; i < LED_COUNT; i++) {
            leds[i] = fcolor;
          }
          state = 0;
        }
        
        FastLED.show();
      }
      timer++;
    }

    void setSpeed(int speed) {
      Mode::setSpeed(speed);
      timer = -1;
    }
    void setSpeed2(int speed2) {
      Mode::setSpeed2(speed2);
      timer = -1;
    }
};

#endif
