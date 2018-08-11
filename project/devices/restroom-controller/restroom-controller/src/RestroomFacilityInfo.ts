import { Restroom } from "./Facility";

export class FacilityInfo {
    constructor(private id: string,
        restRoomsInfo: Restroom[],
        smokeDetected: boolean,
        trashCapacities: number[],
        soapDispensersCapacities: number[]) {
    }
}