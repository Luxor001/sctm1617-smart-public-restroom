import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  constructor(private http: HttpClient) { }

  getNearbyRestrooms() {
    this.http.post("api/data/getRestrooms", {}).subscribe(result => {
      console.log(result);
    });
  }
}
