#ifndef MAPPING_H
#define MAPPING_H
#include "Arduino.h"

const String ULTRASONIC_TRIGGER = "ULTRASONIC_TRIGGER";
const int ULTRASONIC_TRIGGER_PIN = 2;

const String ULTRASONIC_ECHO = "ULTRASONIC_ECHO";
const int ULTRASONIC_ECHO_PIN  = 3;

const String FORCE_RESISTOR = "FORCE_RESISTOR";
const String SMOKE = "SMOKE";


String sink_inputMapping(int value)
{
    switch(value)
    {
        
        case ULTRASONIC_ECHO_PIN: return ULTRASONIC_ECHO;
        case 54: return FORCE_RESISTOR;
        case 55: return SMOKE;
        default:
            return "";
    }
    
};

String evaluateMappingType(int value)
{
    if (value>21 && value < 54)
    {
        return "O_DGT";

    }
    else if (value>=54 && value <69)
        return "O_ANL";
    else
    {
        switch(value)
        {
            case 0:return "I_RX";
            case 1:return "O_TX";
            case 2: 
            case 3: return "O_REAL";
            case 4:
            case 5:
            case 6:
            case 7: 
            case 8:
            case 9:
            case 10:
            case 11:
            case 12: return "O_PWM";
            case 13: return "I_LED_BUILTIN";
            case 14: return "O_TX3";
            case 15: return "I_RX3";
            case 16: return "O_TX2";
            case 17: return "I_RX2";
            case 18: return "O_TX1";
            case 19: return "I_RX1";
            case 20: return "O_SDA";
            case 21: return "O_SCL";
            default:return "";
        }
    }
}

String sink_outputMapping(int value)
{
    switch(value)
    {
        
        
        default:
            return "";
    }
};
#endif