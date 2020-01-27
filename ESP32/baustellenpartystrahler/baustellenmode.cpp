
#ifndef BAUSTELLENMODE_H
#define BAUSTELLENMODE_H

#include "mode.cpp"

class BaustellenMode : public Mode {
  private:
    long timer = -1;
    int state = 0;
  public:
    BaustellenMode() {
    }
    void update() {
      if((state == 0 && timer >= 600000) || (state == 1 && timer >= speed * 1000) || timer == -1) {
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
};

#endif
