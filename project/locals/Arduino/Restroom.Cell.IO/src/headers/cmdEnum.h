#ifndef CMD_ENUM_H
#define CMD_ENUM_H
#include "Arduino.h"
enum CMD_ENUM
{
    GET_MAPPING,
    READ,
    WRITE,
    HS,
    NO_MAPPING
};

CMD_ENUM getCmdEnum(String cmd)
{

    if (cmd=="GET_MAPPING")
        return GET_MAPPING;
    else if (cmd =="READ")
        return READ;
    else if (cmd == "WRITE")
        return WRITE;
    else if (cmd=="HANDSHAKE")
        return HS;
    else return NO_MAPPING;
};
#endif

