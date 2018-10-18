import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RestRoom } from '../code/restRoom';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  constructor(private http: HttpClient) { }

  getNearbyRestrooms() {
    return this.http.post<RestRoom[]>("api/consumer/getRestrooms", {}).pipe(map((result: any) => {

      return result.filter(restroom => restroom.sensorData != null).map(restroom => {
        restroom.address = restroom.address.map(address => parseFloat(address));
        return restroom;
      });
    }));
  }
}
