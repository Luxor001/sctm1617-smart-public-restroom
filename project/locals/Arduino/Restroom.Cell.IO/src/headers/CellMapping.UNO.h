#ifndef MAPPING_H
#define MAPPING_H
#include "Arduino.h"

const String LASER_EMIT = "LASER_EMIT";
const String LED_BUILTIN_S = "LED_BUILTIN";
const String PHOTO_RESISTOR = "PHOTO_RESISTOR";
const String TEMP_AND_HUMIDITY ="TEMP_AND_HUMIDITY";


String cell_inputMapping(int value)
{
    switch(value)
    {
        case 4: return TEMP_AND_HUMIDITY;   //HUMIDITY
        case 23:                            //DOOR
        case 24:                            //PAPER
        case 25:                            //LIGHT
            return PHOTO_RESISTOR;
        default:
            return "";
    }
    
};

String evaluateMappingType(int value)
{
    if (value>2 && value < 13)
    {
        return "O_DGT";

    }
    else if (value>=23 && value <28)
        return "O_ANL";
    else
    {
        switch(value)
        {
            case 0:return "I_RX";
            case 1:return "O_TX";
            case 2: 
            case 3:return "I_PWM";
            case 4: return "O_REAL";
            case 5:
            case 6:
            case 7: 
            case 8:
            case 9:
            case 10:
            case 11:
            case 12: return "O_PWM";
            case 13: return "I_LED_BUILTIN";
            default:return "";
        }
    }
}

String cell_outputMapping(int value)
{
    switch(value)
    {
        case 2:                             //DOOR
        case 3: return LASER_EMIT;          //PAPER
        case 13: return LED_BUILTIN_S;
        default:
            return "";
    }
};
#endif