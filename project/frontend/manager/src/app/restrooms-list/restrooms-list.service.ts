import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RestRoom } from '../code/restRoom';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RestroomsListService {

  constructor(private httpClient: HttpClient) { }

  getRestrooms() {
    return this.httpClient.post('api/manager/getRestrooms', {}).pipe(map((restrooms: any) => {
      return restrooms.restrooms;
    }));
  }
}
