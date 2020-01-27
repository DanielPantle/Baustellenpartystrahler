
#ifndef CIRCLEMODE_H
#define CIRCLEMODE_H

#include "mode.cpp"

class CircleMode : public Mode {
  private:
    long timer = -1;
    int pos = 0;
  public:
    CircleMode() {
      
    }
    void update() {
      if(timer >= speed * 3000 || timer == -1) {
        timer = 0;
        
        pos++;
        if(pos >= LED_COUNT) {
          pos = 0;
        }
        
        for(int i = 0; i < LED_COUNT; i++) {
          leds[i] = bcolor;
        }
        
        leds[pos] = fcolor;
        
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
