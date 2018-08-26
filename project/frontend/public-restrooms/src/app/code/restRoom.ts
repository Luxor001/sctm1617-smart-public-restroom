export interface RestRoom {
    address: number[];
    id: string;
    restRoomsInfo: RestRoomsInfo[];
    smokeDetected: boolean;
    trashCapacities: number[];
    soapDispensersCapacities: number[];
  }
  
  export interface RestRoomsInfo {
    id: number;
    paperAvaiable: boolean;
    umidity: number;
    lightWorking: boolean;
    closed: boolean;
  }
