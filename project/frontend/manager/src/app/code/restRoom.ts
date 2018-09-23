export interface RestRoom {
  uid: string;
  cityAddress: string;
  address: string[];
  company: string;
  device: string;
  sensorData: SensorData;
}


export interface SensorData {
  roomsInfo: RoomsInfo[];
  smokeDetected: boolean;
  trashCapacities: number[];
  soapDispensersCapacities: number[];
}

export interface RoomsInfo {
  id: number;
  paperAvaiable: boolean;
  umidity: number;
  lightWorking: boolean;
  closed: boolean;
}