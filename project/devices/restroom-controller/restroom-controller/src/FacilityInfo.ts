export class Restroom {
    constructor(private id: number,
        private paperAvaiable: boolean,
        private umidity: number,
        private lightWorking: boolean,
        private closed: boolean) {
    }
}