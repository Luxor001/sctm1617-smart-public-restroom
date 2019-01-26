#include <Arduino.h>
#include "headers/CellMapping.UNO.h"
#include "headers/cmdEnum.h"
#include "DHT.h"

DHT dht;

const String deviceName="CELL.01";

void log(String message,bool withTX)
{
    //Serial.println(message);
    if (withTX)
        Serial.write((message+"\n").c_str());
}

void beginSerial()
{
    Serial.begin(9600);
    Serial.setTimeout(50);
    Serial.flush();
    
}

void initSensors()
{
    dht.setup(4);
}

String getSensorNameByPin(int pin)
{
    String sensorName= cell_inputMapping(pin);
    if (sensorName == "") sensorName = cell_outputMapping(pin);
    return sensorName;
}

bool checkConflicts()
{
    for(int i=0;i<28;i++)
    {
        if (cell_inputMapping(i)!=""&&cell_outputMapping(i)!="")
            return true;
    }
    return false;
}

void beginInputSensors()
{
    for(int i=0;i<28; i++)
    {
        if (cell_inputMapping(i)!="")
        {
            pinMode(i,INPUT);
        }
    }
}

void beginOutputSensors()
{
    for(int i=0;i<28; i++)
    {
        if (cell_outputMapping(i)!="")
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
    if (sensorName == PHOTO_RESISTOR)
    {
        log("READ,"+String(pin)+","+String(analogRead(pin)),true);
    }
    else if (sensorName == TEMP_AND_HUMIDITY)
    {
        delay(dht.getMinimumSamplingPeriod());
        log("READ,"+String(pin)+","+String(dht.getHumidity()),true);
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
    initSensors();
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
                log("Hello",true);
            default:
                break;
        }
    
        
    }
    
}