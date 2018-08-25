import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RestRoom } from '../code/restRoom';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  constructor(private http: HttpClient) { }

  getNearbyRestrooms() {
    return this.http.post<RestRoom[]>("api/data/getRestrooms", {});
  }
}
