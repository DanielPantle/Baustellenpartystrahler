
#define MODE_ALL_FCOLOR 0
#define MODE_CIRCLE 1
#define MODE_BAUSTELLE 2

int validLedModes[] = {
  0, 1
};


void modeAllLedsFColor() {
  for(int i = 0; i < LED_COUNT; i++) {
    leds[i] = fcolor;
  }
  FastLED.show();
  delay(100);
}
//void modeAllLedsFColor() {
//}

void modeCircle() {
  for(int i = 0; i < LED_COUNT; i++) {
    leds[i] = fcolor;
    FastLED.show();
    delay(speed);
  }
  for(int i = 0; i < LED_COUNT; i++) {
    leds[i] = bcolor;
    FastLED.show();
    delay(speed);
  }
}
//void modeCircle() {
//}

void modeBaustelle() {
  for(int i = 0; i < LED_COUNT; i++) {
    leds[i] = CRGB::Black;
  }
  FastLED.show();
  delay(speed2);
  for(int i = 0; i < LED_COUNT; i++) {
    leds[i] = CRGB(255, 156, 0);
  }
  FastLED.show();
  delay(speed);
}
