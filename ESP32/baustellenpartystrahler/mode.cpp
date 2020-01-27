
#ifndef MODE_H
#define MODE_H

#include "definitions.h"

class Mode {
  protected:
    int speed = 50;
    int speed2 = 50;
    CRGB fcolor = CRGB::White;
    CRGB bcolor = CRGB::Black;
    
  public:
    Mode() {
    }

    virtual void update() = 0;

    void setSpeed(int speed) {
      this->speed = speed;
    }
    void setSpeed2(int speed2) {
      this->speed2 = speed2;
    }
    void setFColor(CRGB fcolor) {
      this->fcolor = fcolor;
    }
    void setBColor(CRGB bcolor) {
      this->bcolor = bcolor;
    }
};

#endif
