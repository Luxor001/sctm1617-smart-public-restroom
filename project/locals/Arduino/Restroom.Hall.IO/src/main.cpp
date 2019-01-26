#include <Arduino.h>
#include "headers/SinkMapping.Uno.h"
#include "headers/cmdEnum.h"


const String deviceName="SINK.01";

void log(String message,bool withTX)
{
    //Serial.println(message);
    if (withTX)
        Serial.write((message+"\n").c_str());
}

void beginSerial()
{
    Serial.begin(9600);
    Serial.setTimeout(100);
    Serial.flush();
    
}

bool checkConflicts()
{
    for(int i=0;i<28;i++)
    {
        
        if (sink_inputMapping(i)!=""&&sink_outputMapping(i)!="")
            return true;
    }
    return false;
}
String getSensorNameByPin(int pin)
{
    String sensorName= sink_inputMapping(pin);
    if (sensorName == "") sensorName = sink_outputMapping(pin);
    return sensorName;
}

void beginInputSensors()
{
    for(int i=0;i<28; i++)
    {
        if (sink_inputMapping(i)!="")
        {
            pinMode(i,INPUT);
        }
    }
}

void beginOutputSensors()
{
    for(int i=0;i<28; i++)
    {
        if (sink_outputMapping(i)!="")
        {
            pinMode(i,OUTPUT);
        }
    }
}

void getMapping()
{
    log("GET_MAPPING_BEGIN:"+deviceName,true);
    delay(1000);
    for(int i=0; i<28; i++)
    {
        String sensorName=getSensorNameByPin(i);
        if (sensorName!="") log((String(i)+","+sensorName+","+evaluateMappingType(i)),true);
    }
    log("GET_MAPPING_END:"+deviceName,true);
}

void readFromPin(int pin)
{
    //NEED TO BE CHANGED FOR REAL SENSORS
    String sensorName = getSensorNameByPin(pin);
    if (sensorName == FORCE_RESISTOR || sensorName == SMOKE)
    {
        log("READ,"+String(pin)+","+String(analogRead(pin)),true);
    }
    else if (sensorName == ULTRASONIC_ECHO)
    {
        digitalWrite(ULTRASONIC_TRIGGER_PIN,LOW);
        delayMicroseconds(2);
        digitalWrite(ULTRASONIC_TRIGGER_PIN,HIGH);
        delayMicroseconds(10);
        digitalWrite(ULTRASONIC_TRIGGER_PIN,LOW);
        long duration=pulseIn(ULTRASONIC_ECHO_PIN,HIGH);
        long distance=duration/58.2;
        if (distance <=0 || distance>=200)
            log("READ,"+String(pin)+","+String(-1),true);
        else
            log("READ,"+String(pin)+","+String(distance),true);
    }
    else 
    {
        log("READ,"+String(pin)+","+String(digitalRead(pin)),true);
    }
}

void writeToPin(int pin,int value)
{
    log("WRITE,"+String(pin)+","+String(value),false);
    digitalWrite(pin,value);
}

void setup() {
    beginSerial();
    if (!checkConflicts())
    {
        beginInputSensors();
        beginOutputSensors();
    }
    
}

void loop() {
    if (Serial.available())
    {
        String cmd=Serial.readStringUntil(',');
        
        String remainingString=Serial.readStringUntil('\n');
        
        switch(getCmdEnum(cmd))
        {
            case GET_MAPPING:
                getMapping();
                break;
            case READ:
                readFromPin(remainingString.substring(0,2).toInt());
                break;
            case WRITE:
                writeToPin(remainingString.substring(0,2).toInt(),remainingString.substring(3).toInt());
                break;
            case HS:
                delay(500);
                log("Hello",true);
            default:
                break;
        }
    
        
    }
    
}