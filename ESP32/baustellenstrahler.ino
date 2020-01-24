
//#include <timer.h>
//Timer timer = timer_create_default();
//auto ledTimer;

#include <FastLED.h>

#define LED_COUNT 17
#define LED_PIN 18
CRGB leds[LED_COUNT];

int temp;
int counter;
int counter2;
int ledMode = 0;
String receivedData;
int firstCall = false;
int speed = 50;
int speed2 = 50;
CRGB fcolor = CRGB::White;
CRGB bcolor = CRGB::Black;

#include "ledmodes.h"

#include "BluetoothSerial.h"
BluetoothSerial ESP_BT;
#define DELIMITER ":"

void setup() {
  Serial.begin(9600);

  FastLED.addLeds<NEOPIXEL, LED_PIN>(leds, LED_COUNT);
  FastLED.setBrightness(255);

  ESP_BT.begin("Baustellenpartystrahler");
  Serial.println("Bluetooth Device is Ready to Pair");

  //ledTimer = timer.every(speed, updateLeds());
}

void loop() {
  //Check if we receive anything from Bluetooth
  if (ESP_BT.available()) {
    receiveData();
    parseReceivedData();
  }

  updateLeds();
  //timer.tick();
}

void receiveData() {
  receivedData = "";
  char c;
  while (ESP_BT.available()) {
    c = ESP_BT.read();
    receivedData += c;
  }
  Serial.println("received data: " + receivedData);
}

void parseReceivedData() {
  if(receivedData.indexOf(DELIMITER) == -1) {
    Serial.println("parse error: no delimiter");
    return;
  }

  char tmp[50];
  receivedData.toCharArray(tmp, 50);
  String key = String(strtok(tmp, DELIMITER));
  long val = strtol(strtok(NULL, DELIMITER), 0, 16);

  Serial.println("key: " + key);
  Serial.println("val: " + String(val));

  if (key.equals("mode")) {
    setMode(val);
  }
  else if (key.equals("fcolor")) {
    setFColor(val);
  }
  else if (key.equals("bcolor")) {
    setBColor(val);
  }
  else if (key.equals("bright")) {
    setBright(val);
  }
  else if (key.equals("speed")) {
    setSpeed(val);
    //timer.cancel(ledTimer);
    //ledTimer = timer.every(speed, updateLeds);
  }
  else if (key.equals("speed2")) {
    setSpeed2(val);
  }
}

void setMode(long val) {
  for(int i = 0; i < sizeof(validLedModes); i++) {
    if(val == validLedModes[i]) {
    Serial.println("setMode success: mode " + String(val) + " found");
      ledMode = val;
      //startMode(ledMode);
      return;
    }
  }
  Serial.println("setMode error: mode not found");
}
void setFColor(long val) {
  fcolor = val;
  Serial.println("setFColor success: color set to " + String(val));
}
void setBColor(long val) {
  bcolor = val;
  Serial.println("setBColor success: color set to " + String(val));
}
void setBright(long val) {
  FastLED.setBrightness(val);
}
void setSpeed(long val) {
  speed = val;
}
void setSpeed2(long val) {
  speed2 = val;
}


//void startMode() {
//  if (ledMode == MODE_ALL_FCOLOR) {
//    modeAllLedsFColor();
//  }
//  else if (ledMode == MODE_CIRCLE) {
//    modeCircle();
//  }
//  else if (ledMode == MODE_BAUSTELLE) {
//    modeBaustelle();
//  }
//}
void updateLeds() {
  if (ledMode == MODE_ALL_FCOLOR) {
    modeAllLedsFColor();
  }
  else if (ledMode == MODE_CIRCLE) {
    modeCircle();
  }
  else if (ledMode == MODE_BAUSTELLE) {
    modeBaustelle();
  }
  
  else if (ledMode == 53) {
    if(firstCall) {
      for(int i = 0; i < LED_COUNT; i++) {
        leds[i] = CRGB(random(0, 255), random(0, 255), random(0, 255));
      }
      FastLED.show();
      delay(100);
      firstCall = false;
      Serial.println("first call");
    }
    for(int i = 0; i < LED_COUNT; i++) {
      leds[i].r += random(-1, 1) * 15;
      leds[i].g += random(-1, 1) * 15;
      leds[i].b += random(-1, 1) * 15;
    }
    FastLED.show();
    delay(speed / 10);
  }
  else if (ledMode == 54) {
    for(int i = 0; i < LED_COUNT; i++) {
      leds[i] = CRGB(255, 0, 0);
      FastLED.show();
      delay(speed);
    }
    for(int i = 0; i < LED_COUNT; i++) {
      leds[i] = CRGB(0, 156, 0);
      FastLED.show();
      delay(speed);
    }
  }
  else if (ledMode == 55) {
    if(firstCall) {
      for(int i = 0; i < LED_COUNT; i++) {
        //leds[i] = CRGB(random(255), random(255), random(255));
        leds[i] = CRGB::Black;
      }
      FastLED.show();
      delay(100);
      firstCall = false;
    }
    for(int i = 0; i < LED_COUNT; i++) {
      leds[i].r += random(5);
      leds[i].g += random(5);
      leds[i].b += random(5);
    }
    FastLED.show();
    delay(speed);
  }
  else if (ledMode == 56) {
    for(int i = 0; i < LED_COUNT; i++) {
      leds[i] = CRGB::Black;
    }
    FastLED.show();
    delay(2000);
    for(int i = 0; i < LED_COUNT; i++) {
      leds[i] = CRGB(255, 156, 0);
    }
    FastLED.show();
    delay(200);
  }
  else if (ledMode == 57) {
    if(firstCall) {
      counter = 0;
      firstCall = false;
    }
    fill_rainbow(leds, LED_COUNT, 0, counter);
    counter++;
    delay(100);
    FastLED.show();
  }
  else if (ledMode == 58) {
    if (firstCall) {
      counter = 0;
      counter2 = 0;
      firstCall = false;
    }
    for (int i = 0; i < LED_COUNT; i++) {
      if (i < counter || i > LED_COUNT - counter) {
        leds[i] = CRGB::Red;
      }
      else {
        leds[i] = CRGB::Black;
      }
    }
    if (counter2 == 0) {
      counter++;
      if (counter > LED_COUNT / 2) {
        counter2 = 1;
      }
    }
    else {
      counter--;
      if (counter <= 0) {
        counter2 = 0;
      }
    }
    
    delay(50);
    FastLED.show();
  }
}
