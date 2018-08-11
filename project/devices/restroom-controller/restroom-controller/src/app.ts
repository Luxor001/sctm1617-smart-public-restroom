import * as SerialPort from 'serialport';
import * as ReadLineParser from '@serialport/parser-readline';
import * as https from 'https'
import Axios, { AxiosInstance, AxiosStatic } from 'axios';
import { machineIdSync } from 'node-machine-id';


import { FacilityInfo } from './RestroomFacilityInfo'

class Main {
    //use https://alexandre.alapetite.fr/doc-alex/raspberrypi-nodejs-arduino/index.en.html
    static arduinoSerialPort = '/dev/ttyACM0';	//Serial port over USB connection between the Raspberry Pi and the Arduino
    static httpRequester: AxiosInstance;
    static facilityInfo: FacilityInfo;

    static initialize() {
        //Main.initializeUsb();
        Main.initializeHTTP();
        Main.initializeFacilityInfo();
        setInterval(function mainLoop() {

            Main.httpRequester.post('https://localhost:44323/api/data/send', Main.facilityInfo).then(response => {
                console.log("received", response);
            });

        }, 2000);
    }

    static initializeUsb() {
        var serialPort = new SerialPort(Main.arduinoSerialPort, error => {
            console.log("error on usb occurred");
            console.log(error);
        });
        const parser = serialPort.pipe(new ReadLineParser());
    }

    static initializeFacilityInfo() {
        let id = machineIdSync()

        // TODO: sense all connected arduinos and build a FacilityInfo
        // ID will always be unique machine identifier
        Main.facilityInfo = new FacilityInfo(id, undefined, undefined, undefined, undefined);
    }

    static initializeHTTP() {

        //FIXME: da fixare con certificati giusti, vedere https://github.com/axios/axios/issues/1495
        //per ora workaround in accordo con 
        Main.httpRequester = Axios.create({
            httpsAgent: new https.Agent({
                rejectUnauthorized: false
            })
        });
    }
}

Main.initialize();