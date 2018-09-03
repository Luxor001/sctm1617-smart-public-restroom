import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RestRoom } from '../code/restRoom';

@Injectable({
  providedIn: 'root'
})
export class RestroomsListService {

  constructor(private httpClient: HttpClient) { }

  getRestrooms() {
    return  this.httpClient.post<RestRoom[]>('api/manager/getRestrooms', {});
  }
}
